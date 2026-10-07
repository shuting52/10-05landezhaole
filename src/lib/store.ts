// ============================================================
// 全局状态 store：连接、数据加载、应用/发布双模式（写死规则8）
// ============================================================

import { CONFIG_PATH, ghReadMirror, ghReadText, ghWriteText, purgeCdn } from "./github";
import type { AdminData } from "@/types/admin";

export type ConnState = "idle" | "loading" | "connected" | "readonly" | "error";

export interface StoreState {
  state: ConnState;
  admin: AdminData | null;
  sha: string;
  error: string;
  mode: "content" | "release";
  lastSyncAt: string;
}

const listeners = new Set<(s: StoreState) => void>();

let current: StoreState = {
  state: "idle",
  admin: null,
  sha: "",
  error: "",
  mode: "content",
  lastSyncAt: "",
};

export function getStore(): StoreState {
  return current;
}

export function subscribe(fn: (s: StoreState) => void): () => void {
  listeners.add(fn);
  fn(current);
  return () => listeners.delete(fn);
}

function set(partial: Partial<StoreState>) {
  current = { ...current, ...partial };
  listeners.forEach((fn) => fn(current));
}

/** 加载 admin-data.json（优先 GitHub API，失败走镜像链只读） */
export async function loadAdmin(preferMirror = false): Promise<void> {
  set({ state: "loading", error: "" });
  try {
    if (preferMirror) {
      const { content } = await ghReadMirror(CONFIG_PATH);
      const admin = JSON.parse(content) as AdminData;
      set({ admin, sha: "", state: "readonly", lastSyncAt: new Date().toLocaleString("zh-CN") });
      return;
    }
    const { content, sha } = await ghReadText(CONFIG_PATH);
    const admin = JSON.parse(content) as AdminData;
    const mode = admin.autoRelease?.mode === "release" ? "release" : "content";
    set({ admin, sha, state: "connected", mode, lastSyncAt: new Date().toLocaleString("zh-CN") });
  } catch (e) {
    // 带 token 直读失败 → 尝试镜像只读
    try {
      const { content } = await ghReadMirror(CONFIG_PATH);
      const admin = JSON.parse(content) as AdminData;
      const mode = admin.autoRelease?.mode === "release" ? "release" : "content";
      set({ admin, sha: "", state: "readonly", mode, error: (e as Error).message });
      return;
    } catch (e2) {
      set({ state: "error", error: (e2 as Error).message });
      return;
    }
  }
}

/**
 * 应用并实时同步（写死规则8：mode=content）
 * 只写内容，不 bump 版本，不弹更新窗；本体轮询实时生效
 */
export async function applyToDevice(): Promise<void> {
  if (!current.admin) throw new Error("尚未加载数据");
  const next: AdminData = {
    ...current.admin,
    autoRelease: {
      ...(current.admin.autoRelease || { enabled: true, repo: "", workflow: "", lastRelease: { version: "", code: 0, at: "", sha: "" } }),
      mode: "content",
    },
  };
  const msg = `console: 应用并实时同步 ${new Date().toLocaleString("zh-CN")}`;
  const newSha = await ghWriteText(CONFIG_PATH, JSON.stringify(next, null, 2), current.sha, msg);
  await purgeCdn();
  set({ admin: next, sha: newSha, mode: "content", lastSyncAt: new Date().toLocaleString("zh-CN") });
}

/**
 * 发布新版本（写死规则8：mode=release）
 * bump version.code/name，写入 changelog，本体弹更新窗
 */
export async function publishRelease(changeDesc?: string): Promise<{ name: string; code: number }> {
  if (!current.admin) throw new Error("尚未加载数据");
  const admin = current.admin;
  const oldCode = Number(admin.version?.code) || 1;
  const oldName = admin.version?.name || "1.0";
  const parts = oldName.split(".");
  if (parts.length > 1) {
    parts[parts.length - 1] = String(Number(parts[parts.length - 1] || 0) + 1);
  } else {
    parts.push("1");
  }
  const newName = parts.join(".");
  const newCode = oldCode + 1;

  const meaningful = (line: string) => line && !["console:", "release:", "ci:", "merge", "chore", "docs:"].some((k) => line.includes(k));

  const changes = (admin.version?.changelog || []).filter(meaningful);
  if (changeDesc && meaningful(changeDesc)) changes.unshift(changeDesc);

  const next: AdminData = {
    ...admin,
    version: {
      ...admin.version,
      code: newCode,
      name: newName,
      changelog: ["✨ v" + newName + " 更新来啦～", ...changes].slice(0, 12),
      force: false,
    },
    updateDialog: {
      ...(admin.updateDialog || {}),
      title: `懒得找了 v${newName} 已上线`,
      changelog: ["✨ v" + newName + " 更新来啦～", ...changes].slice(0, 10),
    },
    autoRelease: {
      ...(admin.autoRelease || { enabled: true, repo: "", workflow: "", lastRelease: { version: "", code: 0, at: "", sha: "" } }),
      mode: "release",
    },
  };
  const msg = `console: [发布新版本 v${newName} code:${newCode}] ${changeDesc || "更新"}`;
  const newSha = await ghWriteText(CONFIG_PATH, JSON.stringify(next, null, 2), current.sha, msg);
  await purgeCdn();
  set({ admin: next, sha: newSha, mode: "release", lastSyncAt: new Date().toLocaleString("zh-CN") });
  return { name: newName, code: newCode };
}

/** 保存 admin 数据（通用写回，mode 由参数决定） */
export async function saveAdmin(admin: AdminData, opts?: { message?: string; mode?: "content" | "release" }): Promise<void> {
  const next: AdminData = {
    ...admin,
    autoRelease: {
      ...(admin.autoRelease || { enabled: true, repo: "", workflow: "", lastRelease: { version: "", code: 0, at: "", sha: "" } }),
      mode: opts?.mode || "content",
    },
  };
  const msg = opts?.message || `console: 应用并实时同步 ${new Date().toLocaleString("zh-CN")}`;
  const newSha = await ghWriteText(CONFIG_PATH, JSON.stringify(next, null, 2), current.sha, msg);
  await purgeCdn();
  set({ admin: next, sha: newSha, mode: opts?.mode || "content", lastSyncAt: new Date().toLocaleString("zh-CN") });
}
