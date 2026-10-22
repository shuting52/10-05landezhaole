#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Auto Release · 检测与版本号推进
在 GitHub Actions 中运行：判断 app 源码、构建配置、后台 JSON 或站点批次是否有新提交需要发布，
若有则自动推进 versionCode/versionName（写回 app/build.gradle.kts）；站点批次会先合并进 admin-data.json，
并把结果输出到 $GITHUB_OUTPUT 供后续步骤使用。

规则（遵循 AGENTS.md「版本严格对齐 / 四要素同步」）：
1. 读取云端 admin-data.json 的 version.code（上次已发布版本）
2. 读取 app/build.gradle.kts 的 versionCode（源码当前版本）
3. 若 admin-data.json 记录 lastBuildSha == 当前 HEAD → 该提交已发布，跳过
4. 若自 lastBuildSha 以来 app/**、构建配置、admin-data.json 或 sites_p*.json 有改动 → 需要发布：
   - 若源码 versionCode <= 云端 code → versionCode+1 且 versionName patch+1
   - 若源码 versionCode > 云端 code（开发者已手动升过）→ 直接采用源码版本
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
MERGE_SITES_FILE = os.path.join(REPO_ROOT, "merge_sites.py")
RELEASE_PATHS = [
    "app", "build.gradle.kts", "settings.gradle.kts", "gradle.properties",
    "gradle/", "gradlew", "gradlew.bat", "admin-data.json", "sites_p*.json",
]
SITE_BATCH_RE = re.compile(r"sites_p\d+.*\.json$")

def sh(cmd):
    return subprocess.run(cmd, shell=True, capture_output=True, text=True).stdout.strip()

def changed_files(base, head):
    """List release-relevant paths between the saved baseline and this push."""
    if base:
        args = ["git", "diff", "--name-only", f"{base}..{head}", "--", *RELEASE_PATHS]
    else:
        # First tracked release: use the current commit without assuming a previous build SHA.
        args = [
            "git", "diff-tree", "--root", "--no-commit-id", "--name-only",
            "-r", head, "--", *RELEASE_PATHS,
        ]
    result = subprocess.run(args, cwd=REPO_ROOT, capture_output=True, text=True)
    if result.returncode != 0:
        print("::error::无法比较发布基线与当前提交：", result.stderr.strip())
        sys.exit(1)
    return [line.strip() for line in result.stdout.splitlines() if line.strip()]

def main():
    # 1. 云端版本（admin-data.json 是唯一真相源）
    with open(ADMIN_FILE, encoding="utf-8") as f:
        admin = json.load(f)
    cloud_code = admin["version"]["code"]
    cloud_name = admin["version"]["name"]
    last_build_sha = admin["version"].get("lastBuildSha", "")

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

    print(f"云端 version: code={cloud_code} name={cloud_name} lastBuildSha={last_build_sha[:10] if last_build_sha else '(无)'}")
    print(f"源码 version: code={src_code} name={src_name} HEAD={head[:10]}")

    # 3. 已发布过该提交 → 跳过
    if last_build_sha and last_build_sha == head:
        print("==> 当前 HEAD 已构建发布，跳过"); write_out(need="no"); return

    # 4. 检查源码、构建配置和云端内容数据自上次发布以来是否有改动。
    base = last_build_sha if last_build_sha else ""
    if base:
        check = subprocess.run(
            ["git", "cat-file", "-e", f"{base}^{{commit}}"],
            cwd=REPO_ROOT, capture_output=True, text=True,
        )
        if check.returncode != 0:
            print(f"  基线 {base[:10]} 无效（仓库可能重建），回退用 HEAD~1 作为基线")
            parent = subprocess.run(
                ["git", "rev-parse", "--verify", f"{head}~1"],
                cwd=REPO_ROOT, capture_output=True, text=True,
            )
            base = parent.stdout.strip() if parent.returncode == 0 else ""

    changed = changed_files(base, head)
    site_batches = [path for path in changed if SITE_BATCH_RE.fullmatch(os.path.basename(path))]
    if site_batches:
        print("检测到站点批次更新，先按 URL 去重并合并到 admin-data.json：", ", ".join(site_batches))
        merge = subprocess.run(
            [sys.executable, MERGE_SITES_FILE, *(os.path.basename(path) for path in site_batches)],
            cwd=REPO_ROOT, capture_output=True, text=True,
        )
        if merge.stdout:
            print(merge.stdout, end="" if merge.stdout.endswith("\n") else "\n")
        if merge.stderr:
            print(merge.stderr, file=sys.stderr, end="" if merge.stderr.endswith("\n") else "\n")
        if merge.returncode != 0:
            print("::error::站点批次合并失败，停止本次发布")
            sys.exit(merge.returncode)

    print("自上次构建以来的发布相关改动:", len(changed), "个文件" if changed else "(无)")
    if changed:
        print("  示例:", "; ".join(changed[:3]))

    # 无发布相关内容改动且源码版本 == 云端版本 → 无需发布
    if not changed and src_code <= cloud_code:
        print("==> app 源码、构建配置与内容 JSON 均无新改动，跳过"); write_out(need="no"); return

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
