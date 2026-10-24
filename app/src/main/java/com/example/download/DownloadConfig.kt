package com.example.download

/**
 * 下载器可调参数（翻译自 shuiyingongju 的 lib/download_config.dart DownloadTuning）。
 *
 * 与 NativeDownloader 内部常量保持一致的那几个（SEGMENT_BYTES 等）是协议的一部分，
 * 留在 NativeDownloader 里不许动；这里只放真正调过的档位。
 */
data class DownloadTuning(
    /** 同时下载的文件数。 */
    val concurrency: Int = 4,
    /** 超过这个字节数才分段并行。 */
    val segmentedFromBytes: Long = 8L * 1024 * 1024,
    /** 一段多大。 */
    val segmentBytes: Long = 4L * 1024 * 1024,
    /** 一个大文件最多同时开几条 Range 连接。32 是真机扫出来的，别往下调。 */
    val maxSegments: Int = 32,
    /** 一条连接收一段的超时预算（毫秒）。 */
    val connectionBudgetMs: Long = 10_000L,
)
