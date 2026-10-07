package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// v1.0.18 移植组件库（u-badge / u-list / u-tab）
// 依据用户提供的 CSS 代码翻译为 Compose 组件：
//   .u-badge 徽标（弹窗标题）
//   .u-list  列表（设置版块）
//   .u-tab   选择栏（软件版块）
// 风格：白底 + 4px 厚描边(#0a3d63 / --ink) + 偏移硬阴影(--c1) + 圆角
// ============================================================

/** Neo-Brutalism 调色板（对应 CSS 变量） */
/** Neo-Brutalism 调色板（对应 CSS 变量） */
val V15Ink = Color(0xFF26303C)      // --ink  深蓝灰（边框 / 主文字）
val V15C1 = Color(0xFFFFB020)       // --c1   活力橙（主阴影 / 选中态）
val V15C2 = Color(0xFFFF5E8A)       // --c2   草莓粉（次色 / 粉徽标）
val V15Bg = Color(0xFF4D96FF)       // --bg   天蓝（头像背景 / 蓝徽标）
val V15Divider = Color(0xFFE6EEF5)  // 分隔线

/**
 * Neo-Brutalism 卡片容器：白底 + 4dp 粗描边 + 右下偏移硬阴影 + 圆角。
 * 对应 CSS：`background:#fff;border:4px solid var(--ink);border-radius:16px;
 *           box-shadow:.35em .35em 0 var(--c1)`
 */
@Composable
fun ULayoutCard(
    modifier: Modifier = Modifier,
    // v1.1.7 整体胶囊化：默认圆角增大到 28dp（胶囊感）
    cornerRadius: Int = 28,
    borderWidth: Int = 4,
    shadowColor: Color = V15C1,
    borderColor: Color = V15Ink,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    // v1.1.8 控制台主题工具箱同步修复：设置页容器/行实时消费主题代码（card 组件级 + global 全局）
    val themes = LocalComponentThemes.current
    val compStyle = ComponentThemeResolver.resolve(themes, "card") ?: ComponentThemeResolver.resolve(themes, "global")
    // 主题自定义圆角生效时覆盖默认胶囊圆角
    val shape = if (compStyle != null) {
        RoundedCornerShape(compStyle.cornerRadius)
    } else {
        RoundedCornerShape(cornerRadius.dp)
    }
    val bgColor = compStyle?.backgroundColor ?: MaterialTheme.colorScheme.surface
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale = if (pressed) 0.97f else 1f
    val lift = if (pressed) 2 else 6

    Box(modifier = modifier) {
        // 硬阴影层（右下偏移）
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = lift.dp, y = lift.dp)
                .clip(shape)
                .background(shadowColor)
        )
        // 主体层
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .clip(shape)
                .background(bgColor)
                .border(borderWidth.dp, compStyle?.borderColor ?: borderColor, shape)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                    } else Modifier
                )
        ) {
            content()
        }
    }
}

// ============================================================
// .u-list 全局列表 —— 设置版块主体
// CSS:
//   .u-list{width:min(300px,100%);background:#fff;border:4px solid var(--ink);border-radius:16px;
//           overflow:hidden;box-shadow:.35em .35em 0 var(--c1)}
//   .u-list .row{display:flex;align-items:center;gap:12px;padding:13px 16px;
//                border-bottom:3px solid #e6eef5;...}
//   .u-list .row .ic{width:40px;height:40px;border-radius:10px;background:var(--bg);color:#fff;...
//                    box-shadow:.15em .15em 0 var(--c1)}
//   .u-list .row:nth-child(2) .ic{background:var(--c2)}
//   .u-list .row:nth-child(3) .ic{background:var(--c1);color:var(--ink)}
// ============================================================
@Composable
fun UListContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ULayoutCard(modifier = modifier, cornerRadius = 16, borderWidth = 4, shadowColor = V15C1) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

/**
 * 列表行：图标方块 + 标题/副标题 + 右侧内容（时间 / 徽标 / chevron）。
 * rowIndex 用于循环配色（第 2/3 行 icon 换色，对应 CSS nth-child）
 */
@Composable
fun UListRow(
    title: String,
    subtitle: String = "",
    iconText: String = "?",
    iconUrl: String = "",
    trailing: String = "›",
    rowIndex: Int = 0,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // v1.0.19 设置版块主题同步：图标块/文字/分隔线跟随当前主题色（MaterialTheme），
    // 不再使用固定 V15 色板，切换主题后设置页同步换肤。
    val themePrimary = MaterialTheme.colorScheme.primary
    val themeSecondary = MaterialTheme.colorScheme.secondary
    val themeOnSurface = MaterialTheme.colorScheme.onSurface
    // CSS nth-child 配色：第 2 行次色，第 3 行主色，其余主色
    val iconBg = when (rowIndex) {
        1 -> themeSecondary
        else -> themePrimary
    }
    val iconFg = Color.White
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(if (pressed) themePrimary.copy(alpha = 0.08f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 13.dp)
    ) {
        // 图标方块
        Box(
            modifier = Modifier
                .size(40.dp)
                .graphicsLayer {
                    scaleX = if (pressed) 1.1f else 1f
                    scaleY = if (pressed) 1.1f else 1f
                    rotationZ = if (pressed) 12f else 0f
                }
                .offset(x = 3.dp, y = 3.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            if (iconUrl.isNotBlank()) {
                coil.compose.AsyncImage(
                    model = iconUrl,
                    contentDescription = title,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                )
            } else {
                Text(
                    text = iconText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = iconFg
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = themeOnSurface,
                maxLines = 1
            )
            if (subtitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF7A8CA0),
                    maxLines = 1
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        if (trailing.isNotBlank()) {
            Text(
                text = trailing,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF9AA8B6)
            )
        }
    }
    // 行底部分隔线
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(V15Divider)
    )
}

// ============================================================
// .u-tab 选择栏 —— 软件版块顶部
// CSS:
//   .u-tab{display:flex;background:#fff;border:4px solid var(--ink);border-radius:14px;
//          overflow:hidden;box-shadow:.3em .3em 0 var(--c1)}
//   .u-tab button{flex:1;border:none;background:transparent;padding:12px;font-weight:800;
//                 font-size:13px;color:#7a8ca0;cursor:pointer;border-right:3px solid #e6eef5;
//                 transition:all .25s}
//   .u-tab button.on{background:var(--c1);color:var(--ink);animation:tabpulse .4s cubic-bezier(.34,1.56,.64,1)}
//   @keyframes tabpulse{0%{transform:scale(.9)}100%{transform:scale(1)}}
// ============================================================
@Composable
fun UTabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pulse = remember { Animatable(1f) }
    LaunchedEffect(selectedIndex) {
        pulse.snapTo(0.9f)
        pulse.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }
    ULayoutCard(modifier = modifier, cornerRadius = 14, borderWidth = 4, shadowColor = V15C1) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            tabs.forEachIndexed { index, tabName ->
                val selected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .graphicsLayer {
                            scaleX = if (selected) pulse.value else 1f
                            scaleY = if (selected) pulse.value else 1f
                        }
                        .background(if (selected) V15C1 else Color.Transparent)
                        .clickable { onSelect(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tabName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = if (selected) V15Ink else Color(0xFF7A8CA0)
                    )
                }
                if (index < tabs.size - 1) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .fillMaxHeight()
                            .background(V15Divider)
                    )
                }
            }
        }
    }
}

// ============================================================
// .u-badge 徽标 —— 弹窗标题 / 标识
// CSS:
//   .u-badge{display:inline-flex;align-items:center;gap:6px;background:#fff;border:3px solid var(--ink);
//            border-radius:30px;padding:5px 12px;font-weight:800;font-size:12px;color:var(--ink);
//            box-shadow:.18em .18em 0 var(--c1);transition:all .2s}
//   .u-badge:hover{transform:translateY(-3px) rotate(-4deg);box-shadow:.28em .3em 0 var(--c1)}
//   .u-badge.pink{background:var(--c2);color:#fff}
//   .u-badge.blue{background:var(--bg);color:#fff;box-shadow:.18em .18em 0 var(--c2)}
//   .u-badge .x{cursor:pointer;opacity:.7;font-weight:900}
//   .u-badge .x:hover{opacity:1;transform:scale(1.3) rotate(90deg)}
// ============================================================
enum class UBadgeVariant { DEFAULT, PINK, BLUE }

@Composable
fun UBadge(
    text: String,
    variant: UBadgeVariant = UBadgeVariant.DEFAULT,
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null
) {
    val bg = when (variant) {
        UBadgeVariant.DEFAULT -> Color.White
        UBadgeVariant.PINK -> V15C2
        UBadgeVariant.BLUE -> V15Bg
    }
    val fg = when (variant) {
        UBadgeVariant.DEFAULT -> V15Ink
        UBadgeVariant.PINK, UBadgeVariant.BLUE -> Color.White
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(bg)
            .border(3.dp, V15Ink, RoundedCornerShape(30.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = fg,
            modifier = Modifier
                .graphicsLayer { shadowElevation = 2f }
                .offset(x = 2.dp, y = 2.dp)
        )
        if (onClose != null) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "✕",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = fg.copy(alpha = 0.7f),
                modifier = Modifier
                    .clickable(onClick = onClose)
                    .padding(2.dp)
            )
        }
    }
}
