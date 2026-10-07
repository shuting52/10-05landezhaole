package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.CutePeach
import com.example.ui.theme.CutePink
import com.example.ui.theme.FlameRed
import com.example.ui.theme.JadeGreen
import com.example.ui.theme.SunsetOrange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

private data class FeedbackCategoryItem(
    val title: String,
    val subtitle: String
)

private val FEEDBACK_CATEGORIES = listOf(
    FeedbackCategoryItem("🐛 软件 BUG", "异常闪退、功能报错、页面崩溃"),
    FeedbackCategoryItem("💡 功能优化", "交互体验改进、界面排版建议"),
    FeedbackCategoryItem("🎨 主题外观", "可爱主题、颜色搭配、动态效果建议"),
    FeedbackCategoryItem("🔍 资源补充", "增加新工具、扩展应用导航收录"),
    FeedbackCategoryItem("⚡ 卡顿闪退", "响应缓慢、内存占用或设备兼容"),
    FeedbackCategoryItem("❓ 其它疑问", "商务合作、交流咨询及其他反馈")
)

/**
 * 软件反馈弹窗（横屏友好适配版）
 * - 全屏宽 Dialog，采用「横屏优先」自适应排版：
 *   横屏（宽 > 高）时左右双栏呈现：左侧反馈类型时间轴 + 右侧表单与操作按钮；
 *   竖屏设备时自动回退单栏纵向滚动，保证任何方向都完整可用。
 * - 逻辑不变：FormSubmit 真实送达开发者邮箱，失败回退系统邮件客户端。
 */
@Composable
fun FeedbackDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    // v1.0.4：读取控制台配置的官方反馈邮箱（formsubmit 送达目标）
    cloudSettings: com.example.data.remote.SettingsDto? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedCategory by remember { mutableStateOf(FEEDBACK_CATEGORIES[0]) }
    var feedbackContent by remember { mutableStateOf("") }
    var userContact by remember { mutableStateOf("") }
    var includeDeviceInfo by remember { mutableStateOf(true) }
    var isSending by remember { mutableStateOf(false) }

    // v1.0.4：官方反馈邮箱 = 控制台 settings.feedbackEmail（优先），未配置则回退内置邮箱
    // v1.0.12：默认接收邮箱更新为 chenshuting0923@gmail.com
    val feedbackEmail = remember(cloudSettings) {
        cloudSettings?.feedbackEmail?.trim()?.takeIf { it.isNotBlank() } ?: "chenshuting0923@gmail.com"
    }

    val deviceInfoSummary = remember {
        // v1.0.1：版本号动态取自 BuildConfig，不再硬编码，避免发版后反馈信息携带旧版本号
        "应用：懒得找了 v${com.example.BuildConfig.VERSION_NAME} (code ${com.example.BuildConfig.VERSION_CODE}) | 机型：${Build.MANUFACTURER} ${Build.MODEL} | Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }

    val buildFullReport = {
        buildString {
            appendLine("【软件反馈】")
            appendLine("类型：${selectedCategory.title}")
            appendLine("时间：${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}")
            appendLine()
            appendLine("内容：")
            appendLine(feedbackContent.ifBlank { "（无描述）" })
            appendLine()
            appendLine("联系方式：${userContact.ifBlank { "未留" }}")
            if (includeDeviceInfo) {
                appendLine()
                appendLine("环境信息：$deviceInfoSummary")
            }
        }
    }

    val sendDirectFeedback = {
        if (feedbackContent.isBlank()) {
            Toast.makeText(context, "请先填写反馈内容", Toast.LENGTH_SHORT).show()
        } else {
            isSending = true
            coroutineScope.launch {
                val fullReport = buildFullReport()
                // v1.0.4：优先通过 FormSubmit 邮件服务真实送达官方反馈邮箱（无需用户安装邮件客户端）
                val email = feedbackEmail
                var sentMsg = ""
                val ok = withContext(Dispatchers.IO) {
                    try {
                        val client = OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(15, TimeUnit.SECONDS)
                            .build()
                        val json = JSONObject().apply {
                            put("_subject", "【懒得找了·软件反馈】${selectedCategory.title}")
                            put("_template", "table")
                            put("_captcha", "false")
                            put("_replyto", userContact.ifBlank { "" })
                            put("反馈类型", selectedCategory.title)
                            put("反馈内容", feedbackContent)
                            put("联系方式", userContact.ifBlank { "未留" })
                            put("设备环境", if (includeDeviceInfo) deviceInfoSummary else "未附带")
                            put("时间", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
                        }
                        val body = json.toString().toRequestBody("application/json".toMediaType())
                        val request = Request.Builder()
                            .url("https://formsubmit.co/ajax/$email")
                            .post(body)
                            .build()
                        client.newCall(request).execute().use { resp ->
                            sentMsg = resp.body?.string() ?: ""
                            resp.isSuccessful || resp.code == 200 || resp.code == 201
                        }
                    } catch (e: Exception) {
                        sentMsg = e.message ?: ""
                        false
                    }
                }
                isSending = false
                if (ok) {
                    // 本地留底
                    try {
                        val sp = context.getSharedPreferences("feedback_records", Context.MODE_PRIVATE)
                        val prev = sp.getString("history", "") ?: ""
                        sp.edit().putString("history", "$fullReport\n---\n$prev").apply()
                    } catch (_: Exception) {}
                    Toast.makeText(context, "✅ 反馈已送达官方邮箱（$email），感谢您的宝贵建议", Toast.LENGTH_LONG).show()
                    onDismiss()
                } else {
                    // v1.0.4 修复收不到反馈：失败时先复制到剪贴板保证不丢失，再尝试邮件客户端代发
                    try {
                        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.setPrimaryClip(ClipData.newPlainText("反馈信息", fullReport))
                    } catch (_: Exception) {}
                    try {
                        val subject = Uri.encode("【懒得找了·软件反馈】${selectedCategory.title}")
                        val body = Uri.encode(fullReport)
                        val mailto = "mailto:$email?subject=$subject&body=$body"
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(mailto))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        Toast.makeText(
                            context,
                            "网络发送未成功（${sentMsg.take(30)}），已复制反馈并打开邮件客户端代发；首次使用请在邮箱中点击确认激活后即可正常接收",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "发送失败，反馈已复制到剪贴板（请粘贴发至 $email）", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    // 反馈类型选择（时间轴样式，支持在横屏左栏 / 竖屏上方复用）
    @Composable
    fun CategoryPicker(onPick: (FeedbackCategoryItem) -> Unit = { selectedCategory = it }) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val totalSteps = FEEDBACK_CATEGORIES.size
            val selectedIndex = FEEDBACK_CATEGORIES.indexOf(selectedCategory).coerceAtLeast(0)
            FEEDBACK_CATEGORIES.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                val isPassed = index < selectedIndex
                val isLast = index == totalSteps - 1

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onPick(item) }
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // 左侧：垂直进度轴与指示节点
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(22.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 18.dp else 14.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        isPassed -> JadeGreen
                                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            } else if (isPassed) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }

                        if (!isLast) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(28.dp)
                                    .background(
                                        if (index < selectedIndex) JadeGreen.copy(alpha = 0.75f)
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // 右侧：类型标题与说明
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = if (isLast) 2.dp else 8.dp)
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 12.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = item.subtitle,
                            fontSize = 10.5.sp,
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }
    }

    // 反馈内容输入区（横屏右栏 / 竖屏下方共用）
    @Composable
    fun FeedbackForm() {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "反馈内容 *",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = feedbackContent,
                onValueChange = { feedbackContent = it },
                placeholder = {
                    Text(
                        "请描述您遇到的问题或想要的功能建议...",
                        fontSize = 12.sp
                    )
                },
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 联系方式输入框
            Text(
                text = "联系方式（选填）",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = userContact,
                onValueChange = { userContact = it },
                placeholder = { Text("QQ / 微信 / 邮箱", fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 附带诊断信息勾选项
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { includeDeviceInfo = !includeDeviceInfo }
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = includeDeviceInfo,
                    onCheckedChange = { includeDeviceInfo = it },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "附带设备环境信息",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${Build.MANUFACTURER} ${Build.MODEL} · Android ${Build.VERSION.RELEASE}",
                        fontSize = 10.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // 底部操作区
    @Composable
    fun ActionBar() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    val fullReport = buildFullReport()
                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(ClipData.newPlainText("反馈信息", fullReport))
                    Toast.makeText(context, "已复制反馈文本到剪贴板！", Toast.LENGTH_SHORT).show()
                },
                enabled = !isSending,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("复制内容", fontSize = 12.sp)
            }
            Button(
                onClick = { sendDirectFeedback() },
                enabled = !isSending,
                colors = ButtonDefaults.buttonColors(containerColor = CutePink),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1.6f)
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("正在发送...", fontSize = 13.sp)
                } else {
                    Icon(Icons.Filled.Send, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("直接发送", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }

    Dialog(
        onDismissRequest = {
            if (!isSending) onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { if (!isSending) onDismiss() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.96f)
                    .fillMaxHeight(0.92f)
                    .clip(RoundedCornerShape(26.dp))
                    .shadow(
                        24.dp,
                        RoundedCornerShape(26.dp),
                        ambientColor = Color(0xFFA855F7).copy(alpha = 0.25f),
                        spotColor = Color(0xFF38BDF8).copy(alpha = 0.35f)
                    )
                    .streamingBorder(
                        cornerRadius = 26.dp,
                        strokeWidth = 1.8.dp,
                        glowWidth = 3.8.dp,
                        baseBorderColor = Color.White.copy(alpha = 0.65f),
                        rainbow = true,
                        showGlow = true
                    ),
                shape = RoundedCornerShape(26.dp),
                color = Color.White.copy(alpha = 0.92f),
                tonalElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.96f),
                                    Color(0xFFFBF8FF).copy(alpha = 0.92f),
                                    Color(0xFFFFF8FB).copy(alpha = 0.92f),
                                    Color.White.copy(alpha = 0.96f)
                                )
                            )
                        )
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                    // ===== 弹窗头部 =====
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Brush.linearGradient(listOf(CutePink, CutePeach))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.BugReport,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "软件反馈",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "横屏双栏排版 · 真实送达开发者邮箱",
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        IconButton(
                            onClick = onDismiss,
                            enabled = !isSending
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = "关闭", modifier = Modifier.size(20.dp))
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                    // ===== 自适应主体：横屏双栏 / 竖屏单栏 =====
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        val landscape = maxWidth > maxHeight

                        if (landscape) {
                            // ===== 横屏：左右双栏 =====
                            Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                // 左侧：反馈类型时间轴
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    shape = RoundedCornerShape(14.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState())
                                            .padding(horizontal = 14.dp, vertical = 12.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "反馈类型",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = selectedCategory.title,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        CategoryPicker()
                                        Spacer(modifier = Modifier.height(8.dp))
                                        // v1.0.12：删除「点击直接发送将真实送达开发者邮箱…」提示行（默认邮箱已更新，不再展示邮箱字样）
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // 右侧：表单 + 操作
                                Column(
                                    modifier = Modifier
                                        .weight(1.4f)
                                        .fillMaxHeight()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    FeedbackForm()
                                    Spacer(modifier = Modifier.height(12.dp))
                                    ActionBar()
                                }
                            }
                        } else {
                            // ===== 竖屏：单栏滚动 =====
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "反馈类型",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = selectedCategory.title,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                CategoryPicker()
                                Spacer(modifier = Modifier.height(12.dp))
                                FeedbackForm()
                                Spacer(modifier = Modifier.height(10.dp))
                                // v1.0.12：删除「点击直接发送将真实送达开发者邮箱…感谢您的宝贵建议」提示行
                                Spacer(modifier = Modifier.height(12.dp))
                                ActionBar()
                            }
                        }
                        }
                    }
                }
            }
        }
    }
}