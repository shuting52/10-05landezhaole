#!/usr/bin/env bash
# =============================================================================
# 懒得找了 · 自动检测 + 一键发布新版本脚本
#
# 用法:
#   export GH_TOKEN="<github token>"        # 必填：操作仓库的 Token（建议最小权限）
#   bash auto_release.sh [--force]          # --force: 即使无源码改动也强制 bump 发布
#
# 流程:
#   1. 拉取远端最新代码
#   2. 对比 admin-data.json 的 lastBuildSha 与 HEAD，判断 app 源码是否有新改动
#   3. 有改动 → 自动推进 versionCode/versionName → 构建 Release APK
#   4. 校验 APK 版本号与签名
#   5. 更新两个仓库 admin-data.json（四要素 + changelog）+ 上传 APK
#   6. purge jsDelivr CDN
# =============================================================================
set -euo pipefail

REPO_URL="https://github.com/shuting52/10-05landezhaole.git"
OWNER="shuting52"
REPO="10-05landezhaole"
OLD_REPO="landezhaole10-02"   # App 实际读取的数据源仓库
BRANCH="main"
CACHE_BUST="20261004"
FORCE="${1:-}"

if [ -z "${GH_TOKEN:-}" ]; then
  echo "❌ 缺少 GH_TOKEN 环境变量" >&2
  exit 1
fi
PUSH_URL="https://x-access-token:${GH_TOKEN}@github.com/${OWNER}/${REPO}.git"

echo "==> [1/7] 拉取远端最新代码"
if [ ! -d .git ]; then
  echo "当前目录不是 git 仓库，请在本项目目录运行" >&2; exit 1
fi
git fetch origin "$BRANCH" 2>&1 | tail -1 || true
git checkout -q "$BRANCH" 2>/dev/null || git checkout -q -B "$BRANCH" origin/"$BRANCH"
git pull --ff-only origin "$BRANCH" 2>&1 | tail -1 || true
HEAD=$(git rev-parse HEAD)
echo "HEAD: ${HEAD:0:10}"

echo "==> [2/7] 检测是否有源码改动需要发布"
python3 .github/scripts/auto_release_check.py
NEED=$(grep '^need=' /tmp/auto_release_out.txt 2>/dev/null | cut -d= -f2 || echo "no")
NEW_CODE=$(grep '^new_code=' /tmp/auto_release_out.txt 2>/dev/null | cut -d= -f2 || echo "")
NEW_NAME=$(grep '^new_name=' /tmp/auto_release_out.txt 2>/dev/null | cut -d= -f2 || echo "")

# 兼容：check 脚本输出到 GITHUB_OUTPUT 或 stdout；这里直接读 build.gradle 确认最终版本
CODE=$(grep -oP 'versionCode\s*=\s*\K\d+' app/build.gradle.kts | head -1)
NAME=$(grep -oP 'versionName\s*=\s*"\K[^"]+' app/build.gradle.kts | head -1)

if [ "$NEED" != "yes" ] && [ "$FORCE" != "--force" ]; then
  echo "ℹ️ app 源码无新改动（或已发布），无需发布。如要强制发布请加 --force"
  exit 0
fi
echo "==> 将发布: v${NAME} (code ${CODE})"

echo "==> [3/7] 构建 Release APK"
if [ ! -f debug.keystore ]; then
  echo "  拉取 debug.keystore（签名与 1.3.x 系列一致）..."
  curl -sL --retry 3 --retry-all-errors -o debug.keystore \
    "https://raw.githubusercontent.com/${OWNER}/${OLD_REPO}/main/debug.keystore"
fi
chmod +x gradlew
JAVA_HOME="${JAVA_HOME:-}"
if [ -z "$JAVA_HOME" ]; then
  echo "❌ 请设置 JAVA_HOME（JDK 17）与 ANDROID_HOME" >&2; exit 1
fi
./gradlew :app:assembleRelease --no-daemon -Dorg.gradle.jvmargs="-Xmx2g -XX:MaxMetaspaceSize=512m"
APK="app/build/outputs/apk/release/app-release.apk"
[ -f "$APK" ] || { echo "❌ 构建产物不存在" >&2; exit 1; }

echo "==> [4/7] 校验 APK 版本号与签名"
AAPT2="${ANDROID_HOME}/build-tools/36.0.0/aapt2"
"$AAPT2" dump badging "$APK" | grep -E "package: name|versionCode|versionName" | head -2

echo "==> [5/7] 更新 admin-data + 放置 APK"
TS=$(date +%s)
APK_NAME="landezhao-v${NAME}-${TS}.apk"
mkdir -p dist/apk
cp "$APK" "dist/apk/${APK_NAME}"
python3 - <<EOF
import json
code = int($CODE); name = "$NAME"
chg = ["✨ v${NAME} 更新来啦～", "本次更新包含多项优化与修复"]
admin = json.load(open("admin-data.json", encoding="utf-8"))
admin["version"].update({
  "code": code, "name": name, "changelog": chg, "force": False,
  "apkUrl": "https://raw.githubusercontent.com/${OWNER}/${REPO}/main/dist/apk/${APK_NAME}",
  "apkUrlRaw": "https://github.com/${OWNER}/${REPO}/raw/main/dist/apk/${APK_NAME}",
  "lastBuildSha": "$HEAD",
})
admin["updateDialog"]["title"] = "懒得找了 v${NAME} 已上线"
admin["updateDialog"]["changelog"] = chg
json.dump(admin, open("admin-data.json", "w", encoding="utf-8"), ensure_ascii=False, indent=2)
print("admin-data.json 已更新:", code, name)
EOF

echo "==> [6/7] 推送本仓库"
git add app/build.gradle.kts admin-data.json dist/apk/
git -c user.name="shuting52" -c user.email="shuting52@users.noreply.github.com" \
  commit -m "release: v${NAME}（versionCode ${CODE}）自动发布" || true
git push "$PUSH_URL" "$BRANCH" 2>&1 | tail -2

echo "==> [7/7] 同步 ${OLD_REPO}（App 读取源）"
APK_B64=$(base64 -w0 "dist/apk/${APK_NAME}")
curl -s -X PUT -H "Authorization: Bearer ${GH_TOKEN}" -H "Accept: application/vnd.github+json" \
  --data "{\"message\":\"release: v${NAME} 本体 APK\",\"content\":\"${APK_B64}\"}" \
  "https://api.github.com/repos/${OWNER}/${OLD_REPO}/contents/dist/apk/${APK_NAME}" \
  | python3 -c "import json,sys; d=json.load(sys.stdin); print('旧仓库 APK:', 'OK' if 'content' in d else d.get('message','?'))"

OLD_ADM=$(curl -s -H "Authorization: Bearer ${GH_TOKEN}" -H "Accept: application/vnd.github+json" \
  "https://api.github.com/repos/${OWNER}/${OLD_REPO}/contents/admin-data.json?ref=${BRANCH}")
python3 - "$OLD_ADM" <<'EOF'
import json, sys, base64
d = json.loads(sys.argv[1])
sha = d["sha"]
adm = json.loads(base64.b64decode(d["content"]).decode())
name = "${NAME}"; code = ${CODE}
chg = ["✨ v${NAME} 更新来啦～", "本次更新包含多项优化与修复"]
adm["version"].update({
  "code": code, "name": name, "changelog": chg, "force": False,
  "apkUrl": "https://raw.githubusercontent.com/${OWNER}/${OLD_REPO}/main/dist/apk/${APK_NAME}",
  "apkUrlRaw": "https://github.com/${OWNER}/${OLD_REPO}/raw/main/dist/apk/${APK_NAME}",
})
adm["updateDialog"]["title"] = "懒得找了 v${NAME} 已上线"
adm["updateDialog"]["changelog"] = chg
payload = {"message": f"release: v{name} 四要素同步（code {code}）", "content": base64.b64encode(json.dumps(adm, ensure_ascii=False, indent=2).encode()).decode(), "sha": sha}
open("/tmp/oldadm_payload.json","w").write(json.dumps(payload))
EOF
curl -s -X PUT -H "Authorization: Bearer ${GH_TOKEN}" -H "Accept: application/vnd.github+json" \
  --data @/tmp/oldadm_payload.json \
  "https://api.github.com/repos/${OWNER}/${OLD_REPO}/contents/admin-data.json" \
  | python3 -c "import json,sys; d=json.load(sys.stdin); print('旧仓库 admin-data:', 'OK' if 'content' in d else d.get('message','?'))"

curl -s -A "Mozilla/5.0" -o /dev/null -w "purge http=%{http_code}\n" \
  "https://purge.jsdelivr.net/gh/${OWNER}/${OLD_REPO}@main/admin-data.json?v=${CACHE_BUST}"

echo ""
echo "==============================================================="
echo "✅ 发布完成: v${NAME} (code ${CODE})"
echo "   APK: outputs/landezhao-${APK_NAME}"
echo "   老版本 App 下次轮询将检测到 v${NAME} 并弹更新窗"
echo "==============================================================="
