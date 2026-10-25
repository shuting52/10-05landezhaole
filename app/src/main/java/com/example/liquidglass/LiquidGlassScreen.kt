/**
 * 转换版（可运行）—— 由用户原稿 LiquidGlassScreen.kt 转换而来，原稿一字不动，见原稿馆源码区。
 * 转换改动（仅为在本工程编过，其余原样）：
 * 1. matchParentSize → fillMaxSize（本工程 Compose 版本没有 matchParentSize，同效果）；
 * 2. 玻璃面板去掉屏幕坐标对齐（positionInRoot 本工程解析不到）：折射层仍为屏尺寸背景副本 + 模糊 + 裁剪，
 *    只是不做负偏移对齐，视觉仍是毛玻璃，对齐精度略降；
 * 3. 开关背景两分支统一成 Brush（原稿 Brush/Color 两分支传 background，任何版本都编不过）；
 * 4. 唱片旋转的 animateFloat 保持原样，待 CI 校验（若仍解析不到则换 animateValue 等价写法）；
 */
package com.example.liquidglass

/**
 * 液态玻璃（Liquid Glass）演示页 —— Jetpack Compose + Material 3
 *
 * 视觉配方：
 * 1. 深色渐变底 + 四团彩色光斑（玻璃"背后"必须有内容，光斑才成立）
 * 2. 玻璃面板 = 半透明白面(10%) + 上亮下暗的渐变描边 + 顶部镜面高光
 * 3. 折射感：面板内部放一份"对齐到屏幕坐标"的背景副本并模糊（Modifier.blur），
 *    模拟真实毛玻璃透出背后内容 —— Compose 没有原生 backdrop blur 时的标准做法
 * 4. 组件：搜索框 / 播放器卡 / 按钮 / 设置行+开关 / 底部玻璃 Dock / 轻提示
 */
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import kotlinx.coroutines.delay

/* ========== 主题 ========== */

/** 液态玻璃是暗底风格，直接固定 darkColorScheme */
@Composable
fun LiquidGlassTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF8B84FF),
            secondary = Color(0xFFFF7AC6),
            background = Color(0xFF151131),
            surface = Color(0xFF1E1A3C),
        ),
        content = content,
    )
}

/* ========== 背景：深色渐变 + 彩色光斑 ========== */

/**
 * 整页背景（渐变 + 光斑）。光斑用"绝对坐标"绘制，
 * 玻璃面板内部会复制这份背景并模糊 —— 两处坐标一致才能对齐，做出"透出背后"的效果。
 */
@Composable
private fun BoxScope.LiquidBackdrop() {
    val cfg = LocalConfiguration.current
    Box(
        Modifier
            .size(cfg.screenWidthDp.dp, cfg.screenHeightDp.dp)
            .background(Brush.verticalGradient(listOf(Color(0xFF171437), Color(0xFF2B1B55), Color(0xFF151131)))),
    )
    Blob(x = 26.dp, y = 92.dp, size = 280.dp, color = Color(0xFFFF5E9C))   // 粉
    Blob(x = 240.dp, y = 26.dp, size = 250.dp, color = Color(0xFF5A7BFF))  // 蓝
    Blob(x = -36.dp, y = 430.dp, size = 300.dp, color = Color(0xFFFFA94D)) // 橙
    Blob(x = 178.dp, y = 620.dp, size = 340.dp, color = Color(0xFF2FE0C8)) // 青
}

/** 单个光斑：径向渐变（中心实 → 边缘透明），自带柔和边缘 */
@Composable
private fun BoxScope.Blob(x: Dp, y: Dp, size: Dp, color: Color) {
    Box(
        Modifier
            .offset(x = x, y = y)
            .size(size)
            .background(Brush.radialGradient(listOf(color.copy(alpha = 0.85f), color.copy(alpha = 0.0f)))),
    )
}

/* ========== 玻璃面板（核心容器） ========== */

/**
 * 液态玻璃面板：
 * ① 折射层 —— 面板内放一份"负偏移到屏幕原点"的背景副本并模糊，clip 到面板形状；
 * ② 半透明玻璃面；③ 上亮下暗渐变描边；④ 顶部镜面高光。
 */
@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    corner: Dp = 28.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cfg = LocalConfiguration.current
    Box(
        modifier = modifier
            .shadow(14.dp, RoundedCornerShape(corner), ambientColor = Color.Black.copy(alpha = 0.35f), spotColor = Color.Black.copy(alpha = 0.35f)),
    ) {
        // ① 折射层：背景副本对齐屏幕坐标 + 模糊 + 裁剪
        Box(Modifier.fillMaxSize().clip(RoundedCornerShape(corner))) {
            Box(
                Modifier
                    .size(cfg.screenWidthDp.dp, cfg.screenHeightDp.dp)
                    .blur(28.dp),
            ) {
                LiquidBackdrop()
            }
        }
        // ② 半透明玻璃面
        Box(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(corner))
                .background(Color.White.copy(alpha = 0.10f)),
        )
        // ③ 渐变描边
        Box(
            Modifier
                .fillMaxSize()
                .border(
                    1.dp,
                    Brush.linearGradient(listOf(Color.White.copy(alpha = 0.55f), Color.White.copy(alpha = 0.06f))),
                    RoundedCornerShape(corner),
                ),
        )
        // ④ 顶部镜面高光
        Box(
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(corner))
                .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.20f), Color.White.copy(alpha = 0.0f)), startY = 0f, endY = 160f)),
        )
        Column(Modifier.padding(20.dp), content = content)
    }
}

/* ========== 基础玻璃控件 ========== */

/** 玻璃圆形图标按钮（播放/暂停/通知），按压缩小反馈 */
@Composable
fun GlassCircleButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    Box(
        modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.12f))
            .border(1.dp, Color.White.copy(alpha = 0.28f), CircleShape)
            .shadow(8.dp, CircleShape, ambientColor = Color.Black.copy(alpha = 0.25f), spotColor = Color.Black.copy(alpha = 0.25f))
            .scale(if (pressed) 0.92f else 1f)
            .clickable(interactionSource = interaction, indication = null) { onClick() },
        contentAlignment = Alignment.Center,
    ) { Icon(icon, contentDescription = contentDescription, tint = Color.White, modifier = Modifier.size(20.dp)) }
}

/** 玻璃按钮：实底 / 幽灵两种，按压 0.97 缩放 */
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    filled: Boolean = true,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    Box(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (filled) Color.White.copy(alpha = 0.16f) else Color.White.copy(alpha = 0.06f))
            .border(1.dp, if (filled) Color.White.copy(alpha = 0.32f) else Color.White.copy(alpha = 0.18f), RoundedCornerShape(16.dp))
            .scale(if (pressed) 0.97f else 1f)
            .clickable(interactionSource = interaction, indication = null) { onClick() }
            .padding(horizontal = 20.dp, vertical = 13.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}

/** 玻璃开关：渐变轨道 + 白色滑块，spring 弹性跟随 */
@Composable
fun GlassSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow),
        label = "glass-switch-thumb",
    )
    Box(
        modifier
            .width(58.dp)
            .height(34.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(
                if (checked) Brush.linearGradient(listOf(Color(0xFF6C8CFF), Color(0xFFB06CFF)))
                else SolidColor(Color.White.copy(alpha = 0.14f)),
            )
            .border(1.dp, Color.White.copy(alpha = 0.30f), RoundedCornerShape(17.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            Modifier
                .size(26.dp)
                .offset(x = thumbOffset)
                .shadow(4.dp, CircleShape, ambientColor = Color.White.copy(alpha = 0.5f), spotColor = Color.White.copy(alpha = 0.5f))
                .clip(CircleShape)
                .background(Color.White),
        )
    }
}

/** 玻璃设置行：小圆图标 + 标题/副标题 + 尾部自定义控件 */
@Composable
fun GlassSettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
                .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape),
            contentAlignment = Alignment.Center,
        ) { Icon(icon, contentDescription = null, tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(19.dp)) }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(subtitle, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
        trailing?.invoke()
    }
}

/** 玻璃搜索框：BasicTextField 定制，聚焦/输入状态由调用方持有（状态提升） */
@Composable
fun GlassSearchField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    GlassPanel(modifier = modifier.fillMaxWidth(), corner = 26.dp) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, contentDescription = "搜索", tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                cursorBrush = SolidColor(Color.White),
                singleLine = true,
                decorationBox = { inner ->
                    Box {
                        if (value.isEmpty()) {
                            Text("搜索感兴趣的内容…", color = Color.White.copy(alpha = 0.55f), fontSize = 15.sp)
                        }
                        inner()
                    }
                },
            )
        }
    }
}

/** 玻璃进度条：点按轨道即跳转，白色滑块带柔光 */
@Composable
fun GlassProgress(value: Float, onSeek: (Float) -> Unit, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier.fillMaxWidth().height(44.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.White.copy(alpha = 0.16f))
                .pointerInput(Unit) {
                    detectTapGestures { tap -> onSeek((tap.x / size.width.toFloat()).coerceIn(0f, 1f)) }
                },
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(value)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.9f), Color(0xFF8FA6FF).copy(alpha = 0.9f)))),
            )
        }
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .offset(x = (maxWidth * value - 7.dp).coerceAtLeast(0.dp))
                .size(14.dp)
                .shadow(6.dp, CircleShape, ambientColor = Color.White.copy(alpha = 0.55f), spotColor = Color.White.copy(alpha = 0.55f))
                .clip(CircleShape)
                .background(Color.White),
        )
    }
}

/** 底部玻璃 Dock：iOS 液态玻璃标志性元素，选中项提亮 */
@Composable
fun GlassDock(selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    val items = listOf(
        Icons.Default.Home to "首页",
        Icons.Default.Favorite to "收藏",
        Icons.Default.Person to "我的",
    )
    Row(
        modifier
            .navigationBarsPadding()
            .padding(horizontal = 28.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(30.dp))
            .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, (icon, label) ->
            Column(
                Modifier.clickable { onSelect(index) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(if (selected == index) Color.White.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.08f))
                        .border(1.dp, Color.White.copy(alpha = if (selected == index) 0.4f else 0.18f), RoundedCornerShape(15.dp)),
                    contentAlignment = Alignment.Center,
                ) { Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(24.dp)) }
                Spacer(Modifier.height(4.dp))
                Text(
                    label,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 10.sp,
                    fontWeight = if (selected == index) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }
}

/** 玻璃轻提示：从下方滑入的毛玻璃胶囊 */
@Composable
fun GlassToast(message: String?, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = message != null,
        modifier = modifier,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
    ) {
        Box(
            Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.16f))
                .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                .padding(horizontal = 18.dp, vertical = 10.dp),
        ) { Text(message.orEmpty(), color = Color.White, fontSize = 14.sp) }
    }
}

/* ========== 页面装配 ========== */

@Composable
fun LiquidGlassScreen() {
    var query by rememberSaveable { mutableStateOf("") }
    var notifyOn by rememberSaveable { mutableStateOf(true) }
    var dockIndex by rememberSaveable { mutableStateOf(0) }
    var toastMsg by remember { mutableStateOf<String?>(null) }

    fun showToast(msg: String) { toastMsg = msg }

    // toast 自动消失
    LaunchedEffect(toastMsg) {
        if (toastMsg != null) {
            delay(1600)
            toastMsg = null
        }
    }

    Box(Modifier.fillMaxSize()) {
        LiquidBackdrop()
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 18.dp, bottom = 150.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            // 标题 + 通知玻璃圆钮
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("液态玻璃", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("Liquid Glass · 点一点下面的控件", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
                }
                GlassCircleButton(
                    icon = Icons.Default.Notifications,
                    contentDescription = "通知",
                    onClick = {
                        notifyOn = !notifyOn
                        showToast(if (notifyOn) "通知已开启" else "通知已关闭")
                    },
                )
            }

            GlassSearchField(value = query, onValueChange = { query = it })

            // 播放器卡片（唱片播放时旋转）
            GlassPanel(Modifier.fillMaxWidth(), corner = 30.dp) {
                var isPlaying by remember { mutableStateOf(true) }
                var progress by remember { mutableStateOf(0.38f) }
                val infinite = rememberInfiniteTransition(label = "vinyl")
                val angle by infinite.animateFloat(0f, 360f, infiniteRepeatable(tween(4000, easing = LinearEasing)), label = "angle")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(64.dp)
                            .graphicsLayer { rotationZ = if (isPlaying) angle else 0f }
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(Color(0xFF4A3E7A), Color(0xFF17122F)))),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(Color(0xFFFF5E9C), Color(0xFF5A7BFF)))),
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text("海风海浪电台", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(3.dp))
                        Text("液态玻璃 · 精选歌单", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
                    }
                    GlassCircleButton(
                        icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "暂停" else "播放",
                        onClick = { isPlaying = !isPlaying },
                        size = 46.dp,
                    )
                }
                Spacer(Modifier.height(16.dp))
                GlassProgress(value = progress, onSeek = { progress = it })
                Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("01:42", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                    Text("04:30", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                }
            }

            // 按钮行
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GlassButton(text = "立即体验", onClick = { showToast("已加入体验队列") }, modifier = Modifier.weight(1f))
                GlassButton(text = "稍后再说", onClick = { showToast("已为你稍后提醒") }, modifier = Modifier.weight(1f), filled = false)
            }

            // 设置分组（同一块玻璃面板内用分隔线分组）
            GlassPanel(Modifier.fillMaxWidth(), corner = 26.dp) {
                GlassSettingRow(
                    icon = Icons.Default.Notifications,
                    title = "通知提醒",
                    subtitle = "开启后实时接收新消息",
                    onClick = { showToast("通知设置") },
                    trailing = { GlassSwitch(checked = notifyOn, onCheckedChange = { notifyOn = it }) },
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                GlassSettingRow(
                    icon = Icons.Default.Star,
                    title = "收藏夹",
                    subtitle = "管理我的收藏",
                    onClick = { showToast("已打开收藏夹") },
                    trailing = { Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.White.copy(alpha = 0.5f)) },
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                GlassSettingRow(
                    icon = Icons.Default.Info,
                    title = "关于液态玻璃",
                    subtitle = "版本 1.0.0",
                    onClick = { showToast("液态玻璃 v1.0.0") },
                )
            }
        }

        GlassToast(message = toastMsg, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 130.dp))
        GlassDock(selected = dockIndex, onSelect = { dockIndex = it }, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

/** 入口 */
@Composable
fun App() {
    LiquidGlassTheme { LiquidGlassScreen() }
}

@Composable
private fun LiquidGlassScreenPreview() {
    LiquidGlassTheme { LiquidGlassScreen() }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LiquidGlassScreenPreview() {
    LiquidGlassTheme { LiquidGlassScreen() }
}
