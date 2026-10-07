plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example"
    compileSdk = 36

    defaultConfig {
        // v2.1.0：原生 Compose 控制台（由 v2.1.0 APK 反编译重建源码）
        // 与发布物 dist/console/console-apk-v2.1.0-1791350711.apk 完全对齐：
        //   applicationId=com.aistudio.landezhaole.qrxwpm / code=58 / name=2.1.0
        applicationId = "com.aistudio.landezhaole.qrxwpm"
        minSdk = 24
        targetSdk = 36
        versionCode = 58
        versionName = "2.1.0"
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    signingConfigs {
        create("release") {
            // 与本体一致：优先仓库签名密钥，缺失回退 debug.keystore
            val repoKey = rootProject.file("../signing/lzdz-release.keystore")
            val dbgKey = rootProject.file("../debug.keystore")
            val chosen = if (repoKey.exists()) repoKey else dbgKey
            val useDebugFallback = (chosen == dbgKey)
            if (useDebugFallback) println("==> 警告：未找到正式签名密钥，回退使用 debug.keystore（产物为调试签名）")
            storeFile = chosen
            storePassword = if (useDebugFallback) "android" else (System.getenv("CONSOLE_STORE_PASSWORD") ?: "lzdz123456")
            keyAlias = if (useDebugFallback) "androiddebugkey" else (System.getenv("CONSOLE_KEY_ALIAS") ?: "lzdz-release")
            keyPassword = if (useDebugFallback) "android" else (System.getenv("CONSOLE_KEY_PASSWORD") ?: "lzdz123456")
        }
    }
}

dependencies {
    // 与 v2.1.0 发布物一致的依赖（compose + okhttp + coroutines）
    val composeBom = platform("androidx.compose:compose-bom:2024.09.00")
    implementation(composeBom)
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
