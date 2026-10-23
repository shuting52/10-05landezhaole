package com.example.download

import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

// ============================================================
// 2026-10-23：移植自 jicun（即存，github.com/dhvbjvvb/jicun）
// android/.../NativeDownloader.kt —— Range 分段并行下载器。
//
// 核心机制（原样保留）：
// - 探针 Range: bytes=0-0 测总大小 + 是否支持 Range
// - ≥8MB 且支持 Range → 分段并行（共享游标认领 4MB 段，尾部 32MB 按 1MB 收）
// - 段级重试（3 次，退避）+ 慢连接轮换（10 秒没收完一段就掐掉重开）
// - 微信视频号式"Range 回 200"兼容（wholeFileAsRange）
// - B站 CDN 桌面 UA + Referer
//
// 去掉 Flutter MethodChannel，改为 Kotlin 回调。
// ============================================================

object JicunDownloader {

    private const val SEGMENT_BYTES = 4L * 1024 * 1024
    private const val TAIL_BYTES = 32L * 1024 * 1024
    private const val TAIL_CHUNK_BYTES = 1L * 1024 * 1024
    private const val SEGMENT_FROM_BYTES = 8L * 1024 * 1024
    private const val DEFAULT_LANES = 16
    private const val MAX_SEGMENTS = 32
    private const val CONNECTION_BUDGET_MS = 10_000L
    private const val PROGRESS_INTERVAL_MS = 200L
    private const val CHUNK_ATTEMPTS = 3
    private const val CHUNK_ATTEMPT_LIMIT = 40

    private const val MOBILE_UA =
        "Mozilla/5.0 (Linux; Android 14; 23127PN0CC) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36"
    private const val DESKTOP_UA =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"

    class CancelledException : IOException("cancelled")

    data class Result(val bytesWritten: Long, val totalBytes: Long)

    /** 按域名给请求头（B站 CDN 要桌面 UA + Referer，否则 403） */
    fun defaultHeaders(url: String): Map<String, String> {
        val host = try { URL(url).host.lowercase() } catch (_: Exception) { "" }
        return if (host.contains("bilivideo") || host.contains("bilibili") || host.contains("akamaized")) {
            mapOf("User-Agent" to DESKTOP_UA, "Referer" to "https://www.bilibili.com/")
        } else {
            mapOf("User-Agent" to MOBILE_UA)
        }
    }

    // ---------- 段认领（jicun claimedChunk / claimedChunkWithTail 原样） ----------

    private fun claimedChunk(claim: Long, size: Long, chunkBytes: Long): LongRange? {
        require(size > 0 && chunkBytes > 0 && claim >= 0)
        val start = claim * chunkBytes
        if (start >= size) return null
        return start..(minOf(size, start + chunkBytes) - 1)
    }

    private fun claimedChunkWithTail(claim: Long, size: Long): LongRange? {
        val tailFrom = if (size > TAIL_BYTES * 2) size - TAIL_BYTES else size
        val headCount = (tailFrom + SEGMENT_BYTES - 1) / SEGMENT_BYTES
        if (claim < headCount) return claimedChunk(claim, tailFrom, SEGMENT_BYTES)
        if (tailFrom >= size) return null
        return claimedChunk(claim - headCount, size - tailFrom, TAIL_CHUNK_BYTES)
            ?.let { (it.first + tailFrom)..(it.last + tailFrom) }
    }

    // ---------- 主入口 ----------

    @Throws(IOException::class)
    fun download(
        url: String,
        dest: File,
        headers: Map<String, String> = defaultHeaders(url),
        onProgress: (downloaded: Long, total: Long) -> Unit = { _, _ -> },
        isCancelled: () -> Boolean = { false }
    ): Result {
        val probe = probe(url, headers, isCancelled)
        return if (probe.rangeSupported && probe.size >= SEGMENT_FROM_BYTES) {
            downloadSegmented(url, headers, dest, probe.size, onProgress, isCancelled)
        } else {
            downloadSingle(url, headers, dest, probe, onProgress, isCancelled)
        }
    }

    // ---------- 探针 ----------

    private data class Probe(
        val size: Long,
        val rangeSupported: Boolean,
        val contentType: String?
    )

    @Throws(IOException::class)
    private fun probe(
        url: String,
        headers: Map<String, String>,
        isCancelled: () -> Boolean
    ): Probe {
        var conn: HttpURLConnection? = null
        try {
            conn = open(url, headers + ("Range" to "bytes=0-0"))
            if (isCancelled()) throw CancelledException()
            val code = conn.responseCode
            return when (code) {
                206 -> {
                    val cr = conn.getHeaderField("Content-Range") // bytes 0-0/12345
                    val size = cr?.substringAfterLast('/')?.toLongOrNull() ?: -1L
                    Probe(size, size > 0, conn.contentType)
                }
                200 -> {
                    // 服务端忽略 Range：可能是整文件（微信视频号式），也可能是不支持
                    val len = conn.getHeaderField("Content-Length")?.toLongOrNull() ?: -1L
                    Probe(len, false, conn.contentType)
                }
                else -> throw IOException("probe HTTP $code")
            }
        } finally {
            conn?.disconnect()
        }
    }

    // ---------- 分段并行 ----------

    @Throws(IOException::class)
    private fun downloadSegmented(
        url: String,
        headers: Map<String, String>,
        dest: File,
        size: Long,
        onProgress: (Long, Long) -> Unit,
        isCancelled: () -> Boolean
    ): Result {
        dest.parentFile?.mkdirs()
        val raf = RandomAccessFile(dest, "rw")
        try {
            raf.setLength(size)
        } catch (e: Exception) {
            raf.close()
            throw IOException("preallocate failed: ${e.message}")
        }
        val received = AtomicLong(0)
        val claim = AtomicLong(0)
        val lastReport = AtomicLong(0)
        val lanes = minOf(DEFAULT_LANES, MAX_SEGMENTS)
        val errors = ConcurrentLinkedQueue<Throwable>()
        val latch = CountDownLatch(lanes)
        val pool = Executors.newFixedThreadPool(lanes)

        fun report(force: Boolean = false) {
            val now = System.currentTimeMillis()
            if (force || now - lastReport.get() >= PROGRESS_INTERVAL_MS) {
                lastReport.set(now)
                onProgress(received.get(), size)
            }
        }

        repeat(lanes) {
            pool.execute {
                try {
                    while (true) {
                        if (isCancelled()) throw CancelledException()
                        val chunk = claimedChunkWithTail(claim.getAndIncrement(), size) ?: break
                        downloadChunk(url, headers, chunk, raf, received, size, ::report, isCancelled)
                    }
                } catch (e: Throwable) {
                    if (e !is CancelledException) errors.add(e)
                    else errors.add(e) // 取消也记录，调用方识别
                } finally {
                    latch.countDown()
                }
            }
        }
        try {
            latch.await()
        } finally {
            pool.shutdown()
            raf.close()
        }
        val err = errors.poll()
        if (err is CancelledException) throw err
        if (err != null) throw IOException("segment failed: ${err.message}", err)
        if (isCancelled()) throw CancelledException()
        val actual = dest.length()
        if (actual != size) throw IOException("incomplete: $actual/$size")
        onProgress(size, size)
        return Result(actual, size)
    }

    @Throws(IOException::class)
    private fun downloadChunk(
        url: String,
        headers: Map<String, String>,
        range: LongRange,
        raf: RandomAccessFile,
        received: AtomicLong,
        total: Long,
        report: (Boolean) -> Unit,
        isCancelled: () -> Boolean
    ) {
        var offset = range.first
        var attempt = 0
        while (offset <= range.last) {
            if (isCancelled()) throw CancelledException()
            if (attempt >= CHUNK_ATTEMPT_LIMIT) throw IOException("chunk $range stalled")
            var conn: HttpURLConnection? = null
            try {
                conn = open(url, headers + ("Range" to "bytes=$offset-${range.last}"))
                val code = conn.responseCode
                val okRange = code == 206 ||
                        (code == 200 && offset == 0L && range.first == 0L) // wholeFileAsRange
                if (!okRange) throw IOException("range rejected: HTTP $code")
                val input = conn.inputStream
                val buf = ByteArray(32 * 1024)
                val connStart = System.currentTimeMillis()
                var progressed = false
                while (true) {
                    if (isCancelled()) throw CancelledException()
                    val n = input.read(buf)
                    if (n < 0) break
                    synchronized(raf) {
                        raf.seek(offset)
                        raf.write(buf, 0, n)
                    }
                    offset += n
                    received.addAndGet(n.toLong())
                    progressed = true
                    report(false)
                    // 慢连接轮换：10 秒没收完这一段就掐掉重开
                    if (offset <= range.last &&
                        System.currentTimeMillis() - connStart > CONNECTION_BUDGET_MS
                    ) {
                        break
                    }
                }
                if (offset > range.last) {
                    report(false)
                    return // 本段完成
                }
                // 连接提前结束但还有剩余：换连接续（不计为失败）
                if (!progressed) {
                    attempt++
                    Thread.sleep(minOf(200L * attempt, 2000L))
                }
            } catch (e: IOException) {
                if (e is CancelledException) throw e
                attempt++
                if (attempt >= CHUNK_ATTEMPTS) throw e
                Thread.sleep(minOf(300L * attempt, 2000L))
            } finally {
                conn?.disconnect()
            }
        }
    }

    // ---------- 单连接（小文件 / 不支持 Range） ----------

    @Throws(IOException::class)
    private fun downloadSingle(
        url: String,
        headers: Map<String, String>,
        dest: File,
        probe: Probe,
        onProgress: (Long, Long) -> Unit,
        isCancelled: () -> Boolean
    ): Result {
        dest.parentFile?.mkdirs()
        var attempt = 0
        var lastErr: IOException? = null
        while (attempt < CHUNK_ATTEMPTS) {
            if (isCancelled()) throw CancelledException()
            var conn: HttpURLConnection? = null
            try {
                conn = open(url, headers)
                val code = conn.responseCode
                if (code !in 200..299) throw IOException("HTTP $code")
                val total = probe.size.takeIf { it > 0 } ?: -1L
                // 断点续传：已有部分文件则续
                val startAt = if (dest.exists()) dest.length() else 0L
                var downloaded = startAt
                val lastReport = AtomicLong(0)
                dest.outputStream().use { out ->
                    // 注：续传需服务端支持；此处为简化，单连接失败即整体重试
                    val input = conn.inputStream
                    val buf = ByteArray(64 * 1024)
                    while (true) {
                        if (isCancelled()) throw CancelledException()
                        val n = input.read(buf)
                        if (n < 0) break
                        out.write(buf, 0, n)
                        downloaded += n
                        val now = System.currentTimeMillis()
                        if (now - lastReport.get() >= PROGRESS_INTERVAL_MS) {
                            lastReport.set(now)
                            onProgress(downloaded, total)
                        }
                    }
                }
                if (total > 0 && dest.length() != total) {
                    throw IOException("incomplete: ${dest.length()}/$total")
                }
                onProgress(dest.length(), total.takeIf { it > 0 } ?: dest.length())
                return Result(dest.length(), total)
            } catch (e: IOException) {
                if (e is CancelledException) throw e
                lastErr = e
                attempt++
                if (attempt < CHUNK_ATTEMPTS) Thread.sleep(minOf(500L * attempt, 2000L))
            } finally {
                conn?.disconnect()
            }
        }
        throw lastErr ?: IOException("download failed")
    }

    // ---------- 连接 ----------

    private fun open(url: String, headers: Map<String, String>): HttpURLConnection {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.connectTimeout = 15_000
        conn.readTimeout = 30_000
        conn.instanceFollowRedirects = true
        for ((k, v) in headers) conn.setRequestProperty(k, v)
        return conn
    }
}
