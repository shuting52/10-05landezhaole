# 构建 & 签名 配置说明

## 本项目使用固定签名密钥，保证 APK 覆盖安装永远不被 Android 拒绝

> v1.7.5 之前：每次本地构建都会生成新 keystore，导致覆盖安装时弹出「软件包与现有软件包存在冲突」。
> v1.7.5 之后：签名已固定，老版本升级 v1.7.5+ 任意版本都不会再出现冲突。

---

## 签名方案：V1 + V2 + V3 三重签名（v1.0.13 起）

本体 APK 采用 **V1 + V2 + V3** 三重签名方案（`app/build.gradle.kts` signingConfigs.release 中显式配置）：

| 签名方案 | 说明 | 适用系统 |
|---|---|---|
| V1 (JAR Signature) | 传统 jar 签名 | Android 7.0 (API 24) 以下 |
| V2 (APK Signature Scheme v2) | 整包快速校验 | Android 7.0+ |
| V3 (APK Signature Scheme v3) | 支持密钥轮换 | Android 9.0+ |

```kotlin
signingConfigs {
    create("release") {
        ...
        enableV1Signing = true
        enableV2Signing = true
        enableV3Signing = true
        enableV4Signing = false  // V4 仅 adb 增量安装需要，普通分发关闭
    }
}
```

> 三重签名保证：低版本系统设备（Android 7.0 以下）也能正常安装（V1），主流设备走快速校验（V2/V3），覆盖安装/升级完全兼容。

---

## 一、本地构建（推荐首次设置）

### 1. 生成永久 keystore（**只生成一次，保管好**）

```bash
# 本体 APK keystore（20年有效期，妥善保管！）
keytool -genkeypair -v \
  -keystore signing/lzdz-release.keystore \
  -alias lzdz-release \
  -keyalg RSA -keysize 2048 \
  -validity 20000 \
  -storepass "lzdz2026!secure" \
  -keypass "lzdz2026!secure" \
  -dname "CN=Lzdz, OU=Mobile, O=Shuting52, L=Shenzhen, ST=GD, C=CN"

# 控制台 APK keystore
keytool -genkeypair -v \
  -keystore signing/lzdz-console.keystore \
  -alias lzdz-console \
  -keyalg RSA -keysize 2048 \
  -validity 20000 \
  -storepass "lzdz123456" \
  -keypass "lzdz123456" \
  -dname "CN=Lzdz, OU=Console, O=Shuting52, L=Shenzhen, ST=GD, C=CN"

# 让 app 模块使用 stdlib 作为签名密钥
cp signing/lzdz-release.keystore my-upload-key.jks
```

> ⚠️ **重要**：这两个 keystore 文件一旦丢失，所有已发布版本的覆盖安装链路会立刻失效（用户升级会失败）。请至少备份 2 处。

### 2. 构建 Release APK

```bash
# 本体（com.landezhaole）
./gradlew :app:assembleRelease

# 控制台（com.yuntai）
./gradlew :console-apk:assembleRelease
```

产物路径：
- `app/build/outputs/apk/release/app-release.apk`
- `console-apk/app/build/outputs/apk/release/app-release.apk`

### 3. 重命名并上传

```bash
# 上传到 GitHub 后通知本体 App
mv app/build/outputs/apk/release/app-release.apk dist/apk/landezhao-v1.7.8-$(date +%s).apk
mv console-apk/app/build/outputs/apk/release/app-release.apk dist/console/console-apk-v1.0.14-$(date +%s).apk

git add dist/ && git commit -m "release: v1.7.8 / v1.0.14" && git push
```

更新 `admin-data.json` 中的 `version.apkUrl` 与 `console.apkUrl`，控制台「? 推送并触发本体更新弹窗」即可。

---

## 二、CI 自动构建（GitHub Actions）

`.github/workflows/build.yml` 已配置完成，按以下步骤启用：

### 1. 推送当前代码并把 keystore 编码后存入 GitHub Secrets

```bash
# 把 keystore 编码为 base64
base64 -w0 signing/lzdz-release.keystore > /tmp/lzdz-release.b64
base64 -w0 signing/lzdz-console.keystore > /tmp/lzdz-console.b64

# 在 GitHub → Settings → Secrets and variables → Actions 增加：
#   LZDZ_KEYSTORE_BASE64      = <lzdz-release.keystore 的 base64 内容>
#   LZDZ_KEYSTORE_PASSWORD    = lzdz2026!secure
#   LZDZ_KEY_ALIAS            = lzdz-release
#   LZDZ_KEY_PASSWORD         = lzdz2026!secure
#   CONSOLE_KEYSTORE_BASE64   = <lzdz-console.keystore 的 base64 内容>
#   CONSOLE_KEYSTORE_PASSWORD = lzdz123456
#   CONSOLE_KEY_ALIAS         = lzdz-console
#   CONSOLE_KEY_PASSWORD      = lzdz123456
```

### 2. 触发工作流

- **手动**：GitHub → Actions → Build Android APKs → Run workflow
- **自动**：push 一个 tag `v*`（例如 `git tag v1.7.8 && git push --tags`），会自动构建 + 创建 Release + 上传到 dist/

---

## 三、签名冲突应急处理

如果用户的旧版本（v1.7.4 或更早）正在使用，现升级 v1.7.5+ 任意版本时仍然提示「签名异常」：

1. **首选**：本体 App 的更新弹窗已自动检测签名冲突，会引导用户卸载旧版本（v1.7.5 之后版本之间的互相升级永不触发）。
2. **手动兜底**：用户手动到系统「设置 → 应用 → 懒得找了 → 卸载」，然后从「下载」文件夹点击新 APK 安装即可（弹窗已自动把安装包拷贝到下载目录）。

> ⚠️ 只有「跨签名」升级才会触发卸载引导。**同一签名的新版本永远走无缝覆盖安装**，不会丢数据。