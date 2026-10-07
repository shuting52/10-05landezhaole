package com.example.data.local

import com.example.data.model.BadgeType
import com.example.data.model.NavCard
import com.example.data.model.NavCategory
import com.example.data.model.NavSubCategory
import com.example.data.model.SearchEngine

object NavData {

    val searchEngines = listOf(
        SearchEngine("站内搜索", "搜资源 / 搜影视 / 搜AI神器...", null),
        SearchEngine("必应", "必应全网极速搜索...", "https://cn.bing.com/search?q=%s"),
        SearchEngine("B站", "哔哩哔哩视频与教程搜索...", "https://search.bilibili.com/all?keyword=%s"),
        SearchEngine("百度网盘", "搜网盘资源、提取码、文档...", "https://wangpan.xin/search?keyword=%s"),
        SearchEngine("阿里云盘", "搜阿里网盘4K无损原画资源...", "https://v2pan.com/search?q=%s"),
        SearchEngine("夸克网盘", "搜夸克秒存大容量资源...", "https://www.kkso.cc/search?kw=%s"),
        SearchEngine("Google", "谷歌全球资源与前沿科技搜索...", "https://www.google.com/search?q=%s")
    )

    private val baseCategories: List<NavCategory> = listOf(
        // 1. 常用AI与大模型
        NavCategory(
            id = "ai_models",
            name = "常用AI与大模型",
            iconKey = "sparkle",
            desc = "顶尖AI对话 · 智能助手 · 深度推理大模型",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("models", "通用对话"),
                NavSubCategory("creative", "生图与视频"),
                NavSubCategory("agent", "智能体与效率")
            ),
            cards = listOf(
                NavCard(
                    id = "site_new_loomi",
                    title = "Loomi",
                    url = "https://loomi.live",
                    icon = "https://loomi.live/favicon.ico",
                    fallbackText = "LM",
                    badge = "NEW",
                    badgeType = BadgeType.NEW,
                    desc = "新一代AI角色动漫与生动视频生成创意平台",
                    categoryId = "ai_models",
                    subcatId = "creative",
                    highlights = "支持角色面部表情与动作一致性生成；高质量动漫场景分镜与运镜控制；创作者高赞的AI动漫与短视频生产工具"
                ),
                NavCard(
                    id = "site_new_amz123",
                    title = "AMZ123 跨境卖家导航",
                    url = "https://www.amz123.com/",
                    icon = "https://www.amz123.com/favicon.ico",
                    fallbackText = "AMZ",
                    badge = "HOT",
                    badgeType = BadgeType.GOLD,
                    desc = "跨境电商一站式综合服务导航平台，出海卖家必备",
                    categoryId = "ai_models",
                    subcatId = "agent",
                    highlights = "汇集亚马逊、TikTok Shop等全球跨境电商工具导航；实时行业资讯、选品分析、海外营销与货源物流生态服务"
                ),
                NavCard(
                    id = "site_new_waclaw",
                    title = "WaClaw",
                    url = "https://waclaw.ihuiwa.com",
                    icon = "https://waclaw.ihuiwa.com/favicon.ico",
                    fallbackText = "WC",
                    badge = "NEW",
                    badgeType = BadgeType.NEW,
                    desc = "阿里出品电商AI Agent：覆盖爆款素材生成、智能选品、AI模特商拍与全渠道自动化分发",
                    categoryId = "ai_models",
                    subcatId = "agent",
                    highlights = "阿里巴巴官方电商生态AI Agent；一站式解决商品拍摄、虚拟模特换装、AI种草文案与主图短视频；全链路提升跨境与主流电商运营与转化效率"
                ),
                NavCard(
                    id = "site_new_dreammaker",
                    title = "DreamMaker",
                    url = "https://dreammaker.vobile.com.cn/",
                    icon = "https://dreammaker.vobile.com.cn/favicon.ico",
                    fallbackText = "DM",
                    badge = "NEW",
                    badgeType = BadgeType.NEW,
                    desc = "阜博集团AIGC数字内容资产与影视短剧创作者平台，赋能创意IP孵化与版权变现",
                    categoryId = "ai_models",
                    subcatId = "creative",
                    highlights = "上市公司阜博集团权威背书；覆盖从影视短剧剧本创意到数字资产存证与全渠道商业变现；专为AIGC创作者打造的版权保护与分镜工作流"
                ),
                NavCard(
                    id = "site_new_onsolo",
                    title = "OnSolo",
                    url = "https://onsolo.ai/",
                    icon = "https://onsolo.ai/favicon.ico",
                    fallbackText = "Solo",
                    badge = "NEW",
                    badgeType = BadgeType.NEW,
                    desc = "IP智能体驱动的AI短剧剧本与短视频故事创作平台，将灵感秒级转化为影视分镜与剧本",
                    categoryId = "ai_models",
                    subcatId = "creative",
                    highlights = "专精IP微调的智能体与Skills；一键输入灵感生成视频就绪（Video-Ready）剧本与镜头语言；大幅缩短短剧与短篇叙事孵化周期"
                ),
                NavCard("ai_-1", "即梦AI", "https://jimeng.jianying.com/", "", "即", "国内顶尖", BadgeType.ROSE, "字节跳动顶尖生图生视频与分镜创作平台", "ai_models", "creative"),
                NavCard("ai_-2", "通义万相", "https://tongyi.aliyun.com/wan/generate", "", "万", "阿里出品", BadgeType.GOLD, "阿里云艺术大模型涂鸦作画与风格重绘", "ai_models", "creative"),
                NavCard("ai_-3", "chatgpt", "https://chatgpt.com/", "", "GPT", "OpenAI", BadgeType.BLUE, "全球领先的多模态通用智能AI对话大脑", "ai_models", "models"),
                NavCard("ai_-4", "千问-阿里 AI 助手", "https://www.qianwen.com/", "", "问", "阿里千问", BadgeType.GOLD, "通义千问官方智能助手超长文档推理", "ai_models", "models"),
                NavCard("ai_-5", "豆包", "https://www.doubao.com/", "", "豆", "字节跳动", BadgeType.ROSE, "超高人气智能对话助手多角色日常陪伴", "ai_models", "models"),
                NavCard("ai_-6", "Dola", "https://www.dola.com/chat/", "", "Dola", "AI助理", BadgeType.NEW, "全能型个人日常生活与日程AI助理", "ai_models", "agent"),
                NavCard("ai_-7", "Duck.ai", "https://duck.ai/", "", "Duck", "隐私保护", BadgeType.BLUE, "DuckDuckGo出品无追踪匿名AI聊天平台", "ai_models", "models"),
                NavCard("ai_-8", "库库AI", "https://kuku.baidu.com/", "", "库", "百度生态", BadgeType.ROSE, "百度旗下年轻化多角色AI互动对话社区", "ai_models", "models"),
                NavCard("ai_-9", "可灵", "https://klingai.com/app", "", "可", "视频大模型", BadgeType.GOLD, "快手顶尖AI视频大幅度逼真物理运动", "ai_models", "creative"),
                NavCard("ai_-10", "海艺", "https://www.seaart.ai/zhCN", "", "海", "二次元写实", BadgeType.ROSE, "丰富模型库与LoRA创作一键微调", "ai_models", "creative"),
                NavCard("ai_-11", "ComfyUI", "https://cloud.comfy.org/cloud/login", "", "Comfy", "云端免配", BadgeType.BLUE, "官方在线ComfyUI免装显卡直接出图", "ai_models", "creative"),
                NavCard("ai_-12", "Veo3", "https://labs.google/", "", "Veo", "谷歌前沿", BadgeType.GOLD, "Google DeepMind最前沿高保真超写实生视频模型", "ai_models", "creative"),
                NavCard("ai_-13", "ideogram", "https://ideogram.ai/t/explore", "", "ideo", "文字排版", BadgeType.NEW, "顶级海报文字生成无畸变英汉字体排版", "ai_models", "creative"),
                NavCard("ai_-14", "Arena AI", "https://arena.ai/", "", "Arena", "大模型盲测", BadgeType.BLUE, "LMSYS权威大模型盲测排位与竞技大厅", "ai_models", "models"),
                NavCard("ai_-15", "LartAICineStudio", "https://ce.lartai.com/", "", "Lart", "影视级AI", BadgeType.GOLD, "面向影视概念与分镜设计的AI工作流", "ai_models", "creative"),
                NavCard("ai_-16", "Higgsfield", "https://higgsfield.ai/", "", "Higgs", "特效运镜", BadgeType.NEW, "手机端与网页端高动态镜头运镜AI生成", "ai_models", "creative"),
                NavCard("ai_-17", "leonardo", "https://app.leonardo.ai/", "", "Leo", "游戏资产", BadgeType.ROSE, "全球顶级游戏插画与概念美术AI出图", "ai_models", "creative"),
                NavCard("ai_-18", "MindVideo AI", "https://www.mindvideo.ai/", "", "Mind", "视频生成", BadgeType.BLUE, "智能文本与多帧镜头视频合成创作平台", "ai_models", "creative"),
                NavCard("ai_-19", "AI绘画", "https://mj.bandeyu.com/", "", "绘", "MJ直连", BadgeType.GOLD, "国内极速直连Midjourney高清绘画出图", "ai_models", "creative"),
                NavCard("ai_-20", "magnific", "https://www.magnific.com/app", "", "Mag", "AI超分重绘", BadgeType.ROSE, "画质超分辨率升级与发丝级细节微调神器", "ai_models", "creative"),
                NavCard("ai_-21", "堆友", "https://d.design/", "", "堆", "阿里设计", BadgeType.NEW, "阿里巴巴设计团队打造的多风格AI作画神器", "ai_models", "creative"),
                NavCard("ai_-22", "Happy Horse", "https://happy-horse.art/auth/signin", "", "马", "AI创作", BadgeType.BLUE, "海外新锐概念插画与角色生成平台", "ai_models", "creative"),
                NavCard("ai_-23", "Labnana", "https://labnana.com/zh", "", "Nana", "灵感实验室", BadgeType.GOLD, "前沿AI视觉探索与设计灵感激发工坊", "ai_models", "creative"),
                NavCard("ai_-24", "evolink", "https://evolink.ai/zh/gpt-image-2", "", "Evo", "GPT画图", BadgeType.ROSE, "智能集成最新GPT生图与API加速引擎", "ai_models", "creative"),
                NavCard("ai_-25", "fal", "https://fal.ai/sandbox?op=video.text_to_video&models=", "", "fal", "极速推理", BadgeType.NEW, "毫秒级高并发AI音视频与模型推理沙盒", "ai_models", "creative"),
                NavCard("ai_-26", "OiiOii", "https://www.oiioii.ai/home", "", "Oii", "智能生图", BadgeType.BLUE, "简单易用的一键式创意AI图像生成器", "ai_models", "creative"),
                NavCard("ai_-27", "MantleGo", "https://www.mantlegoai.com/Home", "", "Mantle", "AI工作台", BadgeType.GOLD, "企业与创作者多模态智能创作协作枢纽", "ai_models", "agent"),
                NavCard("ai_-28", "CreatOK爆款猎人", "https://www.creatok.ai/app/dashboard", "", "猎人", "爆款视频", BadgeType.ROSE, "全自动追踪分析全网爆款视频文案与分镜", "ai_models", "agent"),
                NavCard("ai_-29", "妙构", "https://www.miaogou.tech/zh", "", "妙", "创意架构", BadgeType.NEW, "面向创作者的智能提示词与设计辅助平台", "ai_models", "creative"),
                NavCard("ai_-30", "Skild Art", "https://www.skildart.cn/", "", "Skild", "设计美学", BadgeType.BLUE, "专注现代视觉艺术与设计风格的AI画廊", "ai_models", "creative"),
                NavCard("ai_-31", "FlowPix", "https://www.flowpix.club/", "", "Flow", "动态图片", BadgeType.GOLD, "让静态照片一键流转动起来的AI微动效", "ai_models", "creative"),
                NavCard("ai_-32", "Yapper", "https://yapper.so/dashboard", "", "Yap", "智能播客", BadgeType.ROSE, "输入任意文档一键生成生动对话双人播客", "ai_models", "agent"),
                NavCard("ai_-33", "linkfox", "https://www.linkfox.com/", "", "Fox", "跨境AI", BadgeType.BLUE, "跨境电商与营销文案多语言智能生成器", "ai_models", "agent"),
                NavCard("ai_-34", "小云雀", "https://xyq.jianying.com/", "", "雀", "剪映出品", BadgeType.GOLD, "剪映官方出品的智能剪辑与短视频文案AI", "ai_models", "agent"),
                NavCard("ai_-35", "图片工具", "https://magiceraser.org/", "", "擦", "智能擦除", BadgeType.NEW, "Magic Eraser无痕移除画面杂物与水印", "ai_models", "creative"),
                NavCard("ai_-36", "AI追光", "https://aizhuiguang.tech/", "", "追光", "光影重绘", BadgeType.ROSE, "专业级人像与场景AI重打光智能工具", "ai_models", "creative"),
                NavCard("ai_-37", "灵猫去水印", "https://clearcat.lingxiangtools.top/", "", "猫", "无水印提取", BadgeType.GOLD, "全网短视频与图集无水印原画一键解析", "ai_models", "creative"),
                NavCard("ai_-38", "123apps", "https://123apps.com/cn/", "", "123", "免费全能", BadgeType.BLUE, "浏览器端全套音视频与PDF处理套件", "ai_models", "agent"),
                NavCard("ai_-39", "DiffMind", "https://app.diffmind.ai/zh", "", "Diff", "思维导图", BadgeType.NEW, "基于大模型的结构化思维导图与灵感脑图", "ai_models", "agent"),
                NavCard("ai_-40", "ChatGPT Plus", "https://bewild.ai/subscribe", "", "Plus", "国内直充", BadgeType.ROSE, "官方ChatGPT Plus订阅与充值指引渠道", "ai_models", "models"),
                NavCard("ai_add_claude", "Claude", "https://claude.ai/", "https://claude.ai/favicon.ico", "Cl", "Anthropic", BadgeType.GOLD, "Anthropic顶尖逻辑推理、代码与长文档分析大模型", "ai_models", "models", "全球领先后验推理与安全对齐大模型，3.5 Sonnet代码与长文本综合能力行业标杆"),
                NavCard("ai_add_perplexity", "Perplexity", "https://www.perplexity.ai/", "https://www.perplexity.ai/favicon.ico", "Pplx", "AI搜索", BadgeType.BLUE, "全球顶尖对话式AI搜索引擎，精准网页溯源与深度研究", "ai_models", "models", "整合实时学术与互联网全网信息；带学术引用源与结构化总结；专业研究者必备"),
                NavCard("ai_add_groq", "Groq", "https://groq.com/", "https://groq.com/favicon.ico", "Groq", "极速推理", BadgeType.NEW, "LPU芯片驱动超高速AI推理引擎，每秒数百Token极速输出", "ai_models", "models", "全球最快的大模型推理引擎；免除排队等待；实时对话响应零卡顿"),
                NavCard("ai_add_mistral", "Mistral AI", "https://mistral.ai/", "https://mistral.ai/favicon.ico", "Mis", "欧洲开源", BadgeType.GOLD, "欧洲开源AI领跑者，高能效开源大模型与官方平台", "ai_models", "models", "Mistral Large与Pixtral多模态旗舰；轻量高效可本地部署；开发者极度赞誉"),
                NavCard("ai_add_together", "Together AI", "https://www.together.ai/", "https://www.together.ai/favicon.ico", "Tog", "云端推理", BadgeType.BLUE, "高性能开源大模型一站式微调、私有化部署与极速API服务", "ai_models", "models", "支持Llama 3、DeepSeek等全系开源模型；超高性价比API；毫秒级冷启动"),
                NavCard("ai_add_suno", "Suno", "https://suno.com/", "https://suno.com/favicon.ico", "Suno", "AI编曲", BadgeType.ROSE, "火爆全球的生成式AI音乐创作平台，输入灵感生成完整歌曲", "ai_models", "creative", "支持丰富音乐流派与多国语言人声演唱；智能作词编曲混音全流程"),
                NavCard("ai_add_udio", "Udio", "https://www.udio.com/", "https://www.udio.com/favicon.ico", "Udio", "母带音乐", BadgeType.GOLD, "高保真母带级AI音乐与人声演唱合成神器", "ai_models", "creative", "录音室级音质表现；支持段落延展与乐器单独音轨微调"),
                NavCard("ai_add_luma", "Luma Dream Machine", "https://lumalabs.ai/dream-machine", "https://lumalabs.ai/favicon.ico", "Luma", "电影级运镜", BadgeType.NEW, "高质量物理动效与电影级镜头AI视频生成引擎", "ai_models", "creative", "逼真物理碰撞与真实世界动力学模拟；支持5秒高动态流畅运镜扩展"),
                NavCard("ai_add_flux", "Flux.1", "https://blackforestlabs.ai/", "https://blackforestlabs.ai/favicon.ico", "Flux", "开源画质王", BadgeType.GOLD, "Black Forest Labs打造的新一代文生图开源大模型标杆", "ai_models", "creative", "超越Midjourney的文本渲染与真实人像质感；提示词理解极其精准"),
                NavCard("ai_add_elevenlabs", "ElevenLabs", "https://elevenlabs.io/", "https://elevenlabs.io/favicon.ico", "11", "声音克隆", BadgeType.ROSE, "业内标杆级AI声音克隆、拟真人声朗读与多语言配音平台", "ai_models", "creative", "极其自然的人类呼吸、语调起伏与情感表达；支持一分钟样本极速克隆"),
                NavCard("ai_add_fishaudio", "Fish Audio", "https://fish.audio/zh-CN/", "https://fish.audio/favicon.ico", "Fish", "免费克隆", BadgeType.NEW, "开源高保真极速语音合成与免显卡零样本声音克隆社区", "ai_models", "creative", "数千款精选二次元与写实音色库；几秒音频样本即可生成专属人声"),
                NavCard("ai_add_chattts", "ChatTTS", "https://chattts.com/", "https://chattts.com/favicon.ico", "Chat", "自然口语", BadgeType.BLUE, "专为中英双语对话优化的极度自然口语生成大模型", "ai_models", "creative", "自然口语语调、笑声与语气词拟真；非常适合播客与对话助手制作")
            )
        ),
        // 2. 无限画布与创作
        NavCategory(
            id = "ai_canvas",
            name = "无限画布与创作",
            iconKey = "brush",
            desc = "节点画布 · 灵感工作台 · 视觉与视频生成",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("canvas", "无限画布"),
                NavSubCategory("video", "视频生成"),
                NavSubCategory("platform", "聚合平台")
            ),
            cards = listOf(
                NavCard("canvas_-1", "京东云灵境", "https://lingjing.jdcloud.com/", "", "京", "无限画布", BadgeType.GOLD, "多模态无限画布生图生视频智能商用设计", "ai_canvas", "canvas"),
                NavCard("canvas_-2", "万镜一刻", "https://www.yikeai.com/#/home", "", "镜", "节点漫游", BadgeType.ROSE, "智能无限画布自由排列生视频与灵感碰撞", "ai_canvas", "canvas"),
                NavCard("canvas_-3", "新片场", "https://aigc.xinpianchang.com/", "", "新", "影视创作", BadgeType.BLUE, "中国最大影视创作人平台与AIGC实战案例库", "ai_canvas", "platform"),
                NavCard("canvas_-4", "flova.tv", "https://flova.tv/zh-CN", "", "Flova", "视觉故事", BadgeType.NEW, "连贯多镜头分镜与故事长视频AI生成平台", "ai_canvas", "video"),
                NavCard("canvas_-5", "LibTV", "https://www.liblib.tv/", "", "Lib", "LoRA模型", BadgeType.GOLD, "LiblibAI旗下在线模型试玩与影视创作者社区", "ai_canvas", "platform"),
                NavCard("canvas_-6", "AI应用聚合平台", "https://ccai.quantv.com/dashboard", "", "CC", "应用广场", BadgeType.ROSE, "集成上百款实用AI工具与算力工作台", "ai_canvas", "platform"),
                NavCard("canvas_-7", "RunningHub", "https://www.runninghub.cn/#/home", "", "Hub", "高算力节点", BadgeType.BLUE, "云端高算力 ComfyUI 工作流画布节点平台", "ai_canvas", "canvas"),
                NavCard("canvas_-8", "Seko", "https://seko.sensetime.com//", "", "Seko", "商汤出品", BadgeType.NEW, "商汤科技新一代生成式AI视觉创意工作台", "ai_canvas", "canvas"),
                NavCard("canvas_-9", "小影马", "https://yingma.tv/", "", "马", "短视频AI", BadgeType.GOLD, "专注短剧与自媒体短视频的AI自动成片工具", "ai_canvas", "video"),
                NavCard("canvas_-10", "AI 音樂影片生成器", "https://app.edimakor.ai/tw/studio/ai-music-video-generator?from=tab", "", "MV", "音乐MV", BadgeType.ROSE, "根据背景音乐节奏全自动生成对拍AI视觉MV", "ai_canvas", "video"),
                NavCard("ai_add_pixso", "Pixso", "https://pixso.cn/", "https://pixso.cn/favicon.ico", "Pix", "协同设计", BadgeType.GOLD, "新一代协同设计工作台，支持UI原型、矢量编辑与实时协作", "ai_canvas", "canvas", "一站式解决原型、设计、交互与交付需求；支持导入Figma与Sketch文件"),
                NavCard("ai_add_mastergo", "MasterGo", "https://mastergo.com/", "https://mastergo.com/favicon.ico", "MG", "国产设计", BadgeType.BLUE, "国产领先的高性能在线协同UI设计与设计系统平台", "ai_canvas", "canvas", "支持万人级设计系统规范与流畅画布缩放；深受国内顶尖互联网团队青睐"),
                NavCard("canvas_add_tldraw", "tldraw", "https://www.tldraw.com/", "https://www.tldraw.com/favicon.ico", "TL", "开源画布", BadgeType.NEW, "极度丝滑流畅的开源无限数字白板画布，支持手绘涂鸦、连线与Make Real功能", "ai_canvas", "canvas", "无限漫游缩放、多人实时同步协作；极简直观的交互与强大的开发者生态支持"),
                NavCard("canvas_add_excalidraw", "Excalidraw", "https://excalidraw.com/", "https://excalidraw.com/favicon.ico", "Ex", "手绘风格", BadgeType.GOLD, "手绘风格虚拟在线无限白板，无拘无束绘制架构图、流程图与脑暴草图", "ai_canvas", "canvas", "极简拟真手绘笔触，支持端到端加密、离线保存与多人连线即时协作"),
                NavCard("canvas_add_boardmix", "博思白板", "https://boardmix.cn/", "https://boardmix.cn/favicon.ico", "博", "AI画布", BadgeType.ROSE, "集成AI生图、思维导图、流程图与多媒体混合排版的国内顶尖AI无限白板平台", "ai_canvas", "canvas", "支持AI一键生成思维导图/流程图/PPT；无边界自由创作与团队敏捷协作空间"),
                NavCard("canvas_add_figjam", "FigJam", "https://www.figma.com/figjam/", "https://www.figma.com/favicon.ico", "Jam", "Figma出品", BadgeType.BLUE, "Figma官方推出的在线协同无限白板，敏捷项目规划、视觉便利贴与创意风暴利器", "ai_canvas", "canvas", "与Figma无缝互通，内置丰富图表组件、情绪贴纸与实时音视频互动工具"),
                NavCard("canvas_add_miro", "Miro", "https://miro.com/", "https://miro.com/favicon.ico", "Miro", "全球顶尖", BadgeType.GOLD, "全球领先的可视化协作无限画布，跨国团队与产品设计首选的数字化协作工作区", "ai_canvas", "canvas", "拥有数千款精选专业模板；支持敏捷看板、用户旅程图与无限工作流布局"),
                NavCard("canvas_add_processon", "ProcessOn", "https://www.processon.com/", "https://www.processon.com/favicon.ico", "PO", "专业脑图", BadgeType.BLUE, "专业强大的在线多人协作脑图与流程图无限画布，知识梳理与业务架构绘制必备", "ai_canvas", "canvas", "支持UML、BPMN、架构图、拓扑图绘制；海量专业行业模板与高效协同分享"),
                NavCard("canvas_add_heptabase", "Heptabase", "https://heptabase.com/", "https://heptabase.com/favicon.ico", "Hepta", "卡片白板", BadgeType.NEW, "以无限视觉画布为核心的卡片笔记与深度知识库，将复杂概念在空间中自由串联与推演", "ai_canvas", "canvas", "打通卡片笔记、白板漫游与白皮书写作；深度思考者与研究人员的知识管理神器"),
                NavCard("canvas_add_affine", "AFFiNE", "https://affine.pro/", "https://affine.pro/favicon.ico", "AF", "开源一体", BadgeType.ROSE, "新一代开源一体化知识操作系统，无缝融合富文本文档与自由节点无限白板画布", "ai_canvas", "canvas", "一键在文档视图与无界白板画布间自由切换；本地优先存储与隐私安全设计")
            )
        ),
        // 3. Prompt与Skill社区
        NavCategory(
            id = "ai_prompts",
            name = "Prompt与Skill社区",
            iconKey = "prompt",
            desc = "提示词工程 · 智能体Skill · 高质量咒语库",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("prompt", "Prompt咒语"),
                NavSubCategory("skill", "Skill社区"),
                NavSubCategory("gallery", "灵感画廊")
            ),
            cards = listOf(
                NavCard("prompts_-1", "Seedance 2.5 Prompt", "https://evolink.ai/zh/seedance-2-5-prompts", "", "词", "海量词库", BadgeType.GOLD, "覆盖人物、场景、镜头语言高端摄影咒语", "ai_prompts", "prompt"),
                NavCard("prompts_-2", "AIART.PICS", "https://aiart.pics/", "", "Pics", "极客画廊", BadgeType.BLUE, "收录全球顶尖AI艺术家的提示词与高清大图", "ai_prompts", "gallery"),
                NavCard("prompts_-3", "AI Prompt Library", "https://promptsref.com/", "", "Lib", "提示词库", BadgeType.NEW, "系统化分类的各行业专业大模型Prompt参考", "ai_prompts", "prompt"),
                NavCard("prompts_-4", "GPT Image 2提示词", "https://opennana.com/awesome-prompt-gallery?media_type=image", "", "GPT", "生图咒语", BadgeType.ROSE, "最新GPT-4o与DALL-E3高质量提示词范例", "ai_prompts", "prompt"),
                NavCard("prompts_-5", "youmind", "https://youmind.com/zh-CN/gpt-image-2-prompts", "", "Mind", "精选Prompt", BadgeType.GOLD, "设计师与画师必备的分类咒语检索与收藏", "ai_prompts", "prompt"),
                NavCard("prompts_-6", "AI工具", "https://familypro.io/en/gpt-image-2", "", "工具", "实用集锦", BadgeType.BLUE, "海外高赞AI工具与工作流提示词实操手册", "ai_prompts", "skill"),
                NavCard("prompts_-7", "Vibe Shot Club", "https://vibeshot.club/gallery", "", "Vibe", "氛围摄影", BadgeType.ROSE, "电影感氛围光影与街头纪实提示词画廊", "ai_prompts", "gallery"),
                NavCard("prompts_-8", "promptmart", "https://www.promptmart.cn/", "", "Mart", "提示词集市", BadgeType.NEW, "创作者交流交易高质量商业化Prompt集市", "ai_prompts", "prompt"),
                NavCard("prompts_-9", "CocoLoop AI社区", "https://www.cocoloop.cn/", "", "Coco", "海量Skill", BadgeType.GOLD, "海量 AI Skill 智能体技能库与前沿工作流", "ai_prompts", "skill"),
                NavCard("prompts_-10", "CocoLoop", "https://hub.cocoloop.cn/", "", "Hub", "工作流枢纽", BadgeType.BLUE, "智能体技能快速导入与在线测试运行平台", "ai_prompts", "skill"),
                NavCard("prompts_-11", "updream", "https://www.updream.cn/", "", "up", "各类Skill", BadgeType.ROSE, "智能体工作流与行业实战提示词集合", "ai_prompts", "skill")
            )
        ),
        // 4. 常用工具与魔法网络
        NavCategory(
            id = "tools_magic",
            name = "常用工具与魔法网络",
            iconKey = "magic",
            desc = "网络检测 · 极客利器 · 临时邮箱 · 开发辅助",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("net", "网络工具"),
                NavSubCategory("dev", "极客工具"),
                NavSubCategory("daily", "日常百宝箱")
            ),
            cards = listOf(
                NavCard("magic_tool-1", "地址生成器", "https://address.aixmb.cn/", "", "住", "真实地址", BadgeType.NEW, "生成全球各国合法格式的虚拟身份与账单地址", "tools_magic", "dev"),
                NavCard("magic_tool-2", "CF", "https://dash.cloudflare.com/", "", "CF", "网络安全", BadgeType.GOLD, "Cloudflare全球CDN加速与DDoS安全防护控制台", "tools_magic", "net"),
                NavCard("magic_tool-3", "GitHub", "https://github.com/", "", "Git", "全球开源", BadgeType.BLUE, "全球最大开发者代码开源与协作平台", "tools_magic", "dev"),
                NavCard("magic_tool-4", "Runable", "https://runable.com/", "", "Run", "云端运行", BadgeType.ROSE, "轻量级云端脚本与微服务极速托管平台", "tools_magic", "dev"),
                NavCard("magic_tool-5", "AI 工作市场", "https://www.runjobs.ai/zh/", "", "职", "自由职业", BadgeType.GOLD, "汇聚全球远程AI标注、开发与Prompt工程师兼职", "tools_magic", "daily"),
                NavCard("magic_tool-6", "X", "https://x.com/", "", "X", "全球推特", BadgeType.BLUE, "全球即时资讯、科技动态与意见领袖主阵地", "tools_magic", "daily"),
                NavCard("magic_tool-7", "Gemini", "https://gemini.google.com/app", "", "G", "谷歌AI", BadgeType.GOLD, "Google新一代原生多模态超强推理大模型", "tools_magic", "dev"),
                NavCard("magic_tool-8", "表答", "https://biaoda.me/", "", "表", "正则语法", BadgeType.NEW, "用自然语言秒转复杂正则表达式的高效神器", "tools_magic", "dev"),
                NavCard("magic_tool-9", "cobalt", "https://cobalt.tools/", "", "Cob", "极速下载", BadgeType.ROSE, "纯净无广告提取YouTube/B站/推特高清音视频", "tools_magic", "daily"),
                NavCard("magic_tool-10", "IP定位", "https://www.ipip.net/", "", "IP", "精准归属", BadgeType.BLUE, "高精度IP地理位置、网络运营商与ASN查询", "tools_magic", "net"),
                NavCard("magic_tool-11", "myip.ipip.net", "https://myip.ipip.net/", "", "MyIP", "秒测出口", BadgeType.GOLD, "极简单行返回当前网络公网出口IP地址", "tools_magic", "net"),
                NavCard("magic_tool-12", "UUID生成器", "https://www.uuidgenerator.net/version4", "", "UUID", "V4生成", BadgeType.NEW, "批量生成标准RFC4122高保真UUID唯一识别码", "tools_magic", "dev"),
                NavCard("magic_tool-13", "免费临时邮箱", "https://mail.cx/zh/", "", "邮", "防垃圾邮件", BadgeType.BLUE, "免注册自动接收验证码保护个人隐私", "tools_magic", "daily"),
                NavCard("magic_tool-14", "atoms", "https://atoms.dev/zh/dashboard", "", "Atom", "AI编程", BadgeType.GOLD, "云端自适应智能代码编写与全栈开发环境", "tools_magic", "dev"),
                NavCard("magic_tool-15", "CREAO", "https://agent.creao.ai/rewards?tab=tasks", "", "创", "激励任务", BadgeType.ROSE, "探索AI前沿应用与完成协作任务赢取积分", "tools_magic", "daily"),
                NavCard("magic_tool-16", "Skywork", "https://skywork.ai/", "", "天工", "昆仑万维", BadgeType.GOLD, "昆仑万维天工AI多功能智能创作搜索助手", "tools_magic", "dev"),
                NavCard("magic_tool-17", "365魔法工具", "https://databridgestackpro.com/", "", "365", "魔法网络", BadgeType.BLUE, "全方位网络环境检测与多平台连接指引", "tools_magic", "net"),
                NavCard("magic_tool-18", "在线网速测试", "http://www.wangsu8.com/", "", "测速", "网速检测", BadgeType.GOLD, "节点分布全国的免客户端网页宽带测速", "tools_magic", "net"),
                NavCard("magic_tool-19", "Cloudflare EDT 导航", "https://bestcf.pages.dev/", "", "EDT", "CF导航", BadgeType.NEW, "Cloudflare生态实用工具与优质节点导航", "tools_magic", "net"),
                NavCard("magic_tool-20", "ping0.cc", "https://ping0.cc", "", "Ping", "路由追踪", BadgeType.ROSE, "专业网络骨干路由追踪、原生IP与丢包率测试", "tools_magic", "net"),
                NavCard("magic_tool-22", "Netlify", "https://app.netlify.com/", "", "Net", "前端托管", BadgeType.BLUE, "全球一流的静态网站极速部署与自动化CI平台", "tools_magic", "dev"),
                NavCard("magic_tool-23", "MotionSites", "https://motionsites.ai/", "", "动", "动效设计", BadgeType.NEW, "收录全球顶尖网页微动效与交互动画灵感", "tools_magic", "daily"),
                NavCard("magic_tool-24", "小渝児工具箱 - 喜庆版", "https://xiaoyuer7758.vercel.app/", "", "渝", "百宝箱", BadgeType.ROSE, "作者精心整理的日常常用百宝箱工具合集", "tools_magic", "daily"),
                NavCard("magic_tool-25", "Link3", "https://link3.cc/user", "", "Link", "个性主页", BadgeType.GOLD, "Web3与社交达人一站式个人聚合多链导航", "tools_magic", "daily"),
                NavCard("magic_tool-26", "4K工具下载器", "https://www.4kdownload.com/thanks-for-downloading?source=videodownloaderandroid", "", "4K", "全网视频", BadgeType.BLUE, "4K Video Downloader官方极速音视频下载器", "tools_magic", "daily"),
                NavCard("magic_tool-27", "币安", "https://www.binance.com/zh-CN/download", "", "币", "全球平台", BadgeType.GOLD, "全球领先的数字资产交易与行情查询客户端", "tools_magic", "daily"),
                NavCard("magic_tool-28", "海量电视电影", "https://moovie.c2v2.com/", "", "影", "家庭影院", BadgeType.ROSE, "汇聚全网优质家庭影院与多屏互动观影推荐", "tools_magic", "daily"),
                NavCard("magic_tool-29", "小红书", "https://www.xiaohongshu.com/", "", "红", "生活搜索", BadgeType.ROSE, "年轻人的生活方式平台与真实消费决策指南", "tools_magic", "daily"),
                NavCard("magic_tool-30", "YouTube", "https://www.youtube.com/", "", "油管", "全球视频", BadgeType.ROSE, "全球最大的高品质视频分享与知识科普平台", "tools_magic", "daily")
            )
        ),
        // 5. 影视娱乐
        NavCategory(
            id = "movie",
            name = "影视娱乐",
            iconKey = "movie",
            desc = "高清在线 · 影视下载 · 网盘秒搜 · 动漫追番",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("online", "在线看"),
                NavSubCategory("download", "下载"),
                NavSubCategory("pan", "网盘"),
                NavSubCategory("anime", "动漫")
            ),
            cards = listOf(
                NavCard("mov-1", "看剧AI", "https://kanju.ai/?utm_source=WZ-buzhaole", "", "剧", "荐", BadgeType.ROSE, "AI剧集推荐与智能解析", "movie", "online"),
                NavCard("mov-2", "懂片帝", "http://dongpian17.com/?utm_source=WZYY-bzldh", "", "懂", "HOT", BadgeType.GOLD, "海量热播剧集高清在线点播", "movie", "online"),
                NavCard("mov-3", "555影视", "https://wu5cn.com/?utm_source=bzl_237", "", "5", "免VIP", BadgeType.NEW, "高清电影美剧全网追更", "movie", "online"),
                NavCard("mov-4", "硬核指南", "https://yinghezhinan.com/", "", "硬", "客观指南", BadgeType.GOLD, "极简客观的高品质影视及数码软件体验指引", "movie", "online"),
                NavCard("mov-5", "千千影视", "https://qqys01.com/", "", "千", "极速秒播", BadgeType.ROSE, "极速秒播无等待看剧大厅", "movie", "online"),
                NavCard("mov-6", "影视大全", "https://yingshidaquan.top/", "", "全", "海量合集", BadgeType.BLUE, "网罗全网各大视频站热播热映资源", "movie", "online"),
                NavCard("mov-7", "不卡顿影视", "https://www.yingshiso.top/", "", "卡", "超快专线", BadgeType.GOLD, "多条自建超清无卡顿高速播放节点", "movie", "online"),
                NavCard("mov-8", "酷影聚搜索", "https://feikuju.com/", "", "聚", "全网搜片", BadgeType.ROSE, "输入片名演员一键全网精准检索播放源", "movie", "online"),
                NavCard("mov-9", "追影看片", "https://zhuiying.vip/", "", "追", "每日更新", BadgeType.NEW, "院线新片与热播美剧韩剧同步收录", "movie", "online"),
                NavCard("mov-10", "奇点影视", "https://www.qdys1.cc/", "", "奇", "蓝光画质", BadgeType.BLUE, "奇点超清影视库无广告观影体验", "movie", "online"),
                NavCard("mov-11", "极光影视", "https://hellociqryx6e.com/", "", "极", "蓝光", BadgeType.BLUE, "蓝光画质4K无损杜比音效", "movie", "online"),
                NavCard("mov-12", "注视影视", "https://gazes.site/", "", "注", "影迷社区", BadgeType.ROSE, "资深影迷精选高分电影与文艺佳作点播", "movie", "online"),
                NavCard("mov-13", "66大片网", "https://www.66dpw.vip/", "", "66", "动作院线", BadgeType.GOLD, "经典动作大片与最新科幻电影专区", "movie", "online"),
                NavCard("mov-14", "荐片官网", "https://jianpian25.com/", "", "荐", "P2P直下", BadgeType.ROSE, "采用P2P高速边下边播技术的影视看片神器", "movie", "online"),
                NavCard("mov-15", "免费五五短剧", "https://www.duanju55.com/", "", "短", "爆款短剧", BadgeType.GOLD, "逆袭重生穿越全套爽文短剧免费看", "movie", "online"),
                NavCard("mov-16", "免费在线看球", "https://www.88kq.me/", "", "球", "体育直播", BadgeType.BLUE, "五大联赛、欧冠与NBA高清无插件体育直播", "movie", "online"),
                NavCard("mov-17", "剧OK", "https://juok5.top/", "", "OK", "省心追剧", BadgeType.NEW, "界面清爽加载飞快的现代在线观影站", "movie", "online"),
                NavCard("mov-19", "搜剧AI", "https://souju.ai/?utm_source=WZYY-bzldh&utm_medium=shoulu", "", "搜", "AI找剧", BadgeType.GOLD, "只需描述剧情模糊片段即可瞬间精准找出电影", "movie", "online"),
                NavCard("mov-20", "4K在线", "https://www.bbibb.top/", "", "4K", "真4K", BadgeType.ROSE, "极高码率4K超清电影网盘与直链合集", "movie", "online"),
                NavCard("mov-21", "迅雷电影天堂", "https://www.xl720.com/", "", "天", "BT磁力", BadgeType.ROSE, "老牌迅雷电影资源更新站", "movie", "download"),
                NavCard("mov-22", "悠悠MP4", "https://www.uump4.cc/", "", "悠", "小巧MP4", BadgeType.ROSE, "超小体积高质量MP4移动端首选", "movie", "download"),
                NavCard("mov-23", "老电影时光", "https://www.ldysg.com/", "", "老", "怀旧胶片", BadgeType.GOLD, "上世纪经典老电影、译制片与黑白胶片回顾", "movie", "download"),
                NavCard("mov-24", "美剧下载", "https://www.mjf2020.com/", "", "美", "生肉熟肉", BadgeType.BLUE, "每日更新高品质双语字幕美剧英剧下载", "movie", "download"),
                NavCard("mov-25", "爱努努", "https://video.ainunu.cc/", "", "努", "日韩欧美", BadgeType.ROSE, "海量海外热映大片与独立电影在线播放", "movie", "online"),
                NavCard("mov-26", "4K指南", "https://4kzn.cc/", "", "指", "原盘REMUX", BadgeType.GOLD, "4K高码率HDR原盘电影下载指南", "movie", "download"),
                NavCard("mov-27", "盘多多", "https://www.pansou.asia/", "", "盘", "秒存网盘", BadgeType.ROSE, "海量网盘资源秒搜神器", "movie", "pan"),
                NavCard("mov-28", "夸夸搜索", "https://www.kkso.cc/", "", "夸", "夸克秒存", BadgeType.NEW, "夸克网盘影视合集秒存", "movie", "pan"),
                NavCard("mov-29", "未知搜索", "https://projext.run/", "", "未", "全网磁力", BadgeType.BLUE, "极简清爽的深层网络资源与磁力搜索引擎", "movie", "pan"),
                NavCard("mov-30", "汪盘搜索", "https://wangpan.xin/", "", "汪", "搜网盘", BadgeType.BLUE, "精准聚合全网各类网盘影视资源", "movie", "pan"),
                NavCard("mov-31", "无名搜盘", "https://wmxz.click/", "", "无", "网盘资源", BadgeType.GOLD, "海量百度、阿里、夸克电影直链搜索", "movie", "pan"),
                NavCard("mov-32", "剧多多", "https://www.judodo.cn/", "", "多", "全网热播", BadgeType.ROSE, "热门电视剧集无广告纯净看剧直通车", "movie", "online"),
                NavCard("mov-33", "西瓜动漫", "https://www.agekk.com/", "", "瓜", "热血番", BadgeType.ROSE, "最新日漫国漫同步连载追更", "movie", "anime"),
                NavCard("mov-34", "漫本", "https://www.manben.com/", "", "漫", "日漫韩漫", BadgeType.GOLD, "海量高清全彩黑白漫画在线翻页阅读", "movie", "anime"),
                NavCard("mov-35", "蜜柑计划", "https://mikanani.me/", "", "蜜", "新番追更", BadgeType.NEW, "二次元新番字幕组直达聚合", "movie", "anime"),
                NavCard("mov-36", "诸神字幕组", "https://subs.kamigami.org/", "", "诸", "双语精校", BadgeType.BLUE, "高品质双语精校日漫日剧字幕", "movie", "anime"),
                NavCard("mov-37", "AGE动漫", "http://www.age.tv/", "", "A", "纯净无广", BadgeType.ROSE, "纯净无广动漫在线观看社区", "movie", "anime"),
                NavCard("mov-38", "樱之空", "https://skr.skr3.cc:666/", "", "樱", "动漫音乐", BadgeType.ROSE, "二次元动漫原声OST与精美同人影视集", "movie", "anime")
            )
        ),
        // 6. 省钱助手
        NavCategory(
            id = "discounts",
            name = "省钱助手",
            iconKey = "money",
            desc = "外卖神券 · 咖啡打折 · 滴滴打车 · 特价门票",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("takeout", "外卖美食"),
                NavSubCategory("drink", "咖啡饮品"),
                NavSubCategory("travel", "出行生活")
            ),
            cards = listOf(
                NavCard("dis-1", "美团外卖红包", "https://www.meituan.com", "", "团", "天天领券", BadgeType.ROSE, "美团外卖每日限时大额外卖立减神券", "discounts", "takeout"),
                NavCard("dis-2", "京东外卖红包", "https://www.jd.com", "", "京", "免配送费", BadgeType.GOLD, "京东特价免配送费立减外卖神券", "discounts", "takeout"),
                NavCard("dis-3", "饿了么红包", "https://www.ele.me", "", "饿", "超级吃货", BadgeType.BLUE, "饿了么无门槛超级吃货红包天天领", "discounts", "takeout"),
                NavCard("dis-4", "肯德基5折起", "https://www.kfc.com.cn", "", "KFC", "疯狂星期四", BadgeType.ROSE, "KFC疯狂星期四与半价桶官方优惠", "discounts", "takeout"),
                NavCard("dis-5", "瑞幸咖啡5折起", "https://m.lkcoffee.com", "", "鹿", "咖啡立减", BadgeType.BLUE, "瑞幸生椰拿铁与当季新品专属打折券", "discounts", "drink"),
                NavCard("dis-6", "星巴克5折起", "https://www.starbucks.com.cn", "", "星", "星享好礼", BadgeType.GOLD, "星巴克咖啡特惠券与第二杯立减特权", "discounts", "drink"),
                NavCard("dis-7", "滴滴打车红包", "https://www.didiglobal.com", "", "滴", "打车立减", BadgeType.ROSE, "滴滴快车特惠出行无门槛代金券", "discounts", "travel"),
                NavCard("dis-8", "花小猪打车红包", "https://www.huaxiaozhu.com", "", "猪", "便宜打车", BadgeType.GOLD, "花小猪超值便宜打车立减红包", "discounts", "travel"),
                NavCard("dis-9", "电影票8折起", "https://maoyan.com", "", "票", "特惠看电影", BadgeType.ROSE, "猫眼特惠电影票全城影院立减特权", "discounts", "travel"),
                NavCard("dis-10", "特价景点门票", "https://www.qunar.com", "", "景", "5A景区", BadgeType.BLUE, "去哪儿全国5A景区特价门票极速预订", "discounts", "travel"),
                NavCard("dis-11", "特价寄快递", "https://kuaidi100.com", "", "递", "全网比价", BadgeType.NEW, "快递100官方寄件大额运费折扣券", "discounts", "travel")
            )
        ),
        // 7. 资源网站
        NavCategory(
            id = "resources",
            name = "资源网站",
            iconKey = "folder",
            desc = "纯净软件 · 高清壁纸 · 原版系统 · 论坛交流",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("soft_hub", "软件库"),
                NavSubCategory("wallpaper", "高清壁纸"),
                NavSubCategory("community", "极客社区")
            ),
            cards = listOf(
                NavCard("res-1", "佛系软件", "https://foxirj.com/", "", "佛", "纯净分享", BadgeType.GOLD, "知名绿色纯净精品绿色软件博客", "resources", "soft_hub"),
                NavCard("res-2", "果核剥壳", "https://www.ghxi.com/", "", "果", "知名良心", BadgeType.GOLD, "纯净绿色无绑软件与实用教程分享站", "resources", "soft_hub"),
                NavCard("res-3", "吾爱破解", "https://www.52pojie.cn/", "", "爱", "国内顶流", BadgeType.GOLD, "国内规模最大的安全技术与原创软件论坛", "resources", "community"),
                NavCard("res-4", "软仓", "https://www.ruancang.net/", "", "仓", "设计必备", BadgeType.BLUE, "Adobe与CAD工业设计软件全版本集合", "resources", "soft_hub"),
                NavCard("res-5", "MSDN原版系统", "https://msdn.itellyou.cn/", "", "MS", "官方纯净", BadgeType.BLUE, "微软MSDN原版官方纯净ISO镜像集合", "resources", "soft_hub"),
                NavCard("res-6", "独特吧", "https://www.dute8.cn/", "", "独", "黑科技", BadgeType.ROSE, "精选各类黑科技工具与装机精品", "resources", "soft_hub"),
                NavCard("res-7", "小K网", "https://www.xkwo.com/", "", "K", "实用补丁", BadgeType.NEW, "实用系统补丁与绿色工具聚合站点", "resources", "soft_hub"),
                NavCard("res-8", "浏览器插件CRX", "https://www.crxsoso.com/", "", "CRX", "离线扩展", BadgeType.GOLD, "Chrome与Edge浏览器CRX离线扩展高速下载", "resources", "soft_hub"),
                NavCard("res-9", "壁纸吧", "https://www.bizhib.com/", "", "吧", "精选大图", BadgeType.NEW, "风景动漫赛博朋克高质量壁纸免费下", "resources", "wallpaper"),
                NavCard("res-10", "免费4K壁纸", "https://haowallpaper.com/", "", "4K", "超清原图", BadgeType.ROSE, "海量电脑手机高分辨率唯美桌面壁纸", "resources", "wallpaper"),
                NavCard("res-11", "高清壁纸大全", "https://www.3gbizhi.com/", "", "壁", "每日美图", BadgeType.BLUE, "4K超清动漫自然极简桌面美图精选", "resources", "wallpaper"),
                NavCard("res-12", "日夸壁纸", "https://www.rikua.com/", "", "日", "治愈桌面", BadgeType.BLUE, "清新治愈系高清桌面美图每日更新", "resources", "wallpaper"),
                NavCard("res_add_iconfont", "Iconfont", "https://www.iconfont.cn/", "https://www.iconfont.cn/favicon.ico", "Icon", "阿里图标", BadgeType.GOLD, "阿里巴巴矢量图标库，数百万设计师与开发者必备的免费图标资源", "resources", "all", "国内最大矢量图标平台；支持SVG、PNG、代码字体一键导出与项目管理"),
                NavCard("res_add_feather", "Feather Icons", "https://feathericons.com/", "https://feathericons.com/favicon.ico", "Fea", "极简矢量", BadgeType.BLUE, "极简美观、开源且免费的现代化精美SVG线性矢量图标库", "resources", "all", "280+ 精心调校的24x24栅格纯净图标；支持自定义尺寸、粗细与颜色"),
                NavCard("res_add_lucide", "Lucide Icons", "https://lucide.dev/", "https://lucide.dev/favicon.ico", "Luc", "现代图标", BadgeType.NEW, "广受赞誉的开源现代图标库，支持React、Vue、Flutter等多框架组件", "resources", "all", "持续更新超过1500+精致图标；现代UI设计与开发首选图标体系")
            )
        ),
        // 8. 音乐听歌
        NavCategory(
            id = "music",
            name = "音乐听歌",
            iconKey = "music",
            desc = "无损音质 · 在线电台 · 歌曲解析 · Hi-Res母带",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("lossless", "无损听歌"),
                NavSubCategory("radio", "网络电台"),
                NavSubCategory("downloader", "歌曲下载")
            ),
            cards = listOf(
                NavCard("mus-1", "米兔音乐", "https://www.qqmp3.vip/", "", "米", "全网搜歌", BadgeType.ROSE, "免费试听并下载全网热门高品质MP3/FLAC", "music", "lossless"),
                NavCard("mus-2", "歌曲宝", "https://www.gequbao.com/", "", "宝", "秒速下载", BadgeType.NEW, "输入歌名歌手一键极速解析音频", "music", "downloader"),
                NavCard("mus-3", "开开无损音乐", "https://www.kkws.cc/", "", "开", "无损母带", BadgeType.GOLD, "发烧友无损车载音乐与无损母带合集", "music", "lossless"),
                NavCard("mus-4", "糖果无损音乐", "https://www.tgws.cc/", "", "糖", "经典粤语", BadgeType.ROSE, "海量经典粤语流行歌曲FLAC无损分享", "music", "lossless"),
                NavCard("mus-5", "24bit无损音乐网", "https://www.24bit.net/", "", "24", "Hi-Res", BadgeType.BLUE, "24bit母带级音质黑胶唱片抓轨资源", "music", "lossless"),
                NavCard("mus-6", "GD音乐台", "https://music.gdstudio.org/", "", "GD", "极简清爽", BadgeType.ROSE, "开源轻量在线音乐播放器无缝畅听", "music", "lossless"),
                NavCard("mus-8", "瑞丢网络电台", "https://radio5.cn/", "", "瑞", "全球电台", BadgeType.GOLD, "汇聚国内外数百个经典音乐新闻广播台", "music", "radio"),
                NavCard("mus-9", "思奥FM电台", "https://sao.fm/", "", "思", "白噪音舒缓", BadgeType.BLUE, "深夜治愈与工作学习专注白噪音流", "music", "radio")
            )
        ),
        // 9. 小说阅读
        NavCategory(
            id = "novel",
            name = "小说阅读",
            iconKey = "book",
            desc = "热门网文 · 经典古籍 · 有声评书 · 电子书库",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("web_novel", "热门网文"),
                NavSubCategory("audio_book", "有声听书"),
                NavSubCategory("library", "经典书库")
            ),
            cards = listOf(
                NavCard("nov-1", "悦读", "https://yuedu.163.com", "", "悦", "品质阅读", BadgeType.GOLD, "网易云阅读精品出版书与原创连载", "novel", "web_novel"),
                NavCard("nov-2", "无限小说网", "https://wuxianbook.com/", "", "无", "全本免更", BadgeType.ROSE, "玄幻修真都市言情海量全本TXT下载", "novel", "web_novel"),
                NavCard("nov-3", "金庸武侠网", "https://www.jinyongwx.com/", "", "侠", "典藏武侠", BadgeType.GOLD, "金庸古龙经典武侠小说全集精校版", "novel", "web_novel"),
                NavCard("nov-4", "哔哩轻小说", "https://www.linovelib.com/", "", "轻", "日轻国轻", BadgeType.BLUE, "高质量排版插图轻小说在线阅读社区", "novel", "web_novel"),
                NavCard("nov-5", "八零电子书", "https://www.txt80.cc/", "", "80", "完结精选", BadgeType.NEW, "全本完结小说TXT/EPUB免费高速下载", "novel", "web_novel"),
                NavCard("nov-6", "Z-Library镜像", "https://zh.101sat.ru/", "", "Z", "全球书库", BadgeType.GOLD, "全球最大数字图书馆免翻极速镜像站点", "novel", "library"),
                NavCard("nov-7", "鸠摩搜索", "https://www.jiumodiary.com/", "", "鸠", "找电子书", BadgeType.GOLD, "全网电子书格式PDF/EPUB/MOBI极速搜", "novel", "library"),
                NavCard("nov-8", "识典古籍", "https://www.shidianguji.com/", "", "典", "字节开源", BadgeType.NEW, "字节跳动联合北大打造的传统文化古籍库", "novel", "library"),
                NavCard("nov-9", "蜻蜓有声小说", "https://www.qtfm.cn/", "", "蜻", "声临其境", BadgeType.ROSE, "专业演播精品有声书评书相声畅听", "novel", "audio_book"),
                NavCard("nov-10", "听友FM", "https://tingyou.fm/", "", "听", "播客连载", BadgeType.BLUE, "有声书有声连载高品质播客电台", "novel", "audio_book"),
                NavCard("nov-11", "时光图书馆", "https://atimebook.com/", "", "光", "名著全本", BadgeType.ROSE, "经典文学名著全本无广告纯净阅读", "novel", "library"),
                NavCard("nov-12", "读者阁", "https://duzhege.cn/", "", "阁", "期刊杂志", BadgeType.NEW, "读者文摘、青年文摘经典期刊在线阅览", "novel", "library")
            )
        ),
        // 10. 游戏资源
        NavCategory(
            id = "game",
            name = "游戏资源",
            iconKey = "game",
            desc = "PC单机 · 联机对战 · 经典模拟器 · 怀旧红白机",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("pc", "单机免米"),
                NavSubCategory("online_game", "在线秒玩"),
                NavSubCategory("retro", "怀旧模拟器")
            ),
            cards = listOf(
                NavCard("gam-1", "霓虹游戏港", "https://www.sogay.xyz/", "", "霓", "大型单机", BadgeType.ROSE, "3A大作汉化补丁免安装解压即玩", "game", "pc"),
                NavCard("gam-2", "游仓酷", "https://quwan521.com/", "", "游", "PC游戏", BadgeType.GOLD, "精选Steam热销游戏与免安装学习版汇总", "game", "pc"),
                NavCard("gam-3", "GameFreer资源网", "https://www.gamefreer.com/", "", "GF", "免米单机", BadgeType.GOLD, "海量PC独立神作与精品合集", "game", "pc"),
                NavCard("gam-4", "梨子乐游戏", "https://lzlgo.com/", "", "梨", "热门大作", BadgeType.ROSE, "网罗经典怀旧与前沿电脑单机游戏资源", "game", "pc"),
                NavCard("gam-5", "掌游网Switch", "https://www.switchxiazai.com/", "", "掌", "掌机必备", BadgeType.NEW, "Switch与掌机ROM资源更新站", "game", "pc"),
                NavCard("gam-6", "小叽资源Steam", "https://steamzg.com/", "", "叽", "Steam资源", BadgeType.BLUE, "海量Steam单机联机游戏免安装下载", "game", "pc"),
                NavCard("gam-7", "老机库FC", "https://laojiku.com/", "", "老", "红白机ROM", BadgeType.GOLD, "童年经典红白机GBA与街机ROM大全", "game", "retro"),
                NavCard("gam-8", "植物大战僵尸全集", "https://pan.quark.cn/s/3f82680fcb81", "", "PVZ", "童年经典", BadgeType.GOLD, "PVZ原版加杂交版改版合集", "game", "pc"),
                NavCard("gam-9", "红色警戒2&尤里", "https://pan.quark.cn/s/2f1d9bd396ca", "", "RA2", "局域网对战", BadgeType.ROSE, "红警2尤里的复仇win10/11防卡死纯净版", "game", "pc"),
                NavCard("gam-10", "在线小霸王红白机", "https://www.yikm.net/", "", "红白", "童年回忆", BadgeType.GOLD, "魂斗罗、超级玛丽红白机在线模拟器", "game", "retro"),
                NavCard("gam-11", "在线玩红警", "https://game.ra2web.cn/", "", "红警", "联机对战", BadgeType.ROSE, "无需下载客户端直接浏览器联机作战", "game", "retro"),
                NavCard("gam-12", "在线玩CS 1.6", "https://vpn.zongsang.com/", "", "CS", "枪战神作", BadgeType.ROSE, "CS 1.6网页对战支持手柄和键鼠", "game", "retro"),
                NavCard("gam-13", "在线三国杀", "https://web.sanguosha.com/login/x/index", "", "杀", "即点即玩", BadgeType.ROSE, "网页版全武将身份局与国战模式", "game", "online_game"),
                NavCard("gam-14", "我的世界网页版", "https://mcjs.link/", "", "MC", "免下载", BadgeType.NEW, "纯浏览器端运行像素沙盒世界", "game", "online_game"),
                NavCard("gam-15", "Poki在线游戏", "https://poki.com/", "", "Poki", "全球爆款", BadgeType.GOLD, "数千款免安装休闲竞技微游戏", "game", "online_game")
            )
        ),
        // 11. 好用工具
        NavCategory(
            id = "tools",
            name = "好用工具",
            iconKey = "tool",
            desc = "实用百宝箱 · 抠图转换 · 文件快传 · 在线PS",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("ai_assist", "AI助理"),
                NavSubCategory("web_tool", "在线工具"),
                NavSubCategory("file_proc", "文件处理"),
                NavSubCategory("img_proc", "图片处理")
            ),
            cards = listOf(
                NavCard("too-1", "DeepSeek", "https://chat.deepseek.com/", "", "深", "国产之光", BadgeType.GOLD, "深度求索开创性推理与代码大模型", "tools", "ai_assist"),
                NavCard("too-2", "讯飞绘文", "https://turbodesk.xfyun.cn/", "", "绘", "图文排版", BadgeType.BLUE, "新一代智能公文及长文创作", "tools", "ai_assist"),
                NavCard("too-3", "AI视频创作", "https://www.liblib.art", "", "Lib", "视频创作", BadgeType.GOLD, "LiblibAI全功能AI视频与图像生成", "tools", "ai_assist"),
                NavCard("too-4", "AI简历生成", "https://upcv.tech/", "", "履", "高薪模板", BadgeType.NEW, "根据意向岗位智能生成排版优雅的专业简历", "tools", "ai_assist"),
                NavCard("too-5", "AI编程Atoms", "https://atoms.dev", "", "Atom", "代码生成", BadgeType.BLUE, "智能对话生成全栈可用代码与在线运行", "tools", "ai_assist"),
                NavCard("too-6", "PDF24 Tools", "https://tools.pdf24.org/zh/", "", "PDF", "免费全能", BadgeType.ROSE, "免费无限制的在线PDF合并、压缩与转换", "tools", "file_proc"),
                NavCard("too-7", "文叔叔-传文件", "https://www.wenshushu.cn/", "", "叔", "大文件免登", BadgeType.GOLD, "不限速收集发送大文件极速神器", "tools", "file_proc"),
                NavCard("too-8", "局域网传文件LocalSend", "https://localsend.org/#/", "", "Send", "开源免流量", BadgeType.BLUE, "手机电脑无缝无线高速传文件神器", "tools", "file_proc"),
                NavCard("too-9", "在线PS", "https://zaixianps.net/", "", "PS", "免安装", BadgeType.GOLD, "浏览器端直接打开PSD图层修图编辑", "tools", "img_proc"),
                NavCard("too-10", "在线一键抠图", "https://www.remove.bg/zh", "", "抠", "5秒抠图", BadgeType.ROSE, "精准发丝级透明背景智能移除", "tools", "img_proc"),
                NavCard("too-11", "免费商用字体", "https://www.maoken.com/", "", "字", "免版权", BadgeType.GOLD, "猫啃网精心整理的可商用免费中英文字体库", "tools", "img_proc"),
                NavCard("too-12", "草料二维码", "https://cli.im/", "", "草", "二维码生成", BadgeType.BLUE, "国内最知名的二维码生成与美化平台", "tools", "web_tool"),
                NavCard("too-13", "帮小忙(腾讯)", "https://tool.browser.qq.com/", "", "帮", "腾讯出品", BadgeType.ROSE, "QQ浏览器推出的全能免费百宝箱", "tools", "web_tool"),
                NavCard("too-14", "临时邮箱", "https://www.linshi-email.com/", "", "邮", "即用即丢", BadgeType.NEW, "10分钟临时电子邮箱免受垃圾邮件骚扰", "tools", "web_tool"),
                NavCard("too-15", "全球物流查询", "https://www.17track.net/zh-cn", "", "17", "全球追踪", BadgeType.GOLD, "多达数百家国际国内快递一站查询", "tools", "web_tool"),
                NavCard("too_add_raycast", "Raycast", "https://www.raycast.com/", "https://www.raycast.com/favicon.ico", "Ray", "极速启动", BadgeType.GOLD, "极客生产力桌面启动器，数百款开源插件与内置AI助理", "tools", "web_tool", "毫秒级剪贴板历史、窗口管理、脚本执行与多模型AI对话一键呼出"),
                NavCard("too_add_obsidian", "Obsidian", "https://obsidian.md/", "https://obsidian.md/favicon.ico", "Obs", "第二大脑", BadgeType.ROSE, "强大的离线优先Markdown双链笔记与个人知识图谱构建神器", "tools", "file_proc", "完全属于自己的本地数据；丰富社区插件与主题生态；构建互联知识网"),
                NavCard("too_add_notion", "Notion", "https://www.notion.so/", "https://www.notion.so/favicon.ico", "Not", "全能办公", BadgeType.BLUE, "集成文档、知识库、项目管理与强大AI助手的一体化工作空间", "tools", "file_proc", "高度自由的Database模块与页面嵌套；跨平台实时多端云端协同"),
                NavCard("too_add_eagle", "Eagle", "https://eagle.cool/zh-CN/", "https://eagle.cool/favicon.ico", "Eag", "素材管家", BadgeType.GOLD, "专为设计师打造的图片、灵感与设计素材本地分类管理神器", "tools", "img_proc", "毫秒级颜色筛选、标签分类与高保真素材预览；支持数十种专业格式"),
                NavCard("too_add_tinypng", "TinyPNG", "https://tinypng.com/", "https://tinypng.com/favicon.ico", "Tiny", "智能压缩", BadgeType.NEW, "智能有损无损图片压缩算法，在保留高质量前提下极大缩减体积", "tools", "img_proc", "全球最知名的PNG/JPEG/WebP图片在线极限压缩服务；支持批量拖拽"),
                NavCard("too_add_squoosh", "Squoosh", "https://squoosh.app/", "https://squoosh.app/favicon.ico", "Sq", "谷歌压缩", BadgeType.BLUE, "Google Chrome团队出品的高级图片压缩与现代格式转换神器", "tools", "img_proc", "纯浏览器端离线运算；支持MozJPEG、WebP、AVIF实时左右滑块画质对比")
            )
        ),
        // 12. 装机必备
        NavCategory(
            id = "pc_setup",
            name = "装机必备",
            iconKey = "desktop",
            desc = "纯净PE · 硬件检测 · 必备软件 · 驱动管理",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("system", "系统装机"),
                NavSubCategory("hardware", "硬件检测"),
                NavSubCategory("soft_must", "日常必备")
            ),
            cards = listOf(
                NavCard("pc_-2", "图吧工具箱", "http://www.tbtool.cn/", "", "图", "图拉丁神兵", BadgeType.GOLD, "最全电脑硬件检测跑分烤机排障工具箱", "pc_setup", "hardware"),
                NavCard("pc_-4", "HelloWindows", "https://hellowindows.cn/", "", "Win", "纯净镜像", BadgeType.NEW, "纯净Windows原版系统与软件高速直链", "pc_setup", "system"),
                NavCard("pc_-5", "USB启动盘Rufus", "http://rufus.ie/zh/", "", "Ruf", "极速制作", BadgeType.ROSE, "制作Windows和Linux可引导USB闪存盘", "pc_setup", "system"),
                NavCard("pc_-6", "CPU/显卡天梯榜", "https://gpu.exprank.com/", "", "梯", "天梯排行", BadgeType.ROSE, "最新桌面及移动端显卡与CPU性能天梯排位", "pc_setup", "hardware"),
                NavCard("pc_-7", "显示器刷新率测试", "https://testufo.com/", "", "UFO", "屏幕检测", BadgeType.NEW, "UFO Test屏幕高刷检测与响应排查", "pc_setup", "hardware"),
                NavCard("pc_-8", "在线键盘测试", "https://www.zfrontier.com/lab/keyboardTester", "", "键", "按键检测", BadgeType.BLUE, "客制化机械键盘键位响应与防冲突检测", "pc_setup", "hardware"),
                NavCard("pc_-9", "微信电脑版", "https://pc.weixin.qq.com/", "", "微", "官方正版", BadgeType.GOLD, "腾讯微信官方PC客户端", "pc_setup", "soft_must"),
                NavCard("pc_-10", "QQ官方版", "https://im.qq.com/pcqq", "", "QQ", "官方正版", BadgeType.BLUE, "腾讯QQ官方PC客户端", "pc_setup", "soft_must"),
                NavCard("pc_-11", "火绒安全", "https://www.huorong.cn/", "", "火", "安静不打扰", BadgeType.GOLD, "纯净无捆绑拦截流氓弹窗与系统防护", "pc_setup", "soft_must"),
                NavCard("pc_-12", "Everything搜索", "https://www.voidtools.com/zh-cn/", "", "Ev", "秒搜文件", BadgeType.ROSE, "毫秒级全盘文件名极速搜索神器", "pc_setup", "soft_must"),
                NavCard("pc_-13", "7-Zip解压", "https://www.7-zip.org/", "", "7z", "开源解压", BadgeType.BLUE, "高压缩比纯净开源解压缩神器", "pc_setup", "soft_must"),
                NavCard("pc_-14", "PotPlayer播放器", "https://potplayer.daum.net", "", "Pot", "万能播片", BadgeType.GOLD, "支持硬解超多滤镜的最佳电脑本地播放器", "pc_setup", "soft_must"),
                NavCard("pc_-15", "Steam游戏平台", "https://store.steampowered.com/about/", "", "Steam", "正版游戏", BadgeType.ROSE, "全球最大的正版数字游戏分发与社区平台", "pc_setup", "soft_must"),
                NavCard("pc_-16", "谷歌Chrome", "https://www.google.cn/intl/zh-CN/chrome/", "", "谷歌", "极速浏览", BadgeType.GOLD, "全球最快速安全的现代网页浏览器", "pc_setup", "soft_must")
            )
        ),
        // 13. 探索世界
        NavCategory(
            id = "explore",
            name = "探索世界",
            iconKey = "compass",
            desc = "趣味摸鱼 · 创意交互 · 极客社区 · 优质导航",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("fun", "趣味解压"),
                NavSubCategory("dev", "极客开发"),
                NavSubCategory("nav_hubs", "导航聚合")
            ),
            cards = listOf(
                NavCard("exp-1", "观影导航", "https://www.qhdh.top/", "", "影", "影视大全", BadgeType.GOLD, "精选全网优质观影站点与无广告播放源", "explore", "nav_hubs"),
                NavCard("exp-2", "摸鱼导航", "https://www.moyudh.com/", "", "鱼", "上班摸鱼", BadgeType.ROSE, "汇聚全网最搞笑有趣的摸鱼打卡小工具", "explore", "fun"),
                NavCard("exp-3", "聚资源导航", "https://juzyw.com/", "", "聚", "综合资源", BadgeType.BLUE, "综合性优质网盘与影视生活导航合集", "explore", "nav_hubs"),
                NavCard("exp-4", "信任的进化", "https://dccxi.com/", "", "信", "博弈论互动", BadgeType.GOLD, "风靡全球的人性与合作博弈论生动游戏", "explore", "fun"),
                NavCard("exp-5", "全景故宫", "https://pano.dpm.org.cn/#/", "", "宫", "云游故宫", BadgeType.GOLD, "360度高清沉浸式漫游故宫宏伟殿堂", "explore", "fun"),
                NavCard("exp-6", "在线放烟花", "http://fangyanhua.top/", "", "烟", "浪漫视觉", BadgeType.NEW, "鼠标点击绽放灿烂的3D粒子烟花秀", "explore", "fun"),
                NavCard("exp-7", "指尖陀螺", "https://ffffidget.com/", "", "陀", "减压旋转", BadgeType.ROSE, "解压3D物理模拟指尖陀螺在线旋转", "explore", "fun"),
                NavCard("exp-8", "捏泡泡", "https://bubblespop.netlify.app/", "", "泡", "解压捏捏", BadgeType.BLUE, "解压神器在线捏泡泡塑料包装纸体验", "explore", "fun"),
                NavCard("exp-9", "天空有多高", "https://www.secaibi.com/howbigisspace/", "", "空", "宇宙科普", BadgeType.GOLD, "沉浸式向上滑动探索太空高度全景", "explore", "fun"),
                NavCard("exp-10", "Trae 代码助手", "https://www.trae.cn", "", "Trae", "AI IDE", BadgeType.BLUE, "字节跳动出品的新一代自适应AI编程IDE", "explore", "dev"),
                NavCard("exp-11", "VsCode", "https://code.visualstudio.com/", "", "Code", "全能代码", BadgeType.BLUE, "微软出品生态最丰富的现代轻量代码编辑器", "explore", "dev"),
                NavCard("exp-12", "Gitee 码云", "https://gitee.com/", "", "Gitee", "国内代码", BadgeType.ROSE, "国内领先的代码托管与研发协作平台", "explore", "dev"),
                NavCard("exp-13", "Apifox 接口调试", "https://apifox.com/", "", "Api", "接口一体化", BadgeType.NEW, "API文档、调试、Mock、自动化测试一体化", "explore", "dev"),
                NavCard("exp-14", "多地Ping测试", "https://kzisp.com/", "", "Ping", "网络诊断", BadgeType.GOLD, "全球及全国多节点网络延迟测速", "explore", "dev"),
                NavCard("exp_add_roadmap", "Roadmap.sh", "https://roadmap.sh/", "https://roadmap.sh/favicon.ico", "Road", "学习路线", BadgeType.GOLD, "全球开发者学习路线图，涵盖前端、后端、DevOps、AI等清晰路径", "explore", "all", "社区驱动的开发者成长指南；结构化思维导图与技能树拆解"),
                NavCard("exp_add_producthunt", "Product Hunt", "https://www.producthunt.com/", "https://www.producthunt.com/favicon.ico", "PH", "全球首发", BadgeType.ROSE, "全球最具影响力的全新软硬件产品与AI创意首发发现社区", "explore", "all", "每天发现全球顶尖极客打造的最新创新科技产品与榜单")
            )
        ),
        // 14. 伙伴推荐
        NavCategory(
            id = "partners",
            name = "伙伴推荐",
            iconKey = "star",
            desc = "站长推荐 · 优质副业 · 极速直连 · 精选好站",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("recom", "站长力荐"),
                NavSubCategory("side_hustle", "副业兼职"),
                NavSubCategory("service", "实用专区")
            ),
            cards = listOf(
                NavCard("par-1", "看片神器", "https://追剧.cc", "", "片", "追剧神器", BadgeType.ROSE, "海量影视资源极速秒播随身影院", "partners", "recom"),
                NavCard("par-2", "GPT国内直连", "https://oai.oooai.cc/list/#/register?inviter=XYY4ZL", "", "直连", "国内免梯", BadgeType.GOLD, "国内免翻墙高速直连GPT对话体验平台", "partners", "recom"),
                NavCard("par-3", "映像星球影视", "https://www.yxxq.top/?utm_source=zbl", "", "星", "超清剧集", BadgeType.BLUE, "全网蓝光画质追剧追番纯净站点", "partners", "recom"),
                NavCard("par-4", "抖音游戏躺赚副业", "https://uuv1j2dzx2.feishu.cn/docx/XVq9dnbWBoVOlkxDiJucI9gUnFf?from=from_copylink", "", "副业", "经验干货", BadgeType.GOLD, "抖音小游戏推广与自媒体副业实操指南", "partners", "side_hustle"),
                NavCard("par-5", "AI对话绘画体", "https://www.lanhuohuo.cn/", "", "画", "智能助手", BadgeType.ROSE, "聚合AI绘图与对话写作的多功能小程序", "partners", "recom"),
                NavCard("par-6", "悬赏兼职平台", "https://link3.cc/fmy18", "", "赏", "兼职任务", BadgeType.NEW, "手机利用碎片时间完成悬赏任务赚零花", "partners", "side_hustle"),
                NavCard("par-7", "学术版codex", "https://qiewenpaper.com/app/agent?utm_source=daott", "", "学", "论文润色", BadgeType.BLUE, "高校师生必备的学术论文AI检索与写作助手", "partners", "service"),
                NavCard("par-8", "号卡和宽带", "https://h5.yapingkeji.com/#/pages/sales_index/my_store?mall_id=hqSBva%2FCH6l%2FTh4DC92Btg%3D%3D", "", "卡", "超大流量", BadgeType.GOLD, "全国运营商正规大流量号卡与低价宽带套餐", "partners", "service"),
                NavCard("par-9", "黑料吃瓜群", "http://cg.8gws.com/#bzl", "", "瓜", "网络热点", BadgeType.ROSE, "每日网络热点八卦与圈内大事件吃瓜追踪", "partners", "service")
            )
        )
,
        // 15. AI生态与精选工具
        NavCategory(
            id = "ai_bot_ecosystem",
            name = "AI生态与精选工具",
            iconKey = "sparkle",
            desc = "智能体 · 代码开发 · 视觉生成与办公AI全景",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("coding_agent", "编程代码"),
                NavSubCategory("vision_creative", "视觉与多模态"),
                NavSubCategory("office_writing", "办公写作"),
                NavSubCategory("research_eval", "科研评测"),
                NavSubCategory("tools_all", "实用AI")
            ),
            cards = listOf(
                NavCard("aibot_tool_417", "CakeGrowth(联盟营销)", "https://cakegrowth.cn/", "", "", "AI精选", BadgeType.GOLD, "CakeGrowth(联盟营销) · 前沿实用人工智能创新应用与生产力工具", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_418", "Docmee", "https://docmee.cn/?source=ai-bot", "", "", "AI精选", BadgeType.GOLD, "Docmee · 前沿实用人工智能创新应用与生产力工具", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_419", "果核剥壳", "https://gongke.net/", "", "", "AI精选", BadgeType.GOLD, "果核剥壳 · 前沿实用人工智能创新应用与生产力工具", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_420", "必灵", "https://ibiling.cn/?from=ai-bot", "", "", "AI精选", BadgeType.GOLD, "必灵 · 前沿实用人工智能创新应用与生产力工具", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_421", "LibTV", "https://libtv.cgref.cn/aibot", "", "", "AI精选", BadgeType.GOLD, "LibTV · 前沿实用人工智能创新应用与生产力工具", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_422", "秘塔AI搜索", "https://metaso.cn/?s=aibot1", "", "", "AI搜索", BadgeType.BLUE, "秘塔AI搜索 · 前沿实用人工智能创新应用与生产力工具", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_423", "VipCheap", "https://vipcheap.com/", "", "", "AI精选", BadgeType.GOLD, "VipCheap · 前沿实用人工智能创新应用与生产力工具", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_424", "蛙蛙写作", "https://wawawriter.cgref.cn/aibotbanner", "", "", "办公写作", BadgeType.GOLD, "蛙蛙写作 · 智能化PPT排版、文档处理与高效率写作工具", "ai_bot_ecosystem", "office_writing"),
                NavCard("aibot_tool_425", "AiPPT", "https://www.aippt.cn/", "", "", "办公写作", BadgeType.GOLD, "AiPPT · 智能化PPT排版、文档处理与高效率写作工具", "ai_bot_ecosystem", "office_writing"),
                NavCard("aibot_tool_426", "设计导航", "https://www.designkit.cn/?channel=10026", "", "", "视觉设计", BadgeType.ROSE, "设计导航 · 专业前沿AI图像、音频与数字视频创作平台", "ai_bot_ecosystem", "vision_creative"),
                NavCard("aibot_tool_427", "稿定", "https://www.gaoding.art/", "", "", "AI精选", BadgeType.GOLD, "稿定 · 前沿实用人工智能创新应用与生产力工具", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_428", "即刻", "https://okjk.co", "", "即", "同好社区", BadgeType.GOLD, "年轻人前沿科技、AI与生活方式高品质讨论社区", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_429", "映技派", "https://www.yjpoo.com/", "", "", "AI精选", BadgeType.GOLD, "映技派 · 前沿实用人工智能创新应用与生产力工具", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_430", "微博", "https://weibo.com", "", "", "社交热点", BadgeType.ROSE, "随时随地发现全网新鲜事与实时热搜榜", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_431", "哔哩哔哩", "https://www.bilibili.com", "", "", "弹幕视频", BadgeType.BLUE, "国内知名的视频弹幕网站与年轻人的潮流文化社区", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_432", "百度网盘", "https://pan.baidu.com", "", "", "云存储", BadgeType.BLUE, "国内领先的云存储服务与海量资源分享中心", "ai_bot_ecosystem", "tools_all"),
                NavCard("aibot_tool_433", "迅雷云盘", "https://pan.xunlei.com", "", "", "极速下载", BadgeType.GOLD, "高清4K云播与极速无损下载存储空间", "ai_bot_ecosystem", "tools_all"),
                NavCard("bot_add_dify", "Dify.AI", "https://dify.ai/zh", "https://dify.ai/favicon.ico", "Dify", "Agent开发", BadgeType.GOLD, "开源且易用的LLM应用开发平台，可视化构建Agent与工作流", "ai_bot_ecosystem", "tools_all", "编排多模型应用、知识库RAG与企业级工作流；支持一键发布与API集成"),
                NavCard("bot_add_fastgpt", "FastGPT", "https://fastgpt.in/", "https://fastgpt.in/favicon.ico", "Fast", "知识库问答", BadgeType.BLUE, "基于LLM大语言模型的知识库问答系统与智能客服搭建平台", "ai_bot_ecosystem", "tools_all", "提供强大的数据处理与向量检索能力；可视化Flow流式排版支持复杂业务"),
                NavCard("bot_add_flowise", "Flowise", "https://flowiseai.com/", "https://flowiseai.com/favicon.ico", "Flow", "低代码LLM", BadgeType.NEW, "开源低代码拖拽式LangChain/LlamaIndex工作流可视化平台", "ai_bot_ecosystem", "tools_all", "像搭积木一样轻松组装各类AI智能体、记忆库与外部工具集成")
            )
        ),
        // 16. 编程工具
        NavCategory(
            id = "coding_tools",
            name = "编程工具",
            iconKey = "code",
            desc = "前沿AI编程助手 · 智能IDE · 全栈代码生成与开发Agent",
            subcategories = listOf(
                NavSubCategory("all", "全部"),
                NavSubCategory("agent", "智能体Agent"),
                NavSubCategory("maas", "MaaS与算力"),
                NavSubCategory("framework", "AI框架生态"),
                NavSubCategory("ide", "智能IDE"),
                NavSubCategory("assistant", "代码补全")
            ),
            cards = NewCodingSites.list + listOf(
                NavCard(
                    id = "code_site_1",
                    title = "TRAE",
                    url = "https://www.trae.cn/",
                    icon = "https://www.trae.cn/favicon.ico",
                    fallbackText = "TRAE",
                    badge = "AI IDE",
                    badgeType = BadgeType.BLUE,
                    desc = "字节跳动出品的新一代自适应AI编程IDE，全工程感知与智能代理协作",
                    categoryId = "coding_tools",
                    subcatId = "ide",
                    highlights = "内置Claude 3.5与GPT-4o前沿大模型；支持Builder自主全工程代码生成与即时预览；专为现代全栈开发者打造的自适应AI IDE"
                ),
                NavCard(
                    id = "code_site_2",
                    title = "码上飞",
                    url = "https://codeflying.cgref.cn/aibot",
                    icon = "https://codeflying.cgref.cn/favicon.ico",
                    fallbackText = "码上飞",
                    badge = "AI出码",
                    badgeType = BadgeType.GOLD,
                    desc = "自然语言对话极速生成前后端可用系统与业务逻辑",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "只需描述业务需求，一键自动化构建数据库、接口与交互前端；大幅缩减项目原型与MVP开发周期"
                ),
                NavCard(
                    id = "code_site_3",
                    title = "代码小浣熊",
                    url = "https://www.xiaohuanxiong.com/",
                    icon = "https://www.xiaohuanxiong.com/favicon.ico",
                    fallbackText = "浣熊",
                    badge = "智能代码",
                    badgeType = BadgeType.ROSE,
                    desc = "基于商汤大模型打造的研发智能辅助工具，代码生成、问答与重构",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "支持数十种主流编程语言；毫秒级上下文代码补全与智能重构；安全可靠的开发者日常提效助手"
                ),
                NavCard(
                    id = "code_site_4",
                    title = "Codex",
                    url = "https://openai.com/codex/",
                    icon = "https://openai.com/favicon.ico",
                    fallbackText = "Codex",
                    badge = "OpenAI",
                    badgeType = BadgeType.BLUE,
                    desc = "OpenAI前沿代码智能模型基石，自然语言转换为高质量代码",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "全球开发者智能编程风向标；精通复杂算法逻辑与跨语言转译；为无数编程辅助工具提供强大内核"
                ),
                NavCard(
                    id = "code_site_5",
                    title = "Claude Code",
                    url = "https://www.anthropic.com/claude-code",
                    icon = "https://www.anthropic.com/favicon.ico",
                    fallbackText = "Claude",
                    badge = "Agentic",
                    badgeType = BadgeType.GOLD,
                    desc = "Anthropic推出的终端自主AI编程Agent，直接在命令行解决全工程任务",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "深入本地代码仓库执行Git、测试与多文件协同修改；精准遵循指令，高复杂度系统重构与Bug排查利器"
                ),
                NavCard(
                    id = "code_site_6",
                    title = "OpenCode",
                    url = "https://opencode.ai/",
                    icon = "https://opencode.ai/favicon.ico",
                    fallbackText = "Open",
                    badge = "开源Agent",
                    badgeType = BadgeType.NEW,
                    desc = "开放透明的AI辅助研发与代码生成开源平台",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "强调开源开放架构与本地安全私有化部署；深度融合软件工程全生命周期"
                ),
                NavCard(
                    id = "code_site_7",
                    title = "Agent.Space",
                    url = "https://agentspace.cgref.cn/s/omkl2mve4d",
                    icon = "https://agentspace.cgref.cn/favicon.ico",
                    fallbackText = "Space",
                    badge = "Agent空间",
                    badgeType = BadgeType.BLUE,
                    desc = "多智能体协同编程与自动化开发工作空间",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "串联多种特化智能体协同完成产品构思、架构设计与代码实现；全流程智能自动化协作"
                ),
                NavCard(
                    id = "code_site_8",
                    title = "Qoder",
                    url = "https://qoder.com/",
                    icon = "https://qoder.com/favicon.ico",
                    fallbackText = "Qoder",
                    badge = "极速补全",
                    badgeType = BadgeType.GOLD,
                    desc = "新一代AI代码助理，专注极致低延迟补全与项目级理解",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "毫秒级响应不卡顿；深度解析工程依赖与架构脉络；让双手永远保持心流编写状态"
                ),
                NavCard(
                    id = "code_site_9",
                    title = "Kimi Code",
                    url = "https://www.kimi.com/code",
                    icon = "https://www.kimi.com/favicon.ico",
                    fallbackText = "Kimi",
                    badge = "超长",
                    badgeType = BadgeType.ROSE,
                    desc = "月之暗面长文本AI代码助手，完整读入整套工程架构与长篇API文档",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "支持百万级上下文直接加载全部源码；解决复杂跨模块跨仓库联合排障与代码阅读痛点"
                ),
                NavCard(
                    id = "code_site_10",
                    title = "Kilo Code",
                    url = "https://kilo.ai/",
                    icon = "https://kilo.ai/favicon.ico",
                    fallbackText = "Kilo",
                    badge = "开发",
                    badgeType = BadgeType.NEW,
                    desc = "轻量高能的智能软件开发与代码加速利器",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "极简交互设计，专注把复杂功能需求一键化为整洁优雅的代码结构"
                ),
                NavCard(
                    id = "code_site_11",
                    title = "Google Antigravity",
                    url = "https://antigravity.google/",
                    icon = "https://antigravity.google/favicon.ico",
                    fallbackText = "Anti",
                    badge = "Google",
                    badgeType = BadgeType.BLUE,
                    desc = "Google最新前沿研发平台与智能工程系统探索",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "前沿Google黑科技驱动；颠覆传统开发范式的实验级高智能工程协作"
                ),
                NavCard(
                    id = "code_site_12",
                    title = "Kiro",
                    url = "https://kiro.dev/",
                    icon = "https://kiro.dev/favicon.ico",
                    fallbackText = "Kiro",
                    badge = "DevTools",
                    badgeType = BadgeType.GOLD,
                    desc = "现代Web与全栈工程师的代码生成与智能调试工具箱",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "直击开发者痛点，自动化生成单元测试用例与接口规范代码"
                ),
                NavCard(
                    id = "code_site_13",
                    title = "Cursor",
                    url = "https://www.cursor.com/",
                    icon = "https://www.cursor.com/favicon.ico",
                    fallbackText = "Cursor",
                    badge = "爆款IDE",
                    badgeType = BadgeType.BLUE,
                    desc = "全球风靡的AI-First代码编辑器，基于VSCode深层改造与Tab键智能预测",
                    categoryId = "coding_tools",
                    subcatId = "ide",
                    highlights = "Composer多文件联合修改；@Docs精准文档引用；全球无数工程师的主力生产力IDE"
                ),
                NavCard(
                    id = "code_site_14",
                    title = "Cline",
                    url = "https://cline.bot/",
                    icon = "https://cline.bot/favicon.ico",
                    fallbackText = "Cline",
                    badge = "自主Agent",
                    badgeType = BadgeType.NEW,
                    desc = "能够在你的VSCode中直接读写文件、运行终端命令的自主编程助手",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "开源高星扩展，人类监督下自主创建文件、执行编译排查并在浏览器中验证结果"
                ),
                NavCard(
                    id = "code_site_15",
                    title = "MiMo Code",
                    url = "https://mimo.xiaomi.com/mimocode",
                    icon = "https://mimo.xiaomi.com/favicon.ico",
                    fallbackText = "MiMo",
                    badge = "小米出品",
                    badgeType = BadgeType.GOLD,
                    desc = "小米面向工程师打造的智能编码与研发效率平台",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "小米生态研发实战沉淀；全语言适配，提供智能补全、单测生成与代码评审"
                ),
                NavCard(
                    id = "code_site_16",
                    title = "YouWare",
                    url = "https://www.youware.com/",
                    icon = "https://www.youware.com/favicon.ico",
                    fallbackText = "YW",
                    badge = "低代码AI",
                    badgeType = BadgeType.ROSE,
                    desc = "让所有人都能用AI快速定制数字化软件与业务系统",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "自然语言直达可部署应用，打破传统研发技术壁垒，赋能个人与微小团队"
                ),
                NavCard(
                    id = "code_site_17",
                    title = "ZCode",
                    url = "https://zcode-ai.com/",
                    icon = "https://zcode-ai.com/favicon.ico",
                    fallbackText = "ZCode",
                    badge = "AI重构",
                    badgeType = BadgeType.BLUE,
                    desc = "高精度代码分析、架构重构与语法转译智能平台",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "针对遗留系统与大型工程代码库，提供快速代码迁移、语法升级与缺陷扫描"
                ),
                NavCard(
                    id = "code_site_18",
                    title = "CodeBuddy IDE",
                    url = "https://www.codebuddy.ai/",
                    icon = "https://www.codebuddy.ai/favicon.ico",
                    fallbackText = "Buddy",
                    badge = "AI伴侣",
                    badgeType = BadgeType.GOLD,
                    desc = "全功能云端与桌面AI编程伙伴，随时随地结对编程",
                    categoryId = "coding_tools",
                    subcatId = "ide",
                    highlights = "实时在线协同与智能AI结对结伴编码，快速排查Bug并优化执行性能"
                ),
                NavCard(
                    id = "code_site_19",
                    title = "Lovable",
                    url = "https://lovable.dev/",
                    icon = "https://lovable.dev/favicon.ico",
                    fallbackText = "Love",
                    badge = "全栈构建",
                    badgeType = BadgeType.ROSE,
                    desc = "通过自然语言对话一键生成精美全栈Web应用并秒级上线部署",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "集成现代UI组件与Supabase后端；从草图想法到可分享上线的产品仅需几分钟"
                ),
                NavCard(
                    id = "code_site_20",
                    title = "CatPaw",
                    url = "https://catpaw.meituan.com/",
                    icon = "https://catpaw.meituan.com/favicon.ico",
                    fallbackText = "猫爪",
                    badge = "美团出品",
                    badgeType = BadgeType.GOLD,
                    desc = "美团猫爪代码助手，企业级高并发与海量业务研发辅助",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "经过海量高并发业务严苛验证；提供卓越的代码生成质量、安全合规检查与架构规范把控"
                ),
                NavCard(
                    id = "code_site_21",
                    title = "网易CodeWave",
                    url = "https://codewave.163.com/",
                    icon = "https://codewave.163.com/favicon.ico",
                    fallbackText = "网易",
                    badge = "低代码",
                    badgeType = BadgeType.BLUE,
                    desc = "网易自研智能低代码开发平台，可视化搭建物联网与企业系统",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "网易全栈自研内核，拖拽搭配AI辅助，专业级全生命周期企业数字化解决方案"
                ),
                NavCard(
                    id = "code_site_22",
                    title = "Augment Code",
                    url = "https://www.augmentcode.com/",
                    icon = "https://www.augmentcode.com/favicon.ico",
                    fallbackText = "Aug",
                    badge = "超大仓库",
                    badgeType = BadgeType.GOLD,
                    desc = "专为拥有数百万行代码的超大型团队量身定制的AI编程助手",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "极深度的企业级代码索引；毫秒级跨数十个仓库理解引用关系与类型定义"
                ),
                NavCard(
                    id = "code_site_23",
                    title = "AgnesCode",
                    url = "https://agnes-ai.com/agnescode",
                    icon = "https://agnes-ai.com/favicon.ico",
                    fallbackText = "Agnes",
                    badge = "AI开发",
                    badgeType = BadgeType.NEW,
                    desc = "专注于智能逻辑梳理与高效代码实现的AI工程工作流",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "将业务需求一键分解为清晰的架构蓝图与可靠的代码实现"
                ),
                NavCard(
                    id = "code_site_24",
                    title = "MonkeyCode",
                    url = "https://monkeycode.com/",
                    icon = "https://monkeycode.com/favicon.ico",
                    fallbackText = "Monkey",
                    badge = "趣味编程",
                    badgeType = BadgeType.ROSE,
                    desc = "让写代码变得像玩游戏一样流畅的智能编程体验",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "生动的编码提示与高效率的逻辑生成，深受开发者喜爱的轻巧编码伙伴"
                ),
                NavCard(
                    id = "code_site_25",
                    title = "腾讯云AI代码助手",
                    url = "https://copilot.tencent.com/",
                    icon = "https://copilot.tencent.com/favicon.ico",
                    fallbackText = "腾讯",
                    badge = "腾讯出品",
                    badgeType = BadgeType.BLUE,
                    desc = "腾讯自研混元代码大模型驱动，编码提效与智能问答助手",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "支持代码补全、技术规范答疑、单元测试生成与代码优化，企业研发深度提效"
                ),
                NavCard(
                    id = "code_site_26",
                    title = "通义灵码",
                    url = "https://tongyi.aliyun.com/lingma",
                    icon = "https://tongyi.aliyun.com/favicon.ico",
                    fallbackText = "灵码",
                    badge = "阿里出品",
                    badgeType = BadgeType.GOLD,
                    desc = "阿里云出品的AI智能编码助手，上百万开发者的日常提效首选",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "支持行级/函数级代码补全、自然语言写代码、单元测试生成与代码优化，国内极速无网络阻碍"
                ),
                NavCard(
                    id = "code_site_27",
                    title = "iFlyCode",
                    url = "https://iflycode.xfyun.cn/",
                    icon = "https://iflycode.xfyun.cn/favicon.ico",
                    fallbackText = "讯飞",
                    badge = "讯飞星火",
                    badgeType = BadgeType.ROSE,
                    desc = "科大讯飞星火代码大模型驱动的智能编程辅助平台",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "代码智能补全、代码纠错重构、自动生成单元测试与注释，全面提升研发质量"
                ),
                NavCard(
                    id = "code_site_28",
                    title = "文心快码",
                    url = "https://comate.baidu.com/",
                    icon = "https://comate.baidu.com/favicon.ico",
                    fallbackText = "快码",
                    badge = "百度出品",
                    badgeType = BadgeType.BLUE,
                    desc = "百度基于文心大模型打造的新一代智能代码助手 (Baidu Comate)",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "海量代码库深度训练，支持百种语言与主流IDE，多行代码精准预测补全"
                ),
                NavCard(
                    id = "code_site_29",
                    title = "MarsCode",
                    url = "https://www.marscode.cn/",
                    icon = "https://www.marscode.cn/favicon.ico",
                    fallbackText = "火星",
                    badge = "豆包驱动",
                    badgeType = BadgeType.GOLD,
                    desc = "字节跳动出品的免费AI编程助手，提供IDE扩展与云端开发环境",
                    categoryId = "coding_tools",
                    subcatId = "ide",
                    highlights = "豆包大模型强劲内核，内置AI智能补全、代码解释与单测生成，开箱即用"
                ),
                NavCard(
                    id = "code_site_30",
                    title = "Fitten Code",
                    url = "https://code.fittentech.com/",
                    icon = "https://code.fittentech.com/favicon.ico",
                    fallbackText = "非腾",
                    badge = "极速轻量",
                    badgeType = BadgeType.NEW,
                    desc = "非腾科技打造的高性能AI代码助手，毫秒级极速推理与跨平台支持",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "推理速度极快，内存占用小，支持自动补全、代码查错与智能解释"
                ),
                NavCard(
                    id = "code_orig_copilot",
                    title = "GitHub Copilot",
                    url = "https://github.com/features/copilot",
                    icon = "https://github.com/favicon.ico",
                    fallbackText = "Copilot",
                    badge = "行业标杆",
                    badgeType = BadgeType.BLUE,
                    desc = "GitHub与OpenAI联手打造的全球最普及AI结对程序员",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "深度集成各大主流IDE；海量开源仓库训练支撑；代码补全、对话提问与CLI全方位支持"
                ),
                NavCard(
                    id = "code_orig_firebase",
                    title = "Firebase Studio",
                    url = "https://firebase.studio/",
                    icon = "https://firebase.studio/favicon.ico",
                    fallbackText = "Studio",
                    badge = "云端开发",
                    badgeType = BadgeType.GOLD,
                    desc = "新一代云端全栈工作台与一键部署环境",
                    categoryId = "coding_tools",
                    subcatId = "ide",
                    highlights = "无需本地配置繁琐环境，在浏览器中即可开展全栈编码、调试与即时上线"
                ),
                NavCard(
                    id = "code_orig_windsurf",
                    title = "Windsurf",
                    url = "https://windsurf.com/",
                    icon = "https://windsurf.com/favicon.ico",
                    fallbackText = "Wind",
                    badge = "Agent",
                    badgeType = BadgeType.NEW,
                    desc = "Codeium出品的革命性Agentic AI IDE，具备Cascade心流引擎",
                    categoryId = "coding_tools",
                    subcatId = "ide",
                    highlights = "首创Cascade心流引擎，无缝融合AI自主行动与人类开发者直觉控制，多文件即时联动"
                ),
                NavCard(
                    id = "code_orig_bolt",
                    title = "Bolt.new",
                    url = "https://bolt.new/",
                    icon = "https://bolt.new/favicon.ico",
                    fallbackText = "Bolt",
                    badge = "容器",
                    badgeType = BadgeType.BLUE,
                    desc = "基于WebContainers直接在浏览器运行Node环境并完整生成全栈应用",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "零安装在浏览器沙盒中启动Vite与全栈服务，一句话提示词生成、运行并一键部署"
                ),
                NavCard(
                    id = "code_orig_infcode",
                    title = "InfCode",
                    url = "https://www.tokfinity.com/infcode",
                    icon = "https://www.tokfinity.com/favicon.ico",
                    fallbackText = "Inf",
                    badge = "无限代码",
                    badgeType = BadgeType.ROSE,
                    desc = "Tokfinity出品的无限智能代码与AI生产力驱动引擎",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "聚焦前沿开发生产力与智能体工作流，助力工程师探索自动化编程无限可能"
                ),
                NavCard(
                    id = "code_add_ollama",
                    title = "Ollama",
                    url = "https://ollama.com/",
                    icon = "https://ollama.com/favicon.ico",
                    fallbackText = "Oll",
                    badge = "本地模型",
                    badgeType = BadgeType.GOLD,
                    desc = "本地极速运行Llama 3、DeepSeek等大模型的轻量级命令行工具",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "一行命令拉取、运行并管理海量开源大模型；支持开箱即用OpenAI兼容API"
                ),
                NavCard(
                    id = "code_add_lmstudio",
                    title = "LM Studio",
                    url = "https://lmstudio.ai/",
                    icon = "https://lmstudio.ai/favicon.ico",
                    fallbackText = "LMS",
                    badge = "桌面模型",
                    badgeType = BadgeType.BLUE,
                    desc = "优雅桌面端本地大模型运行工具，支持一键下载运行各类GGUF模型",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "完全离线私有运行；支持GPU全显存卸载加速；本地OpenAI格式服务"
                ),
                NavCard(
                    id = "code_add_jan",
                    title = "Jan AI",
                    url = "https://jan.ai/",
                    icon = "https://jan.ai/favicon.ico",
                    fallbackText = "Jan",
                    badge = "离线优先",
                    badgeType = BadgeType.NEW,
                    desc = "开源离线优先的本地AI对话助手与模型管理桌面客户端",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "100%本地存储聊天记录；支持硬件加速与无缝切换云端与本地大模型"
                ),
                NavCard(
                    id = "code_add_cherry",
                    title = "Cherry Studio",
                    url = "https://cherry-ai.com/",
                    icon = "https://cherry-ai.com/favicon.ico",
                    fallbackText = "Che",
                    badge = "多端聚合",
                    badgeType = BadgeType.ROSE,
                    desc = "全能多模型桌面AI客户端，支持全网主流模型聚合与个人知识库",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "支持数百家国内外API服务商；内置提示词模板与文档代码多轮交互"
                ),
                NavCard(
                    id = "code_add_chatbox",
                    title = "Chatbox",
                    url = "https://chatboxai.app/zh",
                    icon = "https://chatboxai.app/favicon.ico",
                    fallbackText = "CB",
                    badge = "全平台",
                    badgeType = BadgeType.BLUE,
                    desc = "支持Windows/Mac/Android全平台的跨平台AI对话与代码办公桌面端",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "跨端数据同步；支持图像识别与代码高亮高保真渲染；办公好帮手"
                ),
                NavCard(
                    id = "code_add_aider",
                    title = "Aider",
                    url = "https://aider.chat/",
                    icon = "https://aider.chat/favicon.ico",
                    fallbackText = "Aid",
                    badge = "终端结对",
                    badgeType = BadgeType.GOLD,
                    desc = "基于终端命令行的顶尖AI结对编程工具，直接在本地Git仓库协同写代码",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "自动感知整个代码仓库架构；自动编写代码并生成精准Git Commit"
                ),
                NavCard(
                    id = "code_add_devin",
                    title = "Devin",
                    url = "https://preview.devin.ai/",
                    icon = "https://preview.devin.ai/favicon.ico",
                    fallbackText = "Dev",
                    badge = "自主工程师",
                    badgeType = BadgeType.ROSE,
                    desc = "全球首位完全自主执行软件工程任务与解决Issue的AI软件工程师",
                    categoryId = "coding_tools",
                    subcatId = "agent",
                    highlights = "自主分析项目依赖、定位Bug、编写单元测试并提交完整Pull Request"
                ),
                NavCard(
                    id = "code_add_zed",
                    title = "Zed Editor",
                    url = "https://zed.dev/",
                    icon = "https://zed.dev/favicon.ico",
                    fallbackText = "Zed",
                    badge = "Rust极速",
                    badgeType = BadgeType.NEW,
                    desc = "Rust编写的超高性能多人协同与原生AI集成的现代代码编辑器",
                    categoryId = "coding_tools",
                    subcatId = "ide",
                    highlights = "毫秒级冷启动与零延迟按键响应；原生集成GitHub Copilot与Claude"
                ),
                NavCard(
                    id = "code_add_regex101",
                    title = "Regex101",
                    url = "https://regex101.com/",
                    icon = "https://regex101.com/favicon.ico",
                    fallbackText = "Reg",
                    badge = "正则神器",
                    badgeType = BadgeType.GOLD,
                    desc = "全球开发者最常用的在线正则表达式调试、测试与深度解释工具",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "实时高亮匹配分组；逐步分解语法树解释；支持PCRE、Python、JS等引擎"
                ),
                NavCard(
                    id = "code_add_cyberchef",
                    title = "CyberChef",
                    url = "https://gchq.github.io/CyberChef/",
                    icon = "https://gchq.github.io/CyberChef/images/cyberchef-128x128.png",
                    fallbackText = "Chef",
                    badge = "网络军刀",
                    badgeType = BadgeType.BLUE,
                    desc = "网络安全瑞士军刀，支持加密解密、编码转换、散列计算与数据提取",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "数以百计的处理操作链式拖拽组合；免除搭建环境，纯本地浏览器解密"
                ),
                NavCard(
                    id = "code_add_caniuse",
                    title = "Can I use",
                    url = "https://caniuse.com/",
                    icon = "https://caniuse.com/favicon.ico",
                    fallbackText = "Can",
                    badge = "前端兼容",
                    badgeType = BadgeType.ROSE,
                    desc = "Web前端开发必备的浏览器特性与CSS/HTML5兼容性速查表",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "精准展示全球与中国主流浏览器版本支持率；前端架构选型必备参考"
                ),
                NavCard(
                    id = "code_add_carbon",
                    title = "Carbon",
                    url = "https://carbon.now.sh/",
                    icon = "https://carbon.now.sh/favicon.ico",
                    fallbackText = "Car",
                    badge = "代码截图",
                    badgeType = BadgeType.GOLD,
                    desc = "一键将你的源代码生成精美高颜值、适合分享的代码截图",
                    categoryId = "coding_tools",
                    subcatId = "assistant",
                    highlights = "支持数十种代码高亮主题与自定义窗口阴影排版；技术分享神器"
                )
            )
        )
    )

    // 保留全部原始站点不动，在此基础上增量合并 500+ 各行各业新 AI 与云端浏览器站点
    private val allIndustrySites: List<NavCard> by lazy {
        IndustryAiSitesPart1.list + IndustryAiSitesPart2.list + IndustryAiSitesPart3.list
    }

    private fun normUrl(url: String): String {
        return url.trim().lowercase()
            .removePrefix("https://")
            .removePrefix("http://")
            .removePrefix("www.")
            .trimEnd('/')
    }

    val categories: List<NavCategory> by lazy {
        val allBase = listOf(
            baseCategories[0],
            cloudBrowserCategory
        ) + baseCategories.drop(1)
        val byCat = allIndustrySites.groupBy { it.categoryId }
        val existingUrls = allBase
            .flatMap { it.cards }
            .map { normUrl(it.url) }
            .toMutableSet()

        allBase.map { cat ->
            val extra = byCat[cat.id].orEmpty().filter { candidate ->
                val key = normUrl(candidate.url)
                if (key.isBlank() || existingUrls.contains(key)) {
                    false
                } else {
                    existingUrls.add(key)
                    true
                }
            }
            if (extra.isEmpty()) cat else cat.copy(cards = cat.cards + extra)
        }
    }

    fun getAllCards(): List<NavCard> = categories.flatMap { it.cards }

    fun getRandomCard(): NavCard {
        val all = getAllCards()
        return all.random()
    }
}
