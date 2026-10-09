# AGENTS.md · 开发者 / AgentAI 项目维护总纲

> 本文件是「懒得找了」项目的**操作手册**，供人类开发者与接入的 AgentAI 共同使用。
> **首次进入项目必须通读本文件 + `写死规则.md`，后续所有操作持续遵守。**
> 若有冲突，以 `写死规则.md`（宪法级）为准。

---

## 一、项目是什么（30 秒了解）

「懒得找了」是一款 **GitHub 仓库即云端数据中枢** 的 Android 资源导航 App：

```
GitHub 仓库（唯一真相源）
├── admin-data.json      ← 全部内容/配置/版本（分类站点、软件、Skill、开屏、欢迎、更新弹窗、跑马灯、IP监控…）
├── dist/apk/            ← 本体 App 安装包
├── dist/uploads/        ← 云端素材：图片/视频/文档
├── app/                 ← 本体 Android 源码（Kotlin + Compose）
└── .github/workflows/   ← CI：打 tag 自动构建本体并上传 dist/
```

**核心机制**：云端 `admin-data.json` 为唯一真相源（内容/配置/版本）；本体 App 每 6 秒轮询「读」云端（同步内容、检测更新弹窗）。

---

## 二、写死规则速览（详见 写死规则.md）

1. **版本严格对齐**：云端 version.code/name 必须与 APK 内嵌一致，apkUrl 必须指向同版本真实安装包。发布时必须 code+1、name、apkUrl、真实 APK **四要素同步**。装完新版绝不重复弹更新。
3. **预留 AgentAI 对接**：关键操作必须可脚本/API 完成；CI 一键构建；新 AI 先读本文件。
4. **站点只增不删 + 自动去重**：本地站点永远保留；按规范化 URL 去重（云端覆盖同名、本地独有保留、重复跳过）。
5. **联系方式/二维码锁定**：微信/QQ/支付宝二维码、QQ 群链接任何版本迭代不得改动；唯一变更途径=管理员主动上传新二维码并更新 admin-data.json。
6. **定点开发铁律（新增）**：说哪改哪，禁止牵连改动。当前 main 代码即基线；每次开发先对照 `文件功能清单.md` 确定目标文件白名单，只动白名单内文件；严禁顺手重构/格式化/修无关 bug/删历史代码；确需动白名单外文件必须先经用户确认；commit 前用 `git diff --stat` 自查，多余改动一律还原。(详见 写死规则.md)

---

## 三、环境与构建

### 本地构建（需 JDK 17 + Android SDK）
```bash
# 本体 App
./gradlew :app:assembleRelease
# 产物: app/build/outputs/apk/release/app-release.apk
```

### CI 构建（推荐，免本地环境）
在仓库创建一个 tag（如 `v1.7.9-fix`）即自动构建本体并上传到 `dist/`：
```
GitHub API: POST /repos/shuting52/2026-9-21landezhaole/git/refs
  body: {"ref":"refs/tags/vX.Y.Z-tag","sha":"<main分支最新commit>"}
```
> 注意：tag 必须指向**包含所有修改后的最新 commit**，否则 CI 构建的是旧代码。

### 网络层约定（本体）
读取顺序（**禁止删减**）：
```
GitHub API → jsdelivr-testingcf → jsdelivr-cdn → jsdelivr-fastly → jsdelivr-gcore
→ ghfast.top → ghproxy.net → raw.gitmirror.com → raw.githubusercontent.com
```
每条通道都要做「网页劫持检测」（返回 HTML 而非 JSON 则跳过），全部失败才报错并给出诊断。

---

## 四、标准工作流

### A. 日常维护（改内容，不升版本）
1. 读取 `admin-data.json`（GitHub Contents API 或镜像）
2. 修改目标字段（分类/站点/软件/Skill/设置/开屏/欢迎/跑马灯…）
3. 用 PUT Contents API 写回，commit message 形如 `sync: 应用并实时同步 <时间>`
4. 完成后主动 purge jsDelivr：`GET https://purge.jsdelivr.net/gh/shuting52/2026-9-21landezhaole@main/admin-data.json`
5. 本体数秒内无感生效（不弹窗）

### B. 发布新版本（触发本体更新弹窗）
**必须四要素同步，缺一即违规：**
1. `version.code` +1
2. `version.name` 递增（patch+1）
3. **构建新版本 APK 并上传到 `dist/apk/`**（文件名含真实版本号）
4. `version.apkUrl` / `version.apkUrlRaw` 指向**新 APK 的 raw 直链**
5. 更新 `version.changelog` 与 `updateDialog.changelog`（过滤机器垃圾日志）
6. 保留 `force: true`（强制更新，弹窗不可取消，仅一个「立即更新」+「官方群」按钮）
7. 发布后 purge CDN；本体下次轮询弹更新窗


### D. 定点开发（改代码 · 说哪改哪 · 铁律）
1. 先读 `文件功能清单.md`，把“想改的功能 → 对应文件”抄成**白名单清单**。
2. 只改白名单内文件，且只在目标功能代码块内动刀。
3. 不改无关代码：不顺手格式化、不重构、不修无关 bug、不删“看着没用”的代码、不复制粘贴扩散。
4. 若必须动白名单外文件（基础类/资源/配置），先说明原因征得用户同意再动。
5. commit 前自查：`git diff --stat` 必须与白名单一致，多余文件 `git checkout --` 还原；逐文件 diff 确认无无关改动。
6. commit 首行含功能名，正文列改动文件清单。

---

## 五、AgentAI / 开发者常见操作速查

| 任务 | 怎么做 |
|---|---|
| 添加分类/站点 | 改 `admin-data.json → home.categories`，新增卡片；**不删已有站点、URL 去重**（规则4） |
| 更新 App 名称/Slogan | 改 `settings.appName / settings.slogan` |
| 更新欢迎弹窗/开屏 | 改 `welcome.* / splash.*` |
| 发新版本弹更新窗 | 按「工作流 B」四要素同步 |
| 上传新 APK | CI 打 tag 构建（或手动上传 `dist/apk/`） |
| 改二维码 | **只有管理员主动更新** `settings.contact*` 才允许（规则5） |
| 改任何代码 | 先查 `文件功能清单.md` 定位目标文件 → 按工作流 D 定点改，不允许牵连改动（规则6） |

---

## 六、环境变量与安全

- **GITHUB_TOKEN**：仅用于本地脚本发布；**禁止写入任何源码/配置文件**；泄露后必须立即吊销。
- **签名密钥**：本体 `my-upload-key.jks`（经 GitHub Secrets 注入 CI）；本地开发缺密钥时自动回退 `debug.keystore`（产物为调试签名，覆盖安装需先卸载旧版）。
- **admin-data.json 是唯一真相源**：任何本地硬编码只作兜底，云端覆盖本地。

---

*维护：shuting52/2026-9-21landezhaole · 最后更新：2026-09-27*
