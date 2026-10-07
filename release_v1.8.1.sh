#!/usr/bin/env bash
# ============================================================
# 「懒得找了」v1.8.1 一键发布脚本（推送到本体 & 老版本弹更新）
# ------------------------------------------------------------
# 用法（任选其一）：
#   export GITHUB_TOKEN=你的token之后 ./release_v1.8.1.sh
#   GITHUB_TOKEN=你的token ./release_v1.8.1.sh
#
# Token 权限要求：
#   fine-grained PAT：仅授权本仓库 Contents 读写即可；
#   或 classic PAT：勾选 repo 全部权限。
#
# 流程（安全顺序，避免 apkUrl 占位符上线导致更新死循环）：
#   ① 推源码（不含 admin-data.json 版本指针）→ main
#   ② 打 tag v1.8.1 → 触发 GitHub Actions 自动构建两个 APK 并上传 dist/
#   ③ 轮询等待 CI 产物出现（超时 12 分钟）
#   ④ 用真实文件名回填 admin-data.json 的 version.apkUrl / console.apkUrl
#   ⑤ 推送 admin-data.json（版本指针 + 真实链接同步上线 = 老版本收到更新弹窗）
#   ⑥ 校验 apkUrl 可访问
# ============================================================
set -euo pipefail

OWNER="shuting52"
REPO="2026-9-21landezhaole"
BODY_VER="1.8.1"
CONSOLE_VER="2.0.4"
TAG="v1.8.1"
API="https://api.github.com/repos/$OWNER/$REPO"

cd "$(dirname "$0")"

# ---------- 0. Token 检查 ----------
if [ -z "${GITHUB_TOKEN:-}" ]; then
  echo "❌ 未检测到 GITHUB_TOKEN 环境变量。"
  echo "   请在运行前设置：export GITHUB_TOKEN=你的token"
  echo "   或：GITHUB_TOKEN=你的token ./release_v1.8.1.sh"
  exit 1
fi
echo "🔑 Token 已读取（长度 $(echo -n "$GITHUB_TOKEN" | wc -c)，不展示内容）"

# 校验 token 有效且对该仓库有写权限
API_OK=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $GITHUB_TOKEN" "$API")
if [ "$API_OK" != "200" ]; then
  echo "❌ Token 校验失败（HTTP $API_OK）：无法访问 $OWNER/$REPO，请检查 token 权限。"
  exit 1
fi
echo "✅ Token 有效，已授权访问 $OWNER/$REPO"

# ---------- 1. 确认本地改动状态 ----------
if [ -z "$(git status --porcelain)" ]; then
  echo "ℹ️  工作区干净，无改动可提交（将直接进入标签流程）。"
else
  echo "📦 待提交改动："
  git status --short
fi

# ---------- 2. 提交并推送「源码」（先排除 admin-data.json）----------
git add app console-apk merge_sites.py site_data 2>/dev/null || true
git add -u admin-data.json 2>/dev/null || true  # 撤销误加（git add -u 后 unstage）
git reset -q HEAD admin-data.json 2>/dev/null || true
git commit -m "v$BODY_VER: 站点1000+、工具箱52小工具、控制台v$CONSOLE_VER回炉重造(源码先行)" --allow-empty || true
echo "🚀 推送源码到 main..."
git push origin main
echo "✅ 源码已推送"

# ---------- 3. 打 tag 触发 CI 构建 ----------
if git rev-parse "$TAG" >/dev/null 2>&1; then
  echo "⚠️  标签 $TAG 已存在，删除后重建..."
  git push origin :refs/tags/$TAG 2>/dev/null || true
  git tag -d $TAG
fi
git tag -a $TAG -m "v$BODY_VER 发布：站点1000+、工具箱50+、控制台v$CONSOLE_VER"
git push origin $TAG
echo "✅ 已推送 tag $TAG，GitHub Actions 开始构建（约 3-10 分钟）..."

# ---------- 4. 轮询等待 CI 产物 ----------
echo "⏳ 等待 CI 构建产物（最多 12 分钟）..."
BODY_APK=""
CONSOLE_APK=""
for i in $(seq 1 72); do
  sleep 10
  BODY_LIST=$(curl -s -H "Authorization: Bearer $GITHUB_TOKEN" \
    "https://api.github.com/repos/$OWNER/$REPO/contents/dist/apk")
  CONSOLE_LIST=$(curl -s -H "Authorization: Bearer $GITHUB_TOKEN" \
    "https://api.github.com/repos/$OWNER/$REPO/contents/dist/console")
  BODY_APK=$(echo "$BODY_LIST" | python3 -c \
    "import json,sys;d=json.load(sys.stdin);print(next((x['name'] for x in d if x['name'].startswith('landezhao-v$BODY_VER-')),''))" 2>/dev/null || echo "")
  CONSOLE_APK=$(echo "$CONSOLE_LIST" | python3 -c \
    "import json,sys;d=json.load(sys.stdin);print(next((x['name'] for x in d if x['name'].startswith('console-apk-v$CONSOLE_VER-')),''))" 2>/dev/null || echo "")
  if [ -n "$BODY_APK" ] && [ -n "$CONSOLE_APK" ]; then
    echo "✅ 本体包: $BODY_APK"
    echo "✅ 控制台包: $CONSOLE_APK"
    break
  fi
done
if [ -z "$BODY_APK" ] || [ -z "$CONSOLE_APK" ]; then
  echo "❌ 超时未等到产物。请到仓库 Actions 页查看构建日志，"
  echo "   构建成功后手动执行：python3 fix_apk_url.py"
  exit 1
fi

# ---------- 5. 回填 apkUrl ----------
python3 - "$BODY_APK" "$CONSOLE_APK" << 'EOF'
import json, sys
body_apk, console_apk = sys.argv[1], sys.argv[2]
with open('admin-data.json', encoding='utf-8') as f:
    d = json.load(f)
base = "https://raw.githubusercontent.com/shuting52/2026-9-21landezhaole/main/dist/"
d['version']['apkUrl'] = base + 'apk/' + body_apk
d['version']['apkUrlRaw'] = d['version']['apkUrl']
d['console']['apkUrl'] = base + 'console/' + console_apk
with open('admin-data.json', 'w', encoding='utf-8') as f:
    json.dump(d, f, ensure_ascii=False, indent=1)
print('✅ 已回填 apkUrl:')
print('   version.apkUrl  =', d['version']['apkUrl'])
print('   console.apkUrl  =', d['console']['apkUrl'])
EOF

# ---------- 6. 推送 admin-data.json（最终生效：老版本收到更新弹窗）----------
git add admin-data.json
git commit -m "v$BODY_VER 四要素对齐：apkUrl 指向真实安装包（老版本更新推送生效）"
git push origin main
echo "✅ admin-data.json 已推送（version.code=70, name=$BODY_VER）"
echo "   ➜ 所有 1.8.0 及以下版本用户打开 App 将弹出 v$BODY_VER 更新窗！"

# ---------- 7. 校验 apkUrl 可访问 ----------
for u in "$(python3 -c "import json;print(json.load(open('admin-data.json'))['version']['apkUrl'])")" \
         "$(python3 -c "import json;print(json.load(open('admin-data.json'))['console']['apkUrl'])")"; do
  code=$(curl -s -o /dev/null -w "%{http_code}" -L "$u")
  echo "🔗 $u → HTTP $code"
  [ "$code" = "200" ] || echo "⚠️  该链接返回 $code，请检查！"
done

echo ""
echo "🎉 发布完成！老版本本体已能检测到 v1.8.1 更新。"