package com.example.ui.screens.toolbox

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.theme.LocalThemeUiColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * v1.4.22 音乐播放器工具页（v1.4.24 重构：共用 MusicPlayerState，与转盘中心迷你播放器同状态）
 * - 在线搜索试听（iTunes Search API，30 秒预览，无需登录）
 * - 本地音频：自动扫描（MediaStore）+ 手动选择
 * - 播放/暂停/上一首/下一首/播放列表
 */
@Composable
fun MusicPlayerSection() {
    val themeColors = LocalThemeUiColors.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<MusicPlayerState.Track>>(emptyList()) }
    var searching by remember { mutableStateOf(false) }
    var scanning by remember { mutableStateOf(false) }
    // v1.7.11：本地搜索状态
    var localQuery by remember { mutableStateOf("") }
    var localResults by remember { mutableStateOf<List<MusicPlayerState.Track>>(emptyList()) }
    var localSearching by remember { mutableStateOf(false) }
    var pendingLocalSearch by remember { mutableStateOf(false) }

    val state = MusicPlayerState
    val current = state.currentTrack

    // 音频权限 launcher（Android 13+ 用 READ_MEDIA_AUDIO，以下用 READ_EXTERNAL_STORAGE）
    val audioPermission = if (Build.VERSION.SDK_INT >= 33)
        Manifest.permission.READ_MEDIA_AUDIO else Manifest.permission.READ_EXTERNAL_STORAGE

    // v1.7.11：本地搜索（歌名/歌手任意文字匹配，输歌手名列出其全部曲目）
    // 实际查询（调用前已确保有权限）
    fun doLocalSearchQuery() {
        val q = localQuery.trim()
        if (q.isBlank()) {
            Toast.makeText(context, "请输入歌名或歌手", Toast.LENGTH_SHORT).show()
            return
        }
        localSearching = true
        scope.launch {
            val results = withContext(Dispatchers.IO) {
                state.searchLocalMusic(context, q)
            }
            localSearching = false
            localResults = results
            if (results.isEmpty()) Toast.makeText(context, "本地没找到，换个关键词试试", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // v1.7.11：授权后若有挂起的本地搜索则执行，否则走原来的全量扫描
            if (pendingLocalSearch) {
                pendingLocalSearch = false
                doLocalSearchQuery()
            } else {
                scanning = true
                scope.launch {
                    state.scanLocalMusic(context)
                    scanning = false
                }
            }
        } else {
            Toast.makeText(context, "需要音频权限才能扫描本地歌曲", Toast.LENGTH_SHORT).show()
        }
    }

    // v1.7.11：本地搜索入口（含权限检查）
    fun requestLocalSearch() {
        if (localQuery.trim().isBlank()) {
            Toast.makeText(context, "请输入歌名或歌手", Toast.LENGTH_SHORT).show()
            return
        }
        if (ContextCompat.checkSelfPermission(context, audioPermission) != PackageManager.PERMISSION_GRANTED) {
            pendingLocalSearch = true
            permissionLauncher.launch(audioPermission)
            return
        }
        doLocalSearchQuery()
    }

    fun doScanLocal() {
        if (ContextCompat.checkSelfPermission(context, audioPermission) == PackageManager.PERMISSION_GRANTED) {
            scanning = true
            scope.launch {
                state.scanLocalMusic(context)
                scanning = false
            }
        } else {
            permissionLauncher.launch(audioPermission)
        }
    }

    val pickAudioLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            var added = 0
            uris.forEachIndexed { i, uri ->
                val track = MusicPlayerState.Track(
                    title = "本地音频 ${state.playlist.size + i + 1}",
                    artist = "本地文件",
                    uri = uri.toString(),
                    isLocal = true
                )
                val before = state.playlist.size
                state.addTrack(track)
                if (state.playlist.size > before) added++
            }
            Toast.makeText(context, "已添加 $added 首本地音频", Toast.LENGTH_SHORT).show()
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
                        val body = resp.body?.string() ?: return@use emptyList<MusicPlayerState.Track>()
                        val arr = JSONObject(body).optJSONArray("results") ?: return@use emptyList<MusicPlayerState.Track>()
                        (0 until arr.length()).mapNotNull { i ->
                            val o = arr.getJSONObject(i)
                            val preview = o.optString("previewUrl", "")
                            if (preview.isBlank()) null else MusicPlayerState.Track(
                                title = o.optString("trackName", "未知歌曲"),
                                artist = o.optString("artistName", "未知歌手"),
                                uri = preview
                            )
                        }
                    }
                } catch (_: Exception) { emptyList<MusicPlayerState.Track>() }
            }
            searching = false
            searchResults = results
            if (results.isEmpty()) Toast.makeText(context, "没搜到，换个关键词试试", Toast.LENGTH_SHORT).show()
        }
    }

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
            if (state.isLoading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = themeColors.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("加载中…", fontSize = 12.sp, color = themeColors.textMuted)
                }
            }
            if (state.errorMsg.isNotBlank()) {
                Text(state.errorMsg, fontSize = 12.sp, color = Color(0xFFE53935))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 播放模式：顺序 / 随机 / 单曲循环
                IconButton(onClick = { state.cyclePlayMode() }) {
                    val modeIcon = when (state.playMode) {
                        MusicPlayerState.PlayMode.SHUFFLE -> Icons.Filled.Shuffle
                        MusicPlayerState.PlayMode.REPEAT_ONE -> Icons.Filled.RepeatOne
                        else -> Icons.Filled.Repeat
                    }
                    val modeDesc = when (state.playMode) {
                        MusicPlayerState.PlayMode.SHUFFLE -> "随机播放"
                        MusicPlayerState.PlayMode.REPEAT_ONE -> "单曲循环"
                        else -> "顺序播放"
                    }
                    Icon(modeIcon, contentDescription = modeDesc, tint = themeColors.primary, modifier = Modifier.size(28.dp))
                }
                IconButton(onClick = { state.playPrev(context) }) {
                    Icon(Icons.Filled.SkipPrevious, contentDescription = "上一首", tint = themeColors.primary, modifier = Modifier.size(36.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { state.togglePlayPause(context) }) {
                    Icon(
                        if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "播放/暂停",
                        tint = themeColors.primary, modifier = Modifier.size(48.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { state.playNext(context) }) {
                    Icon(Icons.Filled.SkipNext, contentDescription = "下一首", tint = themeColors.primary, modifier = Modifier.size(36.dp))
                }
            }
            // 当前播放模式文字提示
            Text(
                when (state.playMode) {
                    MusicPlayerState.PlayMode.SHUFFLE -> "随机播放"
                    MusicPlayerState.PlayMode.REPEAT_ONE -> "单曲循环"
                    else -> "顺序播放"
                },
                fontSize = 11.sp, color = themeColors.textMuted,
                modifier = Modifier.fillMaxWidth(),
            )
            // ===== 歌词 =====
            if (state.currentLyrics.isNotBlank()) {
                Text("歌词", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(themeColors.surface.copy(alpha = 0.6f))
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        state.currentLyrics,
                        fontSize = 12.sp, color = themeColors.text,
                        lineHeight = 20.sp
                    )
                }
            } else if (current.isLocal) {
                Text("暂无内嵌歌词", fontSize = 11.sp, color = themeColors.textMuted)
            }
        } else {
            Text("还没有播放内容，先搜索试听或扫描本地歌曲", fontSize = 13.sp, color = themeColors.textMuted)
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
                if (searching) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
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
                            val idx = state.playlist.indexOfFirst { it.uri == track.uri }
                            if (idx >= 0) state.playAt(idx, context)
                            else {
                                state.addTrack(track)
                                state.playAt(state.playlist.size - 1, context)
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

        // ===== 本地搜索（v1.7.11：搜本机音乐，输歌手名列出其全部曲目）=====
        Text("本地搜索", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = localQuery,
                onValueChange = { localQuery = it },
                placeholder = { Text("歌名 / 歌手，搜本地音乐", fontSize = 13.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Button(
                onClick = { requestLocalSearch() },
                enabled = !localSearching,
                colors = ButtonDefaults.buttonColors(containerColor = themeColors.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (localSearching) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                else Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
        if (localResults.isNotEmpty()) {
            Text("找到 ${localResults.size} 首本地歌曲", fontSize = 11.sp, color = themeColors.textMuted)
            localResults.forEach { track ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            val idx = state.playlist.indexOfFirst { it.uri == track.uri }
                            if (idx >= 0) state.playAt(idx, context)
                            else {
                                state.addTrack(track)
                                state.playAt(state.playlist.size - 1, context)
                            }
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.MusicNote, contentDescription = null, tint = themeColors.textMuted, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(track.title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = themeColors.text, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(track.artist, fontSize = 11.sp, color = themeColors.textMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Icon(Icons.Filled.PlayArrow, contentDescription = "播放", tint = themeColors.primary)
                }
            }
        }

        // ===== 本地歌曲 =====
        Text("本地歌曲", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { doScanLocal() },
                enabled = !scanning,
                colors = ButtonDefaults.buttonColors(containerColor = themeColors.primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                if (scanning) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                else Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (scanning) "扫描中…" else "自动扫描本地歌曲", fontSize = 13.sp)
            }
            OutlinedButton(
                onClick = { pickAudioLauncher.launch("audio/*") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.FolderOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("手动添加", fontSize = 13.sp)
            }
        }
        Text("自动扫描会找出手机里时长超过 30 秒的音乐文件", fontSize = 11.sp, color = themeColors.textMuted)

        // ===== 播放列表 =====
        if (state.playlist.isNotEmpty()) {
            Text("播放列表（${state.playlist.size}）", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(state.playlist) { i, track ->
                    val selected = i == state.currentIndex
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { state.playAt(i, context) }
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
                        if (track.isLocal) {
                            Text("本地", fontSize = 10.sp, color = themeColors.textMuted)
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        if (selected && state.isPlaying) {
                            Icon(Icons.Filled.MusicNote, contentDescription = null, tint = themeColors.primary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
            OutlinedButton(
                onClick = {
                    state.clearAll()
                    searchResults = emptyList()
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("清空列表", fontSize = 12.sp)
            }
        }
    }
}
