package com.example.ui.screens.toolbox

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 音乐播放器共享状态（单例）
 * 供「转盘中心迷你播放器」与「音乐播放器工具页」共用同一播放状态
 */
object MusicPlayerState {

    data class Track(
        val title: String,
        val artist: String,
        val uri: String,       // 本地 content:// 或在线 http(s)://
        val artworkUrl: String = "",
        val isLocal: Boolean = false
    )

    val playlist = mutableStateListOf<Track>()
    var currentIndex by mutableStateOf(-1)
    var isPlaying by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var errorMsg by mutableStateOf("")

    /** 播放模式：顺序 / 随机 / 单曲循环 */
    enum class PlayMode { SEQUENTIAL, SHUFFLE, REPEAT_ONE }
    var playMode by mutableStateOf(PlayMode.SEQUENTIAL)

    fun cyclePlayMode() {
        playMode = when (playMode) {
            PlayMode.SEQUENTIAL -> PlayMode.SHUFFLE
            PlayMode.SHUFFLE -> PlayMode.REPEAT_ONE
            PlayMode.REPEAT_ONE -> PlayMode.SEQUENTIAL
        }
    }

    private var player: MediaPlayer? = null

    val currentTrack: Track?
        get() = playlist.getOrNull(currentIndex)

    fun playAt(index: Int, context: Context) {
        if (index !in playlist.indices) return
        try {
            player?.release()
            player = null
            val track = playlist[index]
            isLoading = true
            errorMsg = ""
            currentLyrics = ""
            // 异步加载歌词（不阻塞播放）
            try {
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    loadLyrics(context, track)
                }
            } catch (_: Exception) {}
            player = MediaPlayer().apply {
                // v1.4.25 修复：本地 content:// URI 必须用 setDataSource(Context, Uri)，
                // 用 String 版本会被当成文件路径导致播放失败
                if (track.isLocal) {
                    setDataSource(context, Uri.parse(track.uri))
                } else {
                    setDataSource(track.uri)
                }
                setOnPreparedListener {
                    MusicPlayerState.isLoading = false
                    start()
                    MusicPlayerState.isPlaying = true
                    MusicPlayerState.currentIndex = index
                }
                setOnCompletionListener {
                    // 按播放模式决定下一首
                    when (MusicPlayerState.playMode) {
                        PlayMode.REPEAT_ONE -> {
                            MusicPlayerState.playAt(MusicPlayerState.currentIndex, context)
                        }
                        PlayMode.SHUFFLE -> {
                            val size = MusicPlayerState.playlist.size
                            if (size > 1) {
                                var next: Int
                                do {
                                    next = (0 until size).random()
                                } while (next == MusicPlayerState.currentIndex)
                                MusicPlayerState.playAt(next, context)
                            } else if (size == 1) {
                                MusicPlayerState.playAt(0, context)
                            }
                        }
                        PlayMode.SEQUENTIAL -> {
                            val next = MusicPlayerState.currentIndex + 1
                            if (next in MusicPlayerState.playlist.indices) {
                                MusicPlayerState.playAt(next, context)
                            } else {
                                MusicPlayerState.isPlaying = false
                            }
                        }
                    }
                }
                setOnErrorListener { _, _, _ ->
                    MusicPlayerState.isLoading = false
                    MusicPlayerState.isPlaying = false
                    MusicPlayerState.errorMsg = "播放失败，试试下一首"
                    Toast.makeText(context, "播放失败，已跳过", Toast.LENGTH_SHORT).show()
                    val next = MusicPlayerState.currentIndex + 1
                    if (next in MusicPlayerState.playlist.indices) MusicPlayerState.playAt(next, context)
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            isLoading = false
            errorMsg = "无法播放：${e.message}"
        }
    }

    fun togglePlayPause(context: Context) {
        val p = player
        if (p == null) {
            // 没有播放器时从头播放
            if (playlist.isNotEmpty()) playAt(if (currentIndex >= 0) currentIndex else 0, context)
            return
        }
        if (p.isPlaying) {
            p.pause()
            isPlaying = false
        } else {
            p.start()
            isPlaying = true
        }
    }

    fun playNext(context: Context) {
        if (playlist.isEmpty()) return
        // 随机模式下手动下一首也随机
        val next = if (playMode == PlayMode.SHUFFLE && playlist.size > 1) {
            var n: Int
            do { n = (0 until playlist.size).random() } while (n == currentIndex)
            n
        } else if (currentIndex + 1 in playlist.indices) currentIndex + 1 else 0
        playAt(next, context)
    }

    fun playPrev(context: Context) {
        if (playlist.isEmpty()) return
        val prev = if (currentIndex - 1 >= 0) currentIndex - 1 else playlist.size - 1
        playAt(prev, context)
    }

    fun addTrack(track: Track) {
        // 去重：同 uri 不重复添加
        if (playlist.none { it.uri == track.uri }) {
            playlist.add(track)
        }
    }

    fun clearAll() {
        try {
            player?.release()
        } catch (_: Exception) {}
        player = null
        playlist.clear()
        currentIndex = -1
        isPlaying = false
        isLoading = false
    }

    /**
     * 自动扫描本地歌曲（MediaStore）
     * @return 新增的歌曲数量
     */
    suspend fun scanLocalMusic(context: Context): Int = withContext(Dispatchers.IO) {
        val found = mutableListOf<Track>()
        try {
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
            )
            // 只收录时长 > 30 秒的音乐文件，过滤短音效/通知音
            val selection = "${MediaStore.Audio.Media.DURATION} > ? AND ${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val selectionArgs = arrayOf("30000")
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, sortOrder
            )?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val title = cursor.getString(titleCol) ?: "未知歌曲"
                    val artist = cursor.getString(artistCol) ?: "未知歌手"
                    val uri = Uri.withAppendedPath(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString()
                    ).toString()
                    found.add(Track(title = title, artist = artist, uri = uri, isLocal = true))
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "扫描失败：${e.message}", Toast.LENGTH_SHORT).show()
            }
            return@withContext 0
        }

        var added = 0
        withContext(Dispatchers.Main) {
            found.forEach { track ->
                if (playlist.none { it.uri == track.uri }) {
                    playlist.add(track)
                    added++
                }
            }
            Toast.makeText(context, "扫描完成，新增 $added 首本地歌曲", Toast.LENGTH_SHORT).show()
        }
        added
    }

    /**
     * 本地搜索：按关键词匹配歌名/歌手（MediaStore）
     * 输入歌手名即可列出该歌手的全部曲目；任意文字匹配歌名或歌手
     */
    suspend fun searchLocalMusic(context: Context, keyword: String): List<Track> = withContext(Dispatchers.IO) {
        val found = mutableListOf<Track>()
        try {
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
            )
            val like = "%" + keyword.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%"
            val selection = "(${MediaStore.Audio.Media.DURATION} > ?) AND " +
                    "(${MediaStore.Audio.Media.IS_MUSIC} != 0) AND " +
                    "(${MediaStore.Audio.Media.TITLE} LIKE ? ESCAPE '\\' OR " +
                    "${MediaStore.Audio.Media.ARTIST} LIKE ? ESCAPE '\\')"
            val selectionArgs = arrayOf("30000", like, like)
            val sortOrder = "${MediaStore.Audio.Media.ARTIST} ASC, ${MediaStore.Audio.Media.TITLE} ASC"

            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, sortOrder
            )?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val title = cursor.getString(titleCol) ?: "未知歌曲"
                    val artist = cursor.getString(artistCol) ?: "未知歌手"
                    val uri = Uri.withAppendedPath(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString()
                    ).toString()
                    found.add(Track(title = title, artist = artist, uri = uri, isLocal = true))
                }
            }
        } catch (_: Exception) { }
        found
    }

    fun release() {
        try {
            player?.release()
        } catch (_: Exception) {}
        player = null
        isPlaying = false
    }

    /**
     * 提取内嵌歌词（MediaMetadataRetriever）
     * 部分 MP3/M4A 文件内嵌了 USLT 歌词；没有则返回空
     */
    var currentLyrics by mutableStateOf("")

    fun loadLyrics(context: Context, track: Track) {
        currentLyrics = ""
        if (!track.isLocal) return // 在线试听无歌词
        try {
            val retriever = android.media.MediaMetadataRetriever()
            try {
                retriever.setDataSource(context, Uri.parse(track.uri))
                // 尝试读取内嵌歌词（部分 MP3/M4A 内嵌了 USLT 歌词）
                // METADATA_KEY_LYRICS 需要 API 35+，用反射兼容低版本
                var lyrics: String? = null
                try {
                    val field = android.media.MediaMetadataRetriever::class.java.getField("METADATA_KEY_LYRICS")
                    val key = field.getInt(null)
                    lyrics = retriever.extractMetadata(key)
                } catch (_: Exception) {}
                // 部分文件歌词藏在标题里（带时间轴的 LRC 文本）
                if (lyrics.isNullOrBlank()) {
                    val title = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_TITLE)
                    if (title != null && title.contains("[") && title.contains("]")) lyrics = title
                }
                if (!lyrics.isNullOrBlank()) {
                    // 清理 LRC 时间标签，保留纯文本
                    currentLyrics = lyrics.lines()
                        .map { it.replace(Regex("\\[\\d+:\\d+\\.\\d+\\]"), "").trim() }
                        .filter { it.isNotBlank() }
                        .joinToString("\n")
                        .ifBlank { lyrics.trim() }
                }
            } finally {
                try { retriever.release() } catch (_: Exception) {}
            }
        } catch (_: Exception) {}
    }
}
