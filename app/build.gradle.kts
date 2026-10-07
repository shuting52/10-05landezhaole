import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.landezhaole"
    minSdk = 24
    targetSdk = 36
    // v1.7.8 规则（写死）：版本号必须与云端 admin-data.json 的 version.code 完全一致，
    // v1.7.8 警告（大写）：本应用禁止随意修改 versionCode/versionName，
    // 且 apkUrl 必须指向与 versionName 一致的真实安装包，否则会造成「永远提示更新但装不上」死循环。
    // v1.8.0：更新弹窗多巴胺改版 + 多线程下载提速 + 免授权直装引导
    // v1.8.1：站点扩充至 1000+ · 工具箱新增至 50+ 小工具
    // v1.8.2：更新弹窗回退为卡通弹窗（去掉多巴胺 HTML 与「授权未知应用」引导）
    // v1.8.6：恢复站点/软件库 + 开屏倒计时默认 5 秒
    // v1.8.7：控制台深度融合（工具箱云端同步/软件横排/背景媒体）- 更新弹窗免授权直装 + CSS 动态进度条
    // v1.8.9：更新弹窗全新重写——最新动态 CSS 手绘风格（手绘描边/流动渐变/涂鸦粒子/对话气泡）
    // v1.9.1：更新弹窗恢复 v1.5 老样式（白卡片 + 圆点列表 + 官方群 + 立即更新）
    // v1.0.4 发布：开屏CSS粒子动画 + 软件三列网格 + 工具箱精简(4工具+紧急电话) + 设置优化(主题背景同步/官方群跳转/反馈修复) + 新增500+热门站点
    // 控制台 v1.0.9：软件停止运营开关 + 强化型安全加密加固
    // 与云端 admin-data.json version.code=85 / name=1.0.4 四要素对齐（含 apkUrl 指向真实 1.0.4 安装包）
    // v1.0.5：更新弹窗固化「手绘CSS + 免授权直装」——永久删除 WebView 弹窗（写死规则7）
    // v1.0.11：修复官方群跳转（source=qrcode→sharecard，网页兜底提前）+ 追溯1.0.3下载（流式PK校验防OOM + jsdmir镜像）
    // 与云端 admin-data.json version.code=92 / name=1.0.11 四要素对齐（含 apkUrl 指向真实 1.0.11 安装包）
    // v1.0.12：本体软件更新优化（开屏透明特效/软件icon/技能库下载跳转/分享直达/反馈邮箱）
    // 与云端 admin-data.json version.code=93 / name=1.0.12 四要素对齐（含 apkUrl 指向真实 1.0.12 安装包）
    // v1.0.13：回退版本基线（保留更新弹窗），修复更新弹窗安装新版本时旧版本闪退 + 开屏纯色背景
    // 与云端 admin-data.json version.code=94 / name=1.0.13 四要素对齐（含 apkUrl 指向真实 1.0.13 安装包）
    // v1.0.16：设置版块完整恢复（主题切换/联系作者/软件反馈等全功能）+ 角标自动识别技术 + 更新弹窗最新动态CSS特效
    // 与云端 admin-data.json version.code=97 / name=1.0.16 四要素对齐（含 apkUrl 指向真实 1.0.16 安装包）
    // v1.0.17：修复设置版块空容器 + 角标单一化呈现
    // 与云端 admin-data.json version.code=98 / name=1.0.17 四要素对齐（含 apkUrl 指向真实 1.0.17 安装包）
    // v1.0.18 全面洗牌：角标修复（遵循原动态设计、不遮挡站点内容）；更新弹窗安装修复
    // （REQUEST_INSTALL_PACKAGES + 授权引导 + 多镜像下载源 + 失败可关闭）；工具箱/软件版块取消展开收纳；
    // 设置版块主题与软件主题同步；软件主题升级为「国庆节为核心·可爱风格·最新CSS动态效果」，原有主题风格全部移除
    // v1.1.1 正式版：更新弹窗恢复自动下载（弹窗出现即下载安装），免「未知应用」授权也能安装
    // 首页角标一致性 + 取消软件/工具箱/Skill 删除分类 + 国庆主题全 UI 组件
    // v1.1.3 主题升级：新增「霓虹地图·荧光绿」主题预设（深色地图底+荧光绿+白字+星空氛围）并设为默认
    // v1.1.4：角标统一绿色 + 主题组件定制生效 + 软件/Skill/工具箱分类清理 + 设置页 Uiverse 风格
    // 与云端 admin-data.json version.code=104 / name=1.1.4 四要素对齐（含 apkUrl 指向真实 1.1.4 安装包）
    // v1.1.8 控制台主题工具箱同步修复：组件级主题全量生效（设置页/底栏/顶栏/卡片/搜索/弹窗/全局）——
    // 云端 themeKit id 统一映射 + LocalComponentThemes 全局提供，控制台「应用」后本体数秒内实时变更
    // v1.1.7 自检修复：角标全站统一绿色小胶囊（公司角标也改绿）；软件版块分类标签彻底移除 + 多源 icon 识别；
    // 工具箱/设置页/导航整体胶囊化 UI；主题切换由控制台「主题工具箱」统一管理（themeKit 9 组件自定义代码）
    // v1.2.9：恢复首页卡片彩虹旋转渐变背景（首页/软件/Skill 三处呈现）
    // v1.3.1：首页顶部观看人数区域改为迷你翻页时钟
    // v1.3.2：翻页时钟移到右上角 + 颜色跟随主题
    // v1.3.3：删除翻页时钟，恢复观看人数
    // v1.3.4：版本更新发布（与云端 admin-data.json version.code=139 / name=1.3.4 四要素对齐）
    // v1.3.5：新增 500+ AI 行业与云工具站点；工具箱界面优化；UI 动态边框；图片加载稳定性修复
    // v1.3.7：全站二级页面/子工具窗口/底部抽屉/弹窗全面升级液体玻璃半透折射背景
    // v1.3.13：重建仓库后恢复自动发布基线；下拉选择框组件 + Skill Tab 视觉优化
    // v1.3.14：主题切换弹窗液体玻璃升级 + 百宝箱圆盘界面紧凑化微调 + 移除车标识别功能
    versionCode = 150
    versionName = "1.3.15"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      // v1.7.5 修复：写死固定签名密钥与密码，保证每个版本签名永远一致（覆盖安装不再失败）
      // 增强：密钥缺失（如 CI 未配置 secrets）时自动回退仓库内置 debug.keystore，保证仍能出可安装包
      val envPath = System.getenv("KEYSTORE_PATH")
      val envFile = envPath?.let { p -> if (File(p).isAbsolute) File(p) else file("${rootDir}/$p") }
      val uploadKey = file("${rootDir}/my-upload-key.jks")
      val dbgKey = file("${rootDir}/debug.keystore")
      val chosen = when {
        envFile != null && envFile.exists() -> envFile
        uploadKey.exists() -> uploadKey
        else -> dbgKey
      }
      val useDebugFallback = (chosen == dbgKey)
      if (useDebugFallback) println("==> 警告：未找到正式签名密钥，回退使用 debug.keystore（产物为调试签名）")
      storeFile = chosen
      storePassword = if (useDebugFallback) "android" else (System.getenv("STORE_PASSWORD") ?: "lzdz2026!secure")
      keyAlias = if (useDebugFallback) "androiddebugkey" else (System.getenv("KEY_ALIAS") ?: "upload")
      keyPassword = if (useDebugFallback) "android" else (System.getenv("KEY_PASSWORD") ?: "lzdz2026!secure")
      // v1.0.13：签名方案 V1 + V2 + V3 三重签名（兼容 Android 7.0 及以下设备 V1，主流设备 V2/V3）
      // V1 = JAR 签名（Android 7.0 以下）；V2 = APK Signature Scheme v2（Android 7.0+）；
      // V3 = APK Signature Scheme v3（Android 9.0+，支持密钥轮换）
      enableV1Signing = true
      enableV2Signing = true
      enableV3Signing = true
      enableV4Signing = false
    }
    create("debugConfig") {
      storeFile = file("${rootDir}/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      // 代码混淆（R8）+ 资源压缩混淆：提高防破解能力（MT管理器难以篡改）
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug { signingConfig = signingConfigs.getByName("debugConfig") }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files
// to match the convention used in Web projects.
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }

// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  // implementation(libs.accompanist.permissions)
  implementation(libs.androidx.activity.compose)
  // implementation(libs.androidx.camera.camera2)
  // implementation(libs.androidx.camera.core)
  // implementation(libs.androidx.camera.lifecycle)
  // implementation(libs.androidx.camera.view)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  // implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  // implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.converter.moshi)
  implementation(libs.firebase.ai)
  // Uncomment to use Firestore:
  // implementation(libs.firebase.firestore)

  // Uncomment ALL FOUR of the following dependencies together to use Firebase Auth and Google
  // Sign-In via Credential Manager:
  // implementation(libs.firebase.auth)
  // implementation(libs.androidx.credentials)
  // implementation(libs.androidx.credentials.play.services)
  // implementation(libs.googleid)
  implementation(libs.firebase.appcheck.recaptcha)
  implementation(libs.firebase.appcheck.debug)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  // implementation(libs.play.services.location)
  implementation(libs.retrofit)
  implementation(libs.zxing.core)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}
