@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.example.ui.components.streamingBorder
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import com.example.ui.screens.toolbox.AgeCalculatorSection
import com.example.ui.components.UiverseAmber
import com.example.ui.components.UiverseBlue
import com.example.ui.components.UiverseInk
import com.example.ui.components.UiversePink
import com.example.ui.components.UiverseTextMuted
import com.example.ui.components.UiverseTrack
import com.example.ui.components.neoShadow
import com.example.ui.screens.toolbox.ColdJokeSection
import com.example.ui.screens.toolbox.CurrencySection
import com.example.ui.screens.toolbox.EmergencyPhoneSection
import com.example.ui.screens.toolbox.FoodPickerScreenView
import com.example.ui.screens.toolbox.LicensePlateSection
import com.example.ui.screens.toolbox.MouthpieceSection
import com.example.ui.screens.toolbox.OfflineTreasureSection
import com.example.ui.screens.toolbox.PeriodSection
import com.example.ui.screens.toolbox.SpeedTestSection

/**
 * 工具箱：嘴强嘴替 / 年龄推算 / 离线百宝 / 今天吃什么 / 紧急电话
 * v1.2.6：恢复车牌摇号 / 冷笑话 / 测速网 / 货币转换 / 生理期记录 / 环境检测
 */
enum class ToolboxTab(
    val title: String,
    val shortLabel: String,
    val icon: ImageVector,
    val desc: String
) {
    MOUTHPIECE(
        title = "最强嘴替 · 神级回怼",
        shortLabel = "最强嘴替",
        icon = Icons.Filled.Chat,
        desc = "神级回怼生成器 · 专治杠精职场催婚 · 优雅不带脏字"
    ),
    AGE_CALC(
        title = "年龄算术 · 生肖测算",
        shortLabel = "年龄算术",
        icon = Icons.Filled.DateRange,
        desc = "精准年月日时分秒 · 生肖天干地支 · 人生进度条"
    ),
    OFFLINE_TREASURE(
        title = "离线百宝 · 百宝锦囊",
        shortLabel = "离线百宝",
        icon = Icons.Filled.Lightbulb,
        desc = "LED滚动弹幕 · 电子功德木鱼 · 随机做决定器 · SOS爆闪"
    ),
    FOOD_PICKER(
        title = "今天吃什么 · 随机摇签",
        shortLabel = "今天吃什么",
        icon = Icons.Filled.Restaurant,
        desc = "随机摇签 · 各大菜系 · 配料调味料 · 华夏美食宝库"
    ),
    EMERGENCY_PHONE(
        title = "紧急电话 · 救命热线",
        shortLabel = "紧急电话",
        icon = Icons.Filled.Call,
        desc = "救援/道路/举报/法律 · 一键快捷呼出 · 全国通用守护平安"
    ),
    // v1.2.6：恢复以下工具（车牌摇号/冷笑话/测速网/货币转换/生理期记录/环境检测）
    LICENSE_PLATE(
        title = "车牌摇号 · 全国城市选号",
        shortLabel = "车牌摇号",
        icon = Icons.Filled.DirectionsCar,
        desc = "全国城市 · 菜单选择 · 号牌池 · 一键摇号"
    ),
    COLD_JOKE(
        title = "冷笑话 · 一键分享",
        shortLabel = "冷笑话",
        icon = Icons.Filled.SentimentSatisfied,
        desc = "随机一条冷笑话 · 一键分享到聊天/短视频平台"
    ),
    SPEED_TEST(
        title = "测速网 · 真实测速",
        shortLabel = "测速网",
        icon = Icons.Filled.Speed,
        desc = "真实下载测速 · Ping 延迟 · 当前位置"
    ),
    CURRENCY(
        title = "货币转换 · 实时汇率",
        shortLabel = "货币转换",
        icon = Icons.Filled.Paid,
        desc = "全球 50+ 货币 · 实时汇率 · 双向换算"
    ),
    PERIOD(
        title = "生理期记录 · 周期看板",
        shortLabel = "生理期记录",
        icon = Icons.Filled.EventNote,
        desc = "开始/结束日期 · 备注 · 保存删除 · 周期看板"
    ),
    LOCATION_MOCK(
        title = "IP纯净度检测 (ping0.cc 仅作为参考)",
        shortLabel = "IP纯净度",
        icon = Icons.Filled.Security,
        desc = "原生深度诊断 · IP 纯净度 · 住宅/机房识别 · 欺诈评分 (ping0.cc 仅作为参考)"
    ),
    CAR_BRAND(
        title = "车标大全 · 真实车标智能识别",
        shortLabel = "车标识别",
        icon = Icons.Filled.DirectionsCar,
        desc = "网络真实知名车标库(大众/宝马/迈巴赫等) · 品牌档案 · 拍照/图片车标智能识别"
    )
}

/** 工具箱合集分类 */
enum class ToolCategory(
    val id: String,
    val displayName: String,
    val icon: String,
    val defaultExpanded: Boolean = false
) {
    // v1.0.7：分类默认收起（收纳形式呈现），点击标题栏才展开
    CLOUD("cloud", "云端工具", "☁️", false),
    CORE("core", "精选工具", "✨", false)
}

/** 环形菜单呈现方式（由点击中心「懒 得 找 了」进入设置功能切换） */
private enum class RingPresentationStyle(
    val title: String,
    val subtitle: String,
    val badge: String
) {
    LIQUID_CRYSTAL("液态水晶玻璃", "高透流光折射 · 悬浮光斑 · 柔雾景深", "默认推荐"),
    HOLO_PRISM("全息虹彩棱镜", "流光彩虹边缘 · 动态极光折射 · 璀璨高光", "炫彩流光"),
    CYBER_GLASS("深空流光琉璃", "深邃暗夜冰晶 · 霓虹紫电呼吸 · 极客质感", "深色高透"),
    FROST_JADE("凝脂羊脂玉玻", "温润柔光磨砂 · 珍珠白立体浮雕 · 护眼雅致", "温润磨砂")
}

/** 按住时触发的动态效果（长按/按住圆环入口或卡片时实时触发） */
private enum class RingPressEffect(
    val title: String,
    val subtitle: String
) {
    LIQUID_RIPPLE("液态水波荡漾", "按住时向外扩散多层液态光学波纹并弹性外推"),
    JELLY_BOUNCE("Q弹果冻回弹", "按住时软萌挤压形变 + 释放瞬间高弹力果冻缩放"),
    GRAVITY_VORTEX("引力星璇聚能", "按住时触发环绕流光旋转光环与微幅倾角悬浮"),
    NEON_SHOCK("霓虹脉冲呼吸", "按住时高频流光呼吸灯与立体光晕放大")
}

@Composable
fun ToolboxScreen(
    modifier: Modifier = Modifier,
    // v1.8.7：云端工具箱扩展工具（控制台增删，实时同步）
    cloudTools: List<com.example.data.remote.ToolDto> = emptyList(),
    // v1.2.6：工具箱标题/副标题（控制台 UI 文本可云端覆盖）
    title: String? = null,
    subtitle: String? = null
) {
    // 弹窗交互：点击工具弹出独立 CSS 动态窗口
    var activeTool by remember { mutableStateOf<ToolboxTab?>(null) }
    // 点击中心「懒 得 找 了」打开情感语录功能弹窗
    var showEmotionalQuotesDialog by remember { mutableStateOf(false) }
    // 手动向环形工具箱新增自定义工具弹窗
    var showAddRingToolDialog by remember { mutableStateOf(false) }
    // 环形菜单底部抽屉状态（对应 rm-sheet）
    var sheetGroupIndex by remember { mutableStateOf<Int?>(null) }
    // 呈现方式 & 按住时触发的动态效果（本地持久化保存用户的选择）
    val context = androidx.compose.ui.platform.LocalContext.current
    val ringPrefs = remember { context.getSharedPreferences("lzdz_ring_custom_tools", Context.MODE_PRIVATE) }
    var presentationStyle by remember {
        val saved = ringPrefs.getString("ring_style", null)
        mutableStateOf(
            RingPresentationStyle.values().find { it.name == saved } ?: RingPresentationStyle.LIQUID_CRYSTAL
        )
    }
    var pressEffect by remember {
        val saved = ringPrefs.getString("ring_press_effect", null)
        mutableStateOf(
            RingPressEffect.values().find { it.name == saved } ?: RingPressEffect.LIQUID_RIPPLE
        )
    }
    var customRingTools by remember {
        mutableStateOf(loadCustomRingTools(ringPrefs))
    }

    // 汇总全部环形工具：内置工具 + 云端同步工具 + 用户自定义工具（彻底删除与排除「快捷工具12」）
    val allRingGroups = remember(cloudTools, customRingTools) {
        val dynamicGroups = (cloudTools + customRingTools)
            .filterNot { it.name.trim() == "快捷工具12" || it.name.trim().contains("快捷工具12") }
            .mapIndexed { idx, dto ->
                val label = dto.name.ifBlank { "扩展工具${idx + 1}" }
                RingMenuGroup(
                    id = dto.id.ifBlank { "ext_ring_tool_$idx" },
                    entryLabel = label,
                    sheetTitle = label,
                    sheetSub = dto.desc.ifBlank { "点击直达云端/自定义扩展工具" },
                    icon = Icons.Filled.Public,
                    emojiIcon = dto.icon.trim().takeIf { it.isNotEmpty() },
                    externalUrl = dto.url.trim().takeIf { it.isNotEmpty() },
                    options = listOf(
                        RingMenuOption(
                            tab = null,
                            name = label,
                            desc = dto.desc.ifBlank { "云端/自定义扩展工具" },
                            externalUrl = dto.url.trim().takeIf { it.isNotEmpty() }
                        )
                    )
                )
            }
        RING_MENU_GROUPS + dynamicGroups
    }

    // v1.0.19 取消展开收纳标签：工具箱直接平铺展示全部工具，无需点击展开/收起
    val expanded = remember {
        mutableStateMapOf<String, Boolean>().apply {
            // 全部默认展开（直接平铺）
            ToolCategory.entries.forEach { put(it.id, true) }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .testTag("toolbox_screen")
        ) {
            // 顶部标题区（液态玻璃折射半透胶囊）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.46f),
                                Color(0xFFF6F0FF).copy(alpha = 0.30f),
                                Color(0xFFFFEDF5).copy(alpha = 0.34f),
                                Color.White.copy(alpha = 0.50f)
                            )
                        )
                    )
                    .border(
                        width = 1.4.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.94f),
                                Color(0xFFD8B4FE).copy(alpha = 0.50f),
                                Color(0xFFF472B6).copy(alpha = 0.45f),
                                Color.White.copy(alpha = 0.90f)
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title?.ifBlank { "🏮 懒得找了百宝箱" } ?: "🏮 懒得找了百宝箱",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFDE2910)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 液态玻璃多环快捷菜单：每个环形满 8 个工具自动新增独立新环形
            RingMenuSection(
                allGroups = allRingGroups,
                presentationStyle = presentationStyle,
                pressEffect = pressEffect,
                activeGroupIndex = sheetGroupIndex,
                onOpenSettings = { showEmotionalQuotesDialog = true },
                onAddToolClick = { showAddRingToolDialog = true },
                onSelectGroup = { index -> sheetGroupIndex = index },
                onDirectOpenTool = { tab -> activeTool = tab },
                onOpenExternalUrl = { url ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "无法打开链接：$url", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            // ===== 云端工具（控制台实时同步，直接平铺）=====
            if (cloudTools.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                CloudToolGrid(cloudTools = cloudTools, context = context)
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // 环形菜单底部独立交互抽屉（对应 rm-mask + rm-sheet）
        RingMenuBottomSheet(
            visible = sheetGroupIndex != null,
            allGroups = allRingGroups,
            selectedIndex = sheetGroupIndex ?: 0,
            presentationStyle = presentationStyle,
            pressEffect = pressEffect,
            onSelectGroup = { sheetGroupIndex = it },
            onDismiss = { sheetGroupIndex = null },
            onOpenTool = { tab ->
                sheetGroupIndex = null
                activeTool = tab
            },
            onOpenExternalUrl = { url ->
                sheetGroupIndex = null
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "无法打开链接：$url", Toast.LENGTH_SHORT).show()
                }
            }
        )

        // 点击「加工具」或新环形「+」槽位弹出的新增环形工具窗口（满 8 个自动生成下一环）
        if (showAddRingToolDialog) {
            AddRingToolDialog(
                currentTotalCount = allRingGroups.size,
                onDismiss = { showAddRingToolDialog = false },
                onConfirmAdd = { name, desc, url, emoji ->
                    val newTool = com.example.data.remote.ToolDto(
                        id = "custom_ring_${System.currentTimeMillis()}",
                        name = name,
                        desc = desc,
                        url = url,
                        icon = emoji.ifBlank { "✨" }
                    )
                    val updated = customRingTools + newTool
                    customRingTools = updated
                    saveCustomRingTools(ringPrefs, updated)
                    showAddRingToolDialog = false
                    Toast.makeText(
                        context,
                        "已添加「$name」至第 ${(allRingGroups.size / 8) + 1} 环！满 8 个将自动开启新环形",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

        // 点击圆心「懒 得 找 了」弹出的情感语录功能窗口（原环形样式和特效已删除）
        if (showEmotionalQuotesDialog) {
            EmotionalQuotesDialog(
                onDismiss = { showEmotionalQuotesDialog = false }
            )
        }

        // 每个工具专属的独立 CSS 动态窗口（流光边框 + 液态玻璃头部 + 动态光斑背景）
        activeTool?.let { tool ->
            IndependentCssDynamicWindow(
                tool = tool,
                presentationStyle = presentationStyle,
                onDismiss = { activeTool = null }
            ) {
                when (tool) {
                    ToolboxTab.MOUTHPIECE -> MouthpieceScreenView()
                    ToolboxTab.AGE_CALC -> AgeCalculatorScreenView()
                    ToolboxTab.OFFLINE_TREASURE -> OfflineTreasureScreenView()
                    ToolboxTab.FOOD_PICKER -> FoodPickerScreenView()
                    ToolboxTab.EMERGENCY_PHONE -> EmergencyPhoneSection()
                    ToolboxTab.LICENSE_PLATE -> LicensePlateSection()
                    ToolboxTab.COLD_JOKE -> ColdJokeSection()
                    ToolboxTab.SPEED_TEST -> SpeedTestSection()
                    ToolboxTab.CURRENCY -> CurrencySection()
                    ToolboxTab.PERIOD -> PeriodSection()
                    ToolboxTab.LOCATION_MOCK -> com.example.ui.screens.toolbox.LocationMockSection()
                    ToolboxTab.CAR_BRAND -> com.example.ui.screens.toolbox.CarBrandSection()
                }
            }
        }
    }
}

/* ==================== 环形快捷菜单（Ring Menu）数据与组件 ==================== */

private data class RingMenuOption(
    val tab: ToolboxTab? = null,
    val name: String,
    val desc: String,
    val externalUrl: String? = null
)

private data class RingMenuGroup(
    val id: String,
    val entryLabel: String,
    val sheetTitle: String,
    val sheetSub: String,
    val icon: ImageVector,
    val options: List<RingMenuOption>,
    val emojiIcon: String? = null,
    val externalUrl: String? = null,
    val isAddSlot: Boolean = false
)

/** 本地持久化自定义环形工具（JSON 数组轻量序列化） */
private fun loadCustomRingTools(prefs: android.content.SharedPreferences): List<com.example.data.remote.ToolDto> {
    val raw = prefs.getString("custom_tools_v1", null) ?: return emptyList()
    return try {
        val arr = org.json.JSONArray(raw)
        val loaded = buildList {
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val name = obj.optString("name", "").trim()
                // 彻底删除并过滤掉「快捷工具12」
                if (name == "快捷工具12" || name.contains("快捷工具12")) continue
                add(
                    com.example.data.remote.ToolDto(
                        id = obj.optString("id", "custom_$i"),
                        name = name,
                        desc = obj.optString("desc", ""),
                        url = obj.optString("url", ""),
                        icon = obj.optString("icon", "✨")
                    )
                )
            }
        }
        // 如果曾含有「快捷工具12」，立即写回持久化彻底清除
        saveCustomRingTools(prefs, loaded)
        loaded
    } catch (_: Exception) {
        emptyList()
    }
}

private fun saveCustomRingTools(
    prefs: android.content.SharedPreferences,
    list: List<com.example.data.remote.ToolDto>
) {
    try {
        val arr = org.json.JSONArray()
        list.forEach { item ->
            val obj = org.json.JSONObject().apply {
                put("id", item.id)
                put("name", item.name)
                put("desc", item.desc)
                put("url", item.url)
                put("icon", item.icon)
            }
            arr.put(obj)
        }
        prefs.edit().putString("custom_tools_v1", arr.toString()).apply()
    } catch (_: Exception) {
    }
}

/**
 * 按截图从上到下顺序（11 项全部独立），每个圆环最多承载 8 个入口，超出部分自动在下方创建第 2 个圆环：
 * 第一环（1～8）：
 * 1. 最强嘴替（神级回怼生成器）
 * 2. 年龄算术（精准年月日时分秒）
 * 3. 离线百宝（LED滚动弹幕）
 * 4. 今天吃什么（随机摇签）
 * 5. 紧急电话（救援/道路/举报/法律）
 * 6. 车牌摇号（全国城市）
 * 7. 冷笑话（随机一条冷笑话）
 * 8. 测速网（真实下载测速）
 *
 * 第二环（9～11，自动在下方创建）：
 * 9. 货币转换（全球 50+ 货币）
 * 10. 生理期记录（开始/结束日期）
 * 11. 环境检测（IP 地址）
 */
private val RING_MENU_GROUPS: List<RingMenuGroup> = listOf(
    RingMenuGroup(
        id = "mouthpiece",
        entryLabel = "最强嘴替",
        sheetTitle = "最强嘴替",
        sheetSub = "神级回怼生成器",
        icon = ToolboxTab.MOUTHPIECE.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.MOUTHPIECE,
                name = "最强嘴替",
                desc = "神级回怼生成器"
            )
        )
    ),
    RingMenuGroup(
        id = "age_calc",
        entryLabel = "年龄算术",
        sheetTitle = "年龄算术",
        sheetSub = "精准年月日时分秒",
        icon = ToolboxTab.AGE_CALC.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.AGE_CALC,
                name = "年龄算术",
                desc = "精准年月日时分秒"
            )
        )
    ),
    RingMenuGroup(
        id = "offline_treasure",
        entryLabel = "离线百宝",
        sheetTitle = "离线百宝",
        sheetSub = "LED滚动弹幕",
        icon = ToolboxTab.OFFLINE_TREASURE.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.OFFLINE_TREASURE,
                name = "离线百宝",
                desc = "LED滚动弹幕"
            )
        )
    ),
    RingMenuGroup(
        id = "food_picker",
        entryLabel = "今天吃什么",
        sheetTitle = "今天吃什么",
        sheetSub = "随机摇签",
        icon = ToolboxTab.FOOD_PICKER.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.FOOD_PICKER,
                name = "今天吃什么",
                desc = "随机摇签"
            )
        )
    ),
    RingMenuGroup(
        id = "emergency_phone",
        entryLabel = "紧急电话",
        sheetTitle = "紧急电话",
        sheetSub = "救援/道路/举报/法律",
        icon = ToolboxTab.EMERGENCY_PHONE.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.EMERGENCY_PHONE,
                name = "紧急电话",
                desc = "救援/道路/举报/法律"
            )
        )
    ),
    RingMenuGroup(
        id = "license_plate",
        entryLabel = "车牌摇号",
        sheetTitle = "车牌摇号",
        sheetSub = "全国城市",
        icon = ToolboxTab.LICENSE_PLATE.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.LICENSE_PLATE,
                name = "车牌摇号",
                desc = "全国城市"
            )
        )
    ),
    RingMenuGroup(
        id = "cold_joke",
        entryLabel = "冷笑话",
        sheetTitle = "冷笑话",
        sheetSub = "随机一条冷笑话",
        icon = ToolboxTab.COLD_JOKE.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.COLD_JOKE,
                name = "冷笑话",
                desc = "随机一条冷笑话"
            )
        )
    ),
    RingMenuGroup(
        id = "speed_test",
        entryLabel = "测速网",
        sheetTitle = "测速网",
        sheetSub = "真实下载测速",
        icon = ToolboxTab.SPEED_TEST.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.SPEED_TEST,
                name = "测速网",
                desc = "真实下载测速"
            )
        )
    ),
    RingMenuGroup(
        id = "currency",
        entryLabel = "货币转换",
        sheetTitle = "货币转换",
        sheetSub = "全球 50+ 货币",
        icon = ToolboxTab.CURRENCY.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.CURRENCY,
                name = "货币转换",
                desc = "全球 50+ 货币"
            )
        )
    ),
    RingMenuGroup(
        id = "period",
        entryLabel = "生理期记录",
        sheetTitle = "生理期记录",
        sheetSub = "开始/结束日期",
        icon = ToolboxTab.PERIOD.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.PERIOD,
                name = "生理期记录",
                desc = "开始/结束日期"
            )
        )
    ),
    RingMenuGroup(
        id = "location_mock",
        entryLabel = "IP纯净度",
        sheetTitle = "IP 纯净度检测",
        sheetSub = "原生深度诊断 (ping0.cc 仅作为参考)",
        icon = ToolboxTab.LOCATION_MOCK.icon,
        options = listOf(
            RingMenuOption(
                tab = ToolboxTab.LOCATION_MOCK,
                name = "IP 纯净度与欺诈检测",
                desc = "原生深度诊断 · IP纯净度安全分 · 住宅/机房识别 · 代理检测 (ping0.cc 仅作为参考)"
            )
        )
    )
)

@Composable
private fun RingMenuSection(
    allGroups: List<RingMenuGroup>,
    presentationStyle: RingPresentationStyle,
    pressEffect: RingPressEffect,
    activeGroupIndex: Int?,
    onOpenSettings: () -> Unit,
    onAddToolClick: () -> Unit,
    onSelectGroup: (Int) -> Unit,
    onDirectOpenTool: (ToolboxTab) -> Unit,
    onOpenExternalUrl: (String) -> Unit
) {
    val isDark = presentationStyle == RingPresentationStyle.CYBER_GLASS
    val rmText = if (isDark) Color(0xFFF2ECFF) else Color(0xFF34244E)
    val rmTextSoft = if (isDark) Color(0xFFC4B5FD) else Color(0xFF645182)
    val rmAccent = when (presentationStyle) {
        RingPresentationStyle.LIQUID_CRYSTAL -> Color(0xFF7C3AED)
        RingPresentationStyle.HOLO_PRISM -> Color(0xFFEC4899)
        RingPresentationStyle.CYBER_GLASS -> Color(0xFFA78BFA)
        RingPresentationStyle.FROST_JADE -> Color(0xFF6366F1)
    }

    // 液态玻璃流动光斑动画
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_glass_flow")
    val blobPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.283185f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blob_phase"
    )
    val shimmerSweep by infiniteTransition.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_sweep"
    )

    // 核心规则：工具箱里的环形如果有 8 个工具，自动添加新的环形！
    // 1. 按每环最多 8 个工具切分；
    // 2. 若最后一个环形刚好满 8 个工具（或当前没有任何环形），自动在下方追加一个全新的环形（带「添加工具」槽位）
    val ringChunks = remember(allGroups) {
        val baseChunks = allGroups.chunked(8).toMutableList()
        if (baseChunks.isEmpty() || baseChunks.last().size >= 8) {
            val nextRingNum = baseChunks.size + 1
            baseChunks.add(
                listOf(
                    RingMenuGroup(
                        id = "auto_new_ring_slot_$nextRingNum",
                        entryLabel = "新增工具",
                        sheetTitle = "第 $nextRingNum 环 · 新增工具",
                        sheetSub = "前序环形已满 8 个，已自动开启第 $nextRingNum 环形",
                        icon = Icons.Filled.AddCircle,
                        emojiIcon = "➕",
                        isAddSlot = true,
                        options = emptyList()
                    )
                )
            )
        }
        baseChunks
    }

    val containerShape = RoundedCornerShape(26.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .testTag("ring_menu_container"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ringChunks.forEachIndexed { ringIndex, chunkGroups ->
            val baseGroupOffset = ringIndex * 8

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 14.dp,
                        shape = containerShape,
                        ambientColor = rmAccent.copy(alpha = 0.20f),
                        spotColor = rmAccent.copy(alpha = 0.24f)
                    )
                    .clip(containerShape)
                    .background(
                        brush = when (presentationStyle) {
                            RingPresentationStyle.LIQUID_CRYSTAL -> Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.48f),
                                    Color(0xFFF3E8FF).copy(alpha = 0.36f),
                                    Color(0xFFFFE4F3).copy(alpha = 0.42f),
                                    Color.White.copy(alpha = 0.54f)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(1000f, 1600f)
                            )
                            RingPresentationStyle.HOLO_PRISM -> Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE0F2FE).copy(alpha = 0.50f),
                                    Color(0xFFFCE7F3).copy(alpha = 0.44f),
                                    Color(0xFFEDE9FE).copy(alpha = 0.48f),
                                    Color(0xFFFEF3C7).copy(alpha = 0.42f)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(1000f, 1600f)
                            )
                            RingPresentationStyle.CYBER_GLASS -> Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF171126).copy(alpha = 0.78f),
                                    Color(0xFF231938).copy(alpha = 0.72f),
                                    Color(0xFF1B142E).copy(alpha = 0.80f)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(1000f, 1600f)
                            )
                            RingPresentationStyle.FROST_JADE -> Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.72f),
                                    Color(0xFFF8FAFC).copy(alpha = 0.65f),
                                    Color(0xFFF3E8FF).copy(alpha = 0.60f)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(1000f, 1600f)
                            )
                        }
                    )
                    .streamingBorder(
                        cornerRadius = 26.dp,
                        strokeWidth = 1.6.dp,
                        glowWidth = 3.6.dp,
                        accentColor = rmAccent,
                        tailColor = rmAccent.copy(alpha = 0.5f),
                        baseBorderColor = if (isDark) Color.White.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.65f),
                        phaseOffset = ringIndex * 90f,
                        showGlow = true
                    )
                    .padding(top = 10.dp, bottom = 16.dp, start = 10.dp, end = 10.dp)
                    .testTag("ring_card_${ringIndex + 1}")
            ) {
                // 液态玻璃折射光斑 + 镜面高光流光层（每个独立环形卡片各自折射）
                Canvas(modifier = Modifier.matchParentSize()) {
                    val w = size.width
                    val h = size.height
                    val phaseShift = ringIndex * 1.35f

                    // 游走液态光球 1
                    val b1x = w * (0.25f + 0.18f * cos(blobPhase + phaseShift))
                    val b1y = h * (0.24f + 0.14f * sin(blobPhase + phaseShift))
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                if (isDark) Color(0xFF8B5CF6).copy(alpha = 0.32f) else Color(0xFFD8B4FE).copy(alpha = 0.50f),
                                Color.Transparent
                            ),
                            center = Offset(b1x, b1y),
                            radius = w * 0.55f
                        ),
                        center = Offset(b1x, b1y),
                        radius = w * 0.55f
                    )

                    // 游走液态光球 2
                    val b2x = w * (0.76f + 0.16f * sin(blobPhase * 0.8f + phaseShift))
                    val b2y = h * (0.68f + 0.15f * cos(blobPhase * 0.8f + phaseShift))
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                if (isDark) Color(0xFFEC4899).copy(alpha = 0.24f) else Color(0xFFFBCFE8).copy(alpha = 0.52f),
                                Color.Transparent
                            ),
                            center = Offset(b2x, b2y),
                            radius = w * 0.52f
                        ),
                        center = Offset(b2x, b2y),
                        radius = w * 0.52f
                    )

                    // 顶部液态玻璃曲面折射高光带
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = if (isDark) 0.18f else 0.58f),
                                Color.White.copy(alpha = 0.04f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = h * 0.32f
                        ),
                        topLeft = Offset(8.dp.toPx(), 6.dp.toPx()),
                        size = Size(w - 16.dp.toPx(), h * 0.28f),
                        cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LiquidGlassSingleRing(
                        ringIndex = ringIndex,
                        groups = chunkGroups,
                        baseGroupOffset = baseGroupOffset,
                        presentationStyle = presentationStyle,
                        pressEffect = pressEffect,
                        blobPhase = blobPhase,
                        shimmerSweep = shimmerSweep,
                        activeGroupIndex = activeGroupIndex,
                        rmText = rmText,
                        rmTextSoft = rmTextSoft,
                        rmAccent = rmAccent,
                        onOpenSettings = onOpenSettings,
                        onAddToolClick = onAddToolClick,
                        onSelectGroup = onSelectGroup,
                        onDirectOpenTool = onDirectOpenTool,
                        onOpenExternalUrl = onOpenExternalUrl
                    )
                }
            }
        }
    }
}

@Composable
private fun LiquidGlassSingleRing(
    ringIndex: Int,
    groups: List<RingMenuGroup>,
    baseGroupOffset: Int,
    presentationStyle: RingPresentationStyle,
    pressEffect: RingPressEffect,
    blobPhase: Float,
    shimmerSweep: Float,
    activeGroupIndex: Int?,
    rmText: Color,
    rmTextSoft: Color,
    rmAccent: Color,
    onOpenSettings: () -> Unit,
    onAddToolClick: () -> Unit,
    onSelectGroup: (Int) -> Unit,
    onDirectOpenTool: (ToolboxTab) -> Unit,
    onOpenExternalUrl: (String) -> Unit
) {
    val isDark = presentationStyle == RingPresentationStyle.CYBER_GLASS
    val rmAccentSoft = rmAccent.copy(alpha = if (isDark) 0.22f else 0.15f)
    val ringSize = 252.dp
    val ringInner = 106.dp
    val itemRadius = 90.dp
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    // =========================================================================
    // 最新动画技术：按住不动随手指移动而移动（Spring 物理阻尼追随 + 3D 陀螺仪视差倾角 + 环形转盘旋转 + 液态粒子拖尾）
    // =========================================================================
    // 1. 整个星环随手指按住移动的平滑物理位移与自由旋转角
    val ringDragOffsetX = remember { androidx.compose.animation.core.Animatable(0f) }
    val ringDragOffsetY = remember { androidx.compose.animation.core.Animatable(0f) }
    var ringRotationDeg by remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    var isRingDragging by remember { mutableStateOf(false) }
    var fingerCanvasPos by remember { mutableStateOf<Offset?>(null) }

    // 2. 单个工具按钮被按住拖拽时的独立跟随偏移（key = localIdx）
    var draggingItemIdx by remember { mutableStateOf<Int?>(null) }
    val itemDragOffsetX = remember { androidx.compose.animation.core.Animatable(0f) }
    val itemDragOffsetY = remember { androidx.compose.animation.core.Animatable(0f) }

    // 3D 视差倾角（根据当前手指偏移实时计算 X/Y 轴 3D 倾斜角）
    val tiltX by animateFloatAsState(
        targetValue = ((-ringDragOffsetY.value - itemDragOffsetY.value * 0.45f) / 7f).coerceIn(-18f, 18f),
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 320f),
        label = "ring_tilt_x_$ringIndex"
    )
    val tiltY by animateFloatAsState(
        targetValue = ((ringDragOffsetX.value + itemDragOffsetX.value * 0.45f) / 7f).coerceIn(-18f, 18f),
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 320f),
        label = "ring_tilt_y_$ringIndex"
    )
    val activeScale by animateFloatAsState(
        targetValue = if (isRingDragging || draggingItemIdx != null) 1.035f else 1f,
        animationSpec = spring(dampingRatio = 0.50f, stiffness = 360f),
        label = "ring_active_scale_$ringIndex"
    )

    Box(
        modifier = Modifier
            .size(ringSize)
            .offset {
                IntOffset(
                    ringDragOffsetX.value.roundToInt(),
                    ringDragOffsetY.value.roundToInt()
                )
            }
            .graphicsLayer {
                scaleX = activeScale
                scaleY = activeScale
                rotationX = tiltX
                rotationY = tiltY
                cameraDistance = 12f * density
            }
            // 在圆环背景区域按住不放并移动手指：整个圆环跟随手指移动 + 顺滑拨动旋转圆环
            .pointerInput(ringIndex) {
                awaitPointerEventScope {
                    var lastPos: Offset? = null
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull() ?: continue
                        if (change.pressed) {
                            val cur = change.position
                            fingerCanvasPos = cur
                            val prev = lastPos
                            if (prev != null) {
                                val dx = cur.x - prev.x
                                val dy = cur.y - prev.y
                                if (kotlin.math.hypot(dx, dy) > 1.5f) {
                                    isRingDragging = true
                                    // 计算围绕圆心的切向角度增量（手指划圈可拨动星环旋转）
                                    val center = Offset(size.width / 2f, size.height / 2f)
                                    val prevVec = prev - center
                                    val curVec = cur - center
                                    if (prevVec.getDistance() > 28f && curVec.getDistance() > 28f) {
                                        val a1 = kotlin.math.atan2(prevVec.y, prevVec.x)
                                        val a2 = kotlin.math.atan2(curVec.y, curVec.x)
                                        var deltaDeg = Math.toDegrees((a2 - a1).toDouble()).toFloat()
                                        if (deltaDeg > 180f) deltaDeg -= 360f
                                        if (deltaDeg < -180f) deltaDeg += 360f
                                        ringRotationDeg = (ringRotationDeg + deltaDeg * 0.85f) % 360f
                                    }
                                    // 同时整个圆环随手指产生弹性牵引位移（带磁吸边界阻尼）
                                    val nextX = (ringDragOffsetX.value + dx * 0.58f).coerceIn(-68f, 68f)
                                    val nextY = (ringDragOffsetY.value + dy * 0.58f).coerceIn(-68f, 68f)
                                    coroutineScope.launch {
                                        ringDragOffsetX.snapTo(nextX)
                                        ringDragOffsetY.snapTo(nextY)
                                    }
                                }
                            }
                            lastPos = cur
                        } else {
                            if (isRingDragging) {
                                isRingDragging = false
                                fingerCanvasPos = null
                                // 松手后通过高弹力胡克定律弹簧（Spring Physics）丝滑回弹归位
                                coroutineScope.launch {
                                    launch {
                                        ringDragOffsetX.animateTo(
                                            0f,
                                            spring(dampingRatio = 0.46f, stiffness = 360f)
                                        )
                                    }
                                    launch {
                                        ringDragOffsetY.animateTo(
                                            0f,
                                            spring(dampingRatio = 0.46f, stiffness = 360f)
                                        )
                                    }
                                }
                            } else {
                                fingerCanvasPos = null
                            }
                            lastPos = null
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // 液态玻璃圆环本体（高透折射 + 双层镜面高光环 + 中心镂空内凹光学边缘 + 手指液态融合水滴牵引线）
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        ) {
            val outerRadius = size.minDimension / 2f
            val innerRadius = (ringInner.toPx()) / 2f
            val centerOffset = Offset(size.width / 2f, size.height / 2f)

            // 1. 底部液态景深光晕（随手指拖拽反向偏移营造真实悬浮高度）
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        rmAccent.copy(alpha = if (isDark) 0.42f else 0.26f),
                        Color.Transparent
                    ),
                    center = Offset(
                        centerOffset.x - ringDragOffsetX.value * 0.35f,
                        centerOffset.y + 12.dp.toPx() - ringDragOffsetY.value * 0.35f
                    ),
                    radius = outerRadius * 1.03f
                ),
                radius = outerRadius,
                center = centerOffset
            )

            // 2. 液态玻璃环体多层半透明渐变
            val ringStops = when (presentationStyle) {
                RingPresentationStyle.LIQUID_CRYSTAL -> arrayOf(
                    0f to Color.Transparent,
                    (innerRadius / outerRadius) to Color.White.copy(alpha = 0.78f),
                    ((innerRadius + 8.dp.toPx()) / outerRadius).coerceAtMost(0.92f) to Color(0xFFF8F2FF).copy(alpha = 0.56f),
                    ((innerRadius + 36.dp.toPx()) / outerRadius).coerceAtMost(0.96f) to Color(0xFFEDE2FF).copy(alpha = 0.44f),
                    1f to Color.White.copy(alpha = 0.72f)
                )
                RingPresentationStyle.HOLO_PRISM -> arrayOf(
                    0f to Color.Transparent,
                    (innerRadius / outerRadius) to Color.White.copy(alpha = 0.82f),
                    ((innerRadius + 14.dp.toPx()) / outerRadius).coerceAtMost(0.93f) to Color(0xFFFCE7F3).copy(alpha = 0.58f),
                    ((innerRadius + 38.dp.toPx()) / outerRadius).coerceAtMost(0.97f) to Color(0xFFE0F2FE).copy(alpha = 0.54f),
                    1f to Color(0xFFEDE9FE).copy(alpha = 0.78f)
                )
                RingPresentationStyle.CYBER_GLASS -> arrayOf(
                    0f to Color.Transparent,
                    (innerRadius / outerRadius) to Color(0xFF4C3A6E).copy(alpha = 0.74f),
                    ((innerRadius + 14.dp.toPx()) / outerRadius).coerceAtMost(0.94f) to Color(0xFF2D2246).copy(alpha = 0.65f),
                    ((innerRadius + 40.dp.toPx()) / outerRadius).coerceAtMost(0.98f) to Color(0xFF211836).copy(alpha = 0.72f),
                    1f to Color(0xFF3B2C5A).copy(alpha = 0.80f)
                )
                RingPresentationStyle.FROST_JADE -> arrayOf(
                    0f to Color.Transparent,
                    (innerRadius / outerRadius) to Color.White.copy(alpha = 0.90f),
                    ((innerRadius + 18.dp.toPx()) / outerRadius).coerceAtMost(0.94f) to Color(0xFFF5F3FF).copy(alpha = 0.78f),
                    1f to Color.White.copy(alpha = 0.88f)
                )
            }

            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = ringStops,
                    center = centerOffset,
                    radius = outerRadius
                ),
                radius = outerRadius,
                center = centerOffset
            )

            // 3. 动态流光折射高光（随手指位置 + blobPhase 实时追踪折射）
            val highlightX = fingerCanvasPos?.x ?: (size.width * (0.30f + 0.12f * cos(blobPhase)))
            val highlightY = fingerCanvasPos?.y ?: (size.height * (0.24f + 0.12f * sin(blobPhase)))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = if (isDark) 0.34f else 0.90f),
                        rmAccent.copy(alpha = if (fingerCanvasPos != null) 0.22f else 0.06f),
                        Color.Transparent
                    ),
                    center = Offset(highlightX, highlightY),
                    radius = outerRadius * 0.78f
                ),
                radius = outerRadius,
                center = centerOffset
            )

            // 4. 外环液态玻璃高光折射边
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.95f),
                        rmAccent.copy(alpha = 0.45f),
                        Color.White.copy(alpha = 0.90f),
                        Color(0xFFF472B6).copy(alpha = 0.40f),
                        Color.White.copy(alpha = 0.95f)
                    ),
                    center = centerOffset
                ),
                radius = outerRadius - 1.2.dp.toPx(),
                center = centerOffset,
                style = Stroke(width = 2.2.dp.toPx())
            )

            // 5. 镂空中心圆孔
            drawCircle(
                color = Color.Transparent,
                radius = innerRadius,
                center = centerOffset,
                blendMode = BlendMode.Clear
            )

            // 6. 圆孔液态内凹光学折射圈
            drawCircle(
                color = if (isDark) Color(0xFFA78BFA).copy(alpha = 0.45f) else Color(0xFF8B5CF6).copy(alpha = 0.24f),
                radius = innerRadius + 1.5.dp.toPx(),
                center = centerOffset,
                style = Stroke(width = 2.6.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = if (isDark) 0.35f else 0.92f),
                radius = innerRadius,
                center = centerOffset,
                style = Stroke(width = 1.2.dp.toPx())
            )
        }

        // 圆孔中央：「懒 得 找 了」液态玻璃按钮（既可点击打开设置，也可按住拖动整个圆环随手指移动）
        val holeInteraction = remember { MutableInteractionSource() }
        val isHolePressed by holeInteraction.collectIsPressedAsState()
        val holeScale by animateFloatAsState(
            targetValue = if (isHolePressed || isRingDragging) 0.93f else 1f,
            animationSpec = spring(dampingRatio = 0.50f, stiffness = 400f),
            label = "hole_scale_$ringIndex"
        )

        Box(
            modifier = Modifier
                .size(ringInner - 8.dp)
                .scale(holeScale)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.16f else 0.62f),
                            rmAccent.copy(alpha = if (isDark) 0.20f else 0.10f),
                            Color.Transparent
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = if (isDark) 0.30f else 0.80f),
                    shape = CircleShape
                )
                .pointerInput(ringIndex) {
                    var totalDrag = 0f
                    awaitPointerEventScope {
                        while (true) {
                            val ev = awaitPointerEvent()
                            val ch = ev.changes.firstOrNull() ?: continue
                            if (ch.pressed) {
                                val delta = ch.position - ch.previousPosition
                                val dist = delta.getDistance()
                                if (dist > 0.5f) {
                                    totalDrag += dist
                                    if (totalDrag > 8f) {
                                        isRingDragging = true
                                        ch.consume()
                                        val nx = (ringDragOffsetX.value + delta.x * 0.72f).coerceIn(-75f, 75f)
                                        val ny = (ringDragOffsetY.value + delta.y * 0.72f).coerceIn(-75f, 75f)
                                        coroutineScope.launch {
                                            ringDragOffsetX.snapTo(nx)
                                            ringDragOffsetY.snapTo(ny)
                                        }
                                    }
                                }
                            } else {
                                if (isRingDragging) {
                                    isRingDragging = false
                                    coroutineScope.launch {
                                        launch {
                                            ringDragOffsetX.animateTo(
                                                0f,
                                                spring(dampingRatio = 0.45f, stiffness = 360f)
                                            )
                                        }
                                        launch {
                                            ringDragOffsetY.animateTo(
                                                0f,
                                                spring(dampingRatio = 0.45f, stiffness = 360f)
                                            )
                                        }
                                    }
                                } else if (totalDrag in 0f..8f && ch.changedToUpIgnoreConsumed()) {
                                    onOpenSettings()
                                }
                                totalDrag = 0f
                            }
                        }
                    }
                }
                .clickable(
                    interactionSource = holeInteraction,
                    indication = null,
                    onClick = onOpenSettings
                )
                .testTag(if (ringIndex == 0) "ring_center_settings_btn" else "ring_center_settings_btn_$ringIndex"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "懒 得 找 了",
                fontSize = 11.5.sp,
                letterSpacing = 1.sp,
                color = if (isHolePressed || isRingDragging) rmAccent else rmText.copy(alpha = 0.85f),
                fontWeight = FontWeight.Bold
            )
        }

        // 环形入口按钮（支持：1. 轻触直接打开对应功能窗口；2. 按住不放随手指自由移动 + 液态磁吸连线 + 拨动圆环旋转）
        val count = groups.size.coerceAtLeast(1)
        groups.forEachIndexed { localIdx, group ->
            val globalIndex = baseGroupOffset + localIdx
            val angleDeg = (localIdx * (360f / count)) + ringRotationDeg
            val isActive = activeGroupIndex == globalIndex
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val isThisItemDragging = draggingItemIdx == localIdx

            // 根据设置中选中的「按住时触发的动态效果」计算实时形变与动效
            val targetExtraRadius = when {
                (isPressed || isThisItemDragging) && pressEffect == RingPressEffect.LIQUID_RIPPLE -> 12f
                (isPressed || isThisItemDragging) && pressEffect == RingPressEffect.JELLY_BOUNCE -> -5f
                (isPressed || isThisItemDragging) && pressEffect == RingPressEffect.GRAVITY_VORTEX -> 10f
                (isPressed || isThisItemDragging) && pressEffect == RingPressEffect.NEON_SHOCK -> 9f
                isActive -> 8f
                else -> 0f
            }
            val targetScale = when {
                isThisItemDragging -> 1.28f
                isPressed && pressEffect == RingPressEffect.LIQUID_RIPPLE -> 1.18f
                isPressed && pressEffect == RingPressEffect.JELLY_BOUNCE -> 1.24f
                isPressed && pressEffect == RingPressEffect.GRAVITY_VORTEX -> 1.15f
                isPressed && pressEffect == RingPressEffect.NEON_SHOCK -> 1.20f
                isActive -> 1.14f
                else -> 1f
            }
            val targetRotation = when {
                isThisItemDragging -> (itemDragOffsetX.value * 0.18f).coerceIn(-16f, 16f)
                isPressed && pressEffect == RingPressEffect.GRAVITY_VORTEX -> 12f
                isPressed && pressEffect == RingPressEffect.JELLY_BOUNCE -> -6f
                else -> 0f
            }

            val extraRadius by animateFloatAsState(
                targetValue = targetExtraRadius,
                animationSpec = spring(dampingRatio = 0.52f, stiffness = 380f),
                label = "entry_radius_$globalIndex"
            )
            val entryScale by animateFloatAsState(
                targetValue = targetScale,
                animationSpec = spring(dampingRatio = 0.45f, stiffness = 380f),
                label = "entry_scale_$globalIndex"
            )
            val entryRotation by animateFloatAsState(
                targetValue = targetRotation,
                animationSpec = spring(dampingRatio = 0.50f, stiffness = 350f),
                label = "entry_rot_$globalIndex"
            )

            val rad = Math.toRadians(angleDeg.toDouble())
            val currentRadiusDp = itemRadius.value + extraRadius
            val baseOffsetX = (currentRadiusDp * sin(rad)).dp
            val baseOffsetY = (-currentRadiusDp * cos(rad)).dp

            // 当某个按钮被按住随手指移动时，绘制原锚点到当前手指位置的「液态水滴弹性牵引丝（Metaball Elastic Tether）」
            if (isThisItemDragging) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val anchor = Offset(
                        center.x + baseOffsetX.toPx(),
                        center.y + baseOffsetY.toPx()
                    )
                    val current = Offset(
                        anchor.x + itemDragOffsetX.value,
                        anchor.y + itemDragOffsetY.value
                    )
                    // 原始槽位虚影光圈
                    drawCircle(
                        color = rmAccent.copy(alpha = 0.25f),
                        radius = 22.dp.toPx(),
                        center = anchor,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    // 液态弹性磁吸连线
                    drawLine(
                        brush = Brush.linearGradient(
                            colors = listOf(rmAccent.copy(alpha = 0.18f), rmAccent.copy(alpha = 0.65f)),
                            start = anchor,
                            end = current
                        ),
                        start = anchor,
                        end = current,
                        strokeWidth = 4.dp.toPx(),
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    // 沿连线游走的液态能量水滴
                    for (step in 1..3) {
                        val t = step / 4f
                        val bx = anchor.x + (current.x - anchor.x) * t
                        val by = anchor.y + (current.y - anchor.y) * t
                        drawCircle(
                            color = Color.White.copy(alpha = 0.78f),
                            radius = (2.5f + step * 0.8f).dp.toPx(),
                            center = Offset(bx, by)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .offset(x = baseOffsetX, y = baseOffsetY)
                    .offset {
                        if (isThisItemDragging) {
                            IntOffset(
                                itemDragOffsetX.value.roundToInt(),
                                itemDragOffsetY.value.roundToInt()
                            )
                        } else {
                            IntOffset.Zero
                        }
                    }
                    .scale(entryScale)
                    .rotate(entryRotation),
                contentAlignment = Alignment.Center
            ) {
                // 当按住或拖拽按钮时触发的专属 CSS 动态特效层
                if (isPressed || isActive || isThisItemDragging) {
                    val pulseAlpha = 0.45f + 0.35f * sin(blobPhase * 3f)
                    Canvas(modifier = Modifier.size(64.dp)) {
                        when (pressEffect) {
                            RingPressEffect.LIQUID_RIPPLE -> {
                                drawCircle(
                                    color = rmAccent.copy(alpha = 0.30f),
                                    radius = size.minDimension * 0.48f,
                                    style = Stroke(width = 2.dp.toPx())
                                )
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.70f),
                                    radius = size.minDimension * 0.40f,
                                    style = Stroke(width = 1.2.dp.toPx())
                                )
                            }
                            RingPressEffect.JELLY_BOUNCE -> {
                                drawRoundRect(
                                    color = rmAccent.copy(alpha = 0.24f),
                                    cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
                                )
                            }
                            RingPressEffect.GRAVITY_VORTEX -> {
                                drawCircle(
                                    brush = Brush.sweepGradient(
                                        listOf(rmAccent, Color.Transparent, Color(0xFFEC4899), rmAccent)
                                    ),
                                    radius = size.minDimension * 0.46f,
                                    style = Stroke(width = 2.5.dp.toPx())
                                )
                            }
                            RingPressEffect.NEON_SHOCK -> {
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        listOf(rmAccent.copy(alpha = pulseAlpha.coerceIn(0.15f, 0.75f)), Color.Transparent)
                                    ),
                                    radius = size.minDimension * 0.50f
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isActive || isPressed || isThisItemDragging) {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = if (isDark) 0.28f else 0.85f),
                                        rmAccentSoft
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(Color.Transparent, Color.Transparent)
                                )
                            }
                        )
                        .border(
                            width = if (isActive || isPressed || isThisItemDragging) 1.2.dp else 0.dp,
                            color = if (isActive || isPressed || isThisItemDragging) Color.White.copy(alpha = 0.90f) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        // 按住不放随手指移动：既可把当前功能图标拖出跟随手指，也可顺着圆周拨动整个星环旋转；轻触或松手打开功能
                        .pointerInput(globalIndex) {
                            awaitPointerEventScope {
                                var totalMove = 0f
                                var isPressActive = false
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val change = event.changes.firstOrNull() ?: continue
                                    if (change.pressed) {
                                        if (!isPressActive) {
                                            isPressActive = true
                                            totalMove = 0f
                                        }
                                        val delta = change.position - change.previousPosition
                                        val dist = delta.getDistance()
                                        if (dist > 0.5f) {
                                            totalMove += dist
                                            if (totalMove > 6f) {
                                                change.consume()
                                                draggingItemIdx = localIdx
                                                val targetX = (itemDragOffsetX.value + delta.x).coerceIn(-115f, 115f)
                                                val targetY = (itemDragOffsetY.value + delta.y).coerceIn(-115f, 115f)
                                                // 同时带动整个圆环产生柔和协同跟随与微转
                                                ringRotationDeg = (ringRotationDeg + (delta.x - delta.y) * 0.08f) % 360f
                                                coroutineScope.launch {
                                                    itemDragOffsetX.snapTo(targetX)
                                                    itemDragOffsetY.snapTo(targetY)
                                                    ringDragOffsetX.snapTo((ringDragOffsetX.value + delta.x * 0.22f).coerceIn(-48f, 48f))
                                                    ringDragOffsetY.snapTo((ringDragOffsetY.value + delta.y * 0.22f).coerceIn(-48f, 48f))
                                                }
                                            }
                                        }
                                    } else {
                                        if (isPressActive) {
                                            isPressActive = false
                                            val wasDragging = draggingItemIdx == localIdx && totalMove > 10f
                                            if (wasDragging) {
                                                // 松开手指：触发果冻弹簧物理回弹动画归位
                                                coroutineScope.launch {
                                                    val j1 = launch {
                                                        itemDragOffsetX.animateTo(
                                                            0f,
                                                            spring(dampingRatio = 0.42f, stiffness = 420f)
                                                        )
                                                    }
                                                    val j2 = launch {
                                                        itemDragOffsetY.animateTo(
                                                            0f,
                                                            spring(dampingRatio = 0.42f, stiffness = 420f)
                                                        )
                                                    }
                                                    launch {
                                                        ringDragOffsetX.animateTo(
                                                            0f,
                                                            spring(dampingRatio = 0.48f, stiffness = 360f)
                                                        )
                                                    }
                                                    launch {
                                                        ringDragOffsetY.animateTo(
                                                            0f,
                                                            spring(dampingRatio = 0.48f, stiffness = 360f)
                                                        )
                                                    }
                                                    j1.join()
                                                    j2.join()
                                                    if (draggingItemIdx == localIdx) {
                                                        draggingItemIdx = null
                                                    }
                                                }
                                            } else {
                                                draggingItemIdx = null
                                                when {
                                                    group.isAddSlot -> onAddToolClick()
                                                    group.options.firstOrNull()?.tab != null -> {
                                                        group.options.firstOrNull()?.tab?.let { tab ->
                                                            onDirectOpenTool(tab)
                                                        }
                                                    }
                                                    !group.externalUrl.isNullOrBlank() -> {
                                                        onOpenExternalUrl(group.externalUrl)
                                                    }
                                                    else -> onSelectGroup(globalIndex)
                                                }
                                            }
                                            totalMove = 0f
                                        }
                                    }
                                }
                            }
                        }
                        .testTag("ring_entry_${group.id}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (!group.emojiIcon.isNullOrBlank()) {
                        Text(
                            text = group.emojiIcon,
                            fontSize = 14.5.sp
                        )
                    } else {
                        Icon(
                            imageVector = group.icon,
                            contentDescription = "${group.entryLabel}，打开面板",
                            tint = if (isActive || isPressed || isThisItemDragging) rmAccent else rmText,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = group.entryLabel,
                        color = if (isActive || isPressed || isThisItemDragging) rmAccent else rmText,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * 新增环形工具弹窗（当某个圆环达到 8 个工具时，自动开启下一圆环）
 */
@Composable
private fun AddRingToolDialog(
    currentTotalCount: Int,
    onDismiss: () -> Unit,
    onConfirmAdd: (name: String, desc: String, url: String, emoji: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("https://") }
    var emoji by remember { mutableStateOf("✨") }
    val targetRing = (currentTotalCount / 8) + 1
    val slotInRing = (currentTotalCount % 8) + 1

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White.copy(alpha = 0.72f),
            border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.92f)),
            shadowElevation = 18.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "➕ 添加工具到第 $targetRing 环形",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF34244E)
                        )
                        Text(
                            text = "当前槽位：$slotInRing/8（满 8 个自动添加新环形）",
                            fontSize = 11.sp,
                            color = Color(0xFF7C3AED)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "关闭", tint = Color(0xFF6A5A85))
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("工具名称（如：AI绘图中枢）") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = emoji,
                    onValueChange = { emoji = it },
                    label = { Text("图标 Emoji（如：🚀 / 🛠️ / 🌐）") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("跳转链接 URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("简短描述（选填）") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(999.dp),
                        color = Color(0xFFF3E8FF),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(modifier = Modifier.padding(vertical = 11.dp), contentAlignment = Alignment.Center) {
                            Text("取消", fontWeight = FontWeight.Bold, color = Color(0xFF6A5A85), fontSize = 13.sp)
                        }
                    }
                    Surface(
                        onClick = {
                            val finalName = name.trim().ifBlank { "自定义工具${currentTotalCount + 1}" }
                            val finalUrl = url.trim().let {
                                if (it.isBlank() || it == "https://") "https://www.baidu.com"
                                else if (!it.startsWith("http://") && !it.startsWith("https://")) "https://$it"
                                else it
                            }
                            onConfirmAdd(finalName, desc.trim(), finalUrl, emoji.trim())
                        },
                        shape = RoundedCornerShape(999.dp),
                        color = Color(0xFF7C3AED),
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Box(modifier = Modifier.padding(vertical = 11.dp), contentAlignment = Alignment.Center) {
                            Text("加入环形", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

object LocalCustomBgHolder {
    var mediaType by mutableStateOf("none")
    var mediaUri by mutableStateOf("")
}

/**
 * 情感语录功能弹窗（已彻底移除原环形样式和特效）：
 * 点击圆心「懒 得 找 了」触发，汇聚治愈、励志、人间烟火、释怀放下、温柔诗篇等经典心语。
 * 支持随机换一句、一键复制、精美液态玻璃卡片呈现。
 */
@Composable
private fun EmotionalQuotesDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val infiniteTransition = rememberInfiniteTransition(label = "quotes_liquid_glow")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.283185f,
        animationSpec = infiniteRepeatable(tween(4800, easing = LinearEasing), RepeatMode.Restart),
        label = "quote_wave"
    )

    data class EmotionalQuote(
        val text: String,
        val author: String,
        val category: String,
        val tag: String
    )

    val allQuotes = remember {
        listOf(
            EmotionalQuote(
                text = "这短短的一生，我们最终都会失去。你不妨大胆一些，爱一个人，攀一座山，追一个梦。",
                author = "《大鱼海棠》",
                category = "治愈",
                tag = "勇往直前"
            ),
            EmotionalQuote(
                text = "去吹吹晚风吧，只要你愿意，生活总会在转角处送给你意想不到的温柔。",
                author = "林清玄",
                category = "温柔",
                tag = "人间温柔"
            ),
            EmotionalQuote(
                text = "允许一切发生，生活不是为了赶路，而是为了感受路上的每一朵野花。",
                author = "李娟",
                category = "释怀",
                tag = "顺其自然"
            ),
            EmotionalQuote(
                text = "慢品人间烟火色，闲观万事岁月长。生活原本沉闷，但跑起来就会有风。",
                author = "苏轼 · 题记",
                category = "烟火",
                tag = "静心安然"
            ),
            EmotionalQuote(
                text = "世界上只有一种真正的英雄主义，那就是认清生活的真相后依然热爱生活。",
                author = "罗曼·罗兰",
                category = "励志",
                tag = "内心丰盈"
            ),
            EmotionalQuote(
                text = "愿你历经千帆，归来仍是少年；愿你眼里有光，活成自己喜欢的模样。",
                author = "现代诗篇",
                category = "治愈",
                tag = "初心不改"
            ),
            EmotionalQuote(
                text = "每一个不曾起舞的日子，都是对生命的辜负。",
                author = "尼采",
                category = "励志",
                tag = "奔赴热爱"
            ),
            EmotionalQuote(
                text = "凡是过往，皆为序章。所有的失去，都将以另一种方式归来。",
                author = "莎士比亚",
                category = "释怀",
                tag = "向光而行"
            ),
            EmotionalQuote(
                text = "山前山后各有风景，有风无风皆是自由。不必行色匆匆，也不必光芒万丈。",
                author = "杨绛",
                category = "治愈",
                tag = "从容自洽"
            ),
            EmotionalQuote(
                text = "人间骄阳正好，风过林梢，彼时他们正当年少。",
                author = "木苏里",
                category = "温柔",
                tag = "青春岁月"
            ),
            EmotionalQuote(
                text = "总有一天，你会回头看看那些经历过的苦难，然后笑着说：也不过如此。",
                author = "村上春树",
                category = "励志",
                tag = "轻舟已过"
            ),
            EmotionalQuote(
                text = "家人闲坐，灯火可亲。望向窗外的月亮，心里装的是整个宇宙的温柔。",
                author = "汪曾祺",
                category = "烟火",
                tag = "岁月静好"
            ),
            EmotionalQuote(
                text = "别为打翻的牛奶哭泣，向前看，明天依然会有新的日出和新的晴空。",
                author = "泰戈尔",
                category = "释怀",
                tag = "乐观豁达"
            ),
            EmotionalQuote(
                text = "在光芒万丈之前，我们都要欣然接受眼下的阴霾和漫长的积蓄期。",
                author = "毛姆",
                category = "励志",
                tag = "厚积薄发"
            ),
            EmotionalQuote(
                text = "你笑起来的真诚，能治愈整个秋天的落叶和冬天的风霜。",
                author = "席慕蓉",
                category = "温柔",
                tag = "心之所向"
            ),
            EmotionalQuote(
                text = "日落归山海，山海藏深意。回头看，轻舟已过万重山。",
                author = "李白 · 意译",
                category = "释怀",
                tag = "万重山"
            ),
            EmotionalQuote(
                text = "你走过的每一步路都算数，时光从不负有心人，星光不问赶路人。",
                author = "古谚箴言",
                category = "励志",
                tag = "笃行致远"
            ),
            EmotionalQuote(
                text = "幸福其实很简单：有人爱，有事做，有所期待，有一颗感受当下的平常心。",
                author = "康德",
                category = "治愈",
                tag = "当下即真"
            )
        )
    }

    val categories = listOf("全部", "治愈", "励志", "温柔", "烟火", "释怀")
    var selectedCategory by remember { mutableStateOf("全部") }

    val filteredQuotes = remember(selectedCategory, allQuotes) {
        if (selectedCategory == "全部") allQuotes
        else allQuotes.filter { it.category == selectedCategory }
    }

    var currentQuoteIndex by remember(selectedCategory) { mutableStateOf(0) }
    val currentQuote = filteredQuotes.getOrElse(currentQuoteIndex % filteredQuotes.size) { allQuotes.first() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.88f),
                            Color(0xFFFDF4FF).copy(alpha = 0.82f),
                            Color(0xFFF0FDF4).copy(alpha = 0.78f),
                            Color.White.copy(alpha = 0.90f)
                        )
                    )
                )
                .streamingBorder(
                    cornerRadius = 28.dp,
                    strokeWidth = 1.8.dp,
                    glowWidth = 3.8.dp,
                    baseBorderColor = Color.White.copy(alpha = 0.70f),
                    rainbow = true,
                    showGlow = true
                )
        ) {
            // 背景液态玻璃光斑
            Canvas(modifier = Modifier.matchParentSize()) {
                val cx = size.width * (0.28f + 0.16f * cos(wavePhase))
                val cy = size.height * (0.22f + 0.12f * sin(wavePhase))
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFFA855F7).copy(alpha = 0.16f), Color.Transparent),
                        center = Offset(cx, cy),
                        radius = size.width * 0.45f
                    ),
                    center = Offset(cx, cy),
                    radius = size.width * 0.45f
                )
                val cx2 = size.width * (0.75f + 0.14f * sin(wavePhase))
                val cy2 = size.height * (0.78f + 0.10f * cos(wavePhase))
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFFEC4899).copy(alpha = 0.14f), Color.Transparent),
                        center = Offset(cx2, cy2),
                        radius = size.width * 0.42f
                    ),
                    center = Offset(cx2, cy2),
                    radius = size.width * 0.42f
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // 顶部：图标 + 标题 + 徽标 + 关闭
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("💌", fontSize = 19.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "情感语录",
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFA855F7).copy(alpha = 0.12f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "心灵治愈",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9333EA)
                                )
                            }
                        }
                        Text(
                            text = "懒得找了 · 每日心灵共鸣与温柔治愈",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "关闭",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 分类筛选胶囊
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == selectedCategory
                        Surface(
                            onClick = {
                                selectedCategory = cat
                                currentQuoteIndex = 0
                            },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) Color(0xFF8B5CF6) else Color.White.copy(alpha = 0.65f),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color(0xFF8B5CF6) else Color(0xFFE2E8F0)
                            )
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 核心语录主展示卡（晶莹液态玻璃材质）
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = Color.White.copy(alpha = 0.85f),
                    border = BorderStroke(
                        1.4.dp,
                        Brush.linearGradient(
                            listOf(
                                Color.White,
                                Color(0xFFE9D5FF),
                                Color(0xFFFBCFE8),
                                Color.White
                            )
                        )
                    ),
                    shadowElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "“",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFA855F7).copy(alpha = 0.45f)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF3E8FF))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = currentQuote.tag,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7E22CE)
                                )
                            }
                        }

                        Text(
                            text = currentQuote.text,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "—— ${currentQuote.author}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 底部操作区：换一句、复制语录、分享
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            currentQuoteIndex = (currentQuoteIndex + 1) % filteredQuotes.size
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF3E8FF)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = null,
                            tint = Color(0xFF7E22CE),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "换一句",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7E22CE)
                        )
                    }

                    Button(
                        onClick = {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("情感语录", "${currentQuote.text}\n—— ${currentQuote.author}")
                            cm.setPrimaryClip(clip)
                            Toast.makeText(context, "语录已复制到剪贴板 ✨", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8B5CF6)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "复制文案",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Surface(
                        onClick = {
                            try {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "${currentQuote.text}\n—— ${currentQuote.author}（来自【懒得找了】每日心语）")
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "分享这句温柔语录")
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(shareIntent)
                            } catch (_: Exception) {}
                        },
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.8f),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "分享",
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 每个工具专属的独立 CSS 动态窗口（液态玻璃悬浮窗 + 顶部三色动态灯 + 流动极光背景）
 */
@Composable
private fun IndependentCssDynamicWindow(
    tool: ToolboxTab,
    presentationStyle: RingPresentationStyle,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val isDark = presentationStyle == RingPresentationStyle.CYBER_GLASS
    val infiniteTransition = rememberInfiniteTransition(label = "tool_css_window_${tool.name}")
    val glowPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.283185f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Restart),
        label = "tool_glow_phase"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(30.dp))
                .streamingBorder(
                    cornerRadius = 30.dp,
                    strokeWidth = 2.dp,
                    glowWidth = 4.dp,
                    baseBorderColor = Color.White.copy(alpha = 0.65f),
                    rainbow = true,
                    showGlow = true
                ),
            color = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = if (isDark) listOf(
                                Color(0xFF1E1B4B).copy(alpha = 0.92f),
                                Color(0xFF0F172A).copy(alpha = 0.94f)
                            ) else listOf(
                                Color.White.copy(alpha = 0.72f),
                                Color(0xFFF3E8FF).copy(alpha = 0.65f),
                                Color(0xFFE0E7FF).copy(alpha = 0.60f),
                                Color(0xFFFCE7F3).copy(alpha = 0.62f),
                                Color.White.copy(alpha = 0.72f)
                            )
                        )
                    )
            ) {
                // 独立 CSS 动态窗口全屏液态玻璃流光光晕与镜面反射层
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val gx = size.width * (0.35f + 0.25f * cos(glowPhase))
                    val gy = size.height * (0.22f + 0.14f * sin(glowPhase))
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF8B5CF6).copy(alpha = 0.22f),
                                Color(0xFFEC4899).copy(alpha = 0.12f),
                                Color.Transparent
                            ),
                            center = Offset(gx, gy),
                            radius = size.width * 0.62f
                        ),
                        center = Offset(gx, gy),
                        radius = size.width * 0.62f
                    )
                    val gx2 = size.width * (0.72f + 0.18f * sin(glowPhase * 0.85f))
                    val gy2 = size.height * (0.76f + 0.15f * cos(glowPhase * 0.85f))
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF38BDF8).copy(alpha = 0.18f),
                                Color(0xFFF472B6).copy(alpha = 0.10f),
                                Color.Transparent
                            ),
                            center = Offset(gx2, gy2),
                            radius = size.width * 0.56f
                        ),
                        center = Offset(gx2, gy2),
                        radius = size.width * 0.56f
                    )
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    // 独立 CSS 窗口头部：三色窗口控制点 + 工具图标与标题 + 关闭按钮
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF5F56))
                                    .clickable { onDismiss() }
                            )
                            Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFFFFBD2E)))
                            Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Color(0xFF27C93F)))
                        }

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(Color(0xFF8B5CF6).copy(alpha = 0.14f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tool.icon,
                                contentDescription = null,
                                tint = Color(0xFF8B5CF6),
                                modifier = Modifier.size(19.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tool.title,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.5.sp,
                                color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = tool.desc,
                                fontSize = 10.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = if (isDark) Color(0xFFC4B5FD) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "关闭",
                                tint = if (isDark) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    HorizontalDivider(
                        color = Color(0xFF8B5CF6).copy(alpha = 0.18f)
                    )
                    // 独立窗口主体内容
                    Box(modifier = Modifier.fillMaxSize()) {
                        content()
                    }
                }
            }
        }
    }
}

/**
 * 独立交互面板（底部抽屉：对应 .rm-mask + .rm-sheet，支持按住动态效果触发独立 CSS 窗口）
 */
@Composable
private fun RingMenuBottomSheet(
    visible: Boolean,
    allGroups: List<RingMenuGroup>,
    selectedIndex: Int,
    presentationStyle: RingPresentationStyle,
    pressEffect: RingPressEffect,
    onSelectGroup: (Int) -> Unit,
    onDismiss: () -> Unit,
    onOpenTool: (ToolboxTab) -> Unit,
    onOpenExternalUrl: (String) -> Unit
) {
    val isDark = presentationStyle == RingPresentationStyle.CYBER_GLASS
    val rmText = if (isDark) Color(0xFFECE6F5) else Color(0xFF3D2E56)
    val rmTextSoft = if (isDark) Color(0xFFB3A6C9) else Color(0xFF6A5A85)
    val rmAccent = if (isDark) Color(0xFFB799FF) else Color(0xFF8B5CF6)
    val rmAccentSoft = if (isDark) Color(0xFFB799FF).copy(alpha = 0.15f) else Color(0xFF8B5CF6).copy(alpha = 0.12f)
    val rmSurface = if (isDark) Color(0xFF28203A).copy(alpha = 0.76f) else Color.White.copy(alpha = 0.70f)
    val rmSurfaceBorder = if (isDark) Color(0xFF9678C8).copy(alpha = 0.45f) else Color.White.copy(alpha = 0.92f)
    val rmOptionBg = if (isDark) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.52f)
    val rmBtnBg = if (isDark) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.50f)

    val currentGroup = allGroups.getOrElse(selectedIndex) { allGroups.firstOrNull() ?: RING_MENU_GROUPS.first() }

    // 遮罩 (.rm-mask)
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(240)),
        exit = fadeOut(tween(220))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF281446).copy(alpha = 0.38f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
        )
    }

    // 底部抽屉面板 (.rm-sheet)
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(dampingRatio = 0.78f, stiffness = 380f)
        ) + fadeIn(tween(220)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(240)
        ) + fadeOut(tween(200)),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = rmSurface,
                border = BorderStroke(1.5.dp, rmSurfaceBorder),
                shadowElevation = 24.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    // 抓手条 (.rm-grab)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 42.dp, height = 4.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(rmAccentSoft)
                        )
                    }

                    // 头部 (.rm-sheet-head)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 14.dp, end = 14.dp, top = 6.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(rmAccentSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = currentGroup.icon,
                                contentDescription = null,
                                tint = rmAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentGroup.sheetTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = rmText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentGroup.sheetSub,
                                fontSize = 12.sp,
                                color = rmTextSoft,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // 关闭按钮 (.rm-sheet-close)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(rmAccentSoft)
                                .clickable { onDismiss() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "关闭面板",
                                tint = rmTextSoft,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = rmAccentSoft, thickness = 1.dp)

                    // 功能切换标签 (.rm-tabs：全部功能按顺序排列)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allGroups.forEachIndexed { idx, group ->
                            val isSelected = idx == selectedIndex
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(if (isSelected) rmAccent else Color.Transparent)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) Color.Transparent else rmSurfaceBorder,
                                        shape = RoundedCornerShape(999.dp)
                                    )
                                    .clickable { onSelectGroup(idx) }
                                    .padding(horizontal = 13.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = group.entryLabel,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else rmTextSoft
                                )
                            }
                        }
                    }

                    // 面板主体 (.rm-sheet-body + .rm-option，带按住触发动态效果)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        currentGroup.options.forEachIndexed { optIdx, option ->
                            val optInteraction = remember { MutableInteractionSource() }
                            val isOptPressed by optInteraction.collectIsPressedAsState()
                            val optKey = option.tab?.name ?: "ext_$optIdx"
                            val optScale by animateFloatAsState(
                                targetValue = when {
                                    !isOptPressed -> 1f
                                    pressEffect == RingPressEffect.JELLY_BOUNCE -> 1.05f
                                    pressEffect == RingPressEffect.LIQUID_RIPPLE -> 0.97f
                                    else -> 1.03f
                                },
                                animationSpec = spring(dampingRatio = 0.5f, stiffness = 380f),
                                label = "opt_press_$optKey"
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .scale(optScale)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isOptPressed) rmAccentSoft else rmOptionBg)
                                    .border(
                                        width = if (isOptPressed) 1.5.dp else 1.dp,
                                        color = if (isOptPressed) rmAccent else rmSurfaceBorder,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable(
                                        interactionSource = optInteraction,
                                        indication = null
                                    ) {
                                        when {
                                            option.tab != null -> onOpenTool(option.tab)
                                            !option.externalUrl.isNullOrBlank() -> onOpenExternalUrl(option.externalUrl)
                                        }
                                    }
                                    .padding(13.dp)
                                    .testTag("rm_option_$optKey"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(rmAccentSoft),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = option.tab?.icon ?: currentGroup.icon,
                                        contentDescription = null,
                                        tint = rmAccent,
                                        modifier = Modifier.size(19.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = option.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = rmText
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${option.desc} · 点击打开独立CSS动态窗口",
                                        fontSize = 11.5.sp,
                                        color = rmTextSoft
                                    )
                                }

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = rmTextSoft.copy(alpha = 0.55f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = rmAccentSoft, thickness = 1.dp)

                    // 底部操作区 (.rm-sheet-foot → 返回圆环)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Surface(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(999.dp),
                            color = rmBtnBg,
                            border = BorderStroke(1.dp, rmSurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = rmText,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "返回圆环",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = rmText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ==================== 组件 ==================== */

/** v1.0.19：静态分类标题栏（取消展开/收起标签，直接平铺呈现） */
@Composable
private fun ToolCategoryHeader(
    category: ToolCategory,
    count: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp), // v1.1.7 胶囊化
        color = Color.White.copy(alpha = 0.55f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 11.dp)
        ) {
            Text(text = category.icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.displayName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$count 个工具",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 6.dp)
            )
        }
    }
}

/** 云端工具两列网格（点击打开 URL） */
@Composable
private fun CloudToolGrid(
    cloudTools: List<com.example.data.remote.ToolDto>,
    context: Context
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 8.dp)
    ) {
        cloudTools.chunked(2).forEach { rowTools ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowTools.forEach { tool ->
                    CloudToolCell(tool = tool, context = context, modifier = Modifier.weight(1f))
                }
                if (rowTools.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

/** 云端工具小卡片（控制台实时同步，点击打开 URL） */
@Composable
private fun CloudToolCell(
    tool: com.example.data.remote.ToolDto,
    context: Context,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = {
            if (tool.url.isNotBlank()) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tool.url))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "无法打开：${tool.url}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "该云端工具未配置跳转链接", Toast.LENGTH_SHORT).show()
            }
        },
        shape = RoundedCornerShape(24.dp), // v1.1.7 胶囊化
        color = Color.White.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp)
        ) {
            Text(
                text = tool.icon.ifBlank { "?" },
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tool.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (tool.desc.isNotBlank()) {
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = tool.desc,
                        fontSize = 9.5.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ==========================================
// 各工具专属界面（保留工具）
// ==========================================

@Composable
private fun MouthpieceScreenView() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            MouthpieceSection()
        }
    }
}

@Composable
private fun AgeCalculatorScreenView() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            AgeCalculatorSection()
        }
    }
}

@Composable
private fun OfflineTreasureScreenView() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OfflineTreasureSection()
        }
    }
}