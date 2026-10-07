#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Auto Release · 发布（全 API 版，v2）
在 GitHub Actions 中运行（构建成功后）：
1. 生成 changelog（自上次发布以来的 commit 摘要）
2. 用 GitHub Contents API（带 sha 乐观锁，冲突自动重试）：
   - 更新本仓库 admin-data.json（version 四要素 + updateDialog + autoRelease + lastBuildSha）
   - 更新本仓库 app/build.gradle.kts（版本号 bump 同步到云端）
   - 上传本仓库 APK 到 dist/apk/
3. 若配置 RELEASE_PAT → 同步 landezhaole10-02（App 实际读取源）
   admin-data.json + dist/apk，并 purge jsDelivr CDN

v2 说明：不再使用 git commit/push 发布（会与控制台并发冲突），
全部走 GitHub API 更新，409 冲突时拉取最新 sha 重试，天然无冲突。
"""
import base64
import json
import os
import re
import shutil
import subprocess
import sys
import time
import urllib.request

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
ADMIN_FILE = os.path.join(REPO_ROOT, "admin-data.json")
BUILD_FILE = os.path.join(REPO_ROOT, "app", "build.gradle.kts")
APK_SRC = os.path.join(REPO_ROOT, "app", "build", "outputs", "apk", "release", "app-release.apk")
OWNER = "shuting52"
REPO = "10-05landezhaole"
OLD_REPO = "landezhaole10-02"   # App 实际读取的数据源仓库
BRANCH = "main"

def sh(cmd):
    return subprocess.run(cmd, shell=True, capture_output=True, text=True)

def api(url, method="GET", token="", payload=None):
    req = urllib.request.Request(url, method=method)
    req.add_header("Accept", "application/vnd.github+json")
    if token:
        req.add_header("Authorization", f"Bearer {token}")
    if payload is not None:
        req.add_header("Content-Type", "application/json")
        req.data = json.dumps(payload).encode()
    try:
        with urllib.request.urlopen(req, timeout=60) as r:
            return r.status, json.loads(r.read().decode())
    except urllib.error.HTTPError as e:
        try:
            return e.code, json.loads(e.read().decode() or "{}")
        except Exception:
            return e.code, {}
    except Exception as e:
        return 0, {"message": str(e)}

def api_put_with_retry(path, build_content, msg, token, max_retry=5):
    """Contents API PUT：带 sha 乐观锁，409 冲突时拉最新 sha 重试"""
    for attempt in range(max_retry):
        st, d = api(f"https://api.github.com/repos/{OWNER}/{REPO}/contents/{path}?ref={BRANCH}", "GET", token)
        if st != 200:
            return st, d
        sha = d["sha"]
        body = base64.b64encode(build_content.encode("utf-8")).decode()
        st2, d2 = api(f"https://api.github.com/repos/{OWNER}/{REPO}/contents/{path}", "PUT", token,
                      {"message": msg, "content": body, "sha": sha})
        if st2 == 200 or st2 == 201:
            return st2, d2
        if st2 == 409 and attempt < max_retry - 1:
            print(f"  冲突({st2})，拉取最新后重试 {attempt+2}/{max_retry}...")
            time.sleep(3)
            continue
        return st2, d2
    return 0, {"message": "max retry"}

def get_version_from_gradle():
    with open(BUILD_FILE, encoding="utf-8") as f:
        bf = f.read()
    code = re.search(r"versionCode\s*=\s*(\d+)", bf).group(1)
    name = re.search(r'versionName\s*=\s*"([^"]+)"', bf).group(1)
    return code, name

def build_changelog(base_sha):
    """自 base_sha 以来的 commit 摘要（过滤机器垃圾）"""
    r = sh(f"git log --oneline {base_sha}..HEAD -- app/ build.gradle.kts settings.gradle.kts gradle.properties 2>/dev/null | head -30")
    lines = [l for l in r.stdout.splitlines() if l.strip()]
    items = []
    skip_kw = ["console:", "release:", "ci:", "merge", "Merge", "jekyll", "docs:", "chore"]
    for l in lines:
        msg = re.sub(r"^[0-9a-f]{7,}\s*", "", l).strip()
        if any(k in msg for k in skip_kw):
            continue
        items.append(msg)
    return items or ["本次更新包含多项优化与修复"]

def build_new_admin_data(code, name, apk_url, apk_url_raw, changelog, head_sha):
    """构造本仓库新 admin-data.json 内容（字符串）"""
    with open(ADMIN_FILE, encoding="utf-8") as f:
        admin = json.load(f)
    admin["version"].update({
        "code": int(code), "name": name, "changelog": changelog,
        "force": False,
        "apkUrl": apk_url, "apkUrlRaw": apk_url_raw,
        "lastBuildSha": head_sha,
    })
    admin["updateDialog"]["title"] = f"懒得找了 v{name} 已上线"
    admin["updateDialog"]["changelog"] = changelog
    admin.setdefault("autoRelease", {})
    admin["autoRelease"].update({
        "enabled": True,
        "repo": REPO,
        "workflow": "Auto Release (自动发布新版本)",
        "lastRelease": {
            "version": name,
            "code": int(code),
            "at": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime()),
            "sha": head_sha,
        },
    })
    return json.dumps(admin, ensure_ascii=False, indent=2)

def upload_apk(token, target_repo, apk_path, apk_name, msg):
    apk_b64 = base64.b64encode(open(apk_path, "rb").read()).decode()
    st, d = api(f"https://api.github.com/repos/{OWNER}/{target_repo}/contents/dist/apk/{apk_name}", "PUT", token,
                {"message": msg, "content": apk_b64})
    return st, d

def main():
    gh_token = os.environ.get("GH_TOKEN", "")
    pat = os.environ.get("RELEASE_PAT", "")
    if not gh_token:
        print("::error::缺少 GH_TOKEN"); sys.exit(1)

    code, name = get_version_from_gradle()
    head = sh("git rev-parse HEAD").stdout.strip()

    # 1. changelog
    base_sha = ""
    with open(ADMIN_FILE, encoding="utf-8") as f:
        base_sha = json.load(f)["version"].get("lastBuildSha", "")
    changelog = [f"✨ v{name} 更新来啦～"] + build_changelog(base_sha or "HEAD~1")

    # 2. APK 命名
    if not os.path.exists(APK_SRC):
        print("::error::未找到构建产物", APK_SRC); sys.exit(1)
    ts = str(int(time.time()))
    apk_name = f"landezhao-v{name}-{ts}.apk"
    print("APK:", APK_SRC, os.path.getsize(APK_SRC), "bytes ->", apk_name)

    apk_url = f"https://raw.githubusercontent.com/{OWNER}/{REPO}/main/dist/apk/{apk_name}"
    apk_url_raw = f"https://github.com/{OWNER}/{REPO}/raw/main/dist/apk/{apk_name}"

    # 3. 上传本仓库 APK
    st, d = upload_apk(gh_token, REPO, APK_SRC, apk_name, f"release: v{name} 本体 APK")
    print("本仓库 APK 上传:", st, d.get("content", {}).get("name", d.get("message", "")))
    if st not in (200, 201):
        print("::error::本仓库 APK 上传失败"); sys.exit(1)

    # 4. API 更新本仓库 admin-data.json（sha 乐观锁 + 冲突重试）
    new_admin = build_new_admin_data(code, name, apk_url, apk_url_raw, changelog, head)
    st, d = api_put_with_retry("admin-data.json", new_admin,
                               f"release: v{name}（versionCode {code}）自动发布", gh_token)
    print("本仓库 admin-data 更新:", st, d.get("content", {}).get("name", d.get("message", "")))
    if st not in (200, 201):
        print("::error::本仓库 admin-data 更新失败"); sys.exit(1)

    # 5. API 同步 build.gradle.kts 版本号到云端（保证下次 check 基线一致）
    with open(BUILD_FILE, encoding="utf-8") as f:
        bf = f.read()
    st, d = api_put_with_retry("app/build.gradle.kts", bf,
                               f"release: v{name}（versionCode {code}）版本号", gh_token)
    print("本仓库 build.gradle.kts 更新:", st, d.get("content", {}).get("name", d.get("message", "")))

    # 6. 同步 landezhaole10-02（App 读取源）——需 RELEASE_PAT
    if pat:
        print("==> 检测到 RELEASE_PAT，同步 landezhaole10-02 ...")
        # 6.1 上传 APK
        st, d = upload_apk(pat, OLD_REPO, APK_SRC, apk_name, f"release: v{name} 本体 APK")
        print("旧仓库 APK 上传:", st, d.get("content", {}).get("name", d.get("message", "")))
        # 6.2 更新 admin-data.json（old repo 的 apkUrl 指向 old repo 自身）
        for attempt in range(5):
            st, d = api(f"https://api.github.com/repos/{OWNER}/{OLD_REPO}/contents/admin-data.json?ref={BRANCH}", "GET", pat)
            if st != 200:
                break
            old_sha = d["sha"]
            old_adm = json.loads(base64.b64decode(d["content"]).decode())
            old_adm["version"].update({
                "code": int(code), "name": name, "changelog": changelog, "force": False,
                "apkUrl": f"https://raw.githubusercontent.com/{OWNER}/{OLD_REPO}/main/dist/apk/{apk_name}",
                "apkUrlRaw": f"https://github.com/{OWNER}/{OLD_REPO}/raw/main/dist/apk/{apk_name}",
            })
            old_adm["updateDialog"]["title"] = f"懒得找了 v{name} 已上线"
            old_adm["updateDialog"]["changelog"] = changelog
            old_adm.setdefault("autoRelease", {})
            old_adm["autoRelease"].update({
                "enabled": True,
                "repo": REPO,
                "workflow": "Auto Release (自动发布新版本)",
                "lastRelease": {
                    "version": name,
                    "code": int(code),
                    "at": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime()),
                    "sha": head,
                },
            })
            body = base64.b64encode(json.dumps(old_adm, ensure_ascii=False, indent=2).encode()).decode()
            st2, d2 = api(f"https://api.github.com/repos/{OWNER}/{OLD_REPO}/contents/admin-data.json", "PUT", pat,
                          {"message": f"release: v{name} 四要素同步（code {code}）", "content": body, "sha": old_sha})
            if st2 in (200, 201):
                print("旧仓库 admin-data 更新:", st2); break
            if st2 == 409 and attempt < 4:
                print(f"  旧仓库冲突({st2})，重试 {attempt+2}/5..."); time.sleep(3); continue
            print("旧仓库 admin-data 更新失败:", st2, d2.get("message", "")); break
        # 6.3 purge jsDelivr
        subprocess.run(["curl", "-s", "-A", "Mozilla/5.0", "-o", "/dev/null",
                        "-w", "purge http=%{http_code}\n",
                        f"https://purge.jsdelivr.net/gh/{OWNER}/{OLD_REPO}@main/admin-data.json"], timeout=30)
        subprocess.run(["curl", "-s", "-A", "Mozilla/5.0", "-o", "/dev/null",
                        "-w", "purge2 http=%{http_code}\n",
                        f"https://purge.jsdelivr.net/gh/{OWNER}/{OLD_REPO}@main/admin-data.json?v=20261004"], timeout=30)
        print("旧仓库同步 + CDN purge 完成")
    else:
        print("==> 未配置 RELEASE_PAT，跳过旧仓库同步（本仓库已发布，可用控制台手动同步）")

    print(f"==> 发布完成: v{name} (code {code}) apkUrl={apk_url}")

if __name__ == "__main__":
    main()
