package com.example.data.local

import com.example.data.model.BadgeType
import com.example.data.model.NavCard
import com.example.data.model.NavCategory
import com.example.data.model.NavSubCategory

/**
 * 100 款精选全球云端浏览器与沙箱环境（涵盖 Muse 等美国地区云端浏览器、
 * 远程浏览器隔离 RBI、网页端虚拟机、防关联指纹浏览器、即时无痕沙盒）。
 */
val cloudBrowserCards: List<NavCard> = listOf(
    // 1-10 明星云端浏览器（自带美国地区/无痕串流）
    NavCard(
        id = "cloud_br_1",
        title = "Muse 云端浏览器 (自带美国地区)",
        url = "https://muse.run",
        fallbackText = "Muse",
        badge = "HOT",
        badgeType = BadgeType.GOLD,
        desc = "知名免翻云端浏览器：自带美国原生纯净节点，网页端直接串流全功能远程 Chromium，秒开全球网站",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_2",
        title = "Hyperbeam 云端虚拟浏览器",
        url = "https://hyperbeam.com",
        fallbackText = "HB",
        badge = "美国",
        badgeType = BadgeType.NEW,
        desc = "超低延迟云端浏览器房间，自带纯净美国原生 IP，支持音视频同步串流与多人协同浏览",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_3",
        title = "Neverinstall 云端云桌面",
        url = "https://neverinstall.com",
        fallbackText = "NI",
        badge = "推荐",
        badgeType = BadgeType.ROSE,
        desc = "在云端免安装极速运行 Chrome / Brave / Firefox，默认欧美顶尖数据中心高速网络",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_4",
        title = "SquareX 抛弃型云端沙盒",
        url = "https://sqrx.com",
        fallbackText = "SQ",
        badge = "多地区",
        badgeType = BadgeType.GOLD,
        desc = "即用即抛一次性云端浏览器：自由选择美国、欧洲、亚太等多个数据中心，零追踪无痕隔离",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_5",
        title = "Kasm Workspaces 云端容器浏览器",
        url = "https://kasmweb.com",
        fallbackText = "Kasm",
        badge = "隔离",
        badgeType = BadgeType.BLUE,
        desc = "开源容器化远程浏览器隔离 (RBI)：在云端无痕沙盒中安全打开任意可疑网址或私密浏览",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_6",
        title = "Browserling 在线即时云浏览器",
        url = "https://www.browserling.com",
        fallbackText = "Bling",
        badge = "免登录",
        badgeType = BadgeType.NEW,
        desc = "免安装免登录在线云浏览器：一键启动真实云端 Chrome/Firefox/Edge 交互式运行",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_7",
        title = "OnWorks 免费云工作站",
        url = "https://www.onworks.net",
        fallbackText = "OW",
        badge = "免费",
        badgeType = BadgeType.GOLD,
        desc = "网页端免费直接运行 Ubuntu / Fedora 桌面与全功能浏览器，自带美国原生网络环境",
        categoryId = "cloud_browser",
        subcatId = "vps_web"
    ),
    NavCard(
        id = "cloud_br_8",
        title = "DistroSea 网页直接开系统",
        url = "https://distrosea.com",
        fallbackText = "DS",
        badge = "极客",
        badgeType = BadgeType.ROSE,
        desc = "在网页中秒级启动数十款真实 Linux 操作系统，内嵌完整网络与网页浏览器沙盒",
        categoryId = "cloud_browser",
        subcatId = "vps_web"
    ),
    NavCard(
        id = "cloud_br_9",
        title = "Appetize.io 网页云端移动设备",
        url = "https://appetize.io",
        fallbackText = "Appz",
        badge = "移动",
        badgeType = BadgeType.BLUE,
        desc = "在网页浏览器中即时流式运行云端 Android 和 iOS 手机虚拟环境与移动端浏览器",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_10",
        title = "JSLinux 网页虚拟机浏览器",
        url = "https://bellard.org/jslinux",
        fallbackText = "JSL",
        badge = "神作",
        badgeType = BadgeType.GOLD,
        desc = "计算机传奇 Fabrice Bellard 打造：纯 JavaScript 在网页内模拟完整 x86 / RISC-V 虚拟机系统",
        categoryId = "cloud_browser",
        subcatId = "vps_web"
    ),

    // 11-20 开发者云端工作区与在线沙盒
    NavCard(
        id = "cloud_br_11",
        title = "StackBlitz WebContainers",
        url = "https://stackblitz.com",
        fallbackText = "SB",
        badge = "秒级",
        badgeType = BadgeType.NEW,
        desc = "在浏览器内部运行完整 Node.js 与网页应用，内置零延迟云端网页预览环境",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_12",
        title = "Gitpod 自动化云开发工作区",
        url = "https://www.gitpod.io",
        fallbackText = "GP",
        badge = "开发",
        badgeType = BadgeType.BLUE,
        desc = "开箱即用的云端容器开发环境，提供完整的 Linux 终端、端口转发与内置浏览器视图",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_13",
        title = "GitHub Codespaces 云工作站",
        url = "https://github.com/features/codespaces",
        fallbackText = "GH",
        badge = "官方",
        badgeType = BadgeType.GOLD,
        desc = "GitHub 官方云端完整开发虚拟机，全球骨干网络出网与即时网页端口预览",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_14",
        title = "CodeSandbox 现代化云端沙盒",
        url = "https://codesandbox.io",
        fallbackText = "CSB",
        badge = "沙盒",
        badgeType = BadgeType.ROSE,
        desc = "云端微虚拟机 (MicroVM) 驱动的云端全栈容器，毫秒级热更新与无痕隔离浏览器",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_15",
        title = "Replit 云端交互式编程沙盒",
        url = "https://replit.com",
        fallbackText = "Rep",
        badge = "北美",
        badgeType = BadgeType.NEW,
        desc = "全功能云端容器与 Web 运行环境，原生北美数据中心出网 IP，支持一键托管与浏览",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_16",
        title = "Browserless.io 云端无头浏览器",
        url = "https://www.browserless.io",
        fallbackText = "BL",
        badge = "自动化",
        badgeType = BadgeType.BLUE,
        desc = "企业级云端无头 Chrome / Puppeteer 运行集群，支持实时网页调试与远程串流交互",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_17",
        title = "LambdaTest 全球实时云端真机",
        url = "https://www.lambdatest.com",
        fallbackText = "LT",
        badge = "全球",
        badgeType = BadgeType.GOLD,
        desc = "跨越全球 3000+ 种真实浏览器与操作系统组合，支持多地理位置即时远程访问",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_18",
        title = "BrowserStack 真实云设备矩阵",
        url = "https://www.browserstack.com",
        fallbackText = "BST",
        badge = "真机",
        badgeType = BadgeType.ROSE,
        desc = "全球领先的云端真实机房与浏览器矩阵，即时连接真实海外数据中心网络",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_19",
        title = "Sauce Labs 虚拟云浏览器",
        url = "https://saucelabs.com",
        fallbackText = "SL",
        badge = "云端",
        badgeType = BadgeType.BLUE,
        desc = "全球规模最大的自动化与实时交互云端浏览器测试网络之一，提供全方位沙盒环境",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_20",
        title = "TestSigma 云端无代码测试浏览器",
        url = "https://testsigma.com",
        fallbackText = "TS",
        badge = "AI",
        badgeType = BadgeType.NEW,
        desc = "基于云端浏览器的 AI 自动化交互平台，免环境配置即刻体验云端网页",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),

    // 21-35 指纹防关联与跨境云浏览器（自带美国住宅/数据中心IP）
    NavCard(
        id = "cloud_br_21",
        title = "Maskfog 云端指纹浏览器",
        url = "https://www.maskfog.com",
        fallbackText = "Mask",
        badge = "美国住宅",
        badgeType = BadgeType.GOLD,
        desc = "自带原生独享美国住宅 IP 与独享环境隔离，支持一键创建多地区云端环境",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_22",
        title = "比特浏览器 (BitBrowser)",
        url = "https://www.bitbrowser.cn",
        fallbackText = "Bit",
        badge = "多开",
        badgeType = BadgeType.ROSE,
        desc = "专业多开防关联指纹浏览器，支持深度指纹伪装、内核级隔离与全球代理一键绑定",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_23",
        title = "AdsPower 云端反指纹浏览器",
        url = "https://www.adspower.com",
        fallbackText = "Ads",
        badge = "出海",
        badgeType = BadgeType.BLUE,
        desc = "跨境电商与出海业务首选，提供针对 Chromium 和 Firefox 双内核的深度指纹随机化",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_24",
        title = "Hubstudio 跨境指纹多开",
        url = "https://www.hubstudio.cn",
        fallbackText = "Hub",
        badge = "免费",
        badgeType = BadgeType.NEW,
        desc = "纯净独立网络环境隔离，支持永久免费多开与团队协同多账号云端无痕浏览",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_25",
        title = "ixBrowser 永久免费防关联浏览器",
        url = "https://www.ixbrowser.com",
        fallbackText = "IX",
        badge = "零封锁",
        badgeType = BadgeType.GOLD,
        desc = "零限制免费指纹浏览器，独立 Canvas/WebGL/字体环境隔离，杜绝跨窗口关联",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_26",
        title = "Dolphin{anty} 海外云端浏览器",
        url = "https://dolphin-anty.com",
        fallbackText = "Dol",
        badge = "国际",
        badgeType = BadgeType.ROSE,
        desc = "广受海外推崇的抗检测反指纹浏览器，快速切换全球网络节点，防风控封锁",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_27",
        title = "GoLogin Orbita 云端安全浏览器",
        url = "https://gologin.com",
        fallbackText = "GoL",
        badge = "云端",
        badgeType = BadgeType.BLUE,
        desc = "基于自研 Orbita 内核，自带云端虚拟配置与多种多国网络环境模拟",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_28",
        title = "MoreLogin 云端指纹浏览器",
        url = "https://www.morelogin.com",
        fallbackText = "More",
        badge = "真实机",
        badgeType = BadgeType.NEW,
        desc = "Canvas 与音频声学指纹真实采集重混，支持美国等地区原生住宅 IP 对接",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_29",
        title = "MultiLogin 行业标杆防关联系统",
        url = "https://multilogin.com",
        fallbackText = "ML",
        badge = "顶级",
        badgeType = BadgeType.GOLD,
        desc = "防指纹追踪领域的开创者与标杆，彻底隔离设备底层硬件特征与网络栈",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_30",
        title = "Incogniton 结构化反关联浏览器",
        url = "https://incogniton.com",
        fallbackText = "Inc",
        badge = "自动化",
        badgeType = BadgeType.ROSE,
        desc = "强大的多配置隔离工具，模拟人类键入行为与真实操作系统环境",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_31",
        title = "Octo Browser 高性能防指纹浏览器",
        url = "https://octobrowser.net",
        fallbackText = "Octo",
        badge = "高跑分",
        badgeType = BadgeType.BLUE,
        desc = "通过 Pixelscan / CreepJS 严苛检测的顶级伪装能力，专为高性能极速浏览设计",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_32",
        title = "Undetectable.io 云端多开环境",
        url = "https://undetectable.io",
        fallbackText = "Und",
        badge = "无痕",
        badgeType = BadgeType.NEW,
        desc = "本地与云端双架构无痕隔离，不限配置文件数量，支持海量环境批量生成",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_33",
        title = "ClonBrowser 云端防封号沙箱",
        url = "https://www.clonbrowser.com",
        fallbackText = "Clon",
        badge = "风控",
        badgeType = BadgeType.GOLD,
        desc = "全球代理 IP 自动化绑定，多维度伪装系统硬件与网络协议栈指纹",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_34",
        title = "MuLogin 多登防关联浏览器",
        url = "https://www.mulogin.com",
        fallbackText = "MuL",
        badge = "防联",
        badgeType = BadgeType.ROSE,
        desc = "独立虚拟浏览器环境，每个配置独享 Cookie、LocalStorage 与硬件指纹",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_35",
        title = "Puffin 云端加速浏览器",
        url = "https://www.puffin.com",
        fallbackText = "Puf",
        badge = "云渲染",
        badgeType = BadgeType.BLUE,
        desc = "美国云端服务器远程渲染网页后将画面加密串流回客户端，极度节省流量与设备功耗",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),

    // 36-50 企业级远程浏览器隔离 (RBI) 与零信任沙箱
    NavCard(
        id = "cloud_br_36",
        title = "Mighty 极速串流云端浏览器",
        url = "https://www.mightyapp.com",
        fallbackText = "Mgt",
        badge = "黑科技",
        badgeType = BadgeType.GOLD,
        desc = "由美国顶级数据中心双 Intel Xeon 服务器渲染网页，千兆光纤低延迟流畅串流",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_37",
        title = "Island 企业级隔离云浏览器",
        url = "https://www.island.io",
        fallbackText = "Isl",
        badge = "企业",
        badgeType = BadgeType.BLUE,
        desc = "重新定义企业安全：在完全隔离的云端安全环境中进行浏览，彻底防泄露防劫持",
        categoryId = "cloud_browser",
        subcatId = "rbi_security"
    ),
    NavCard(
        id = "cloud_br_38",
        title = "Talon 深度隔离安全云浏览器",
        url = "https://www.talon-sec.com",
        fallbackText = "Tal",
        badge = "零信任",
        badgeType = BadgeType.ROSE,
        desc = "面向分布式混合办公的零信任浏览器隔离体系，保护任意终端的网页访问安全",
        categoryId = "cloud_browser",
        subcatId = "rbi_security"
    ),
    NavCard(
        id = "cloud_br_39",
        title = "Silo by Authentic8 远程隔离浏览器",
        url = "https://www.authentic8.com",
        fallbackText = "Silo",
        badge = "军工级",
        badgeType = BadgeType.GOLD,
        desc = "运行在云端一次性容器中的全隔离网络浏览器，零网页代码在本地设备执行",
        categoryId = "cloud_browser",
        subcatId = "rbi_security"
    ),
    NavCard(
        id = "cloud_br_40",
        title = "Surfly 零安装即时协作云浏览",
        url = "https://www.surfly.com",
        fallbackText = "Surf",
        badge = "免安装",
        badgeType = BadgeType.NEW,
        desc = "基于云端反向代理与协同浏览技术，输入网址即可和好友在云端实时同步漫游",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_41",
        title = "Menlo Security 零信任云端隔离",
        url = "https://www.menlosecurity.com",
        fallbackText = "Men",
        badge = "隔离",
        badgeType = BadgeType.BLUE,
        desc = "全球领先的无害化远程浏览器隔离 (RBI)，所有网页渲染均留在云端沙箱",
        categoryId = "cloud_browser",
        subcatId = "rbi_security"
    ),
    NavCard(
        id = "cloud_br_42",
        title = "Cloudflare Browser Isolation",
        url = "https://www.cloudflare.com/zero-trust/products/browser-isolation/",
        fallbackText = "CF",
        badge = "全球边缘",
        badgeType = BadgeType.GOLD,
        desc = "依托 Cloudflare 全球 300+ 边缘数据中心运行的远程浏览器，毫秒级响应与安全",
        categoryId = "cloud_browser",
        subcatId = "rbi_security"
    ),
    NavCard(
        id = "cloud_br_43",
        title = "Amazon WorkSpaces Web",
        url = "https://aws.amazon.com/workspaces/web/",
        fallbackText = "AWS",
        badge = "亚马逊",
        badgeType = BadgeType.BLUE,
        desc = "AWS 官方按需付费低成本安全网页浏览器，托管在亚马逊北美等全球骨干云网络",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_44",
        title = "Google Cloud Workstations",
        url = "https://cloud.google.com/workstations",
        fallbackText = "GCW",
        badge = "谷歌",
        badgeType = BadgeType.ROSE,
        desc = "谷歌云托管的托管型安全开发环境，全功能云端 Linux 桌面与 Chrome 浏览器",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_45",
        title = "Microsoft Windows 365 Cloud PC",
        url = "https://www.microsoft.com/en-us/windows-365",
        fallbackText = "W365",
        badge = "微软",
        badgeType = BadgeType.GOLD,
        desc = "在任意浏览器打开完整的云端 Windows 操作系统与 Edge 浏览器，随时随地接入",
        categoryId = "cloud_browser",
        subcatId = "vps_web"
    ),
    NavCard(
        id = "cloud_br_46",
        title = "Paperspace Core 虚拟桌面",
        url = "https://www.paperspace.com",
        fallbackText = "Paper",
        badge = "高性能",
        badgeType = BadgeType.NEW,
        desc = "纽约与加州低延迟云端工作站，通过网页直接操控全能云主机与浏览器",
        categoryId = "cloud_browser",
        subcatId = "vps_web"
    ),
    NavCard(
        id = "cloud_br_47",
        title = "Shells 个人云电脑与虚拟浏览器",
        url = "https://www.shells.com",
        fallbackText = "Shell",
        badge = "便携",
        badgeType = BadgeType.BLUE,
        desc = "专为个人设计的便捷虚拟桌面与浏览器，一键接入美国云电脑畅享无限制网络",
        categoryId = "cloud_browser",
        subcatId = "vps_web"
    ),
    NavCard(
        id = "cloud_br_48",
        title = "Shadow PC 高性能云电脑",
        url = "https://shadow.tech",
        fallbackText = "Shd",
        badge = "低延迟",
        badgeType = BadgeType.ROSE,
        desc = "全功能云端高规格 PC，网页或客户端无缝串流，完整 Windows 网络与浏览器环境",
        categoryId = "cloud_browser",
        subcatId = "vps_web"
    ),
    NavCard(
        id = "cloud_br_49",
        title = "Apache Guacamole 网页无端远程桌",
        url = "https://guacamole.apache.org",
        fallbackText = "Guac",
        badge = "开源",
        badgeType = BadgeType.GOLD,
        desc = "无客户端 HTML5 远程桌面网关：网页直接连接云端 RDP/VNC/SSH 与远程浏览器",
        categoryId = "cloud_browser",
        subcatId = "vps_web"
    ),
    NavCard(
        id = "cloud_br_50",
        title = "RustDesk Web 网页端远程控制",
        url = "https://rustdesk.com",
        fallbackText = "Rust",
        badge = "极速",
        badgeType = BadgeType.NEW,
        desc = "开源远程桌面客户端 Web 版，支持纯网页访问海外远程设备与云端浏览器",
        categoryId = "cloud_browser",
        subcatId = "vps_web"
    ),

    // 51-65 云端渲染、网页快照沙盒与无头爬取浏览器
    NavCard(
        id = "cloud_br_51",
        title = "urlscan.io 云端沙箱自动检测",
        url = "https://urlscan.io",
        fallbackText = "UScan",
        badge = "分析",
        badgeType = BadgeType.GOLD,
        desc = "在云端无头沙盒中自动访问目标网址并录制网络请求、DOM 结构与完整截屏",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_52",
        title = "WebPageTest 全球节点真机渲染",
        url = "https://www.webpagetest.org",
        fallbackText = "WPT",
        badge = "多国",
        badgeType = BadgeType.BLUE,
        desc = "覆盖美国弗吉尼亚、加州及全球多地真实 Chrome/Edge 浏览器远程渲染与视频回放",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_53",
        title = "GTmetrix 北美云端浏览器测速",
        url = "https://gtmetrix.com",
        fallbackText = "GTM",
        badge = "北美",
        badgeType = BadgeType.ROSE,
        desc = "基于温哥华与美国机房真实云浏览器加载网页，完整呈现瀑布图与页面视频回放",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_54",
        title = "Browsershots 分布式浏览器快照",
        url = "https://browsershots.org",
        fallbackText = "BS",
        badge = "快照",
        badgeType = BadgeType.NEW,
        desc = "由全球分布式计算节点拍摄数十种浏览器版本下的真实网页渲染结果",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_55",
        title = "ScreenshotMachine 云端渲染引擎",
        url = "https://www.screenshotmachine.com",
        fallbackText = "SSM",
        badge = "工具",
        badgeType = BadgeType.GOLD,
        desc = "在线即时抓取全球网页长图，云端高保真还原网页真实样式与排版",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_56",
        title = "Urlbox 现代化云端渲染服务",
        url = "https://urlbox.com",
        fallbackText = "Ubox",
        badge = "API",
        badgeType = BadgeType.BLUE,
        desc = "采用真实云端 Chromium 渲染 WebGL、Canvas 与复杂 JavaScript 的网页沙盒",
        categoryId = "cloud_browser",
        subcatId = "sandbox"
    ),
    NavCard(
        id = "cloud_br_57",
        title = "Bright Data Scraping Browser",
        url = "https://brightdata.com",
        fallbackText = "BD",
        badge = "住宅IP",
        badgeType = BadgeType.GOLD,
        desc = "集成全球 7200 万真实家庭住宅 IP 的智能云端浏览器，全自动绕过验证码与防爬",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_58",
        title = "Oxylabs Web Unblocker 智能浏览器",
        url = "https://oxylabs.io",
        fallbackText = "Oxy",
        badge = "AI解封",
        badgeType = BadgeType.ROSE,
        desc = "AI 动态模拟人类浏览行为、指纹与请求标头，自动穿透地区限制与反爬虫拦截",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_59",
        title = "Smartproxy 独享住宅云浏览器",
        url = "https://smartproxy.com",
        fallbackText = "SP",
        badge = "城市级",
        badgeType = BadgeType.NEW,
        desc = "覆盖全球 195+ 国家和地区的真实住宅网络，支持精确到美国城市的定向云浏览",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_60",
        title = "Webshare 高速纯净云代理浏览器",
        url = "https://www.webshare.io",
        fallbackText = "WS",
        badge = "极速",
        badgeType = BadgeType.BLUE,
        desc = "超低成本纯净数据中心与静态住宅代理，高速畅游欧美各大数据中心服务",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_61",
        title = "IPRoyal 纯净家庭住宅网络",
        url = "https://iproyal.com",
        fallbackText = "IPR",
        badge = "纯净",
        badgeType = BadgeType.GOLD,
        desc = "100% 真实家庭宽带 IP，提供极致纯净度的网络访问与无痕指纹浏览器集成",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_62",
        title = "NetNut 专线 ISP 架构云浏览器",
        url = "https://netnut.io",
        fallbackText = "NN",
        badge = "专线",
        badgeType = BadgeType.ROSE,
        desc = "直接与全球电信运营商骨干对接，零跳点稳定性极佳的欧美网络环境",
        categoryId = "cloud_browser",
        subcatId = "usa_cloud"
    ),
    NavCard(
        id = "cloud_br_63",
        title = "SOAX 全球移动蜂窝与住宅环境",
        url = "https://soax.com",
        fallbackText = "SOAX",
        badge = "4G/5G",
        badgeType = BadgeType.NEW,
        desc = "真实移动基站与家庭 WiFi 节点，模拟出最无可挑剔的本地化云端浏览特征",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_64",
        title = "Geonode 弹性按需云网络",
        url = "https://geonode.com",
        fallbackText = "Geo",
        badge = "弹性",
        badgeType = BadgeType.BLUE,
        desc = "数百万全球节点随时调度，轻松切换不同国家地理位置进行网页测试",
        categoryId = "cloud_browser",
        subcatId = "anti_detect"
    ),
    NavCard(
        id = "cloud_br_65",
        title = "AnyViewer Web 网页远程控制",
        url = "https://www.anyviewer.com",
        fallbackText = "AV",
        badge = "免端",
        badgeType = BadgeType.GOLD,
        desc = "免装软件随时在浏览器控制异地电脑，秒开远程桌面自带的海外浏览器",
        categoryId = "cloud_browser",
        subcatId = "vps_web"
    ),

    // 66-80 浏览器指纹、环境纯净度与伪装检测诊断
    NavCard(
        id = "cloud_br_66",
        title = "BrowserScan 浏览器深度指纹检测",
        url = "https://www.browserscan.net",
        fallbackText = "BScan",
        badge = "权威",
        badgeType = BadgeType.GOLD,
        desc = "一键全面检测 Canvas、WebGL、音频、字体、IP 纯净度与机器人伪装等级",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_67",
        title = "CreepJS 极客反指纹对抗诊断",
        url = "https://abrahamjuliot.github.io/creepjs/",
        fallbackText = "Creep",
        badge = "地狱级",
        badgeType = BadgeType.ROSE,
        desc = "业内公认最严苛的浏览器指纹检测系统，能轻易穿透一般伪装并给出可信度评级",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_68",
        title = "Pixelscan 浏览器环境一致性评测",
        url = "https://pixelscan.net",
        fallbackText = "Pixel",
        badge = "风控分",
        badgeType = BadgeType.BLUE,
        desc = "针对浏览器指纹与 IP 地理位置的一致性做综合评分，识别伪装瑕疵与代理泄露",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_69",
        title = "Whoer.net 网络匿名度评测",
        url = "https://whoer.net",
        fallbackText = "Whoer",
        badge = "常用",
        badgeType = BadgeType.GOLD,
        desc = "老牌匿名度检测站，检测 DNS 泄露、WebRTC 穿透、时区语言不匹配与黑名单状态",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_70",
        title = "IPhey 浏览器数字指纹可信度",
        url = "https://iphey.com",
        fallbackText = "IPhey",
        badge = "一键查",
        badgeType = BadgeType.NEW,
        desc = "全方位评判当前网络环境纯净度：IP、浏览器、硬件、软件和位置五大维度绿标认证",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_71",
        title = "BrowserLeaks 全套浏览器泄露诊断",
        url = "https://browserleaks.com",
        fallbackText = "BLeaks",
        badge = "专业",
        badgeType = BadgeType.BLUE,
        desc = "全套网络指纹诊断：WebRTC 泄露、Canvas 指纹、字体检测、SSL/TLS 指纹合集",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_72",
        title = "WhatIsMyBrowser 软硬件解析",
        url = "https://www.whatismybrowser.com",
        fallbackText = "WMB",
        badge = "基础",
        badgeType = BadgeType.ROSE,
        desc = "权威查询当前浏览器 User-Agent、屏幕分辨率、Cookies 支持与插件环境",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_73",
        title = "DeviceInfo 终极隐私泄露审查",
        url = "https://deviceinfo.me",
        fallbackText = "DevInfo",
        badge = "详尽",
        badgeType = BadgeType.GOLD,
        desc = "深挖 50+ 硬件特征：电池状态、网络速度、传感器、蓝牙 API 与音频延迟分析",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_74",
        title = "FingerprintJS 设备指纹演示",
        url = "https://fingerprint.com",
        fallbackText = "FPJS",
        badge = "高精准",
        badgeType = BadgeType.BLUE,
        desc = "商业级 99.5% 跨浏览器识别率设备指纹演示，查看自己设备的唯一设备特征码",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_75",
        title = "Am I Unique? 全球指纹独特性对比",
        url = "https://amiunique.org",
        fallbackText = "AIU",
        badge = "学术",
        badgeType = BadgeType.NEW,
        desc = "法国国家信息与自动化研究所主持的学术项目，查看自己在全球指纹库中的稀缺度",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_76",
        title = "Cover Your Tracks (EFF 反追踪测试)",
        url = "https://coveryourtracks.eff.org",
        fallbackText = "EFF",
        badge = "公益",
        badgeType = BadgeType.ROSE,
        desc = "电子前哨基金会官方工具，测试当前浏览器对抗指纹追踪与广告阻断的真实能力",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_77",
        title = "WebRTC Troubleshooter 官方诊断",
        url = "https://test.webrtc.org",
        fallbackText = "RTC",
        badge = "穿透",
        badgeType = BadgeType.GOLD,
        desc = "WebRTC 官方测试：检查麦克风摄像头穿透、STUN/TURN 服务器与真实内网/外网 IP 泄露",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_78",
        title = "DNS Leak Test 全球标准泄露测试",
        url = "https://www.dnsleaktest.com",
        fallbackText = "DNS",
        badge = "防泄露",
        badgeType = BadgeType.BLUE,
        desc = "排查运营商劫持与 DNS 查询泄露，检测是否向非目标地区 DNS 发起请求",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_79",
        title = "Speedtest 全球节点宽带测速",
        url = "https://www.speedtest.net",
        fallbackText = "Speed",
        badge = "测速",
        badgeType = BadgeType.GOLD,
        desc = "Ookla 旗下全球基准网络测速，精确测试全球各节点带宽、时延与抖动",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_80",
        title = "Fast.com 极简全球流媒体测速",
        url = "https://fast.com",
        fallbackText = "Fast",
        badge = "秒测",
        badgeType = BadgeType.ROSE,
        desc = "Netflix 官方打造：测试跨国直连真实带宽与流媒体连接质量，极简无广告",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),

    // 81-100 IP 纯净度、欺诈风控与网络情报检索
    NavCard(
        id = "cloud_br_81",
        title = "ping0.cc 纯净度参考 (仅作为参考)",
        url = "https://ping0.cc",
        fallbackText = "Ping0",
        badge = "参考源",
        badgeType = BadgeType.GOLD,
        desc = "网络骨干路由追踪与原生IP分类参考：机房、住宅、广播与风险类型交叉比对",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_82",
        title = "IPinfo.io 开发者级高精 IP 库",
        url = "https://ipinfo.io",
        fallbackText = "IPinfo",
        badge = "精准",
        badgeType = BadgeType.BLUE,
        desc = "开发者首选 IP 情报源：精确查询 ASN、运营商、机房类型 (Hosting/ISP/Business)",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_83",
        title = "Scamalytics 全球 IP 欺诈评分系统",
        url = "https://scamalytics.com",
        fallbackText = "Scam",
        badge = "风控必备",
        badgeType = BadgeType.ROSE,
        desc = "出海风控必备：查询当前 IP 欺诈分 (Fraud Score 0-100)、黑名单与代理识别",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_84",
        title = "IPQualityScore (IPQS 深度分析)",
        url = "https://www.ipqualityscore.com",
        fallbackText = "IPQS",
        badge = "企业级",
        badgeType = BadgeType.GOLD,
        desc = "金融级反欺诈风控库：识别 VPN、Tor 节点、爬虫 IP 与高危代理环境",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_85",
        title = "ProxyCheck.io 实时网络代理排查",
        url = "https://proxycheck.io",
        fallbackText = "PCheck",
        badge = "检测",
        badgeType = BadgeType.NEW,
        desc = "实时检测当前访问是否来自公开代理、VPN 或匿名出口节点，给出危险度评估",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_86",
        title = "IPLocation.net 多库地理位置交叉",
        url = "https://www.iplocation.net",
        fallbackText = "IPLoc",
        badge = "交叉",
        badgeType = BadgeType.BLUE,
        desc = "同时比对 MaxMind、IP2Location、DB-IP 等 5 大权威地理数据库的定位差异",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_87",
        title = "MaxMind GeoIP 行业标准定位",
        url = "https://www.maxmind.com",
        fallbackText = "MaxM",
        badge = "权威",
        badgeType = BadgeType.GOLD,
        desc = "全球各大科技巨头采用的标准 GeoIP 与 minFraud 欺诈检测数据库",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_88",
        title = "IP2Location 全球精准地理与代理",
        url = "https://www.ip2location.com",
        fallbackText = "IP2L",
        badge = "全面",
        badgeType = BadgeType.ROSE,
        desc = "全球高精度 IP 归属地、时区、邮编、运营商与代理类型全面数据库",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_89",
        title = "DB-IP 全球 IP 地理威胁库",
        url = "https://db-ip.com",
        fallbackText = "DBIP",
        badge = "威胁库",
        badgeType = BadgeType.NEW,
        desc = "免费查询 IP 物理坐标、自治系统 ASN 以及关联的安全威胁等级评分",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_90",
        title = "AbuseIPDB 恶意攻击 IP 黑名单库",
        url = "https://www.abuseipdb.com",
        fallbackText = "Abuse",
        badge = "黑名单",
        badgeType = BadgeType.BLUE,
        desc = "全球网民与服务器协同维护的恶意扫描、爆破与滥用行为黑名单数据库",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_91",
        title = "Shodan 全球联网资产搜索引擎",
        url = "https://www.shodan.io",
        fallbackText = "Shodan",
        badge = "黑客神器",
        badgeType = BadgeType.GOLD,
        desc = "全网联网设备与开放端口资产扫描，探查当前 IP 暴露的服务器与网络服务",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_92",
        title = "Censys 互联网资产扫描引擎",
        url = "https://censys.io",
        fallbackText = "Censys",
        badge = "测绘",
        badgeType = BadgeType.ROSE,
        desc = "快速检索全球主机配置、SSL/TLS 证书链信息与互联网基础设施分布",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_93",
        title = "FOFA 网络空间资产测绘平台",
        url = "https://fofa.info",
        fallbackText = "FOFA",
        badge = "测绘",
        badgeType = BadgeType.NEW,
        desc = "国内知名网络空间资产测绘系统，分析 IP 资产的全球分布与服务运行特征",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_94",
        title = "ZoomEye 钟馗之眼网络测绘",
        url = "https://www.zoomeye.org",
        fallbackText = "Zoom",
        badge = "钟馗",
        badgeType = BadgeType.BLUE,
        desc = "知道创宇旗下网络空间雷达，洞悉全球设备指纹与组件漏洞分布",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_95",
        title = "SecurityTrails 历史 DNS 与资产",
        url = "https://securitytrails.com",
        fallbackText = "SecTr",
        badge = "历史DNS",
        badgeType = BadgeType.GOLD,
        desc = "查询 IP 的历史解析记录、子域名与逆向反查绑定的全部网站",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_96",
        title = "VirusTotal 网址与 IP 威胁分析",
        url = "https://www.virustotal.com",
        fallbackText = "VT",
        badge = "70+杀软",
        badgeType = BadgeType.ROSE,
        desc = "Google 旗下安全平台，聚合 70+ 款顶尖防病毒引擎交叉查杀 IP 与网址安全度",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_97",
        title = "ip.sb 极简纯净 IP 与路由查询",
        url = "https://ip.sb",
        fallbackText = "IPsb",
        badge = "极简",
        badgeType = BadgeType.NEW,
        desc = "极客最爱的极简出网 IP 查询：毫秒响应，提供清晰明了的运营商与经纬度",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_98",
        title = "IP138 传统国内高精 IP 查询",
        url = "https://www.ip138.com",
        fallbackText = "138",
        badge = "中文",
        badgeType = BadgeType.BLUE,
        desc = "国民级老牌 IP 查询站，快速识别国内三大运营商归属省份与城市行政区划",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_99",
        title = "Cloudflare 速度与边缘质量测试",
        url = "https://speed.cloudflare.com",
        fallbackText = "CFSpd",
        badge = "边缘",
        badgeType = BadgeType.GOLD,
        desc = "深入测试当前客户端到 Cloudflare 全球边缘节点的上传、下载、时延与丢包抖动",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    ),
    NavCard(
        id = "cloud_br_100",
        title = "IPify 极简纯文本公网 IP API",
        url = "https://api.ipify.org",
        fallbackText = "ipify",
        badge = "秒回",
        badgeType = BadgeType.ROSE,
        desc = "全球高可用公网 IP 诊断服务，零广告毫秒级返回当前设备真实公网出口 IP",
        categoryId = "cloud_browser",
        subcatId = "purity_check"
    )
)

/**
 * 云端浏览器与沙箱分类导航定义（新增大类，不删减原有任何大类）
 */
val cloudBrowserCategory: NavCategory = NavCategory(
    id = "cloud_browser",
    name = "云端浏览器与沙箱",
    iconKey = "globe",
    desc = "100款全球云端浏览器 · 美国原生地区 · 远程沙箱 · 指纹与无痕免配置",
    subcategories = listOf(
        NavSubCategory("all", "全部"),
        NavSubCategory("usa_cloud", "美国与免翻云端"),
        NavSubCategory("sandbox", "隔离沙箱与开发"),
        NavSubCategory("anti_detect", "指纹多开与住宅"),
        NavSubCategory("rbi_security", "企业隔离RBI"),
        NavSubCategory("vps_web", "网页虚拟机与远程"),
        NavSubCategory("purity_check", "纯净度与风控检测")
    ),
    cards = cloudBrowserCards
)
