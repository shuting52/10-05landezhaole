package com.example.data.model

data class AiChampion(
    val id: String,
    val title: String,
    val championRankTitle: String,
    val modelName: String,
    val vendor: String,
    val coreMetrics: String,
    val badgeTag: String,
    val description: String,
    val whyNumberOne: String,
    val highlights: List<String>,
    val officialUrl: String,
    val accentColor: Long
)

data class AiAssistant(
    val rank: Int,
    val name: String,
    val vendor: String,
    val rating: String,
    val heatScore: String,
    val category: String, // 综合全能, 逻辑代码, 中文写作, 生活日常, 学术检索
    val tags: List<String>,
    val targetAudience: String,
    val killerFeature: String,
    val desc: String,
    val officialUrl: String,
    val pros: List<String>
)

data class AiDynamic(
    val id: String,
    val title: String,
    val timeAgo: String,
    val company: String,
    val category: String, // 大模型突破, 个人助理, 开源生态, 代码推理, 多模态
    val hotIndex: String,
    val tags: List<String>,
    val summary: String,
    val detailedBreakdown: String,
    val impact: String,
    val webUrl: String
)

object AiFrontierData {

    // 🏆 霸榜第一 · 巅峰王座
    val champions = listOf(
        AiChampion(
            id = "c1",
            title = "全网热度霸榜第 1 名",
            championRankTitle = "👑 现象级破圈 · 全网最火国产推理神作",
            modelName = "DeepSeek-R1 / V3",
            vendor = "深度求索 (DeepSeek)",
            coreMetrics = "全球 GitHub 星标破历史纪录 · App Store 登顶第一 · AIME 数学 96.3%",
            badgeTag = "全球现象级第一",
            description = "完全开源开放权重的全球顶尖推理大模型，以万分之一训练成本对齐 OpenAI o1 顶尖水准，彻底打破闭源垄断，引发全球科技巨头跟进与震撼！",
            whyNumberOne = "1. 开源且满血免费体验；2. 显式输出完整思维链；3. 极速推理与数学代码能力登顶全球公开模型之巅；4. 官方网页与手机端开箱即用，人人皆可满血使用。",
            highlights = listOf(
                "全开源权重与详细技术论文，打破高算力神话",
                "多项权威数学与竞赛编程基准追平 o1",
                "极速自研推理框架，极低使用门槛"
            ),
            officialUrl = "https://chat.deepseek.com",
            accentColor = 0xFF4F46E5
        ),
        AiChampion(
            id = "c2",
            title = "全球盲测综合霸榜第 1 名",
            championRankTitle = "🧠 高智商王者 · 开发者与文字创作者最爱",
            modelName = "Claude 3.5 Sonnet / 3.7",
            vendor = "Anthropic (全球顶尖安全实验室)",
            coreMetrics = "LMSYS Chatbot Arena 盲测高智商连续霸榜 · SWE-bench 代码修复率稳居前列",
            badgeTag = "综合智商第一",
            description = "公认全球代码能力与文本逻辑最优雅的大模型。逻辑严密，极少出现低级逻辑幻觉，写出的代码直接可跑，行文风格自然高级。",
            whyNumberOne = "1. 业界公认逻辑推理与复杂系统重构天花板；2. 极佳的长篇论述自洽性，拒绝废话；3. Artifacts 交互式实时预览功能开创行业先河。",
            highlights = listOf(
                "程序员公认的最佳编程重构与排错搭子",
                "长文本连贯性极强，学术文献提炼极准",
                "原生支持交互式代码与页面即时渲染"
            ),
            officialUrl = "https://claude.ai",
            accentColor = 0xFFD97706
        ),
        AiChampion(
            id = "c3",
            title = "国内普及度霸榜第 1 名",
            championRankTitle = "🗣️ 全民级助理 · 最逼真自然拟人语音",
            modelName = "豆包 (Doubao)",
            vendor = "字节跳动 (火山引擎大模型)",
            coreMetrics = "国内移动端日活与使用普及率第一 · 综合月活过亿",
            badgeTag = "国内用户量第一",
            description = "字节跳动倾力打造的日常个人AI生活助理，支持超逼真的全双工拟人化语音通话，无论老人、学生还是上班族都能零门槛上手！",
            whyNumberOne = "1. 语音声线极度逼真，如同真人闺蜜/导师在耳边倾听；2. 对话零延迟，支持实时打断插话；3. 广泛融入社交与生活百科场景。",
            highlights = listOf(
                "极度逼真的情感化拟人语音对话",
                "全免费开放，移动端体验极其丝滑",
                "全能生活百科、英语口语陪练与灵感创作"
            ),
            officialUrl = "https://www.doubao.com",
            accentColor = 0xFF059669
        ),
        AiChampion(
            id = "c4",
            title = "中文长文本霸榜第 1 名",
            championRankTitle = "📚 长篇文献解析之王 · 百万字极速拆解",
            modelName = "Kimi 智能助手",
            vendor = "月之暗面 (Moonshot AI)",
            coreMetrics = "中文超长 200 万字无损上下文第一 · 长文本问答准确率 99.4%",
            badgeTag = "长文本阅读第一",
            description = "开创中文大模型超长上下文无损检索风潮的国民级阅读神作。一口气解析多本几十万字专业书籍、长篇法律合同或数十份行业研报，提炼毫秒级完成。",
            whyNumberOne = "1. 无损 200 万字上下文深度记忆；2. 针对中文语境的学术检索与财报分析深度优化；3. 探索版 k0-math 推理模型实力强悍。",
            highlights = listOf(
                "一次性上传几十份 PDF 研报交叉对比",
                "精准定位长篇合同漏洞与关键条款",
                "提供详尽溯源证据链与文献引用角标"
            ),
            officialUrl = "https://kimi.moonshot.cn",
            accentColor = 0xFF2563EB
        ),
        AiChampion(
            id = "c5",
            title = "全球生态霸榜第 1 名",
            championRankTitle = "🌐 综合通识鼻祖 · 最广阔知识版图",
            modelName = "ChatGPT (GPT-4o / o1)",
            vendor = "OpenAI (全球 AI 领军领袖)",
            coreMetrics = "全球周活跃用户超 2.5 亿 · GPTs 专属应用超 300 万",
            badgeTag = "全球月活生态第一",
            description = "引领现代生成式大模型革命的标杆。具备全方位的通识知识网络、GPTs 定制生态系统以及前沿的慢思考推理 o1/o3-mini 架构。",
            whyNumberOne = "1. 最庞大的开发者与终端用户生态；2. 强大的高级多模态语音与视觉理解能力；3. 持续领先的多轮长程通用规划。",
            highlights = listOf(
                "海量专业 GPTs 插件市场与定制工作流",
                "高级多模态实时语音与摄像头视觉互动",
                "通用性极广，跨各行各业皆有成熟方案"
            ),
            officialUrl = "https://chatgpt.com",
            accentColor = 0xFF10B981
        )
    )

    // 🤖 最受欢迎 AI 个人助理榜单 (Top 10)
    val assistants = listOf(
        AiAssistant(
            rank = 1,
            name = "DeepSeek 深度求索",
            vendor = "深度求索 (DeepSeek)",
            rating = "9.9",
            heatScore = "99.8w",
            category = "综合全能",
            tags = listOf("全网霸榜", "完全免费", "满血R1推理", "开源神作"),
            targetAudience = "全行业人群、学生、程序员、科研人员、解题刚需",
            killerFeature = "显式展示深度思考过程，数学竞赛题与难题秒出正确解",
            desc = "当前最炙手可热的纯国产大模型，凭借突破性算法打破欧美垄断，推理与代码表现令人惊艳，且网页与手机端均免费开放满血版。",
            officialUrl = "https://chat.deepseek.com",
            pros = listOf("思考过程完全透明可查", "数学、算法与代码能力极强", "零付费套路，纯粹极简体验")
        ),
        AiAssistant(
            rank = 2,
            name = "ChatGPT",
            vendor = "OpenAI",
            rating = "9.8",
            heatScore = "98.9w",
            category = "综合全能",
            tags = listOf("全球标杆", "GPT-4o", "高级语音", "多模态视觉"),
            targetAudience = "全球职场白领、日常答疑、跨国交流、多模态应用者",
            killerFeature = "超高情商多模态音视频对话，各领域知识储备极其浩瀚",
            desc = "生成式 AI 的鼻祖与全球标杆，日常问答反应敏捷，支持声音极其动听的高级语音交互，数百万款个性化 GPTs 插件满足万千场景。",
            officialUrl = "https://chatgpt.com",
            pros = listOf("知识面极其宽广全面", "多模态支持图片、文件、实时语音", "海量自定义 GPTs 助手")
        ),
        AiAssistant(
            rank = 3,
            name = "Claude",
            vendor = "Anthropic",
            rating = "9.8",
            heatScore = "98.4w",
            category = "逻辑代码",
            tags = listOf("代码天花板", "高智商推理", "Artifacts", "文本极细腻"),
            targetAudience = "软件工程师、架构师、深度内容创作者、学者",
            killerFeature = "编写生产级代码极少出 Bug，逻辑链条环环相扣，长文润色极富文采",
            desc = "开发者心中的白月光。代码生成、重构和排错能力卓越，支持右侧直接预览网页和交互式组件，是提升专业工作流生产力的绝对王牌。",
            officialUrl = "https://claude.ai",
            pros = listOf("复杂代码架构理解天花板", "行文毫无 AI 味，逻辑极强", "右侧 Artifacts 即时预览组件")
        ),
        AiAssistant(
            rank = 4,
            name = "Kimi 智能助手",
            vendor = "月之暗面 (Moonshot)",
            rating = "9.7",
            heatScore = "97.5w",
            category = "中文写作",
            tags = listOf("200万字长文", "研报利器", "学术溯源", "搜索深度思考"),
            targetAudience = "金融分析师、研究生、律师、自媒体创作者、重度文献读者",
            killerFeature = "一次性吃透 50 篇长研报，毫秒级跨文档事实比对与图表信息提炼",
            desc = "中文超长文档理解与智能互联网检索之王。支持上传超大书籍、财报、合同，自动生成大纲梳理与答疑，带原文引用标注，极度可靠。",
            officialUrl = "https://kimi.moonshot.cn",
            pros = listOf("超长上下文不丢失细节", "深度学术溯源有据可依", "k0-math 逻辑搜索推理模式")
        ),
        AiAssistant(
            rank = 5,
            name = "豆包",
            vendor = "字节跳动",
            rating = "9.6",
            heatScore = "96.8w",
            category = "生活日常",
            tags = listOf("拟人通话", "国民级日活", "口语陪练", "超快响应"),
            targetAudience = "学生党、英语学习者、通勤闲聊、全年龄段家庭用户",
            killerFeature = "声线极其拟人，支持戴上耳机实时无缝语音通话聊天与口语陪练",
            desc = "字节跳动打造的国民级 AI 个人生活伙伴。不仅能写文案改文章，更能化身耐心的英语外教与百科百晓生，语音语气生动自然，毫无机械感。",
            officialUrl = "https://www.doubao.com",
            pros = listOf("拟人化实时语音电话极其逼真", "界面极度清爽易用", "内置丰富好玩的角色扮演与智能体")
        ),
        AiAssistant(
            rank = 6,
            name = "通义千问 (Qwen)",
            vendor = "阿里巴巴",
            rating = "9.5",
            heatScore = "95.2w",
            category = "综合全能",
            tags = listOf("Qwen2.5-Max", "开源最强基座", "阿里生态", "百宝箱工具"),
            targetAudience = "办公白领、电商从业者、企业开发人员、文字工作者",
            killerFeature = "结合阿里办公全家桶，支持文档总结、思维导图自动生成与公式解析",
            desc = "阿里巴巴自研的旗舰大模型。Qwen 2.5 系列在开源界横扫全球榜单，其 Web 与 App 端集成了 PPT 制作、长音频速记、全格式文档秒读等丰富工具。",
            officialUrl = "https://tongyi.aliyun.com",
            pros = listOf("综合实力稳居国内顶尖梯队", "深度集成办公与音视频提炼工具", "对中文古典文化与现代语境理解深刻")
        ),
        AiAssistant(
            rank = 7,
            name = "智谱清言 (GLM)",
            vendor = "智谱 AI (清华大学技术孵化)",
            rating = "9.4",
            heatScore = "94.6w",
            category = "逻辑代码",
            tags = listOf("AutoGLM", "清华系自研", "智能体自主操作", "数据分析"),
            targetAudience = "科研学术人员、移动端自动化极客、职场专业人员",
            killerFeature = "AutoGLM 智能体自主控制手机点外卖发微信，长文档精准解读",
            desc = "源自清华大学计算机系的技术底蕴。不仅在学术与代码上实力过硬，更率先推出了能直接像真人一样操作手机屏幕界面的自主智能体技术。",
            officialUrl = "https://chatglm.cn",
            pros = listOf("技术完全自研可控", "Agent 自主操作手机能力领先", "学术文献理解与多模态图表解析强")
        ),
        AiAssistant(
            rank = 8,
            name = "Perplexity AI",
            vendor = "Perplexity",
            rating = "9.4",
            heatScore = "93.8w",
            category = "学术检索",
            tags = listOf("AI搜索先驱", "实时互联网", "文献引用", "精准答案"),
            targetAudience = "研究员、事实核查员、学生、深度互联网信息搜寻者",
            killerFeature = "每一句话都有权威网页链接角标直接印证，彻底告别传统搜索引擎广告",
            desc = "全球公认最好用的问答式 AI 搜索引擎。通过大模型直接综合全网优质网页信息，归纳出带准确引用来源的直接答案，极大提高信息检索效率。",
            officialUrl = "https://www.perplexity.ai",
            pros = listOf("答案精炼且来源透明", "实时抓取最新全球动态", "支持聚焦学术论文与专业数据库")
        ),
        AiAssistant(
            rank = 9,
            name = "Cursor / GitHub Copilot",
            vendor = "Cursor / Microsoft",
            rating = "9.5",
            heatScore = "93.0w",
            category = "逻辑代码",
            tags = listOf("编程神器", "全工程上下文", "Tab智能补全", "极速重构"),
            targetAudience = "独立开发者、全栈工程师、算法工程师、开源贡献者",
            killerFeature = "理解整个项目工程代码依赖，一键跨多文件重构与生成全套测试用例",
            desc = "重新定义现代软件开发体验的 AI 编程个人助理。直接嵌入代码编辑器中，能准确预判下一行代码，或通过自然语言直接生成整套功能模块。",
            officialUrl = "https://www.cursor.com",
            pros = listOf("整个工程多文件上下文深度关联", "智能代码自动补全体验极佳", "快速排查疑难 Bug")
        ),
        AiAssistant(
            rank = 10,
            name = "Gemini",
            vendor = "Google (谷歌)",
            rating = "9.3",
            heatScore = "92.5w",
            category = "综合全能",
            tags = listOf("Gemini 2.0", "原生多模态", "谷歌全家桶", "超长视频解析"),
            targetAudience = "谷歌生态深度用户、多媒体内容处理者、音视频分析者",
            killerFeature = "原生多模态架构，支持直接扔进去 1 小时高清视频进行关键帧提问",
            desc = "谷歌技术皇冠上的明珠。与 Gmail、Google Drive、YouTube 深度打通，不仅能秒读视频音频，还在 Gemini 2.0 时代展现了极致的多模态推理速度。",
            officialUrl = "https://gemini.google.com",
            pros = listOf("超大数百万 Token 上下文", "直接深度理解音视频文件", "与谷歌生态工具无缝协同")
        )
    )

    // ⚡ 最新 AI 动态热点雷达
    val dynamics = listOf(
        AiDynamic(
            id = "d1",
            title = "DeepSeek-R1 全球开源：打破顶尖闭源推理神话，引发全球算力架构重构",
            timeAgo = "刚刚 · 爆火头条",
            company = "DeepSeek (深度求索)",
            category = "大模型突破",
            hotIndex = "🔥 99.8w 热度",
            tags = listOf("DeepSeek-R1", "完全开源", "强化学习", "超越o1"),
            summary = "深度求索正式开源推理大模型 DeepSeek-R1，公布完整技术报告与蒸馏权重。凭借纯强化学习探索出思维链，以极低训练成本在数学竞赛与编程基准上比肩 OpenAI o1，引发全球开发者疯狂复现。",
            detailedBreakdown = "DeepSeek 团队通过无监督纯强化学习引导模型自主演化出反思、验证与长思维链能力，并免费开源全部权重与技术细节。此举大幅降低了全球企业与开发者部署前沿推理智能的门槛。",
            impact = "彻底重塑全球大模型格局，证实高质量算法设计比盲目堆砌算力更具颠覆性。",
            webUrl = "https://chat.deepseek.com"
        ),
        AiDynamic(
            id = "d2",
            title = "Claude 3.7 正式开启测试：首创即时回答与深度混合推理双模式",
            timeAgo = "今日最新",
            company = "Anthropic",
            category = "代码推理",
            hotIndex = "🔥 97.4w 热度",
            tags = listOf("Claude 3.7", "混合思考", "SWE-bench", "架构天花板"),
            summary = "Anthropic 曝光下一代旗舰 Claude 3.7，引入用户可控的「即时响应」与「深度思考」动态算力分配机制。在软件工程基准测试中再次创造新记录，复杂系统重构与逻辑严密性再创新高。",
            detailedBreakdown = "该版本允许用户通过滑块调节模型的思考深度与响应时间，在需要极快反馈的日常场景秒回，在需要推导大型分布式架构或长代码工程时开启长程深思。",
            impact = "解决了长思考模型响应过慢与普通模型智商不足的矛盾，开辟人机交互新范式。",
            webUrl = "https://claude.ai"
        ),
        AiDynamic(
            id = "d3",
            title = "OpenAI 推出 o3-mini 模型：STEM 领域数学与竞赛编程能力全线拉满",
            timeAgo = "本周精选",
            company = "OpenAI",
            category = "代码推理",
            hotIndex = "🔥 95.8w 热度",
            tags = listOf("o3-mini", "低延迟推理", "竞赛编程", "科学计算"),
            summary = "OpenAI 面向全平台付费与免费用户分批开放 o3-mini，该模型专注于科学、技术、工程与数学领域的极限推理，不仅速度大幅提升，更在国际信息学竞赛与数学测试中斩获超高得分率。",
            detailedBreakdown = "o3-mini 支持三种预设思考等级（Low/Medium/High），兼具高效与深度。开发者可通过 API 自主调节推理深度，大幅削减以往 o1 模型的调用延迟与成本支出。",
            impact = "STEM 科研与编程自动化进入平价化时代，个人开发者即可轻松调用世界顶尖推理智能。",
            webUrl = "https://chatgpt.com"
        ),
        AiDynamic(
            id = "d4",
            title = "Kimi 发布长推理模型 k0-math：专攻极限数理难题与跨学科文献推演",
            timeAgo = "前沿速递",
            company = "月之暗面 (Moonshot)",
            category = "大模型突破",
            hotIndex = "🔥 94.2w 热度",
            tags = listOf("Kimi探索版", "k0-math", "学术研究", "多步验证"),
            summary = "月之暗面探索版上线专注于数理逻辑与学术深度研究的 k0-math。面对多步数学题、物理推演与学术争议观点，可自主拆解数十步探究流程并进行自检验证，中文语境下表现极其抢眼。",
            detailedBreakdown = "结合 Kimi 自研的超级搜索索引，k0-math 能够在推理中自主发起针对性论文追溯，从海量文献中提取公式原出处并比对实证数据，输出结构化严密的论证报告。",
            impact = "大幅提升中文学术研究者、考研学子与科研工作者的文献研读与公式推演效率。",
            webUrl = "https://kimi.moonshot.cn"
        ),
        AiDynamic(
            id = "d5",
            title = "阿里巴巴 Qwen 2.5-Max 强势登顶：国内顶尖旗舰多项综合指标超越 GPT-4o",
            timeAgo = "前沿速递",
            company = "阿里巴巴 (阿里云)",
            category = "大模型突破",
            hotIndex = "🔥 93.6w 热度",
            tags = listOf("Qwen2.5-Max", "闭源旗舰", "通用知识", "全能办公"),
            summary = "阿里云正式公布通义千问最新最大规模旗舰 Qwen 2.5-Max。在 MMLU、Arena-Hard、AlignBench 等多项中英文综合大基准上名列前茅，全面追平甚至超越海外顶尖商用旗舰闭源版本。",
            detailedBreakdown = "Qwen 2.5-Max 在海量高质量合成数据与混合专家架构（MoE）加持下，知识广度、多轮指令遵循与复杂长文档提炼能力实现跨越式升级，已在通义全家桶各端上线。",
            impact = "标志着国产头部自研通用大模型在各维度基准评测上迈入世界第一梯队。",
            webUrl = "https://tongyi.aliyun.com"
        ),
        AiDynamic(
            id = "d6",
            title = "豆包全双工实时拟人语音 2.0 升级：随时打断插话，延迟低至 180 毫秒",
            timeAgo = "热门趋势",
            company = "字节跳动",
            category = "个人助理",
            hotIndex = "🔥 92.5w 热度",
            tags = listOf("豆包语音2.0", "全双工通话", "极低延迟", "真人情感"),
            summary = "字节跳动为豆包 App 全面升级全双工端到端语音架构，实现毫秒级端到端音频生成。支持随时插话、停顿思考与情感语气起伏，带来前所未有的超逼真电话聊天体验。",
            detailedBreakdown = "告别了传统的「语音识别转文字 -> 文字大模型推理 -> 文字合成语音」拼接链路，采用一体化端到端音频大模型，精准捕捉用户的呼吸声、情绪波动与语速节奏。",
            impact = "推动 AI 个人陪伴助手从单纯文本对话全面步入情感化实时语音新纪元。",
            webUrl = "https://www.doubao.com"
        ),
        AiDynamic(
            id = "d7",
            title = "主流手机端侧离线运行 DeepSeek：各大手机厂商全速适配本地 NPU",
            timeAgo = "行业风向",
            company = "手机行业全生态",
            category = "开源生态",
            hotIndex = "🔥 91.0w 热度",
            tags = listOf("端侧离线", "本地NPU", "隐私安全", "零流量运行"),
            summary = "基于 DeepSeek-R1 蒸馏出的 1.5B/7B/8B/14B 小型化开源模型，各大芯片与手机厂商竞相完成端侧 NPU 硬件级适配，无需连接网络即可在手机本地极速推理解题。",
            detailedBreakdown = "通过 4-bit 量化与端侧芯片特定算子优化，7B 模型在当代旗舰手机上每秒可输出 25-30 个 Token，内存占用降至 4GB 以内，兼顾零流量消耗与绝对用户隐私保护。",
            impact = "AI 推理算力彻底走出云端数据中心，迈入每一个人手掌中的智能终端日常。",
            webUrl = "https://github.com/deepseek-ai"
        ),
        AiDynamic(
            id = "d8",
            title = "Cursor 与 AI 编程工具链重构开发生态：个人开发者实现全栈代码自研",
            timeAgo = "开发趋势",
            company = "Cursor & AI 开发者社区",
            category = "代码推理",
            hotIndex = "🔥 89.8w 热度",
            tags = listOf("AI编程", "全栈开发", "独立开发", "效率十倍"),
            summary = "AI 辅助编程工具链进入爆发期，全项目工程上下文理解能力让单个开发者可以在数小时内从零构建起过去需要整个团队数月开发的高复杂度全栈系统。",
            detailedBreakdown = "新一代编程助手不仅能写单行函数，更能自动阅读全局架构设计、修复编译报警、编写单元测试并自动提交 Git Commit，真正成为开发者的全能超级副驾驶。",
            impact = "软件生产力门槛断崖式降低，让个人创作者与小型敏捷团队拥有前所未有的工程爆发力。",
            webUrl = "https://www.cursor.com"
        )
    )
}
