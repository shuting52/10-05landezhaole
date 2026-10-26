package com.example.ui.screens.toolbox

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * v1.7.91 新工具：图片去水印（复刻嗨小葩图片去水印的用法，算法本地实现）
 * - 选图 → 在图上拖动框选水印区域（可框多个）→ 本地修复算法去除 → 保存到相册 / 分享
 * - 与嗨小葩不同：不走对方服务端接口，全程本地处理，不上传图片
 * - 修复算法：边界扩散填充 + 平滑 + 边缘羽化；小面积水印效果最好，大面积复杂纹理会有轻微模糊
 */
@Composable
fun ImageWatermarkSection() {
    val themeColors = LocalThemeUiColors.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var original by remember { mutableStateOf<Bitmap?>(null) }
    var boxes by remember { mutableStateOf<List<Rect>>(emptyList()) }
    var result by remember { mutableStateOf<Bitmap?>(null) }
    var savedUri by remember { mutableStateOf<Uri?>(null) }
    var showOriginal by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var dragCurrent by remember { mutableStateOf<Offset?>(null) }

    val pickLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val bmp = withContext(Dispatchers.IO) { decodeBitmap(context, uri) }
            if (bmp == null) {
                Toast.makeText(context, "图片读取失败，换一张试试", Toast.LENGTH_SHORT).show()
            } else {
                original = bmp
                boxes = emptyList()
                result = null
                savedUri = null
                showOriginal = false
            }
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
            Icon(Icons.Filled.Image, contentDescription = null, tint = themeColors.primary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("图片去水印", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
        }
        Text("在图上拖动框选水印区域，可框多个；本地处理，不上传。", fontSize = 12.sp, color = themeColors.textMuted)
        Text("小面积水印效果最好；大面积复杂纹理会有轻微模糊。", fontSize = 12.sp, color = themeColors.textMuted)

        Button(
            onClick = { pickLauncher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = themeColors.primary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (original == null) "选择图片" else "重新选图", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        val bmp = original
        if (bmp != null) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.05f))
            ) {
                val scale = constraints.maxWidth.toFloat() / bmp.width.toFloat()
                val displayHeightDp = with(LocalDensity.current) { (bmp.height * scale).roundToInt().toDp() }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(displayHeightDp)
                        .pointerInput(bmp) {
                            detectDragGestures(
                                onDragStart = { dragStart = it; dragCurrent = it },
                                onDragEnd = {
                                    val s = dragStart
                                    val c = dragCurrent
                                    dragStart = null
                                    dragCurrent = null
                                    if (s != null && c != null) {
                                        val x1 = (min(s.x, c.x) / scale).roundToInt().coerceIn(0, bmp.width - 1)
                                        val x2 = (max(s.x, c.x) / scale).roundToInt().coerceIn(0, bmp.width - 1)
                                        val y1 = (min(s.y, c.y) / scale).roundToInt().coerceIn(0, bmp.height - 1)
                                        val y2 = (max(s.y, c.y) / scale).roundToInt().coerceIn(0, bmp.height - 1)
                                        if (x2 - x1 >= 8 && y2 - y1 >= 8) {
                                            boxes = boxes + Rect(x1, y1, x2, y2)
                                        }
                                    }
                                },
                                onDrag = { change, _ -> dragCurrent = change.position }
                            )
                        }
                ) {
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "待处理图片",
                        modifier = Modifier.fillMaxSize()
                    )
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        for (box in boxes) {
                            drawRect(
                                color = Color.Red,
                                topLeft = Offset(box.left * scale, box.top * scale),
                                size = Size((box.right - box.left) * scale, (box.bottom - box.top) * scale),
                                style = Stroke(width = 3f)
                            )
                        }
                        val s = dragStart
                        val c = dragCurrent
                        if (s != null && c != null) {
                            val left = min(s.x, c.x)
                            val top = min(s.y, c.y)
                            drawRect(
                                color = Color.Red.copy(alpha = 0.25f),
                                topLeft = Offset(left, top),
                                size = Size(abs(c.x - s.x), abs(c.y - s.y))
                            )
                            drawRect(
                                color = Color.Red,
                                topLeft = Offset(left, top),
                                size = Size(abs(c.x - s.x), abs(c.y - s.y)),
                                style = Stroke(width = 3f)
                            )
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (boxes.isEmpty()) "还没框选：在图上拖动框住水印" else "已框选 ${boxes.size} 处",
                    fontSize = 12.sp,
                    color = themeColors.textMuted,
                    modifier = Modifier.weight(1f)
                )
                if (boxes.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { boxes = emptyList(); result = null },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("清除框选", fontSize = 13.sp)
                    }
                }
            }

            Button(
                onClick = {
                    val src = original ?: return@Button
                    val sel = boxes
                    if (sel.isEmpty()) {
                        Toast.makeText(context, "先在图上框选水印区域", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isProcessing = true
                    scope.launch {
                        val out = withContext(Dispatchers.Default) { inpaintBitmap(src, sel) }
                        result = out
                        savedUri = null
                        showOriginal = false
                        isProcessing = false
                    }
                },
                enabled = !isProcessing && boxes.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = themeColors.primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isProcessing) "处理中…" else "去除水印", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(themeColors.surface.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Image, contentDescription = null, tint = themeColors.textMuted, modifier = Modifier.size(44.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("选一张带水印的图片开始", fontSize = 12.sp, color = themeColors.textMuted)
                }
            }
        }

        val outBmp = result
        if (outBmp != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("处理结果", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = themeColors.text, modifier = Modifier.weight(1f))
                OutlinedButton(
                    onClick = { showOriginal = !showOriginal },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (showOriginal) "看效果" else "对比原图", fontSize = 13.sp)
                }
            }
            val showBmp = if (showOriginal) original else outBmp
            if (showBmp != null) {
                Image(
                    bitmap = showBmp.asImageBitmap(),
                    contentDescription = "处理结果",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        val out = result ?: return@Button
                        scope.launch {
                            val uri = withContext(Dispatchers.IO) { saveBitmapToGallery(context, out) }
                            if (uri == null) {
                                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show()
                            } else {
                                savedUri = uri
                                Toast.makeText(context, "已保存到相册（Pictures/懒得找了）", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColors.primary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("保存到相册", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                OutlinedButton(
                    onClick = {
                        val uri = savedUri
                        if (uri != null) {
                            shareBitmap(context, uri)
                        } else {
                            val out = result ?: return@OutlinedButton
                            scope.launch {
                                val newUri = withContext(Dispatchers.IO) { saveBitmapToGallery(context, out) }
                                if (newUri == null) {
                                    Toast.makeText(context, "保存失败，无法分享", Toast.LENGTH_SHORT).show()
                                } else {
                                    savedUri = newUri
                                    shareBitmap(context, newUri)
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("分享", fontSize = 13.sp)
                }
            }
        }
    }
}

/** 读取用户选中的图片：限制长边 4096 以内防 OOM，并按 EXIF 方向转正。 */
private fun decodeBitmap(context: Context, uri: Uri, maxDim: Int = 4096): Bitmap? {
    return try {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
        var sample = 1
        val maxSide = max(bounds.outWidth, bounds.outHeight)
        while (maxSide / sample > maxDim) sample *= 2
        val opts = BitmapFactory.Options().apply {
            inSampleSize = sample
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        var bmp = context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opts) }
            ?: return null
        val orientation = context.contentResolver.openInputStream(uri)?.use { input ->
            try {
                ExifInterface(input).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            } catch (_: Exception) {
                ExifInterface.ORIENTATION_NORMAL
            }
        } ?: ExifInterface.ORIENTATION_NORMAL
        val degrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        if (degrees != 0f) {
            val matrix = Matrix().apply { postRotate(degrees) }
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
        }
        if (bmp.config != Bitmap.Config.ARGB_8888) {
            bmp.copy(Bitmap.Config.ARGB_8888, true)?.let { bmp = it }
        }
        bmp
    } catch (_: Exception) {
        null
    }
}

/**
 * 本地修复：对每个框选区域做边界扩散填充（从边缘向内逐层取已知邻居均值），
 * 再做 3 轮平滑，最后把框选边缘 2px 与原图羽化混合，藏住接缝。
 * 只处理框选区域 + 2px 外扩，不动图片其他部分。
 */
private fun inpaintBitmap(src: Bitmap, boxes: List<Rect>): Bitmap {
    val result = src.copy(Bitmap.Config.ARGB_8888, true)
    for (box in boxes) {
        val left = max(0, box.left - 2)
        val top = max(0, box.top - 2)
        val right = min(src.width - 1, box.right + 2)
        val bottom = min(src.height - 1, box.bottom + 2)
        val w = right - left + 1
        val h = bottom - top + 1
        if (w <= 0 || h <= 0) continue
        val pixels = IntArray(w * h)
        result.getPixels(pixels, 0, w, left, top, w, h)
        val original = pixels.copyOf()
        val mask = BooleanArray(w * h) { idx ->
            val x = idx % w + left
            val y = idx / w + top
            x in box.left..box.right && y in box.top..box.bottom
        }
        if (mask.none { it }) continue
        val known = BooleanArray(w * h) { !mask[it] }
        val queue = ArrayDeque<Int>()
        for (i in mask.indices) {
            if (!mask[i]) continue
            val x = i % w
            val y = i / w
            val hasKnownNeighbor = (x > 0 && known[i - 1]) || (x < w - 1 && known[i + 1]) ||
                (y > 0 && known[i - w]) || (y < h - 1 && known[i + w])
            if (hasKnownNeighbor) queue.add(i)
        }
        while (queue.isNotEmpty()) {
            val i = queue.removeFirst()
            if (known[i]) continue
            val x = i % w
            val y = i / w
            var rs = 0
            var gs = 0
            var bs = 0
            var n = 0
            if (x > 0 && known[i - 1]) { val p = pixels[i - 1]; rs += p shr 16 and 0xFF; gs += p shr 8 and 0xFF; bs += p and 0xFF; n++ }
            if (x < w - 1 && known[i + 1]) { val p = pixels[i + 1]; rs += p shr 16 and 0xFF; gs += p shr 8 and 0xFF; bs += p and 0xFF; n++ }
            if (y > 0 && known[i - w]) { val p = pixels[i - w]; rs += p shr 16 and 0xFF; gs += p shr 8 and 0xFF; bs += p and 0xFF; n++ }
            if (y < h - 1 && known[i + w]) { val p = pixels[i + w]; rs += p shr 16 and 0xFF; gs += p shr 8 and 0xFF; bs += p and 0xFF; n++ }
            if (n == 0) continue
            pixels[i] = 0xFF000000.toInt() or (rs / n shl 16) or (gs / n shl 8) or (bs / n)
            known[i] = true
            if (x > 0 && mask[i - 1] && !known[i - 1]) queue.add(i - 1)
            if (x < w - 1 && mask[i + 1] && !known[i + 1]) queue.add(i + 1)
            if (y > 0 && mask[i - w] && !known[i - w]) queue.add(i - w)
            if (y < h - 1 && mask[i + w] && !known[i + w]) queue.add(i + w)
        }
        repeat(3) {
            val next = pixels.copyOf()
            for (i in mask.indices) {
                if (!mask[i]) continue
                val x = i % w
                val y = i / w
                var rs = 0
                var gs = 0
                var bs = 0
                var n = 0
                if (x > 0) { val p = pixels[i - 1]; rs += p shr 16 and 0xFF; gs += p shr 8 and 0xFF; bs += p and 0xFF; n++ }
                if (x < w - 1) { val p = pixels[i + 1]; rs += p shr 16 and 0xFF; gs += p shr 8 and 0xFF; bs += p and 0xFF; n++ }
                if (y > 0) { val p = pixels[i - w]; rs += p shr 16 and 0xFF; gs += p shr 8 and 0xFF; bs += p and 0xFF; n++ }
                if (y < h - 1) { val p = pixels[i + w]; rs += p shr 16 and 0xFF; gs += p shr 8 and 0xFF; bs += p and 0xFF; n++ }
                if (n > 0) next[i] = 0xFF000000.toInt() or (rs / n shl 16) or (gs / n shl 8) or (bs / n)
            }
            next.copyInto(pixels)
        }
        for (i in mask.indices) {
            if (!mask[i]) continue
            val x = i % w + left
            val y = i / w + top
            val edgeDist = min(min(x - box.left, box.right - x), min(y - box.top, box.bottom - y))
            if (edgeDist == 0) {
                pixels[i] = blendPixels(pixels[i], original[i], 0.5f)
            } else if (edgeDist == 1) {
                pixels[i] = blendPixels(pixels[i], original[i], 0.25f)
            }
        }
        result.setPixels(pixels, 0, w, left, top, w, h)
    }
    return result
}

private fun blendPixels(filled: Int, orig: Int, origWeight: Float): Int {
    val r = ((filled shr 16 and 0xFF) * (1 - origWeight) + (orig shr 16 and 0xFF) * origWeight).roundToInt()
    val g = ((filled shr 8 and 0xFF) * (1 - origWeight) + (orig shr 8 and 0xFF) * origWeight).roundToInt()
    val b = ((filled and 0xFF) * (1 - origWeight) + (orig and 0xFF) * origWeight).roundToInt()
    return 0xFF000000.toInt() or (r shl 16) or (g shl 8) or b
}

/** 保存到相册：Android 10+ 走 MediaStore（无需权限），低版本走应用专属目录 + 媒体库扫描。 */
private fun saveBitmapToGallery(context: Context, bitmap: Bitmap): Uri? {
    return try {
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val name = "懒得找了去水印_$stamp.jpg"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/懒得找了")
            }
            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: return null
            context.contentResolver.openOutputStream(uri)?.use { out ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)) return null
            }
            uri
        } else {
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
            val file = java.io.File(dir, name)
            file.outputStream().use { out ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)) return null
            }
            android.media.MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), arrayOf("image/jpeg"), null)
            Uri.fromFile(file)
        }
    } catch (_: Exception) {
        null
    }
}

private fun shareBitmap(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/jpeg"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "分享去水印图片"))
}
