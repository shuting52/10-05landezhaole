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

    private var player: MediaPlayer? = null

    val currentTrack: Track?
        get() = playlist.getOrNull(currentIndex)

    fun playAt(index: Int, context: Context? = null) {
        if (index !in playlist.indices) return
        try {
            player?.release()
            player = null
            val track = playlist[index]
            isLoading = true
            errorMsg = ""
            player = MediaPlayer().apply {
                setDataSource(track.uri)
                setOnPreparedListener {
                    isLoading = false
                    start()
                    isPlaying = true
                    currentIndex = index
                }
                setOnCompletionListener {
                    // 自动下一首
                    val next = currentIndex + 1
                    if (next in playlist.indices) {
                        playAt(next, context)
                    } else {
                        isPlaying = false
                    }
                }
                setOnErrorListener { _, _, _ ->
                    isLoading = false
                    isPlaying = false
                    errorMsg = "播放失败，试试下一首"
                    context?.let {
                        Toast.makeText(it, "播放失败，已跳过", Toast.LENGTH_SHORT).show()
                    }
                    val next = currentIndex + 1
                    if (next in playlist.indices) playAt(next, context)
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            isLoading = false
            errorMsg = "无法播放：${e.message}"
        }
    }

    fun togglePlayPause(context: Context? = null) {
        val p = player
        if (p == null) {
            // 没有播放器时从头播放
            if (playlist.isNotEmpty()) playAt(0.coerceAtMost(playlist.size - 1).let { if (currentIndex >= 0) currentIndex else 0 }, context)
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

    fun playNext(context: Context? = null) {
        if (playlist.isEmpty()) return
        val next = if (currentIndex + 1 in playlist.indices) currentIndex + 1 else 0
        playAt(next, context)
    }

    fun playPrev(context: Context? = null) {
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

    fun release() {
        try {
            player?.release()
        } catch (_: Exception) {}
        player = null
        isPlaying = false
    }
}
