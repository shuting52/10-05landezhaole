package com.example.ui.screens.toolbox

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * v1.4.22 新工具：音乐播放器
 * - 在线搜索试听（iTunes Search API，30 秒预览，无需登录）
 * - 本地音频文件播放（mp3/m4a/wav 等）
 * - 播放/暂停/上一首/下一首/进度条
 * - 致敬开源项目 Folia（folia-major）：全屏沉浸式歌词播放器
 *   原项目 chthollyphile/folia-major（AGPL-3.0），国内镜像 M49P6BK/folia-major
 */
private data class Track(
    val title: String,
    val artist: String,
    val url: String,
    val local: Boolean = false
)

@Composable
fun MusicPlayerSection() {
    val themeColors = LocalThemeUiColors.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var playlist by remember { mutableStateOf<List<Track>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(-1) }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var durationMs by remember { mutableStateOf(0) }
    var positionMs by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Track>>(emptyList()) }
    var searching by remember { mutableStateOf(false) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }

    fun stopPlayer() {
        try {
            player?.stop()
            player?.release()
        } catch (_: Exception) {}
        player = null
        isPlaying = false
        progress = 0f
    }

    fun playTrack(index: Int) {
        val track = playlist.getOrNull(index) ?: return
        stopPlayer()
        try {
            val mp = MediaPlayer()
            if (track.local) {
                mp.setDataSource(context, Uri.parse(track.url))
            } else {
                mp.setDataSource(track.url)
            }
            mp.setOnPreparedListener {
                durationMs = it.duration
                it.start()
                isPlaying = true
                currentIndex = index
            }
            mp.setOnCompletionListener {
                // 自动下一首
                val next = index + 1
                if (next < playlist.size) playTrack(next) else { isPlaying = false }
            }
            mp.setOnErrorListener { _, _, _ ->
                Toast.makeText(context, "播放失败，换一首试试", Toast.LENGTH_SHORT).show()
                false
            }
            mp.prepareAsync()
            player = mp
        } catch (e: Exception) {
            Toast.makeText(context, "播放失败：${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun togglePlayPause() {
        val mp = player
        if (mp == null) {
            if (playlist.isNotEmpty()) playTrack(0)
            return
        }
        if (isPlaying) { mp.pause(); isPlaying = false }
        else { mp.start(); isPlaying = true }
    }

    // 进度轮询
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(500)
            player?.let { mp ->
                try {
                    if (mp.isPlaying) {
                        positionMs = mp.currentPosition
                        durationMs = mp.duration
                        progress = if (durationMs > 0) positionMs.toFloat() / durationMs else 0f
                    }
                } catch (_: Exception) {}
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { stopPlayer() }
    }

    val pickAudioLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            val newTracks = uris.mapIndexed { i, uri ->
                Track(
                    title = "本地音频 ${playlist.size + i + 1}",
                    artist = "本地文件",
                    url = uri.toString(),
                    local = true
                )
            }
            playlist = playlist + newTracks
            Toast.makeText(context, "已添加 ${uris.size} 首本地音频", Toast.LENGTH_SHORT).show()
        }
    }

    fun searchOnline() {
        if (searchQuery.isBlank()) {
            Toast.makeText(context, "请先输入歌名或歌手", Toast.LENGTH_SHORT).show()
            return
        }
        searching = true
        scope.launch {
            val results = withContext(Dispatchers.IO) {
                try {
                    val client = OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build()
                    val url = "https://itunes.apple.com/search?term=" +
                            java.net.URLEncoder.encode(searchQuery.trim(), "UTF-8") +
                            "&media=music&entity=song&limit=20&country=CN"
                    val req = Request.Builder().url(url).build()
                    client.newCall(req).execute().use { resp ->
                        val body = resp.body?.string() ?: return@use emptyList<Track>()
                        val arr = JSONObject(body).optJSONArray("results") ?: return@use emptyList<Track>()
                        (0 until arr.length()).mapNotNull { i ->
                            val o = arr.getJSONObject(i)
                            val preview = o.optString("previewUrl", "")
                            if (preview.isBlank()) null else Track(
                                title = o.optString("trackName", "未知歌曲"),
                                artist = o.optString("artistName", "未知歌手"),
                                url = preview
                            )
                        }
                    }
                } catch (_: Exception) { emptyList<Track>() }
            }
            searching = false
            searchResults = results
            if (results.isEmpty()) Toast.makeText(context, "没搜到，换个关键词试试", Toast.LENGTH_SHORT).show()
        }
    }

    fun fmt(ms: Int): String {
        val s = ms / 1000
        return "%d:%02d".format(s / 60, s % 60)
    }

    val current = playlist.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.MusicNote, contentDescription = null, tint = themeColors.primary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("音乐播放器", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
        }

        // ===== 正在播放 =====
        if (current != null) {
            Text(
                current.title, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                color = themeColors.text, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(
                current.artist, fontSize = 12.sp, color = themeColors.textMuted,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = themeColors.primary,
                trackColor = themeColors.surface
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(fmt(positionMs), fontSize = 11.sp, color = themeColors.textMuted)
                Text(fmt(durationMs), fontSize = 11.sp, color = themeColors.textMuted)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (currentIndex > 0) playTrack(currentIndex - 1)
                }) {
                    Icon(Icons.Filled.SkipPrevious, contentDescription = "上一首", tint = themeColors.primary, modifier = Modifier.size(36.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(onClick = { togglePlayPause() }) {
                    Icon(
                        if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "播放/暂停",
                        tint = themeColors.primary, modifier = Modifier.size(48.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(onClick = {
                    if (currentIndex < playlist.size - 1) playTrack(currentIndex + 1)
                }) {
                    Icon(Icons.Filled.SkipNext, contentDescription = "下一首", tint = themeColors.primary, modifier = Modifier.size(36.dp))
                }
            }
        } else {
            Text("还没有播放内容，先搜索试听或添加本地音频", fontSize = 13.sp, color = themeColors.textMuted)
        }

        // ===== 在线搜索 =====
        Text("在线搜索试听", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("歌名 / 歌手", fontSize = 13.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Button(
                onClick = { searchOnline() },
                enabled = !searching,
                colors = ButtonDefaults.buttonColors(containerColor = themeColors.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (searching) CircularProgressIndicator(color = androidx.compose.ui.graphics.Color.White, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                else Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
        Text("搜索结果为 30 秒试听预览（iTunes 免费接口，无需登录）", fontSize = 11.sp, color = themeColors.textMuted)

        if (searchResults.isNotEmpty()) {
            searchResults.take(10).forEach { track ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            // 加入播放列表并播放
                            val idx = playlist.indexOfFirst { it.url == track.url }
                            if (idx >= 0) playTrack(idx)
                            else {
                                playlist = playlist + track
                                playTrack(playlist.size - 1)
                            }
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Album, contentDescription = null, tint = themeColors.textMuted, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(track.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = themeColors.text, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(track.artist, fontSize = 11.sp, color = themeColors.textMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Icon(Icons.Filled.PlayArrow, contentDescription = "播放", tint = themeColors.primary)
                }
            }
        }

        // ===== 本地音频 =====
        OutlinedButton(
            onClick = { pickAudioLauncher.launch("audio/*") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.MusicNote, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("添加本地音频", fontSize = 13.sp)
        }

        // ===== 播放列表 =====
        if (playlist.isNotEmpty()) {
            Text("播放列表（${playlist.size}）", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(playlist) { i, track ->
                    val selected = i == currentIndex
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { playTrack(i) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${i + 1}. ${track.title}",
                            fontSize = 13.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) themeColors.primary else themeColors.text,
                            maxLines = 1, overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        if (selected && isPlaying) {
                            Icon(Icons.Filled.MusicNote, contentDescription = null, tint = themeColors.primary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
            OutlinedButton(
                onClick = {
                    stopPlayer()
                    playlist = emptyList()
                    currentIndex = -1
                    searchResults = emptyList()
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("清空列表", fontSize = 12.sp)
            }
        }

        // ===== 致敬开源 =====
        Text(
            "灵感来源：开源项目 Folia（folia-major）—— 全屏沉浸式歌词音乐播放器",
            fontSize = 11.sp, color = themeColors.textMuted, lineHeight = 16.sp
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = {
                    openUrl(context, "https://github.com/M49P6BK/folia-major")
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.OpenInBrowser, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("M49P6BK 镜像", fontSize = 11.sp)
            }
            OutlinedButton(
                onClick = {
                    openUrl(context, "https://github.com/chthollyphile/folia-major")
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.OpenInBrowser, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("原项目主页", fontSize = 11.sp)
            }
        }
    }
}

private fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {}
}
