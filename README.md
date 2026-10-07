# 懒得找了 (LanDeZhaoLe) · 云端驱动的 Android 全能资源导航与百宝箱

<p align="center">
  <b>GitHub 仓库即云端数据中枢 · 控制台秒级热同步 · 全站液态玻璃与动态环形视觉引擎 · 50+ 实用效率工具箱</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-Kotlin%20%2B%20Jetpack%20Compose-3DDC84?logo=android&logoColor=white" alt="Android Kotlin Compose" />
  <img src="https://img.shields.io/badge/App%20Version-v1.3.5%20(code%20140)-2563EB" alt="App Version" />
  <img src="https://img.shields.io/badge/Console%20Version-v1.3.1%20(code%2055)-9333EA" alt="Console Version" />
  <img src="https://img.shields.io/badge/CI%2FCD-Auto%20Release-22C55E?logo=githubactions&logoColor=white" alt="Auto Release" />
</p>

---

## 一、项目简介

**「懒得找了」** 是一款采用 **“GitHub 仓库即云端数据中枢（Git-as-a-Backend）”** 架构的 Android 全能资源导航与创意工具应用。

项目由 **本体 App（`app/`）** 与 **云端总控台 App（`console-apk/`）** 双端协同组成：
- **本体 App**：面向终端用户，集成了 **1000+ 精选站点导航（AI 行业、开发编程、设计创意、影视娱乐、云工具等）**、**50+ 离线/在线实用小工具箱**、**Prompt 提示词灵感中心**、**软件与 Skill 资源库**，并搭载 **全站液态玻璃（Liquid Glass）+ 10 款动态环形特效引擎**。
- **控制台 App**：面向管理员与开发者，无需额外部署后端服务器，直接通过 GitHub API 与多级 CDN 镜像读写 `admin-data.json`，实现 **分类站点增删、UI 文案实时检索与配置、主题与弹窗下发、APK 发布与自动版本对齐**，本体 App 每 6 秒无感轮询，数秒内全网生效。

---

## 二、核心架构与运行机制

```text
GitHub 仓库（唯一真相源 Single Source of Truth）
├── admin-data.json                  ← 全站内容/配置/版本/UI文案/主题/跑马灯/IP监控/自动发布状态
├── dist/
│   ├── apk/                         ← 本体 App 历次发布安装包（含真实版本号与时间戳）
│   ├── console/                     ← 控制台 App 安装包
│   └── uploads/                     ← 控制台上传的图片/视频/音频/文档素材
├── app/                             ← 本体 Android 源码（Kotlin + Jetpack Compose + Room + Media3）
├── console-apk/                     ← 控制台 Android 源码（原生 Java + 内嵌全功能管理台）
├── .github/
│   ├── workflows/auto-release.yml   ← 自动发布流水线（源码 push 自动 bump 版本、构建、双仓同步、CDN 刷新）
│   └── scripts/                     ← 自动发布检测与发布 Python 脚本
├── auto_release.sh                  ← 本地/服务端一键自动检测与发布脚本
├── merge_sites.py                   ← 站点批量去重合并工具（只增不删、URL 规范化去重）
├── AGENTS.md                        ← 开发者与 AgentAI 维护总纲
├── 写死规则.md                      ← 项目宪法级五大铁律与定点开发规范
└── 文件功能清单.md                  ← 全量源码模块与文件功能速查表
```

### 多通道高可用读取链（防劫持 + 多镜像兜底）
本体 App 与控制台均内置 9 级高可用网络读取链路，自动执行 **HTML 网页劫持检测** 与超时毫秒级切换：
```text
GitHub API → jsdelivr-testingcf → jsdelivr-cdn → jsdelivr-fastly → jsdelivr-gcore
→ ghfast.top → ghproxy.net → raw.gitmirror.com → raw.githubusercontent.com
```

---

## 三、核心功能特性

### 1. 全站液态玻璃（Liquid Glass）与动态环形特效引擎
- **全站液态玻璃呈现**：全局背景画布、顶栏品牌区、搜索框、分类卡片均采用多层冰晶折射、游走高光水滴、曲面镜面反射带与双层高光玻璃描边。
- **10 款动态环形特效**：支持在任意主题上实时叠加 `霓虹脉冲环`、`星轨双旋环`、`液态波纹环`、`赤金耀斑环`、`全息棱镜环`、`流光泡泡环`、`极光星冕环`、`萤火能量环`、`冰晶光晕环`、`时空引力环`。
- **5 档环形样式幅度**：支持从 `微距纤巧环` 到 `全屏超新星环` 5 档实时调节，动态控制全站环形特效的尺寸跨度、层数密度、描边粗细与律动幅度。

### 2. 智能资源导航与站点引擎
- **1000+ 精选站点**：涵盖 AI 行业与大模型、云端开发工具、设计素材、影音娱乐、效率办公等丰富分类。
- **只增不删 & 自动去重**：本地内置站点与云端 `admin-data.json` 动态合并；按规范化 URL（忽略协议头、末尾斜杠、大小写）智能去重，云端同名覆盖、本地独有永久保留。
- **站点品牌图标自动解析**：支持 Favicon 自动抓取与首字母高颜值徽标兜底。

### 3. 全能工具箱（50+ 实用工具） & 百鸟鸣科普
- **生活与效率工具**：年龄计算器、星座运势、「今天吃什么」随机选餐、口播/话术一键复制跳转、离线宝藏库等。
- **百鸟鸣（100 种鸟叫科普）**：内置 100 种鸟类纯离线合成/科普音频（`res/raw/bird_XXX.ogg`），支持搜索与即点即播。
- **Prompt 提示词中心 & 资源上传中心**：内置 3D 渲染、赛博朋克、人像摄影、Sora 视频等提示词灵感库，支持一键复制与云端资源上传。

### 4. 云端总控台（`console-apk`）与全量 UI 文本配置
- **全模块可视化管理**：一站式管理首页分类/站点、软件库、Skill、开屏页、欢迎弹窗、更新弹窗、跑马灯公告、IP 监控挂件与主题工具箱。
- **UI 文本搜索与实时过滤**：支持按关键字、键名或默认值实时过滤全站 UI 文本配置（顶栏、底栏、设置页、各类弹窗），支持「仅看已修改」一键筛选与分区增删。
- **自动发布状态跟随**：自动读取云端 `autoRelease` 状态，实时展示最近自动发布的版本号、VersionCode、构建时间与 Commit SHA。

---

## 四、快速构建指南

### 环境要求
- **JDK**：17
- **Android SDK**：Compile SDK 36 / Min SDK 24
- **构建工具**：Gradle Wrapper (`./gradlew`)

### 1. 本地构建本体 App
```bash
./gradlew :app:assembleRelease
# 产物路径: app/build/outputs/apk/release/app-release.apk
```

### 2. 本地构建控制台 App
```bash
./gradlew -p console-apk :app:assembleRelease
# 产物路径: console-apk/app/build/outputs/apk/release/app-release.apk
```

---

## 五、版本发布与自动化工作流（Auto Release）

本项目严格遵循 **「版本四要素同步铁律」**：
1. `version.code` 递增（`+1`）
2. `version.name` 语义化递增（如 `1.3.4` → `1.3.5`）
3. 真实构建对应版本的 APK 并上传至 `dist/apk/landezhao-v<version>-<timestamp>.apk`
4. `admin-data.json` 中的 `version.apkUrl` / `version.apkUrlRaw` 与 `updateDialog` 同步指向新 APK 直链并刷新 jsDelivr CDN

### 方式 A：GitHub Actions 全自动发布（推荐）
当向 `main` 分支推送 `app/**` 或 Gradle 构建配置改动时，`.github/workflows/auto-release.yml` 会自动触发：
1. 运行 `.github/scripts/auto_release_check.py` 对比 `lastBuildSha` 与当前 `HEAD`，自动推进 `versionCode` 与 `versionName`。
2. 编译签名生成 Release APK。
3. 运行 `.github/scripts/auto_release_publish.py` 自动生成 Changelog、归档 APK 到 `dist/apk/`、回写 `admin-data.json`（含 `autoRelease` 状态），并在配置 `RELEASE_PAT` 时同步更新数据源仓库与刷新 jsDelivr 缓存。
4. 也可在 GitHub Actions 页面手动点击 **Run workflow (`workflow_dispatch`)** 触发构建。

### 方式 B：命令行一键发布脚本
```bash
export GH_TOKEN="你的_GitHub_PAT"
bash auto_release.sh          # 自动检测改动并发布
bash auto_release.sh --force  # 强制推进版本号并重新构建发布
```

---

## 六、项目核心规范（必读）

任何人类开发者或 AgentAI 参与维护前，**必须先行阅读并遵守以下文档**：
1. [`写死规则.md`](./写死规则.md)：项目宪法级规则（版本四要素严格对齐、控制台高可用连接、AgentAI 接口预留、站点只增不删去重、联系方式二维码锁定、定点开发白名单铁律）。
2. [`AGENTS.md`](./AGENTS.md)：日常维护、版本发布、控制台升级、网络层约定的标准作业程序（SOP）。
3. [`文件功能清单.md`](./文件功能清单.md)：定点开发前用于确定改动文件白名单的模块映射表。

---

## 七、安全说明

- **严禁硬编码密钥**：任何 `GITHUB_TOKEN`、`RELEASE_PAT` 或第三方 API Key 严禁写入源码、配置文件或提交历史。
- **签名一致性**：本体与控制台升级包必须保持签名证书一致（详见 [`SIGNING.md`](./SIGNING.md)），确保老版本用户端内免卸载直接覆盖安装。
