package com.example.ui.uiverse

object UiverseRepository {

    // v1.0.19 软件主题升级：原有主题风格已全部移除，仅保留「盛世华诞 · 国庆可爱」主题
    val uiKits = listOf(
        UiKitPreset.DEFAULT_CLASSIC
    )

    val items = listOf(
        // Style 1: Tilt Magnetic Card
        UiverseItem(
            id = "style_1_tilt_card",
            name = "款式1：磁吸跟随卡 (3D旋转流光)",
            author = "Uiverse Style 1",
            category = UiverseCategory.CARDS,
            description = "磁吸卡片跟随旋转、conic-gradient 炫彩外发光环绕、磨砂透光玻璃面板",
            cssCode = """
                /* 磁吸卡：鼠标跟随 3D 旋转 */
                .tilt-wrap{perspective:1000px}
                .tilt-card{width:240px;height:160px;position:relative;border-radius:18px;transform-style:preserve-3d;transition:transform .15s ease-out}
                .tc-glow{position:absolute;inset:-2px;border-radius:20px;background:conic-gradient(#6c63ff,#ff2d78,#00e5ff,#6c63ff);filter:blur(13px);opacity:.4;animation:tSpin 5s linear infinite}
                @keyframes tSpin{to{transform:rotate(360deg)}}
                .tc-face{position:absolute;inset:2px;border-radius:16px;background:linear-gradient(135deg,rgba(255,255,255,.16),rgba(255,255,255,.05));backdrop-filter:blur(14px);border:1px solid rgba(255,255,255,.3);box-shadow:0 20px 50px rgba(0,0,0,.45),inset 0 1px 0 rgba(255,255,255,.4);display:flex;flex-direction:column;align-items:center;justify-content:center;color:#fff;transform:translateZ(36px)}
            """.trimIndent(),
            htmlCode = """
                <div class="tilt-wrap">
                  <div class="tilt-card" data-tilt>
                    <div class="tc-glow"></div>
                    <div class="tc-face">
                      <span class="ic">✦</span>
                      <h4>磁吸跟随卡</h4>
                      <p>鼠标移动 · 3D 跟随</p>
                    </div>
                  </div>
                </div>
            """.trimIndent(),
            associatedKit = UiKitPreset.STYLE_1_TILT_MAGNETIC,
            badge = "款式1"
        ),

        // Style 2: Glass Loader & Glass Button + Input
        UiverseItem(
            id = "style_2_glass_loader",
            name = "款式2：双色旋转环 + 呼吸核心 + 弹跳玻璃珠",
            author = "Uiverse Style 2",
            category = UiverseCategory.LOADERS,
            description = "双色旋转环、呼吸核心光球、5连弹跳玻璃珠、流光渐变玻璃按钮与发光输入框",
            cssCode = """
                /* 加载器：双色旋转环 + 呼吸核心 + 弹跳玻璃珠 */
                .glass-ring{width:66px;height:66px;position:relative}
                .glass-ring .gr-track{position:absolute;inset:0;border-radius:50%;border:4px solid rgba(255,255,255,.12)}
                .glass-ring .gr-arc{position:absolute;inset:0;border-radius:50%;border:4px solid transparent;border-top-color:#8b84ff;border-right-color:#ff2d78;animation:grSpin 1.2s linear infinite;filter:drop-shadow(0 0 6px rgba(139,132,255,.6))}
                .glass-ring .gr-core{position:absolute;inset:18px;border-radius:50%;background:radial-gradient(circle at 35% 30%,rgba(255,255,255,.5),rgba(108,99,255,.3));animation:grBreathe 1.6s ease-in-out infinite}
                @keyframes grSpin{to{transform:rotate(360deg)}}
                @keyframes grBreathe{0%,100%{transform:scale(1);opacity:.7}50%{transform:scale(1.25);opacity:1}}
                .glass-pill{display:flex;gap:7px}
                .glass-pill i{width:13px;height:13px;border-radius:50%;background:radial-gradient(circle at 35% 30%,rgba(255,255,255,.9),rgba(108,99,255,.5));animation:gpBounce 1.2s ease-in-out infinite}
                .glass-pill i:nth-child(2){animation-delay:.15s}.glass-pill i:nth-child(3){animation-delay:.3s}
                .glass-pill i:nth-child(4){animation-delay:.45s}.glass-pill i:nth-child(5){animation-delay:.6s}
                @keyframes gpBounce{0%,100%{transform:translateY(0) scale(1)}50%{transform:translateY(-13px) scale(.9)}}
                /* 玻璃按钮 + 输入框 */
                .glass-btn{padding:14px 44px;border:none;border-radius:13px;cursor:pointer;background:linear-gradient(135deg,#6c63ff,#ff2d78);color:#fff;font-size:15px;font-weight:800;font-family:inherit;letter-spacing:2px;box-shadow:0 10px 26px rgba(108,99,255,.4);transition:all .25s}
                .glass-btn:hover{transform:translateY(-2px);box-shadow:0 14px 34px rgba(255,45,120,.5)}
                .glass-btn:active{transform:scale(.95)}
                .glass-input{width:min(280px,90%);padding:13px 16px;border-radius:12px;outline:none;background:rgba(255,255,255,.12);border:1.5px solid rgba(255,255,255,.25);color:#fff;font-size:14px;font-family:inherit;transition:all .3s}
                .glass-input:focus{border-color:#8b84ff;box-shadow:0 0 0 4px rgba(108,99,255,.2),0 0 20px rgba(108,99,255,.3)}
            """.trimIndent(),
            htmlCode = """
                <div class="glass-ring">
                  <div class="gr-track"></div>
                  <div class="gr-arc"></div>
                  <div class="gr-core"></div>
                </div>
                <div class="glass-pill"><i></i><i></i><i></i><i></i><i></i></div>
                <input class="glass-input" placeholder="请输入内容…">
                <button class="glass-btn">提交</button>
            """.trimIndent(),
            associatedKit = UiKitPreset.STYLE_2_GLASS_LOADER,
            badge = "款式2"
        ),

        // Style 3: Neo-Brutalist Thick Buttons
        UiverseItem(
            id = "style_3_buttons",
            name = "款式3：新野蛮立体厚边阴影按钮",
            author = "Uiverse Style 3",
            category = UiverseCategory.BUTTONS,
            description = "4px深蓝实心描边、.35em立体硬投影位移、立即下载/开始使用/了解更多经典样式",
            cssCode = """
                /* 款式3：新野蛮立体厚边阴影按钮 */
                .u-btn {
                  --bgc: var(--c2); --bor: var(--bg); --sh: var(--bg);
                  background: var(--bgc); color: #fff; border: 4px solid var(--bor);
                  border-radius: 14px; font-weight: 800; font-size: 15px;
                  padding: .9em 1.6em; cursor: pointer;
                  box-shadow: .35em .35em 0 var(--sh); transition: all .18s ease;
                }
                .u-btn:hover { transform: translate(-3px,-3px); box-shadow: .55em .55em 0 var(--sh); }
                .u-btn:active { transform: translate(.25em,.25em); box-shadow: 0 0 0 var(--sh); }
                .u-btn.alt { --bgc: var(--c1); --bor: #0a3d63; --sh: #0a3d63; color: #0a3d63; }
                .u-btn.ghost { --bgc: transparent; --bor: var(--bg); --sh: var(--c2); color: var(--bg); }
            """.trimIndent(),
            htmlCode = """
                <button class="u-btn">立即下载</button>
                <button class="u-btn alt">开始使用</button>
                <button class="u-btn ghost">了解更多</button>
            """.trimIndent(),
            associatedKit = UiKitPreset.STYLE_3_THICK_BUTTON,
            badge = "款式3"
        ),

        // Style 4: Bold Bottombar & Appbar
        UiverseItem(
            id = "style_4_bars",
            name = "款式4：极客厚边导航栏与顶栏",
            author = "Uiverse Style 4",
            category = UiverseCategory.UI_KITS,
            description = "4px描边圆角底部导航栏、内嵌-6px高光指示条、立体描边顶栏与浮雕字效",
            cssCode = """
                /* 款式4：极客厚边导航栏与顶栏 */
                .u-bottombar {
                  display: flex; background: #fff; border: 4px solid #0a3d63;
                  border-radius: 18px; overflow: hidden; box-shadow: .4em .4em 0 var(--bg);
                }
                .u-bottombar button {
                  flex: 1; border: none; background: transparent; padding: 14px 6px 12px; cursor: pointer;
                  font-weight: 800; font-size: 11.5px; color: #7a8ca0;
                  display: flex; flex-direction: column; gap: 5px; align-items: center; border-right: 3px solid #e6eef5;
                }
                .u-bottombar button.on { background: var(--bg); color: #fff; box-shadow: inset 0 -6px 0 var(--c2); }
                .u-appbar {
                  background: var(--bg); border: 4px solid var(--c1); border-bottom-width: 6px;
                  border-radius: 14px; padding: 14px 16px; box-shadow: .35em .35em 0 var(--c2);
                  display: flex; align-items: center; gap: 12px; color: #fff;
                }
                .u-appbar .t { font-weight: 900; font-size: 16px; flex: 1; text-shadow: .05em .07em 0 var(--c2); }
            """.trimIndent(),
            htmlCode = """
                <div class="u-bottombar">
                  <button class="on"><span class="em">⌂</span>首页</button>
                  <button><span class="em">▦</span>组件</button>
                  <button><span class="em">♥</span>收藏</button>
                  <button><span class="em">☰</span>我的</button>
                </div>
            """.trimIndent(),
            associatedKit = UiKitPreset.STYLE_4_BOTTOMBAR_APPBAR,
            badge = "款式4"
        ),

        // Style 5: Capsule Setting Row & Switch
        UiverseItem(
            id = "style_5_setrow",
            name = "款式5：厚边设置行与弹跳开关",
            author = "Uiverse Style 5",
            category = UiverseCategory.TOGGLE_SWITCHES,
            description = "4px描边圆角设置行卡片、橙黄.3em立体投影、弹性触感开关与醒目箭头",
            cssCode = """
                /* 款式5：厚边设置行与弹跳开关 */
                .u-setrow {
                  display: flex; align-items: center; gap: 12px; background: #fff;
                  border: 4px solid #0a3d63; border-radius: 14px; padding: 12px 14px;
                  box-shadow: .3em .3em 0 var(--c1); width: min(300px, 100%);
                }
                .u-setrow .t { flex: 1; font-weight: 800; color: #26303c; font-size: 14px; }
                .u-setrow .chev { color: #9aa8b6; font-weight: 900; font-size: 18px; }
            """.trimIndent(),
            htmlCode = """
                <div class="u-setrow"><span class="t">深色模式</span><label class="u-switch"><input type="checkbox" checked/><span class="track"></span></label></div>
                <div class="u-setrow"><span class="t">消息通知</span><span class="chev">›</span></div>
            """.trimIndent(),
            associatedKit = UiKitPreset.STYLE_5_CAPSULE_SETROW,
            badge = "款式5"
        ),

        // UI Kits Category Items
        UiverseItem(
            id = "kit_cyberpunk",
            name = "Cyberpunk 2077 HUD Full Kit",
            author = "Galahhad",
            category = UiverseCategory.UI_KITS,
            description = "赛博霓虹全套组件：HUD切角卡片、激光流光按钮、终端输入框与科技网格",
            cssCode = """
                /* Cyberpunk 2077 Theme */
                :root {
                  --primary: #00f0ff;
                  --secondary: #ff003c;
                  --bg: #05050a;
                  --surface: #0f101a;
                  --border: 1.5px solid #00f0ff;
                  --glow: 0 0 15px rgba(0, 240, 255, 0.4);
                  --clip: polygon(0 0, calc(100% - 15px) 0, 100% 15px, 100% 100%, 15px 100%, 0 calc(100% - 15px));
                }
            """.trimIndent(),
            htmlCode = """<div class="cyber-kit-wrapper"><button class="cyber-btn">CYBER_SYS</button></div>""",
            associatedKit = UiKitPreset.CYBERPUNK_NEON,
            badge = "POPULAR"
        ),
        UiverseItem(
            id = "kit_glass",
            name = "Frosted Aurora Glassmorphism Kit",
            author = "mrhyddenn",
            category = UiverseCategory.UI_KITS,
            description = "极光磨砂玻璃套件：微透明背景模糊、彩虹渐变细边框、流光悬浮阴影",
            cssCode = """
                /* Glassmorphism Aurora Theme */
                :root {
                  --bg: #0f172a;
                  --surface: rgba(255, 255, 255, 0.08);
                  --border: 1px solid rgba(255, 255, 255, 0.2);
                  --backdrop: blur(12px);
                  --radius: 20px;
                  --box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
                }
            """.trimIndent(),
            htmlCode = """<div class="glass-card"><span class="glass-badge">Aurora</span></div>""",
            associatedKit = UiKitPreset.GLASSMORPHISM_AURORA,
            badge = "TRENDING"
        ),
        UiverseItem(
            id = "kit_neumorphic",
            name = "Soft Neumorphic Clay Claymorphism",
            author = "alexmaracinaru",
            category = UiverseCategory.UI_KITS,
            description = "新拟物微浮雕套件：双向柔和光影阴影、高质感陶土微凸、触觉反馈按压",
            cssCode = """
                /* Neumorphic Clay Theme */
                :root {
                  --bg: #e2e8f0;
                  --surface: #e2e8f0;
                  --shadow-light: -6px -6px 14px #ffffff;
                  --shadow-dark: 6px 6px 14px #cbd5e1;
                  --radius: 24px;
                }
            """.trimIndent(),
            htmlCode = """<div class="neumorphic-panel"><button class="neu-btn">Press</button></div>""",
            associatedKit = UiKitPreset.NEUMORPHISM_CLAY
        ),
        UiverseItem(
            id = "kit_brutalism",
            name = "Neo-Brutalism Pop High-Contrast",
            author = "andrew-demchenk0",
            category = UiverseCategory.UI_KITS,
            description = "新野蛮主义套件：纯黑3px实线描边、4px硬投影、高饱和亮黄鲜明对比",
            cssCode = """
                /* Neo-Brutalism Pop */
                :root {
                  --border: 3px solid #000000;
                  --shadow: 4px 4px 0px #000000;
                  --accent: #ffe600;
                  --bg: #fffdf0;
                  --radius: 8px;
                }
            """.trimIndent(),
            htmlCode = """<div class="brutal-box"><button class="brutal-btn">CLICK ME</button></div>""",
            associatedKit = UiKitPreset.NEO_BRUTALISM_POP,
            badge = "HOT"
        ),
        UiverseItem(
            id = "kit_retro",
            name = "8-Bit Arcade Pixel Nostalgia",
            author = "Pradeepsahu",
            category = UiverseCategory.UI_KITS,
            description = "8-Bit复古街机套件：阶梯像素切边、CRT显示屏复古横纹、投币街机按键",
            cssCode = """
                /* Retro 8-Bit Pixel */
                :root {
                  --border: 2px solid #e94560;
                  --bg: #1a1a2e;
                  --surface: #16213e;
                  --pixel-shadow: 3px 3px 0px #000;
                  --accent: #ffcc00;
                }
            """.trimIndent(),
            htmlCode = """<div class="pixel-frame"><button class="pixel-btn">START</button></div>""",
            associatedKit = UiKitPreset.RETRO_8BIT_ARCADE
        ),
        UiverseItem(
            id = "kit_holographic",
            name = "Prismatic Hologram Crystal",
            author = "NelsonTheDeveloper",
            category = UiverseCategory.UI_KITS,
            description = "全息棱镜水晶套件：光谱流动折射膜、流动渐变晶体倒影",
            cssCode = """
                /* Holographic Prism */
                :root {
                  --gradient: linear-gradient(135deg, #11998e, #38ef7d, #6a11cb);
                  --surface: rgba(255, 255, 255, 0.05);
                  --border: 1px solid rgba(255, 255, 255, 0.3);
                  --radius: 16px;
                }
            """.trimIndent(),
            htmlCode = """<div class="holo-card"><div class="prism-shine"></div></div>""",
            associatedKit = UiKitPreset.HOLOGRAPHIC_PRISM
        ),
        UiverseItem(
            id = "kit_luxury",
            name = "Obsidian Black & Royal Gold",
            author = "vinodjangid07",
            category = UiverseCategory.UI_KITS,
            description = "黑曜石金套件：哑光超深黑沉稳基底、香槟拉丝金属金色描边与微光",
            cssCode = """
                /* Luxury Obsidian Gold */
                :root {
                  --bg: #0d0d0d;
                  --surface: #171717;
                  --border: 1.5px solid #d4af37;
                  --gold-glow: 0 4px 20px rgba(212, 175, 55, 0.25);
                }
            """.trimIndent(),
            htmlCode = """<div class="gold-card"><span class="gold-text">LUXURY</span></div>""",
            associatedKit = UiKitPreset.LUXURY_OBSIDIAN_GOLD
        ),
        UiverseItem(
            id = "kit_classic",
            name = "Default Classic Heritage Red",
            author = "Official",
            category = UiverseCategory.UI_KITS,
            description = "系统原生纯净经典风格：红金高定温润，圆角舒适，阴影轻盈",
            cssCode = """
                /* Default Classic */
                :root {
                  --primary: #d32f2f;
                  --secondary: #ffd700;
                  --bg: #ffebeb;
                  --surface: #ffffff;
                }
            """.trimIndent(),
            htmlCode = """<div class="classic-card">默认经典原生UI</div>""",
            associatedKit = UiKitPreset.DEFAULT_CLASSIC
        ),

        // Buttons Category
        UiverseItem(
            id = "btn_cyber",
            name = "Cyberpunk Neon Glow Button",
            author = "Galahhad",
            category = UiverseCategory.BUTTONS,
            description = "赛博朋克高亮发光按钮，边缘配备扫描光束与科技斜切角",
            cssCode = """
                .cyber-btn {
                  background: #0f101a;
                  border: 1.5px solid #00f0ff;
                  color: #00f0ff;
                  padding: 10px 24px;
                  border-radius: 8px;
                  box-shadow: 0 0 15px rgba(0, 240, 255, 0.4);
                  text-transform: uppercase;
                  letter-spacing: 2px;
                }
            """.trimIndent(),
            htmlCode = """<button class="cyber-btn"><span>ENTER TERMINAL</span></button>""",
            associatedKit = UiKitPreset.CYBERPUNK_NEON
        ),
        UiverseItem(
            id = "btn_glass",
            name = "Glassmorphism Aurora Pill Button",
            author = "mrhyddenn",
            category = UiverseCategory.BUTTONS,
            description = "极光磨砂玻璃胶囊按钮，轻柔微光质感与半透明微边框",
            cssCode = """
                .glass-btn {
                  background: rgba(255, 255, 255, 0.12);
                  border: 1px solid rgba(255, 255, 255, 0.3);
                  backdrop-filter: blur(10px);
                  color: #ffffff;
                  border-radius: 50px;
                  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                }
            """.trimIndent(),
            htmlCode = """<button class="glass-btn">Explore</button>""",
            associatedKit = UiKitPreset.GLASSMORPHISM_AURORA
        ),
        UiverseItem(
            id = "btn_neumorphic",
            name = "Neumorphic 3D Press Button",
            author = "alexmaracinaru",
            category = UiverseCategory.BUTTONS,
            description = "新拟物微浮雕按压按钮，双向凸起与轻度触感回弹反馈",
            cssCode = """
                .neu-btn {
                  background: #e2e8f0;
                  border-radius: 16px;
                  box-shadow: 6px 6px 12px #cbd5e1, -6px -6px 12px #ffffff;
                  color: #1e293b;
                  font-weight: 600;
                }
            """.trimIndent(),
            htmlCode = """<button class="neu-btn">Action</button>""",
            associatedKit = UiKitPreset.NEUMORPHISM_CLAY
        ),
        UiverseItem(
            id = "btn_brutalism",
            name = "Neo-Brutalism Solid Shadow Button",
            author = "andrew-demchenk0",
            category = UiverseCategory.BUTTONS,
            description = "粗实黑边框与4px硬投影野蛮主义高质感按钮",
            cssCode = """
                .brutal-btn {
                  background: #ffe600;
                  border: 3px solid #000;
                  border-radius: 6px;
                  box-shadow: 4px 4px 0px #000;
                  color: #000;
                  font-weight: 900;
                }
            """.trimIndent(),
            htmlCode = """<button class="brutal-btn">BOOM!</button>""",
            associatedKit = UiKitPreset.NEO_BRUTALISM_POP
        ),
        UiverseItem(
            id = "btn_retro",
            name = "8-Bit Pixel Coin Button",
            author = "Pradeepsahu",
            category = UiverseCategory.BUTTONS,
            description = "复古像素阶梯按键，街机投币金色微光质感",
            cssCode = """
                .pixel-btn {
                  background: #ffcc00;
                  border: 2px solid #000;
                  border-radius: 4px;
                  box-shadow: 3px 3px 0px #000;
                  color: #000;
                }
            """.trimIndent(),
            htmlCode = """<button class="pixel-btn">INSERT COIN</button>""",
            associatedKit = UiKitPreset.RETRO_8BIT_ARCADE
        ),

        // Cards Category
        UiverseItem(
            id = "card_cyber",
            name = "Cyberpunk Tech Card",
            author = "Galahhad",
            category = UiverseCategory.CARDS,
            description = "赛博朋克深空暗黑微光卡片，带科技感切角与青蓝霓虹描边",
            cssCode = """
                .cyber-card {
                  background: #0f101a;
                  border: 1.5px solid #00f0ff;
                  border-radius: 12px;
                  box-shadow: 0 0 16px rgba(0, 240, 255, 0.35);
                  color: #00f0ff;
                }
            """.trimIndent(),
            htmlCode = """<div class="cyber-card"><h3>CYBER DATA</h3></div>""",
            associatedKit = UiKitPreset.CYBERPUNK_NEON
        ),
        UiverseItem(
            id = "card_glass",
            name = "Frosted Aurora Glass Card",
            author = "mrhyddenn",
            category = UiverseCategory.CARDS,
            description = "极光流光磨砂半透明卡片，悬浮透光与精致微边框",
            cssCode = """
                .glass-card {
                  background: rgba(255, 255, 255, 0.08);
                  border: 1px solid rgba(255, 255, 255, 0.25);
                  backdrop-filter: blur(12px);
                  border-radius: 16px;
                  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.3);
                }
            """.trimIndent(),
            htmlCode = """<div class="glass-card"><h3>AURORA GLOW</h3></div>""",
            associatedKit = UiKitPreset.GLASSMORPHISM_AURORA
        ),
        UiverseItem(
            id = "card_brutalism",
            name = "Neo-Brutalism Solid Card",
            author = "andrew-demchenk0",
            category = UiverseCategory.CARDS,
            description = "新野蛮主义3px粗黑线纯色卡片，高饱和背景与硬边阴影",
            cssCode = """
                .brutal-card {
                  background: #ffffff;
                  border: 3px solid #000;
                  border-radius: 8px;
                  box-shadow: 4px 4px 0px #000;
                  color: #000;
                }
            """.trimIndent(),
            htmlCode = """<div class="brutal-card"><h3>BRUTAL CONTENT</h3></div>""",
            associatedKit = UiKitPreset.NEO_BRUTALISM_POP
        ),
        UiverseItem(
            id = "card_neumorphic",
            name = "Neumorphic Embossed Clay Card",
            author = "alexmaracinaru",
            category = UiverseCategory.CARDS,
            description = "新拟物微浮雕陶土卡片，柔和双向凹凸立体阴影",
            cssCode = """
                .neu-card {
                  background: #e2e8f0;
                  border-radius: 16px;
                  box-shadow: 6px 6px 14px #cbd5e1, -6px -6px 14px #ffffff;
                  color: #1e293b;
                }
            """.trimIndent(),
            htmlCode = """<div class="neu-card"><h3>CLAY SOFT</h3></div>""",
            associatedKit = UiKitPreset.NEUMORPHISM_CLAY
        ),

        // Loaders Category
        UiverseItem(
            id = "loader_glitch",
            name = "Cyberpunk Glitch Radar",
            author = "Galahhad",
            category = UiverseCategory.LOADERS,
            description = "赛博朋克扫描雷达与霓虹脉冲波",
            cssCode = """
                .cyber-loader {
                  width: 48px;
                  height: 48px;
                  border: 2px solid #00f0ff;
                  border-radius: 50%;
                  box-shadow: 0 0 15px #00f0ff;
                  animation: pulse 1.5s infinite;
                }
            """.trimIndent(),
            htmlCode = """<div class="cyber-loader"></div>""",
            associatedKit = UiKitPreset.CYBERPUNK_NEON
        ),
        UiverseItem(
            id = "loader_orbit",
            name = "Orbiting Planetary Dual Rings",
            author = "mrhyddenn",
            category = UiverseCategory.LOADERS,
            description = "双环行星环绕动态极光流光加载器",
            cssCode = """
                .orbit-loader {
                  border: 3px solid transparent;
                  border-top-color: #6366f1;
                  border-bottom-color: #ec4899;
                  border-radius: 50%;
                  animation: spin 1.2s linear infinite;
                }
            """.trimIndent(),
            htmlCode = """<div class="orbit-loader"></div>""",
            associatedKit = UiKitPreset.GLASSMORPHISM_AURORA
        ),

        // Inputs Category
        UiverseItem(
            id = "input_cyber",
            name = "Cyber Terminal Prompt Input",
            author = "Galahhad",
            category = UiverseCategory.INPUTS,
            description = "赛博终端风格搜索输入框，激光指示标与高科技外框",
            cssCode = """
                .cyber-input {
                  background: #0f101a;
                  border: 1.5px solid #00f0ff;
                  border-radius: 8px;
                  color: #00f0ff;
                  box-shadow: 0 0 10px rgba(0, 240, 255, 0.25);
                }
            """.trimIndent(),
            htmlCode = """<input class="cyber-input" placeholder=">> SCAN TERMINAL" />""",
            associatedKit = UiKitPreset.CYBERPUNK_NEON
        ),
        UiverseItem(
            id = "input_glass",
            name = "Glassmorphic Frosted Search Input",
            author = "mrhyddenn",
            category = UiverseCategory.INPUTS,
            description = "通透极光磨砂半透明输入框，微光流转",
            cssCode = """
                .glass-input {
                  background: rgba(255, 255, 255, 0.1);
                  border: 1px solid rgba(255, 255, 255, 0.25);
                  backdrop-filter: blur(8px);
                  border-radius: 20px;
                  color: #ffffff;
                }
            """.trimIndent(),
            htmlCode = """<input class="glass-input" placeholder="Search universe..." />""",
            associatedKit = UiKitPreset.GLASSMORPHISM_AURORA
        ),
        UiverseItem(
            id = "input_brutal",
            name = "Neo-Brutalist Bold Input",
            author = "andrew-demchenk0",
            category = UiverseCategory.INPUTS,
            description = "3px粗黑实线立体投影搜索框",
            cssCode = """
                .brutal-input {
                  background: #ffffff;
                  border: 3px solid #000000;
                  border-radius: 6px;
                  box-shadow: 3px 3px 0px #000000;
                  color: #000000;
                }
            """.trimIndent(),
            htmlCode = """<input class="brutal-input" placeholder="Type here..." />""",
            associatedKit = UiKitPreset.NEO_BRUTALISM_POP
        ),

        // Toggle switches Category
        UiverseItem(
            id = "switch_glass",
            name = "iOS Glass Morph Switch",
            author = "mrhyddenn",
            category = UiverseCategory.TOGGLE_SWITCHES,
            description = "通透果冻微流体磨砂玻璃切换开关",
            cssCode = """
                .glass-switch {
                  background: rgba(99, 102, 241, 0.4);
                  border: 1px solid rgba(255, 255, 255, 0.3);
                  border-radius: 30px;
                  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
                }
            """.trimIndent(),
            htmlCode = """<label class="glass-switch"><input type="checkbox"><span class="slider"></span></label>""",
            associatedKit = UiKitPreset.GLASSMORPHISM_AURORA
        ),
        UiverseItem(
            id = "switch_cyber",
            name = "Cyber Laser Toggle Switch",
            author = "Galahhad",
            category = UiverseCategory.TOGGLE_SWITCHES,
            description = "赛博激光指示机械开关，高亮霓虹光束指示器",
            cssCode = """
                .cyber-switch {
                  background: #0f101a;
                  border: 1.5px solid #00f0ff;
                  box-shadow: 0 0 10px rgba(0, 240, 255, 0.4);
                }
            """.trimIndent(),
            htmlCode = """<div class="cyber-switch"><div class="laser-dot"></div></div>""",
            associatedKit = UiKitPreset.CYBERPUNK_NEON
        ),

        // Checkboxes Category
        UiverseItem(
            id = "check_neu",
            name = "Neumorphic Inset Checkbox",
            author = "alexmaracinaru",
            category = UiverseCategory.CHECKBOXES,
            description = "新拟物凹槽内陷软触感复选框",
            cssCode = """
                .neu-checkbox {
                  box-shadow: inset 3px 3px 6px #cbd5e1, inset -3px -3px 6px #ffffff;
                  border-radius: 8px;
                }
            """.trimIndent(),
            htmlCode = """<input type="checkbox" class="neu-checkbox" />""",
            associatedKit = UiKitPreset.NEUMORPHISM_CLAY
        ),
        UiverseItem(
            id = "check_cyber",
            name = "Cyberpunk Hexagon Tick",
            author = "Galahhad",
            category = UiverseCategory.CHECKBOXES,
            description = "赛博六边形微光确认复选勾选框",
            cssCode = """
                .cyber-check {
                  border: 1.5px solid #00f0ff;
                  box-shadow: 0 0 8px #00f0ff;
                }
            """.trimIndent(),
            htmlCode = """<input type="checkbox" class="cyber-check" />""",
            associatedKit = UiKitPreset.CYBERPUNK_NEON
        ),

        // Radio buttons Category
        UiverseItem(
            id = "radio_wave",
            name = "Ripple Wave Radar Radio",
            author = "mrhyddenn",
            category = UiverseCategory.RADIO_BUTTONS,
            description = "水波纹涟漪扩散单选框",
            cssCode = """
                .wave-radio {
                  border: 2px solid #6366f1;
                  box-shadow: 0 0 12px rgba(99, 102, 241, 0.5);
                }
            """.trimIndent(),
            htmlCode = """<input type="radio" class="wave-radio" />""",
            associatedKit = UiKitPreset.GLASSMORPHISM_AURORA
        ),

        // Forms Category
        UiverseItem(
            id = "form_floating",
            name = "Floating Glassmorphism Form Card",
            author = "mrhyddenn",
            category = UiverseCategory.FORMS,
            description = "全套悬浮极光磨砂表单卡片，集成分组字段与柔光反馈",
            cssCode = """
                .floating-form {
                  background: rgba(255, 255, 255, 0.08);
                  backdrop-filter: blur(16px);
                  border: 1px solid rgba(255, 255, 255, 0.2);
                  border-radius: 20px;
                  padding: 20px;
                }
            """.trimIndent(),
            htmlCode = """<form class="floating-form"><input type="text"><button>Submit</button></form>""",
            associatedKit = UiKitPreset.GLASSMORPHISM_AURORA
        ),

        // Patterns Category
        UiverseItem(
            id = "pattern_cyber",
            name = "Cyberpunk Matrix Wireframe Grid",
            author = "Galahhad",
            category = UiverseCategory.PATTERNS,
            description = "高科技暗夜三维透视网格线条",
            cssCode = """
                .cyber-grid {
                  background-color: #05050a;
                  background-image: linear-gradient(rgba(0, 240, 255, 0.1) 1px, transparent 1px),
                                    linear-gradient(90deg, rgba(0, 240, 255, 0.1) 1px, transparent 1px);
                  background-size: 24px 24px;
                }
            """.trimIndent(),
            htmlCode = """<div class="cyber-grid"></div>""",
            associatedKit = UiKitPreset.CYBERPUNK_NEON
        ),
        UiverseItem(
            id = "pattern_dots",
            name = "Minimalist Dot Matrix Pattern",
            author = "andrew-demchenk0",
            category = UiverseCategory.PATTERNS,
            description = "极简圆点矩阵背景纹理，低噪点高雅",
            cssCode = """
                .dot-matrix {
                  background-image: radial-gradient(rgba(0, 0, 0, 0.12) 1.5px, transparent 1.5px);
                  background-size: 16px 16px;
                }
            """.trimIndent(),
            htmlCode = """<div class="dot-matrix"></div>""",
            associatedKit = UiKitPreset.NEO_BRUTALISM_POP
        ),
        UiverseItem(
            id = "pattern_wind",
            name = "Warm Peach Wind Waves",
            author = "MuhammadHasann",
            category = UiverseCategory.PATTERNS,
            description = "经典温暖晨风与流动涟漪微光底纹",
            cssCode = """
                .wind-pattern {
                  background: linear-gradient(135deg, #fec195, #fcc196, #fabd92);
                }
            """.trimIndent(),
            htmlCode = """<div class="wind-pattern"></div>""",
            associatedKit = UiKitPreset.DEFAULT_CLASSIC
        ),

        // Tooltips Category
        UiverseItem(
            id = "tooltip_glass",
            name = "Frosted Floating Glass Tooltip",
            author = "mrhyddenn",
            category = UiverseCategory.TOOLTIPS,
            description = "悬浮磨砂玻璃提示气泡，极光微光倒角箭头",
            cssCode = """
                .glass-tooltip {
                  background: rgba(15, 23, 42, 0.85);
                  backdrop-filter: blur(10px);
                  border: 1px solid rgba(255, 255, 255, 0.2);
                  border-radius: 10px;
                  color: #fff;
                }
            """.trimIndent(),
            htmlCode = """<div class="glass-tooltip">Help Info</div>""",
            associatedKit = UiKitPreset.GLASSMORPHISM_AURORA
        )
    )

    fun getItemsByCategory(category: UiverseCategory): List<UiverseItem> {
        return when (category) {
            UiverseCategory.ALL -> items
            else -> items.filter { it.category == category }
        }
    }
}
