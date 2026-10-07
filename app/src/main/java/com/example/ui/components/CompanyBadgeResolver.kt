package com.example.ui.components

import com.example.data.model.NavCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * v1.0.15 角标自动识别技术：
 * 根据站点 URL 域名自动识别所属公司/品牌，生成公司角标（如 openai.com → OpenAI、deepseek.com → 深度求索）。
 * 识别规则：
 *  1. 优先使用云端/手动配置的 badge（card.badge）——不覆盖人工设定
 *  2. 其次按域名匹配知名公司映射表
 *  3. 再按标题关键字匹配（如标题含"OpenAI"）
 *  4. 都没有则回退站点类型角标（AI/工具/影音/游戏等）
 * 每个公司有固定品牌色，角标呈小圆角胶囊样式，贴在卡片右上角（叠加在原有角标下方）。
 */

/** 域名 → 公司/品牌 映射表（含品牌色） */
data class CompanyBadge(
    val name: String,
    val color: Color
)

private val COMPANY_DOMAINS: Map<String, CompanyBadge> = buildMap {
    // 国际 AI 大厂
    put("openai.com", CompanyBadge("OpenAI", Color(0xFF10A37F)))
    put("platform.openai.com", CompanyBadge("OpenAI", Color(0xFF10A37F)))
    put("aistudio.google.com", CompanyBadge("Google", Color(0xFF4285F4)))
    put("anthropic.com", CompanyBadge("Anthropic", Color(0xFFD97757)))
    put("claude.ai", CompanyBadge("Anthropic", Color(0xFFD97757)))
    put("huggingface.co", CompanyBadge("Hugging Face", Color(0xFFFFD21F)))
    put("openrouter.ai", CompanyBadge("OpenRouter", Color(0xFF6466E9)))
    put("groq.com", CompanyBadge("Groq", Color(0xFFF55036)))
    put("together.ai", CompanyBadge("Together AI", Color(0xFFE11D48)))
    put("replicate.com", CompanyBadge("Replicate", Color(0xFF1B48DA)))
    put("perplexity.ai", CompanyBadge("Perplexity", Color(0xFF20B8CD)))
    put("azure.microsoft.com", CompanyBadge("Microsoft", Color(0xFF0078D4)))
    put("aws.amazon.com", CompanyBadge("AWS", Color(0xFFFF9900)))
    put("cloud.google.com", CompanyBadge("Google Cloud", Color(0xFF4285F4)))
    // 国内 AI 大厂
    put("open.bigmodel.cn", CompanyBadge("智谱AI", Color(0xFF3859FF)))
    put("bigmodel.cn", CompanyBadge("智谱AI", Color(0xFF3859FF)))
    put("platform.moonshot.cn", CompanyBadge("月之暗面", Color(0xFF7C3AED)))
    put("moonshot.cn", CompanyBadge("月之暗面", Color(0xFF7C3AED)))
    put("platform.deepseek.com", CompanyBadge("深度求索", Color(0xFF4D6BFE)))
    put("deepseek.com", CompanyBadge("深度求索", Color(0xFF4D6BFE)))
    put("platform.minimaxi.com", CompanyBadge("MiniMax", Color(0xFF9B30FF)))
    put("minimaxi.com", CompanyBadge("MiniMax", Color(0xFF9B30FF)))
    put("bailian.aliyun.com", CompanyBadge("阿里云", Color(0xFFFF6A00)))
    put("aliyun.com", CompanyBadge("阿里云", Color(0xFFFF6A00)))
    put("cloud.baidu.com", CompanyBadge("百度", Color(0xFF2932E1)))
    put("baidu.com", CompanyBadge("百度", Color(0xFF2932E1)))
    put("cloud.tencent.com", CompanyBadge("腾讯云", Color(0xFF006EFF)))
    put("tencent.com", CompanyBadge("腾讯", Color(0xFF006EFF)))
    put("volcengine.com", CompanyBadge("火山引擎", Color(0xFF0052D9)))
    put("xinghuo.xfyun.cn", CompanyBadge("讯飞星火", Color(0xFF00A6F0)))
    put("xfyun.cn", CompanyBadge("讯飞", Color(0xFF00A6F0)))
    put("huaweicloud.com", CompanyBadge("华为云", Color(0xFFCF0A2C)))
    put("baichuan-ai.com", CompanyBadge("百川智能", Color(0xFF1E6FFF)))
    put("lingyiwanwu.com", CompanyBadge("零一万物", Color(0xFF121212)))
    put("stepfun.com", CompanyBadge("阶跃星辰", Color(0xFF4318D1)))
    put("tiangong.cn", CompanyBadge("昆仑万维", Color(0xFF0AA1DD)))
    put("jimeng.jianying.com", CompanyBadge("字节跳动", Color(0xFF00A3FF)))
    put("jianying.com", CompanyBadge("字节跳动", Color(0xFF00A3FF)))
    put("api2d.com", CompanyBadge("API2D", Color(0xFF6C63FF)))
    put("gpt-api.com", CompanyBadge("GPT-API", Color(0xFF10A37F)))
    put("closeai-asia.com", CompanyBadge("CloseAI", Color(0xFF7C3AED)))
    // 开源 / 社区
    put("github.com", CompanyBadge("GitHub", Color(0xFF181717)))
    put("gitcode.com", CompanyBadge("GitCode", Color(0xFF0A66C2)))
    put("gitee.com", CompanyBadge("Gitee", Color(0xFFC71D23)))
    // 娱乐 / 影音
    put("bilibili.com", CompanyBadge("哔哩哔哩", Color(0xFFFB7299)))
    put("youku.com", CompanyBadge("优酷", Color(0xFF0076FF)))
    put("iqiyi.com", CompanyBadge("爱奇艺", Color(0xFF00BE06)))
    put("douyin.com", CompanyBadge("抖音", Color(0xFF111111)))
    put("kuaishou.com", CompanyBadge("快手", Color(0xFFFF4906)))
    put("hongguo.tv", CompanyBadge("红果短剧", Color(0xFFFF2E4D)))
    put("fanqienovel.com", CompanyBadge("番茄小说", Color(0xFFFDBB0F)))
    put("hemashort.com", CompanyBadge("河马剧场", Color(0xFFFF7A00)))
    put("netshort.com", CompanyBadge("NetShort", Color(0xFF8B5CF6)))
    put("xiaohongshu.com", CompanyBadge("小红书", Color(0xFFFF2442)))
    put("weibo.com", CompanyBadge("微博", Color(0xFFE6162D)))
    // 效率 / 工具
    put("geekuninstaller.com", CompanyBadge("Geek", Color(0xFF0F766E)))
    put("potplayer.daum.net", CompanyBadge("Daum", Color(0xFFE11D48)))
    put("voidtools.com", CompanyBadge("Voidtools", Color(0xFF0EA5E9)))
    put("xmind.net", CompanyBadge("Xmind", Color(0xFFF26D00)))
    put("office.com", CompanyBadge("Microsoft", Color(0xFF0078D4)))
    put("google.com", CompanyBadge("Google", Color(0xFF4285F4)))
}

/** 标题关键字 → 公司/品牌 */
private val COMPANY_TITLES: List<Pair<Regex, CompanyBadge>> = listOf(
    Regex("openai", RegexOption.IGNORE_CASE) to CompanyBadge("OpenAI", Color(0xFF10A37F)),
    Regex("claude", RegexOption.IGNORE_CASE) to CompanyBadge("Anthropic", Color(0xFFD97757)),
    Regex("deepseek|深度求索", RegexOption.IGNORE_CASE) to CompanyBadge("深度求索", Color(0xFF4D6BFE)),
    Regex("kimi|moonshot|月之暗面", RegexOption.IGNORE_CASE) to CompanyBadge("月之暗面", Color(0xFF7C3AED)),
    Regex("glm|智谱", RegexOption.IGNORE_CASE) to CompanyBadge("智谱AI", Color(0xFF3859FF)),
    Regex("文心|千帆|百度", RegexOption.IGNORE_CASE) to CompanyBadge("百度", Color(0xFF2932E1)),
    Regex("通义|百炼|阿里", RegexOption.IGNORE_CASE) to CompanyBadge("阿里云", Color(0xFFFF6A00)),
    Regex("混元|腾讯", RegexOption.IGNORE_CASE) to CompanyBadge("腾讯", Color(0xFF006EFF)),
    Regex("豆包|火山|字节", RegexOption.IGNORE_CASE) to CompanyBadge("字节跳动", Color(0xFF00A3FF)),
    Regex("星火|讯飞", RegexOption.IGNORE_CASE) to CompanyBadge("讯飞", Color(0xFF00A6F0)),
    Regex("盘古|华为", RegexOption.IGNORE_CASE) to CompanyBadge("华为", Color(0xFFCF0A2C)),
    Regex("minimax", RegexOption.IGNORE_CASE) to CompanyBadge("MiniMax", Color(0xFF9B30FF)),
    Regex("百川", RegexOption.IGNORE_CASE) to CompanyBadge("百川智能", Color(0xFF1E6FFF)),
    Regex("阶跃|stepfun", RegexOption.IGNORE_CASE) to CompanyBadge("阶跃星辰", Color(0xFF4318D1)),
    Regex("天工|昆仑", RegexOption.IGNORE_CASE) to CompanyBadge("昆仑万维", Color(0xFF0AA1DD)),
    Regex("即梦|剪映|字节", RegexOption.IGNORE_CASE) to CompanyBadge("字节跳动", Color(0xFF00A3FF)),
    Regex("openrouter", RegexOption.IGNORE_CASE) to CompanyBadge("OpenRouter", Color(0xFF6466E9)),
    Regex("huggingface|hugging face", RegexOption.IGNORE_CASE) to CompanyBadge("Hugging Face", Color(0xFFFFD21F)),
    Regex("哔哩哔哩|bilibili", RegexOption.IGNORE_CASE) to CompanyBadge("哔哩哔哩", Color(0xFFFB7299)),
    Regex("爱奇艺|iqiyi", RegexOption.IGNORE_CASE) to CompanyBadge("爱奇艺", Color(0xFF00BE06)),
    Regex("优酷|youku", RegexOption.IGNORE_CASE) to CompanyBadge("优酷", Color(0xFF0076FF)),
    Regex("抖音|douyin", RegexOption.IGNORE_CASE) to CompanyBadge("抖音", Color(0xFF111111)),
    Regex("快手|kuaishou", RegexOption.IGNORE_CASE) to CompanyBadge("快手", Color(0xFFFF4906)),
    Regex("小红书|xhs", RegexOption.IGNORE_CASE) to CompanyBadge("小红书", Color(0xFFFF2442)),
    Regex("微博|weibo", RegexOption.IGNORE_CASE) to CompanyBadge("微博", Color(0xFFE6162D)),
    Regex("github", RegexOption.IGNORE_CASE) to CompanyBadge("GitHub", Color(0xFF181717)),
    Regex("gitee|码云", RegexOption.IGNORE_CASE) to CompanyBadge("Gitee", Color(0xFFC71D23))
)

/**
 * 自动识别站点所属公司/品牌。
 * @return null 表示未识别到公司（此时可回退类型角标）
 */
fun detectCompanyBadge(card: NavCard): CompanyBadge? {
    // 1. 域名匹配（最准确）
    val domain = try {
        val u = if (card.url.startsWith("http")) card.url else "https://$card.url"
        java.net.URI(u).host?.lowercase() ?: ""
    } catch (e: Exception) { "" }
    COMPANY_DOMAINS.forEach { (key, badge) ->
        if (domain.contains(key) || domain.endsWith("." + key)) {
            return badge
        }
    }
    // 2. 标题关键字匹配
    COMPANY_TITLES.forEach { (regex, badge) ->
        if (regex.containsMatchIn(card.title)) {
            return badge
        }
    }
    return null
}

/**
 * 角标自动识别组件：在卡片右上角渲染公司角标（小圆角胶囊 + 品牌色）。
 * 叠加在原有 badge 之下（原有 badge 仍是 NEW/HOT 等角标，公司角标展示归属公司）。
 */
@Composable
fun CompanyBadgeChip(
    card: NavCard,
    modifier: Modifier = Modifier
) {
    val company = detectCompanyBadge(card) ?: return
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(company.color.copy(alpha = 0.15f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
            // 品牌色圆点
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(company.color)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = company.name,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black,
                color = company.color,
                maxLines = 1
            )
        }
    }
}
