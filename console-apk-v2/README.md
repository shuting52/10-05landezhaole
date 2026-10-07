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

> 注意：源码为 Java 形式（由 Kotlin 产物反编译），含 jadx 注释与 `@Metadata`；
> 反编译重建的源码可直接阅读、可作为维护参考。若需恢复原生 Kotlin 源码，
> 建议基于本工程结构逐步改写回 Kotlin。

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
