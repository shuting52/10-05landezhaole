package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.uiverse.UiverseCssEngine

/**
 * v1.1.4 主题分支：组件级主题定制真正生效的核心工具。
 *
 * 背景：主题切换中「组件定制」此前只把 CSS 存进 Map（componentThemes），
 * 没有任何 UI 组件消费它 → 用户定制组件主题完全没效果（牛头不对马嘴）。
 *
 * 本工具负责：把组件 CSS 解析成可消费的视觉样式（背景色/文字色/圆角/描边），
 * 并对外提供「按组件 id 取样式」的接口，供各 UI 组件直接应用。
 */
data class ResolvedComponentStyle(
    val backgroundColor: Color? = null,
    val textColor: Color? = null,
    val borderColor: Color? = null,
    val cornerRadius: Dp = 12.dp,
    val borderWidth: Dp = 0.dp,
    val shadowColor: Color? = null,
    val shadowElevation: Dp = 0.dp
)

/**
 * v1.1.8 控制台主题工具箱同步修复：
 * 全局组件主题表（组件 id -> CSS），由 NavViewModel 从云端 themeKit 解析后写入，
 * 通过 CompositionLocal 提供给所有 UI 组件实时消费——控制台「应用」后本体数秒内生效。
 */
val LocalComponentThemes = staticCompositionLocalOf<Map<String, String>> { emptyMap() }

object ComponentThemeResolver {

    /**
     * 从组件定制 CSS Map 中解析出指定组件的样式。
     * @param componentThemes 组件 id -> CSS（来自 ActiveUiverseState.componentThemes）
     * @param compId          目标组件 id（home_header / bottom_nav / search_box / card_item / ...）
     */
    fun resolve(componentThemes: Map<String, String>, compId: String): ResolvedComponentStyle? {
        val css = componentThemes[compId]?.takeIf { it.isNotBlank() } ?: return null
        return try {
            val parsed = UiverseCssEngine.parseCss(css, "")
            ResolvedComponentStyle(
                backgroundColor = parsed.backgroundColor,
                textColor = parsed.textColor,
                borderColor = parsed.borderColor.takeIf { it != Color.Transparent },
                cornerRadius = parsed.cornerRadius,
                borderWidth = parsed.borderWidth,
                shadowColor = parsed.shadowColor,
                shadowElevation = parsed.shadowElevation
            )
        } catch (e: Exception) {
            null
        }
    }

    /** 简便包装：背景色（无定制返回 null，调用方自行兜底） */
    fun bgColor(componentThemes: Map<String, String>, compId: String): Color? =
        resolve(componentThemes, compId)?.backgroundColor

    /** 简便包装：文字色 */
    fun textColor(componentThemes: Map<String, String>, compId: String): Color? =
        resolve(componentThemes, compId)?.textColor

    /** 简便包装：圆角 */
    fun cornerRadius(componentThemes: Map<String, String>, compId: String): Dp? =
        resolve(componentThemes, compId)?.cornerRadius
}
