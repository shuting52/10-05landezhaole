# 懒得找了 · 控制台 App v2.x（原生 Compose 版）

> **本工程源码由已发布的 v2.1.0 APK（`dist/console/console-apk-v2.1.0-1791350711.apk`）反编译重建，**
> 与发布物版本信息**完全对齐**，作为后续维护与再发布的基础。

## 版本信息（与发布物一致）

| 项目 | 值 |
|---|---|
| applicationId | `com.aistudio.landezhaole.qrxwpm` |
| versionCode | 58 |
| versionName | 2.1.0 |
| minSdk / targetSdk / compileSdk | 24 / 36 / 36 |
| 主 Activity | `com.example.MainActivity`（Compose） |

## 源码结构

```
console-apk-v2/app/src/main/
├── AndroidManifest.xml          # 与发布物一致（去 debuggable）
├── java/com/example/
│   ├── MainActivity.kt          # 入口（反编译 Java 版）
│   ├── data/AdminRepository.kt  # GitHub API 对接（读写 admin-data / 上传 / 刷新CDN）
│   ├── model/                   # 18 个配置模型类
│   ├── ui/AdminAppShell.kt      # 应用壳 + 导航
│   ├── ui/screens/              # 6 个管理页面（仪表盘/卡片/分类/按钮文本/模块主题/设置）
│   ├── ui/components/           # 通用组件（表格/弹窗/对话框）
│   ├── ui/theme/                # Compose 主题
│   └── viewmodel/AdminViewModel # 管理核心逻辑（增删改查/发布/上传）
├── res/                         # 从 APK 提取的资源
└── assets/admin-data.json       # 内置缓存数据
```

> 反编译重建的源码可作为**维护参考基线**，核心逻辑已改写为可读 Kotlin。

## 源码状态

| 层级 | 状态 | 说明 |
|---|---|---|
| 数据层 `data/AdminRepository.kt` | ✅ Kotlin 改写完成 | GitHub API 读写/镜像链/上传/CDN 刷新，**编译通过** |
| 模型层 `model/*.kt`（25 个） | ✅ Kotlin data class | 与发布物字段完全对齐，**编译通过** |
| 逻辑层 `viewmodel/AdminViewModel.kt` | ✅ Kotlin 改写完成 | 全部管理操作/发布/持久化，**编译通过** |
| UI 层 `ui/*.java` | ⚠️ jadx 反编译 Java | Compose 反编译产物，**可读但含反编译伪代码，无法直接编译**（`not an enclosing class` 等为 jadx 限制）；如需从源码出可运行 APK，需按 `ui/` 结构人工重写为 Kotlin Compose |

> Kotlin 层编译验证：`.github/workflows/verify-console-v2.yml`（手动触发，`compileReleaseKotlin`）。

## 构建

```bash
# 在本仓库根目录（含 gradlew / debug.keystore）
./gradlew -p console-apk-v2 :app:assembleRelease
# 产物: console-apk-v2/app/build/outputs/apk/release/app-release.apk
```

## 与旧版控制台的关系

- `console-apk/`：v1.3.2 WebView 壳控制台（旧版，保留历史）
- `console-apk-v2/`：v2.1.0 原生 Compose 控制台（本工程，当前发布线）

## 发布到云端（控制台自更新）

发布后需同步更新 `admin-data.json` 的 `console` 字段（version/code/apkUrl），
App 才会向老版本推送更新弹窗（下载方式与本体软件一致：多镜像 + 进度条 + 自动安装）。
