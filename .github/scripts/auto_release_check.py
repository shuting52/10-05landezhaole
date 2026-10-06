#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Auto Release · 检测与版本号推进（v3：控制台双模式）
在 GitHub Actions 中运行：判断仓库是否有需要发布的改动，
若有则自动推进 versionCode/versionName（写回 app/build.gradle.kts），
并把结果输出到 $GITHUB_OUTPUT 供后续步骤使用。

【写死规则8 · 控制台双模式（2026-10-06 定）】
- 控制台「✅ 应用并实时同步」→ autoRelease.mode = "content"
  → 仅内容改动时不构建、不 bump 版本、不弹更新窗；本体 5 秒轮询实时生效
- 控制台「🚀 发布 / 触发更新」→ autoRelease.mode = "release"
  → 构建新 APK、更新 apkUrl、本体弹出更新窗口
- 源码/构建改动（app/、build.gradle.kts、gradle 等）→ 始终触发自动发布（v2 核心价值保留）

规则（遵循 AGENTS.md「版本严格对齐 / 四要素同步」）：
1. 读取云端 admin-data.json 的 version.code（上次已发布版本）
2. 读取 app/build.gradle.kts 的 versionCode（源码当前版本）
3. 若 admin-data.json 记录 lastBuildSha == 当前 HEAD → 该提交已发布，跳过
4. 分类自 lastBuildSha 以来的改动：
   - 源码/构建类 → 需要发布
   - 仅内容类（admin-data.json 等）→ 仅当 mode=release 才发布
5. 写回 build.gradle.kts 并输出 need=yes / newCode / newName
"""
import json
import os
import re
import subprocess
import sys

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
BUILD_FILE = os.path.join(REPO_ROOT, "app", "build.gradle.kts")
ADMIN_FILE = os.path.join(REPO_ROOT, "admin-data.json")

# 源码/构建类改动：这些改动始终需要发版（自动发布的核心价值）
SRC_PATTERNS = (
    "app/", "build.gradle.kts", "settings.gradle.kts",
    "gradle.properties", "gradle/", "gradlew",
)

def sh(cmd):
    return subprocess.run(cmd, shell=True, capture_output=True, text=True).stdout.strip()

def main():
    # 1. 云端版本（admin-data.json 是唯一真相源）
    with open(ADMIN_FILE, encoding="utf-8") as f:
        admin = json.load(f)
    cloud_code = admin["version"]["code"]
    cloud_name = admin["version"]["name"]
    last_build_sha = admin["version"].get("lastBuildSha", "")
    # 控制台操作模式（写死规则8）：content=内容同步不弹窗 | release=显式发布弹窗
    mode = admin.get("autoRelease", {}).get("mode", "content")

    # 2. 源码版本
    with open(BUILD_FILE, encoding="utf-8") as f:
        bf = f.read()
    m_code = re.search(r"versionCode\s*=\s*(\d+)", bf)
    m_name = re.search(r'versionName\s*=\s*"([^"]+)"', bf)
    if not m_code or not m_name:
        print("::error::无法解析 app/build.gradle.kts 版本号"); sys.exit(1)
    src_code = int(m_code.group(1))
    src_name = m_name.group(1)

    head = sh("git rev-parse HEAD")

    print(f"云端 version: code={cloud_code} name={cloud_name} lastBuildSha={last_build_sha[:10] if last_build_sha else '(无)'} mode={mode}")
    print(f"源码 version: code={src_code} name={src_name} HEAD={head[:10]}")

    # 3. 已发布过该提交 → 跳过
    if last_build_sha and last_build_sha == head:
        print("==> 当前 HEAD 已构建发布，跳过"); write_out(need="no"); return

    # 4. 检查自上次构建以来的仓库内容改动
    EXCLUDE = (
        ".github/", "console-apk/", "src/", "*.md", "metadata.json", ".env.example", ".gitignore",
        "debug.keystore", "*.jks", "auto_release.sh", "merge_sites.py",
        "release_v1.8.1.sh", "add_cloud_browsers.py", "sites_", "gradle-wrapper",
    )
    base = last_build_sha if last_build_sha else ""
    if base:
        # 基线校验：仓库重建/force push 后旧 SHA 会失效，此时回退用 HEAD~1 作为基线
        valid = sh(f"git cat-file -e {base}^{{commit}} 2>/dev/null && echo yes || echo no")
        if valid != "yes":
            print(f"  基线 {base[:10]} 无效（仓库可能重建），回退用 HEAD~1 作为基线")
            base = sh("git rev-parse HEAD~1 2>/dev/null || echo ''").strip()
        if base:
            all_changed = sh(f"git diff --name-only {base}..HEAD 2>/dev/null").splitlines()
        else:
            all_changed = sh("git log --oneline -1 2>/dev/null").splitlines()
    else:
        # 无 lastBuildSha：以仓库内已存在的 admin-data 版本为基准，有内容提交即视为需要发布
        all_changed = sh("git log --oneline -1 2>/dev/null").splitlines()
    # 过滤排除项
    changed = [l for l in all_changed if l.strip() and not any(l.startswith(p) or l.endswith(p.rstrip("/")) for p in EXCLUDE)]
    print("自上次构建以来的内容改动:", len(changed), "个文件" if changed else "(无)")
    if changed:
        print("  示例:", "; ".join(changed[:3]))

    # 无内容改动且源码版本 == 云端版本 → 无需发布
    if not changed and src_code <= cloud_code:
        print("==> 仓库无内容更新，跳过"); write_out(need="no"); return

    # 【写死规则8】分类判定：源码改动始终发版；仅内容改动看控制台 mode
    src_changed = [l for l in changed if l.startswith(SRC_PATTERNS)]
    content_changed = [l for l in changed if not l.startswith(SRC_PATTERNS)]

    if src_changed:
        print(f"==> 检测到源码/构建改动 {len(src_changed)} 个文件 -> 需要发布")
    elif content_changed and mode == "release":
        print(f"==> 控制台显式发布（mode=release），内容改动 {len(content_changed)} 个 -> 需要发布")
    else:
        if content_changed:
            print(f"==> 仅内容同步（mode={mode}）：不 bump 版本、不弹更新窗，本体已实时生效")
        print("==> 无需发布，跳过"); write_out(need="no"); return

    # 5. 推进版本
    if src_code <= cloud_code:
        new_code = cloud_code + 1
        # patch+1
        parts = cloud_name.split(".")
        try:
            parts[-1] = str(int(parts[-1]) + 1)
        except ValueError:
            pass
        new_name = ".".join(parts)
    else:
        new_code = src_code
        new_name = src_name

    # 写回 build.gradle.kts（保持注释与四要素同步）
    nf = re.sub(r"versionCode\s*=\s*\d+", f"versionCode = {new_code}", bf)
    nf = re.sub(r'versionName\s*=\s*"[^"]+"', f'versionName = "{new_name}"', nf)
    with open(BUILD_FILE, "w", encoding="utf-8") as f:
        f.write(nf)
    print(f"==> 版本推进: {src_code}/{src_name} -> {new_code}/{new_name}")

    write_out(need="yes", new_code=new_code, new_name=new_name)

def write_out(need, new_code="", new_name=""):
    gh = os.environ.get("GITHUB_OUTPUT")
    lines = [f"need={need}"]
    if new_code: lines.append(f"new_code={new_code}")
    if new_name: lines.append(f"new_name={new_name}")
    if gh:
        with open(gh, "a") as f:
            f.write("\n".join(lines) + "\n")
    else:
        # 本地模式：写固定输出文件供 auto_release.sh 读取
        with open("/tmp/auto_release_out.txt", "w") as f:
            f.write("\n".join(lines) + "\n")
        print("\n".join(lines))

if __name__ == "__main__":
    main()
