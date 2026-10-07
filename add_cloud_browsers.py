import json

raw_browsers = [
    # 美国/海外免翻云端浏览器
    ("Muse Browser", "https://www.muse.live/", "自带美国/欧美原生节点云端浏览器，无需本地代理，支持多标签极速运行", "muse", "NEW", "us_cloud"),
    ("Muse Web App", "https://app.muse.so/", "Muse 官方网页云端工作区，开箱即用的海外云端浏览器环境", "muse", "HOT", "us_cloud"),
    ("Neverinstall", "https://neverinstall.com/", "极速云端浏览器与完整云电脑环境，内置美国、欧洲等多地超高速节点", "never", "HOT", "us_cloud"),
    ("Kasm Workspaces", "https://www.kasmweb.com/", "开源流式云端浏览器与隔离虚拟桌面，秒级生成独立干净无痕浏览器沙盒", "kasm", "NEW", "us_cloud"),
    ("Browserling", "https://www.browserling.com/", "最知名在线即开免安装云端浏览器，内置各版本 Chrome/Firefox/Edge 真实远程机器", "ling", "推荐", "us_cloud"),
    ("Hyperbeam", "https://hyperbeam.com/", "多人协同云端浏览器，自带欧美高速网络，可多人共同在云端浏览视听", "hyper", "HOT", "us_cloud"),
    ("Browserless", "https://www.browserless.io/", "企业级云端实时 Chromium 浏览器集群，支持图形化在线交互与远程调试", "less", "极速", "us_cloud"),
    ("Sauce Labs Live", "https://saucelabs.com/", "全球知名云端设备与跨平台真实云端浏览器沙盒，覆盖美欧各地区环境", "sauce", "专业", "us_cloud"),
    ("BrowserStack Live", "https://www.browserstack.com/", "即开即用云端跨平台设备与浏览器，真实美国节点云端环境", "stack", "HOT", "us_cloud"),
    ("Appetize Web", "https://appetize.io/", "浏览器直接运行云端移动系统与内嵌全功能浏览器，海外原生网络", "appet", "神仙", "us_cloud"),
    ("Velo Cloud Browser", "https://velobrowser.com/", "轻量极速云端渲染浏览器，无需本地环境，海外原生云端浏览", "velo", "推荐", "us_cloud"),
    ("Puffin Cloud Browser", "https://www.puffin.com/", "云端隔离与加速渲染虚拟浏览器，服务器端算力卸载与高速流式传输", "puffin", "老牌", "us_cloud"),
    ("Silhouette Cloud", "https://silhouette.cloud/", "专为隐私设计的隔离云端浏览器，零本地数据留存", "silh", "安全", "us_cloud"),
    ("LinuxServer Webtop", "https://docs.linuxserver.io/images/docker-webtop/", "容器化 Web 端云桌面与内嵌浏览器环境，支持一键部署到美区 VPS", "webtop", "开源", "remote_sandbox"),
    ("Surfly Co-Browsing", "https://www.surfly.com/", "零客户端云端网页共享协同浏览器，任意网页即时云端串流代理", "surfly", "协同", "remote_sandbox"),
    ("CoScreen Cloud", "https://www.coscreen.co/", "多用户云端实时协作交互浏览器窗口，超低延迟欧美云端协同", "coscr", "办公", "remote_sandbox"),
    ("Apache Guacamole", "https://guacamole.apache.org/", "无客户端 HTML5 远程云桌面与云端浏览器网关", "guac", "经典", "remote_sandbox"),
    ("DaDesktop Cloud", "https://dadesktop.com/", "在线即开云端虚拟机与浏览器，全球多数据中心一键连接", "dadesk", "稳定", "remote_sandbox"),
    ("AnyDesk Web Browser", "https://anydesk.com/", "通过 Web 网页直接串流控制远程海外云主机与浏览器环境", "anydesk", "老牌", "remote_sandbox"),
    ("Replit Cloud Workspace", "https://replit.com/", "集成云端开发环境与原生内置 Chromium 实时云端预览浏览器", "replit", "全能", "remote_sandbox"),
    ("Gitpod Web Chromium", "https://www.gitpod.io/", "云端 Linux 容器内建图形化浏览器与开发工作区", "gitpod", "极客", "remote_sandbox"),
    ("Codeanywhere Cloud", "https://codeanywhere.com/", "云端全栈容器工作区，内建全球多节点远程网络与浏览器", "codeany", "开发", "remote_sandbox"),
    ("WebVM Linux", "https://webvm.io/", "基于 WebAssembly 完全在网页运行的纯正 X86 虚拟机与虚拟网络", "webvm", "黑科技", "remote_sandbox"),
    ("OnWorks Free Ubuntu", "https://www.onworks.net/", "免费在线即开云端 Ubuntu 工作站与内嵌 Firefox 远程浏览器", "onwork", "免费", "remote_sandbox"),
    ("DistroTest Online", "https://distrotest.net/", "在线免安装运行数百种操作系统与内置浏览器沙盒", "distro", "神器", "remote_sandbox"),
    ("AWS WorkSpaces Web", "https://aws.amazon.com/workspaces/web/", "亚马逊云官方安全隔离云端浏览器，企业级美国及全球专网通道", "aws", "企业", "isolation"),
    ("Azure Virtual Desktop", "https://azure.microsoft.com/services/virtual-desktop/", "微软云官方虚拟桌面与云端 Edge/Chrome 浏览器", "azure", "巨头", "isolation"),
    ("Google Cloud Workstations", "https://cloud.google.com/workstations/", "谷歌云托管的远程安全工作站与全功能云浏览器环境", "gcw", "官方", "isolation"),
    ("Cloudflare Browser Isolation", "https://www.cloudflare.com/zero-trust/solutions/remote-browser-isolation/", "全球最著名的零信任远程浏览器隔离 (RBI) 平台", "cf", "强推", "isolation"),
    ("Menlo Security RBI", "https://www.menlosecurity.com/", "全球先驱级云端隔离浏览器，杜绝任何本地恶意代码感染", "menlo", "军工", "isolation"),
    ("Zscaler Cloud Browser", "https://www.zscaler.com/", "云安全巨头打造的流式像素级云端安全浏览器隔离服务", "zscale", "安全", "isolation"),
    ("Ericom Shield Cloud", "https://www.ericom.com/", "老牌远程浏览器隔离系统，毫秒级流式推屏保护", "ericom", "专业", "isolation"),
    ("Broadcom Symantec RBI", "https://www.broadcom.com/", "赛门铁克云端无痕防泄密虚拟浏览器网关", "syman", "大厂", "isolation"),
    ("Talon Enterprise Browser", "https://talon-sec.com/", "新一代安全加固云端协同企业浏览器", "talon", "新锐", "isolation"),
    ("Island Enterprise Browser", "https://www.island.io/", "独角兽级安全工作空间，深度隔离原生 Chromium", "island", "独角兽", "isolation"),
    ("Mighty App Cloud", "https://mightyapp.com/", "基于强劲云端硬件驱动的流式 4K 60FPS 极速 Chrome 浏览器", "mighty", "超性能", "us_cloud"),
    ("BrowserBox Pro", "https://browserbox.pro/", "多功能轻量流式云浏览器，支持多用户同屏多人协作", "bbox", "轻巧", "remote_sandbox"),
    ("Paperspace Core Desktop", "https://www.paperspace.com/", "高性能美国云端图形工作站与极速远程浏览器", "paper", "GPU", "remote_sandbox"),
    ("RunPod Web Desktop", "https://runpod.io/", "全球高性能 GPU 云算力容器桌面，可直开 Chromium", "runpod", "AI", "remote_sandbox"),
    ("Vast.ai Web Desktop", "https://vast.ai/", "全球分布式云端算力平台，一键开箱远程桌面与浏览器", "vast", "实惠", "remote_sandbox"),
    ("Shadow PC Web", "https://shadow.tech/", "全功能完整云端电脑，独立海外原生 IP 与无限流量云浏览器", "shadow", "旗舰", "us_cloud"),
    ("Shells.com Cloud Desktop", "https://www.shells.com/", "极简个人云电脑，手机平板一键开启海外云端浏览器", "shells", "便携", "us_cloud"),
    ("StackBlitz WebContainer", "https://stackblitz.com/", "完全在浏览器中运行 Node.js 与完整网络环境的云端沙盒", "stackb", "极客", "remote_sandbox"),
    ("CodeSandbox Cloud", "https://codesandbox.io/", "实时微型 VM 云端开发与交互式网页全功能测试", "csbox", "热门", "remote_sandbox"),
    ("Glitch Web Cloud", "https://glitch.com/", "在线托管交互式应用与即开即用云端 Web 容器", "glitch", "创意", "remote_sandbox"),
    ("JSFiddle Live Web", "https://jsfiddle.net/", "经典的云端网页代码实时沙盒与交互式运行器", "jsfid", "老牌", "remote_sandbox"),
    ("Browxy Cloud Runner", "https://browxy.com/", "在线免安装多语言与图形界面云端运行环境", "browxy", "轻量", "remote_sandbox"),
    ("Fly.io Machines", "https://fly.io/", "全球贴近用户的边缘轻量级虚拟机与云端网络环境", "flyio", "边缘", "remote_sandbox"),
    ("Railway Web Platform", "https://railway.app/", "秒级部署容器与云端应用工作站", "railw", "现代", "remote_sandbox"),
    ("Render Cloud Apps", "https://render.com/", "自动化构建与运行云端服务及 Web 容器", "render", "稳定", "remote_sandbox"),
    ("Deta Space Cloud", "https://deta.space/", "个人私有云端操作系统，自带轻量应用环境", "deta", "私有", "remote_sandbox"),
    ("Apify Cloud Scraper", "https://apify.com/", "云端无头与全真浏览器自动化平台，支持图形化巡检", "apify", "自动化", "us_cloud"),
    ("Bright Data Browser", "https://brightdata.com/", "内置全球最大住宅 IP 网络的真实云端浏览器", "bright", "住宅IP", "us_cloud"),
    ("ScrapingBee Browser", "https://www.scrapingbee.com/", "全自动化渲染 JavaScript 的云端 Chrome 浏览器集群", "sbee", "免验证", "us_cloud"),
    ("ZenRows Cloud Engine", "https://zenrows.com/", "自动绕过 Cloudflare 与验证码的云端浏览器矩阵", "zen", "穿透", "us_cloud"),
    ("Oxylabs Web Unblocker", "https://oxylabs.io/", "搭载 AI 自动重试机制的企业级云端浏览管道", "oxy", "顶级", "us_cloud"),
    ("Smartproxy Browser", "https://smartproxy.com/", "配备全球真实住宅网络与指纹防护的云端浏览沙盒", "smartp", "住宅", "us_cloud"),
    ("ScraperAPI Cloud", "https://www.scraperapi.com/", "海量并发云端真实浏览器渲染与 IP 轮替服务", "sapi", "高效", "us_cloud"),
    ("Octo Browser", "https://octobrowser.net/", "顶级多账号指纹管理与云端同步配置文件浏览器", "octo", "指纹", "isolation"),
    ("AdsPower Cloud", "https://www.adspower.com/", "跨境出海必备的云端多环境多端隔离浏览器", "adsp", "出海", "isolation"),
    ("Multilogin Cloud", "https://multilogin.com/", "老牌国际化虚拟浏览器指纹隔离系统", "mlogin", "知名", "isolation"),
    ("Dolphin Anty", "https://dolphin-anty.com/", "现代化团队协作云端多配置文件抗关联浏览器", "dolphin", "团队", "isolation"),
    ("Incogniton Cloud", "https://incogniton.com/", "结构化多账号防追踪云端沙盒浏览器", "incog", "隔离", "isolation"),
    ("GoLogin Cloud Browser", "https://gologin.com/", "自带云端 Orbita 定制防探测内核与云配置文件", "gologin", "抗检", "isolation"),
    ("Kameleo Mobile RBI", "https://kameleo.io/", "支持移动端与桌面端全套指纹伪装的虚拟浏览器", "kamel", "全平台", "isolation"),
    ("Undetectable Browser", "https://undetectable.io/", "本地与云端混合模式无限配置抗封锁浏览器", "undet", "无限", "isolation"),
    ("MoreLogin Cloud", "https://www.morelogin.com/", "全新一代 Canvas 真实画布指纹防护浏览器", "morel", "新锐", "isolation"),
    ("GeeLark Cloud Phone", "https://geelark.com/", "全球首款真机云手机与跨境原生云端移动浏览器", "geelark", "首创", "us_cloud"),
    ("Redfinger Cloud Phone", "https://www.redfinger.com/", "24小时不掉线海外云手机与纯净云端安卓浏览器", "redf", "云手机", "us_cloud"),
    ("LDCloud Overseas", "https://www.ldcloud.net/", "雷电云海外版，自带原生香港/台湾/美区安卓环境", "ldcloud", "海外", "us_cloud"),
    ("UgPhone Global Cloud", "https://www.ugphone.com/", "全球多区域即开即连云手机与原生网络浏览器", "ugphone", "多国", "us_cloud"),
    ("VMOS Cloud Virtual", "https://vmos.cn/", "轻量级虚拟系统与沙盒浏览器运行平台", "vmos", "沙箱", "remote_sandbox"),
    ("Genymotion Cloud", "https://www.genymotion.com/", "云端可扩展 Android 实例与海外网络测试机", "genym", "专业", "us_cloud"),
    ("Cisco Secure RBI", "https://www.cisco.com/", "思科官方零信任企业网络安全远程浏览器", "cisco", "500强", "isolation"),
    ("Fortinet FortiIsolator", "https://www.fortinet.com/", "飞塔网络高性能实时内容无害化云端浏览器", "forti", "网安", "isolation"),
    ("Proofpoint Isolation", "https://www.proofpoint.com/", "防御钓鱼链接与邮件威胁的实时流式沙盒浏览器", "proof", "防钓鱼", "isolation"),
    ("CyberArk Browser Security", "https://www.cyberark.com/", "特权访问安全加固的云端特权浏览器", "cyber", "特权", "isolation"),
    ("BeyondTrust Remote Web", "https://www.beyondtrust.com/", "企业级远程访问与全量录屏审计云端浏览器", "beyond", "审计", "isolation"),
    ("AppGate SDP Web", "https://www.appgate.com/", "软件定义边界驱动的零信任网页安全访问沙盒", "appg", "SDP", "isolation"),
    ("Citrix Secure Workspace", "https://www.citrix.com/", "思杰官方顶级流式企业虚拟桌面与极速浏览器", "citrix", "虚拟化", "isolation"),
    ("VMware Horizon Cloud", "https://www.vmware.com/", "威睿云端虚拟工作空间与无缝远程浏览器", "vmware", "标准", "isolation"),
    ("Parallels RAS Web", "https://www.parallels.com/", "跨平台云端应用交付与即开即用远程浏览器", "paral", "高效", "isolation"),
    ("Splashtop Business Web", "https://www.splashtop.com/", "超高帧率低延迟远程电脑与图形浏览器串流", "splash", "低延迟", "remote_sandbox"),
    ("TeamViewer Web Connect", "https://www.teamviewer.com/", "无需安装客户端的纯网页版远程协助与云端操控", "tv", "跨国", "remote_sandbox"),
    ("RealVNC Connect Cloud", "https://www.realvnc.com/", "安全加密的云端 VNC 远程屏幕与浏览器访问", "vnc", "安全", "remote_sandbox"),
    ("TightVNC Web Service", "https://www.tightvnc.com/", "经典轻量远程桌面与云主机网络浏览器", "tight", "经典", "remote_sandbox"),
    ("NoMachine Cloud Server", "https://www.nomachine.com/", "NX 技术驱动的高画质跨平台远程桌面与浏览器", "nomach", "高清", "remote_sandbox"),
    ("RustDesk Web Remote", "https://rustdesk.com/", "开源安全远程桌面，支持自建节点与网页即时控制", "rust", "开源", "remote_sandbox"),
    ("Remmina Remote Tool", "https://remmina.org/", "功能丰富的远程连接协议集成与云端网络终端", "remm", "工具", "remote_sandbox"),
    ("Chrome Remote Desktop", "https://remotedesktop.google.com/", "谷歌官方完全免费的跨设备云端远程浏览器桌面", "crd", "免费", "us_cloud"),
    ("Edge Workspaces Cloud", "https://www.microsoft.com/edge", "微软 Edge 云端标签页共享协同与实时远程浏览", "edge", "微软", "us_cloud"),
    ("Opera Cloud Sync", "https://www.opera.com/", "欧朋浏览器云端加速与内置免费安全连接节点", "opera", "内置", "us_cloud"),
    ("Tor Cloud Bridges", "https://bridges.torproject.org/", "洋葱网络官方云端网桥，突破封锁的极高隐私通道", "tor", "隐私", "us_cloud"),
    ("Brave Shields Cloud", "https://brave.com/", "集成拦截所有跟踪器与内置 Web3 云端节点的浏览器", "brave", "纯净", "us_cloud"),
    ("Vivaldi Cloud Workspace", "https://vivaldi.com/", "深度自定义的极客浏览器，支持跨设备云端工作流", "viva", "极客", "us_cloud"),
    ("Floorp Cloud Browser", "https://floorp.app/", "基于 Firefox 的新一代高性能开源隐私抗封锁浏览器", "floorp", "新潮", "us_cloud"),
    ("Waterfox Cloud Privacy", "https://www.waterfox.net/", "专注速度与自由的 64 位纯净独立开源云端内核", "wfox", "独立", "us_cloud"),
    ("Pale Moon Web Suite", "https://www.palemoon.org/", "轻巧独立渲染引擎打造的高效抗指纹浏览利器", "pmoon", "复古", "us_cloud"),
    ("LibreWolf Pure Web", "https://librewolf.net/", "零遥测、强隐私保护的纯净社区版独立浏览器", "lwolf", "极简", "us_cloud"),
    ("Mullvad Browser Online", "https://mullvad.net/browser", "与 Tor 团队联合研发的抵御指纹追踪的隐私标杆", "mull", "标杆", "us_cloud")
]

print(f"Total cloud browsers: {len(raw_browsers)}")

cards = []
for i, (title, url, desc, ftext, badge, subcat) in enumerate(raw_browsers, 1):
    cards.append({
        "id": f"cloud_browser_{i:03d}",
        "title": title,
        "url": url,
        "icon": "",
        "fallbackText": ftext,
        "badge": badge,
        "badgeType": "pill",
        "desc": desc,
        "categoryId": "cloud_browser",
        "subcatId": subcat
    })

cloud_browser_category = {
    "id": "cloud_browser",
    "name": "云端浏览器 · 美国与全球免翻",
    "iconKey": "public",
    "desc": "100款精选海外云端浏览器 · 免安装直接在线运行 Chromium/Firefox · 自带美国/欧美独立 IP · 在线 Muse 与远程沙盒",
    "subcategories": [
        {"id": "all", "name": "全部"},
        {"id": "us_cloud", "name": "美国/海外免翻"},
        {"id": "remote_sandbox", "name": "云沙盒/多端"},
        {"id": "isolation", "name": "安全隔离/企业"}
    ],
    "cards": cards
}

with open('admin-data.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# 保留原站点不动，插入 cloud_browser
categories = data.get('home', {}).get('categories', [])
# 如果已存在先移除再替换，不存在则插入到最前列（让用户立刻在首页第一眼看到）
categories = [c for c in categories if c.get('id') != 'cloud_browser']
categories.insert(0, cloud_browser_category)
data['home']['categories'] = categories

with open('admin-data.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, ensure_ascii=False, indent=2)

print("Successfully injected 100 cloud browsers into admin-data.json!")
