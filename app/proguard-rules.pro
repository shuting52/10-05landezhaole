# ============================================================
# 懒得找了 · R8 混淆规则（防破解二改加固）
# ============================================================

# ---------- 基础保护 ----------
-keepattributes Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable
-keepattributes *Annotation*,Exceptions

# ---------- Kotlin ----------
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ---------- Room ----------
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class **
-keep @androidx.room.Dao interface **
-keep class com.example.data.local.db.** { *; }
-keep class com.example.data.local.db.CloneAppEntity { *; }

# ---------- Moshi 反射 JSON（云端配置解析必须保留） ----------
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
# 云端数据模型（AdminData 系列）全部保留字段名，保证反射解析正确
-keepclassmembers class com.example.data.remote.** {
    <fields>;
}
-keep class com.example.data.remote.** { *; }
-dontwarn com.squareup.moshi.**

# ---------- Compose ----------
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable *;
}
# Compose 生成的 @Stable / @Immutable 类
-keep class **$$** { *; }

# ---------- Coil ----------
-keep class coil.** { *; }
-dontwarn coil.**

# ---------- OkHttp ----------
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# ---------- 本应用核心（全部保留，防止反射/序列化崩溃） ----------
-keep class com.example.BuildConfig { *; }
-keep class com.example.data.model.** { *; }
-keep class com.example.data.local.** { *; }
-keep class com.example.ui.uiverse.** { *; }

# 签名校验工具（SecurityGuard 涉及签名反射，保留）
-keep class com.example.SecurityGuard { *; }

# ---------- WebView JS 桥 ----------
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# ---------- FileProvider ----------
-keep class androidx.core.content.FileProvider { *; }

# ---------- 防调试/防篡改（配合 SecurityGuard 运行时校验） ----------
-keep class com.example.ui.viewmodel.NavViewModel { *; }

# ============================================================
# v1.0.9 强化型加密加固（控制台安全大升级）
# ============================================================

# ---------- 混淆字典：类名/方法名/包名使用无规律混淆名（防反编译可读） ----------
-obfuscationdictionary obfuscation-dictionary.txt
-classobfuscationdictionary obfuscation-dictionary.txt
-packageobfuscationdictionary obfuscation-dictionary.txt

# ---------- 全模式优化（强化混淆深度与代码精简，提高逆向难度） ----------
-allowaccessmodification
-mergeinterfacesaggressively
-optimizationpasses 8

# ---------- Android 组件：Manifest 声明的组件类必须保留原名（防止打包/启动崩溃） ----------
-keep class com.example.MainActivity { *; }
-keep class com.example.ui.components.UpdateInstallReceiver { *; }

# ---------- 资源/反射安全 ----------
# 保留所有资源 ID 引用（Compose 动态访问 R 内部字段）
-keep class com.example.R$* { *; }
# 保留 WebView/Theme 相关避免资源混淆崩溃
-keepattributes ResourceId,SourceDebugExtension
