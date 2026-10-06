plugins {
    id("com.android.application")
}

android {
    namespace = "com.yuntai"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.yuntai"
        minSdk = 24
        targetSdk = 36
        // v1.0.6：更新弹窗免授权直装 + 软件/Skill 内容识别优化 + 工具箱合集；v1.0.7：版本+1
        // v1.0.8：更新弹窗全新重写——最新动态 CSS 手绘弹窗（手绘双层描边/流动渐变/涂鸦粒子/对话气泡）
        // v1.0.9：设置版块新增「软件停止运营」开关（本体强制弹窗并退出）+ 强化型安全加密加固说明
        // v1.0.13：控制台 APK 自更新安装错误修复（PendingIntent FLAG_MUTABLE + API33 getParcelableExtra 双参）
        // v1.0.15：控制台工具箱新增「主题工具箱」（9 组件自定义代码 → 本体所有组件实时生效）
        // v1.3.0：控制台布局重构——功能菜单 + 全屏独立窗口 + 输入弹窗 + UI文本分区增删 + 删除永久同步仓库
        // v1.3.1：完全对接本体所有内容（首页/软件/Skill/设置/开屏/欢迎/更新/APK/工具箱/主题/UI文本/跑马灯/IP监控）+ 增删永久同步 + 自动发布跟随显示
        // v1.3.2：UI 文本编辑页搜索/筛选/高亮 + 液态玻璃环形特效视觉升级（index.html 随本体 ec11b9a 更新）
        versionCode = 56
        versionName = "1.3.2"
    }

    signingConfigs {
        create("release") {
            // v1.0.12 修复：优先使用仓库内固定签名密钥，保证每次构建签名一致，
            // 老版本控制台才能通过覆盖安装完成自更新（否则签名不同会被系统拒绝）。
            // 增强：密钥缺失时自动回退仓库内置 debug.keystore（CI 无 secrets 也能出可安装包）
            val repoKey = rootProject.file("../signing/lzdz-release.keystore")
            val envKey = System.getenv("CONSOLE_KEYSTORE_PATH")
            val envFile = envKey?.let { p -> if (File(p).isAbsolute) File(p) else file("${rootProject.projectDir}/${p.removePrefix("./")}") }
            val dbgKey = rootProject.file("../debug.keystore")
            val chosen = when {
                envFile != null && envFile.exists() -> envFile
                repoKey.exists() -> repoKey
                else -> dbgKey
            }
            val useDebugFallback = (chosen == dbgKey)
            if (useDebugFallback) println("==> 警告：未找到正式签名密钥，回退使用 debug.keystore（产物为调试签名）")
            storeFile = chosen
            storePassword = if (useDebugFallback) "android" else (System.getenv("CONSOLE_STORE_PASSWORD") ?: "lzdz123456")
            keyAlias = if (useDebugFallback) "androiddebugkey" else (System.getenv("CONSOLE_KEY_ALIAS") ?: "lzdz-release")
            keyPassword = if (useDebugFallback) "android" else (System.getenv("CONSOLE_KEY_PASSWORD") ?: "lzdz123456")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // FileProvider（控制台自更新安装 APK 用）
    implementation("androidx.core:core:1.13.1")
}
