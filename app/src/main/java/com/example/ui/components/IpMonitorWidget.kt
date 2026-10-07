package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.MarqueeDto
import java.util.Calendar
import java.util.TimeZone
import kotlinx.coroutines.delay

/**
 * 24小时不断轮播跑马灯公告内容（默认官宣文案）
 */
const val MARQUEE_ANNOUNCEMENT_TEXT = "本软件集成了上百款站点和应用，有些内容都是可以白嫖的哟～具体内容请自行发掘体验。后续我们会陆续的更新新内容的，请尽情期待吧～  懒得找了 官宣"

/** 北京时间（Asia/Shanghai）当前日历，用于 24 小时轮播与公告判断 */
fun beijingCalendar(now: Calendar = Calendar.getInstance()): Calendar {
    return Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).apply {
        timeInMillis = now.timeInMillis
    }
}

/**
 * 根据当前北京时间小时（0-23点）动态生成对应时间段播报文案。
 * 24 小时逐小时轮播不同内容，每天同一时段文案固定不重复。
 */
fun getHourlyMarqueeText(calendar: Calendar = beijingCalendar()): String {
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val hh = "%02d".format(hour)
    val mm = "%02d".format(minute)
    val prefix = "本软件集成了上百款站点和应用，有些内容都是可以白嫖的哟～具体内容请自行发掘体验。"
    val tail = "后续我们会陆续的更新新内容的，请尽情期待吧～  懒得找了 官宣"
    // 24 个时段逐小时文案（6-7点 / 7-8点…每天对应时段固定，不重复）
    val hourText = when (hour) {
        0 -> "现在是$hh:$mm，深夜了，早点休息养足精神，明天继续探索～"
        1 -> "现在是$hh:$mm，凌晨1点啦，放下手机睡个好觉吧～"
        2 -> "现在是$hh:$mm，凌晨2点，熬夜伤身，快去休息～"
        3 -> "现在是$hh:$mm，凌晨3点，万籁俱寂，愿你好梦～"
        4 -> "现在是$hh:$mm，凌晨4点，夜将尽黎明将至，养精蓄锐～"
        5 -> "现在是$hh:$mm，清晨5点，早起的人运气不会差，去发现宝藏吧！"
        6 -> "现在是早上$hh:$mm，记得出门带伞。吃早餐哟，早餐是最重要的一餐。"
        7 -> "现在是早上$hh:$mm，是不是要准备去上班了，路上注意安全哟，不要看手机，注意车辆。"
        8 -> "现在是早上$hh:$mm，一日之计在于晨，工作学习加油，记得喝水～"
        9 -> "现在是上午$hh:$mm，专注工作学习的同时，记得起身活动一下哦～"
        10 -> "现在是上午$hh:$mm，来杯水提提神，高效时刻来啦～"
        11 -> "现在是上午$hh:$mm，再坚持一会儿就午休啦，加油～"
        12 -> "现在是中午$hh:$mm，记得按时吃饭，饭后小憩一会儿，下午继续～"
        13 -> "现在是下午$hh:$mm，午休好了吗？精神满满再出发～"
        14 -> "现在是下午$hh:$mm，犯困的话起来走走，伸个懒腰吧～"
        15 -> "现在是下午$hh:$mm，下午茶时间，放松一下继续探索资源吧～"
        16 -> "现在是下午$hh:$mm，离下班越来越近啦，稳住～"
        17 -> "现在是傍晚$hh:$mm，忙碌了一天辛苦了，给自己一点放松时间吧～"
        18 -> "现在是傍晚$hh:$mm，下班路上注意安全，回家好好休息～"
        19 -> "现在是晚上$hh:$mm，晚饭吃了吗？记得按时吃饭哦～"
        20 -> "现在是晚上$hh:$mm，休闲时光，看看喜欢的资源放松一下吧～"
        21 -> "现在是晚上$hh:$mm，早点洗漱，准备进入梦乡吧～"
        22 -> "现在是晚上$hh:$mm，夜深了，放下手机，好好休息～"
        23 -> "现在是晚上$hh:$mm，祝你好梦，明天又是元气满满的一天～"
        else -> "现在是$hh:$mm，祝你开心每一天～"
    }
    return prefix + hourText + tail
}
@Composable
fun IpMonitorWidget(
    modifier: Modifier = Modifier,
    cloudMarquee: com.example.data.remote.MarqueeDto? = null
) {
    MarqueeNoticeWidget(modifier = modifier, cloudMarquee = cloudMarquee)
}

/**
 * 跑马灯公告栏主组件
 * - 云端配置 enabled=false 时完全不渲染（后台关闭后本体不再呈现公告）
 * - 支持云端 icon / segments（24小时逐小时轮播）/ defaultText
 */
@Composable
fun MarqueeNoticeWidget(
    modifier: Modifier = Modifier,
    text: String = MARQUEE_ANNOUNCEMENT_TEXT,
    cloudMarquee: MarqueeDto? = null
) {
    // 后台关闭跑马灯：本体完全不渲染公告内容（严格修复开关无效问题）
    if (cloudMarquee != null && cloudMarquee.enabled == false) return

    // 播放队列（v1.2.4：严格遵循控制台设置的内容——仅 defaultText + 时段 segments，不再混入自动生成的逐小时文案）
    val defaultText = cloudMarquee?.defaultText?.ifBlank {
        MARQUEE_ANNOUNCEMENT_TEXT
    } ?: MARQUEE_ANNOUNCEMENT_TEXT
    val sortedSegments = (cloudMarquee?.segments.orEmpty())
        .filter { it.text.isNotBlank() }
        .sortedBy { it.start }
        .map { it.text }
    val playlist = remember(defaultText, sortedSegments) {
        buildList {
            add(defaultText)
            addAll(sortedSegments)
        }.distinct()
    }

    var displayText by remember { mutableStateOf(playlist.firstOrNull() ?: defaultText) }
    var rotateIndex by remember { mutableIntStateOf(0) }
    // v1.2.4：文字完整走完一圈再切换下一条（不再固定 10 秒硬切导致“文字没走完就切”）
    // 一圈距离 = 文字宽度 + 容器宽度；时长 = 距离 / 滚动速度(35dp/s)
    var containerWidthPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.bodySmall.copy(
        fontSize = 11.5.sp,
        fontWeight = FontWeight.Medium
    )
    LaunchedEffect(cloudMarquee, playlist, displayText, containerWidthPx) {
        val measured = textMeasurer.measure(
            AnnotatedString(displayText),
            style = textStyle
        )
        val textWidthPx = measured.size.width.coerceAtLeast(1)
        val velocityPx = with(density) { 35.dp.toPx() }.coerceAtLeast(1f)
        val distancePx = (textWidthPx + containerWidthPx).coerceAtLeast(1)
        val cycleMs = ((distancePx / velocityPx) * 1000L).toLong()
        // v1.2.6：优化跑马灯停顿——需要滚动的长文字走完一圈后仅留 150ms 缓冲立即切下一条，滚动更连贯；
        // 文字短于容器宽度（无需滚动）则静态展示 2.5 秒再切换，避免长时间空白停顿。
        val needScroll = textWidthPx > containerWidthPx
        val holdMs = if (needScroll) cycleMs + 150L else 2500L
        delay(holdMs)
        rotateIndex = (rotateIndex + 1) % playlist.size
        displayText = playlist[rotateIndex]
    }
    val effectiveText = displayText
    // 云端公告图标（默认小喇叭）
    val marqueeIcon = cloudMarquee?.icon?.ifBlank { null } ?: null

    val infiniteTransition = rememberInfiniteTransition(label = "marquee_horn_pulse")
    val hornScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hornScale"
    )

    val primaryColor = MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = primaryColor.copy(alpha = 0.22f),
                shape = RoundedCornerShape(10.dp)
            )
            .testTag("ip_monitor_widget"),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 5.dp)
                .fillMaxWidth()
        ) {
            // 广播喇叭徽标（云端可自定义图标）
            Box(
                modifier = Modifier
                    .scale(hornScale)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                primaryColor,
                                primaryColor.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .padding(horizontal = 5.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                if (marqueeIcon != null && marqueeIcon != "📢") {
                    Text(
                        text = marqueeIcon,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Campaign,
                            contentDescription = "公告播报",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "公告",
                            color = Color.White,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 跑马灯文字（v1.2.4：测量容器宽度，按滚动一圈时长切换下一条，文字完整走完再切）
            Text(
                text = effectiveText,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .onSizeChanged { containerWidthPx = it.width }
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        velocity = 35.dp
                    )
                    .testTag("marquee_announcement_text")
            )

            Spacer(modifier = Modifier.width(6.dp))

            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}

/**
 * 解析跑马灯文字：优先命中云端配置的时间段（按北京时间），否则使用云端默认文字，再回退到按小时动态文案。
 */
fun resolveMarqueeText(cloudMarquee: MarqueeDto?): String {
    if (cloudMarquee?.enabled != true) return getHourlyMarqueeText()
    val now = beijingCalendar()
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val segments = cloudMarquee.segments.orEmpty()
    segments.firstOrNull { seg ->
        val s = seg.start % 24
        val e = seg.end % 24
        if (s <= e) hour >= s && hour < e
        else hour >= s || hour < e
    }?.let { seg ->
        if (seg.text.isNotBlank()) return seg.text
    }
    return cloudMarquee.defaultText.ifBlank { getHourlyMarqueeText(now) }
}

/**
 * 兼容性存根，保证任何旧类型引用安全
 */
data class IpLocationInfo(
    val ip: String = "127.0.0.1",
    val location: String = "中国",
    val xhsLocation: String = "IP属地：中国",
    val latencyMs: Long = 20L
)

object IpRepository {
    val cachedInfo: IpLocationInfo? = IpLocationInfo()
}
