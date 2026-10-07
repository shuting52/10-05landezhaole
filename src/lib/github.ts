// ============================================================
// GitHub API 底层封装
// 对接《管理工作台对接规范 V1》§3 —— 仓库即云端数据中枢
// ============================================================

const DEFAULT_OWNER = "shuting52";
const DEFAULT_REPO = "10-05landezhaole";
const DEFAULT_BRANCH = "main";
const CONFIG_PATH = "admin-data.json";

export interface GithubConfig {
  owner: string;
  repo: string;
  branch: string;
}

// ---------- Token 管理（localStorage，绝不硬编码/入库） ----------
export function getToken(): string {
  try {
    return localStorage.getItem("lzdz_gh_token") || "";
  } catch {
    return "";
  }
}

export function setToken(token: string) {
  try {
    localStorage.setItem("lzdz_gh_token", token.trim());
  } catch {
    /* ignore */
  }
}

export function getConfig(): GithubConfig {
  try {
    const raw = localStorage.getItem("lzdz_gh_config");
    if (raw) {
      const c = JSON.parse(raw);
      return { owner: c.owner || DEFAULT_OWNER, repo: c.repo || DEFAULT_REPO, branch: c.branch || DEFAULT_BRANCH };
    }
  } catch {
    /* ignore */
  }
  return { owner: DEFAULT_OWNER, repo: DEFAULT_REPO, branch: DEFAULT_BRANCH };
}

export function setConfig(cfg: GithubConfig) {
  try {
    localStorage.setItem("lzdz_gh_config", JSON.stringify(cfg));
  } catch {
    /* ignore */
  }
}

function apiBase() {
  return "https://api.github.com";
}

function headers(withAuth = true): Record<string, string> {
  const h: Record<string, string> = {
    Accept: "application/vnd.github+json",
    "X-GitHub-Api-Version": "2022-11-28",
  };
  if (withAuth) {
    const token = getToken();
    if (token) h.Authorization = `Bearer ${token}`;
  }
  return h;
}

/** 读取文件（Contents API，返回解码后的文本与 sha） */
export async function ghReadText(path: string): Promise<{ content: string; sha: string }> {
  const { owner, repo, branch } = getConfig();
  const url = `${apiBase()}/repos/${owner}/${repo}/contents/${path}?ref=${branch}`;
  const r = await fetch(url, { headers: headers(true) });
  if (!r.ok) throw new Error(`读取失败 HTTP ${r.status}: ${path}`);
  const d = await r.json();
  // base64 → UTF-8 安全解码
  const binary = atob(d.content);
  const bytes = Uint8Array.from(binary, (c) => c.charCodeAt(0));
  const text = new TextDecoder("utf-8").decode(bytes);
  return { content: text, sha: d.sha };
}

/** 读取文件（走镜像链兜底，无 token 也行；用于「只读浏览/连接诊断」） */
export async function ghReadMirror(path: string): Promise<{ content: string; sha: string }> {
  const { owner, repo, branch } = getConfig();
  const cacheBust = new Date().toISOString().slice(0, 10).replace(/-/g, "");
  const mirrors = [
    `https://testingcf.jsdelivr.net/gh/${owner}/${repo}@${branch}/${path}?v=${cacheBust}`,
    `https://cdn.jsdelivr.net/gh/${owner}/${repo}@${branch}/${path}?v=${cacheBust}`,
    `https://fastly.jsdelivr.net/gh/${owner}/${repo}@${branch}/${path}?v=${cacheBust}`,
    `https://gcore.jsdelivr.net/gh/${owner}/${repo}@${branch}/${path}?v=${cacheBust}`,
    `https://ghfast.top/https://raw.githubusercontent.com/${owner}/${repo}/${branch}/${path}`,
    `https://ghproxy.net/https://raw.githubusercontent.com/${owner}/${repo}/${branch}/${path}`,
    `https://raw.gitmirror.com/${owner}/${repo}/${branch}/${path}`,
    `https://raw.githubusercontent.com/${owner}/${repo}/${branch}/${path}`,
  ];
  let lastErr = "全部镜像不可用";
  for (const url of mirrors) {
    try {
      const ctrl = new AbortController();
      const timer = setTimeout(() => ctrl.abort(), 12000);
      const r = await fetch(url, { signal: ctrl.signal });
      clearTimeout(timer);
      if (!r.ok) continue;
      const text = await r.text();
      if (looksLikeHtml(text)) {
        lastErr = `镜像被劫持: ${url}`;
        continue;
      }
      // 尝试解析 JSON 验证合法性
      try {
        JSON.parse(text);
      } catch {
        lastErr = `非 JSON: ${url}`;
        continue;
      }
      return { content: text, sha: "" };
    } catch {
      continue;
    }
  }
  throw new Error(lastErr);
}

/** HTML 劫持检测 */
export function looksLikeHtml(text: string): boolean {
  const t = text.trim().toLowerCase();
  return t.startsWith("<!doctype") || t.startsWith("<html") || t.startsWith("<head") || t.startsWith("<body");
}

function b64EncodeUtf8(str: string): string {
  const bytes = new TextEncoder().encode(str);
  let bin = "";
  for (const b of bytes) bin += String.fromCharCode(b);
  return btoa(bin);
}

/** 写入文件（Contents API PUT，带 sha 乐观锁 + 409 冲突重试） */
export async function ghWriteText(path: string, content: string, sha: string, message: string): Promise<string> {
  const token = getToken();
  if (!token) throw new Error("请先在「系统设置」中配置 GitHub Token");
  const { owner, repo } = getConfig();
  const url = `${apiBase()}/repos/${owner}/${repo}/contents/${path}`;
  const body = { message, content: b64EncodeUtf8(content), sha };
  const r = await fetch(url, {
    method: "PUT",
    headers: { ...headers(true), "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!r.ok) {
    const t = await r.text();
    throw new Error(`写入失败 HTTP ${r.status}: ${t.slice(0, 200)}`);
  }
  const d = await r.json();
  return d.content.sha;
}

/** 带 409 冲突重试的写入（并发安全） */
export async function ghWriteWithRetry(
  path: string,
  build: (cur: string) => string,
  message: string,
  maxRetry = 5
): Promise<string> {
  for (let attempt = 0; attempt < maxRetry; attempt++) {
    try {
      const { content, sha } = await ghReadText(path);
      const next = build(content);
      return await ghWriteText(path, next, sha, message);
    } catch (e) {
      const msg = (e as Error).message || "";
      if (msg.includes("409")) {
        await new Promise((r) => setTimeout(r, 3000));
        continue;
      }
      throw e;
    }
  }
  throw new Error("写入冲突重试次数超限");
}

/** 上传二进制（APK 等大文件走 Contents API，base64 分块不需要，GitHub 支持 <=100MB） */
export async function ghUploadBinary(path: string, base64Content: string, message: string): Promise<string> {
  const token = getToken();
  if (!token) throw new Error("请先配置 GitHub Token");
  const { owner, repo } = getConfig();
  // 先查是否已存在（存在则带 sha）
  let sha = "";
  try {
    const r = await fetch(`${apiBase()}/repos/${owner}/${repo}/contents/${path}`, { headers: headers(true) });
    if (r.ok) {
      const d = await r.json();
      sha = d.sha || "";
    }
  } catch {
    /* 新文件 */
  }
  const body: Record<string, string> = { message, content: base64Content };
  if (sha) body.sha = sha;
  const r = await fetch(`${apiBase()}/repos/${owner}/${repo}/contents/${path}`, {
    method: "PUT",
    headers: { ...headers(true), "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!r.ok) {
    const t = await r.text();
    throw new Error(`上传失败 HTTP ${r.status}: ${t.slice(0, 200)}`);
  }
  const d = await r.json();
  return d.content.sha;
}

/** CDN 刷新（写入后调用，本体秒级生效） */
export async function purgeCdn(path = CONFIG_PATH): Promise<boolean> {
  const { owner, repo, branch } = getConfig();
  const url = `https://purge.jsdelivr.net/gh/${owner}/${repo}@${branch}/${path}`;
  try {
    const r = await fetch(url);
    return r.ok || r.status === 200;
  } catch {
    return false;
  }
}

export { CONFIG_PATH };
