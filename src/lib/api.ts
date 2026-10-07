// ============================================================
// 数据访问层（真实 GitHub API 版）
// 对接《管理工作台对接规范 V1》—— 保持函数签名不变，页面组件无需改动
// 数据源：admin-data.json（GitHub 仓库即云端数据中枢）
// ============================================================

import type {
  ActivityLog,
  CardQuery,
  Category,
  Paginated,
  ResourceButton,
  ResourceCard,
  SkillItem,
  StatItem,
  TextItem,
} from "@/types";
import { getStore, loadAdmin, saveAdmin } from "./store";
import {
  buildStats,
  cardToResourceCard,
  categoryToModel,
  resourceCardToCard,
  softwareToButton,
  uiTextToModel,
} from "./mapper";

/**
 * 从 store 获取 admin 数据（未加载则自动加载一次）
 */
async function ensureAdmin() {
  const s = getStore();
  if (!s.admin) {
    await loadAdmin();
  }
  const st = getStore();
  if (!st.admin) {
    throw new Error(st.error || "数据加载失败，请检查网络或在「系统设置」中配置 Token");
  }
  return st.admin;
}

/** 全部卡片（扁平化，按分类归类） */
function collectCards() {
  const admin = getStore().admin!;
  const cats = admin.home?.categories || [];
  const out: ResourceCard[] = [];
  for (const c of cats) {
    for (const card of c.cards || []) {
      out.push(cardToResourceCard(card, c));
    }
  }
  return out;
}

export const api = {
  async getStats(): Promise<StatItem[]> {
    const admin = await ensureAdmin();
    return buildStats(admin);
  },

  async getActivityLogs(): Promise<ActivityLog[]> {
    await ensureAdmin();
    const s = getStore();
    return [
      {
        id: "log-sync",
        operator: "系统",
        avatarColor: "#C63C32",
        action: "update",
        content: `数据已同步（${s.lastSyncAt || "-"}）`,
        time: s.lastSyncAt || "-",
      },
      {
        id: "log-mode",
        operator: "系统",
        avatarColor: "#C99A3D",
        action: "publish",
        content: s.mode === "release" ? "当前为发布模式（发布将弹更新窗）" : "当前为内容同步模式（应用不弹窗）",
        time: s.lastSyncAt || "-",
      },
    ];
  },

  async getOperationLogs(): Promise<ActivityLog[]> {
    const admin = await ensureAdmin();
    const ar = admin.autoRelease;
    return [
      {
        id: "op-release",
        operator: "系统",
        avatarColor: "#C63C32",
        action: "publish",
        content: `最近自动发布 v${ar.lastRelease?.version || "-"} (code ${ar.lastRelease?.code ?? "-"})`,
        time: ar.lastRelease?.at || "-",
        ip: ar.repo || "-",
      },
      {
        id: "op-mode",
        operator: "系统",
        avatarColor: "#193B3D",
        action: "update",
        content: `双模式：autoRelease.mode=${getStore().mode}`,
        time: getStore().lastSyncAt || "-",
      },
    ];
  },

  async getCards(query: CardQuery = {}): Promise<Paginated<ResourceCard>> {
    await ensureAdmin();
    const { keyword = "", status = "all", category = "all", page = 1, pageSize = 6 } = query;
    const kw = keyword.trim().toLowerCase();

    let list = collectCards().filter((c) => {
      const matchKeyword =
        !kw ||
        c.name.toLowerCase().includes(kw) ||
        c.description.toLowerCase().includes(kw) ||
        c.category.toLowerCase().includes(kw);
      const matchStatus = status === "all" || c.status === status;
      const matchCategory = category === "all" || c.category === category;
      return matchKeyword && matchStatus && matchCategory;
    });

    const total = list.length;
    const start = (page - 1) * pageSize;
    list = list.slice(start, start + pageSize);
    return { items: list, total, page, pageSize };
  },

  async getAllCards(): Promise<ResourceCard[]> {
    await ensureAdmin();
    return collectCards();
  },

  async getButtons(): Promise<ResourceButton[]> {
    const admin = await ensureAdmin();
    return (admin.software || []).map(softwareToButton);
  },

  async getTexts(): Promise<TextItem[]> {
    const admin = await ensureAdmin();
    const items: TextItem[] = [];
    // UI 文本覆盖
    for (const [k, v] of Object.entries(admin.uiText || {})) {
      items.push(uiTextToModel(k, v));
    }
    // 关键文案（settings / marquee / welcome）
    const extra: Array<[string, string]> = [
      ["appName", admin.settings?.appName || ""],
      ["slogan", admin.settings?.slogan || ""],
      ["marquee.defaultText", admin.marquee?.defaultText || ""],
      ["welcome.welcomeText", admin.welcome?.welcomeText || ""],
      ["updateDialog.title", admin.updateDialog?.title || ""],
    ];
    for (const [k, v] of extra) {
      if (v) items.push(uiTextToModel(k, v));
    }
    return items;
  },

  async getCategories(): Promise<Category[]> {
    const admin = await ensureAdmin();
    const cats = admin.home?.categories || [];
    const allCards = collectCards().map((c) => c._raw as never);
    return cats.map((c) => categoryToModel(c, allCards as never[]));
  },

  // ---------- 写操作（管理台改动 → 应用/发布到云端） ----------

  /** 保存卡片（新增/编辑），返回新卡片 */
  async saveCard(card: ResourceCard, opts?: { categoryId?: string }): Promise<ResourceCard> {
    const admin = await ensureAdmin();
    const cats = admin.home?.categories || [];
    const targetCat = opts?.categoryId
      ? cats.find((c) => c.id === opts.categoryId)
      : cats.find((c) => c.name === card.category);
    if (!targetCat) throw new Error(`分类「${card.category}」不存在，请先创建`);
    const categoryId = targetCat.id;

    const existing = (targetCat.cards || []).find((c) => c.id === card.id) || null;
    const newCard = resourceCardToCard(card, categoryId, existing);

    if (existing) {
      const idx = targetCat.cards.findIndex((c) => c.id === card.id);
      targetCat.cards[idx] = newCard;
    } else {
      targetCat.cards.push(newCard);
    }
    await saveAdmin(admin, { message: `console: 卡片「${card.name}」${existing ? "编辑" : "新增"}` });
    return cardToResourceCard(newCard, targetCat);
  },

  /** 删除卡片 */
  async deleteCard(id: string): Promise<void> {
    const admin = await ensureAdmin();
    for (const c of admin.home?.categories || []) {
      const idx = (c.cards || []).findIndex((x) => x.id === id);
      if (idx >= 0) {
        c.cards.splice(idx, 1);
        await saveAdmin(admin, { message: `console: 删除卡片 ${id}` });
        return;
      }
    }
    throw new Error("卡片不存在");
  },

  /** 保存分类（新增/编辑） */
  async saveCategory(cat: Category): Promise<void> {
    const admin = await ensureAdmin();
    const cats = admin.home?.categories || [];
    const existing = cats.find((c) => c.id === cat.id);
    if (existing) {
      existing.name = cat.name;
    } else {
      cats.push({
        id: cat.id || `cat_${Date.now()}`,
        name: cat.name,
        iconKey: "folder",
        desc: "",
        subcategories: [{ id: "all", name: "全部" }],
        cards: [],
      });
    }
    await saveAdmin(admin, { message: `console: 分类「${cat.name}」${existing ? "编辑" : "新增"}` });
  },

  /** 删除分类（连同其下卡片） */
  async deleteCategory(id: string): Promise<void> {
    const admin = await ensureAdmin();
    admin.home.categories = (admin.home?.categories || []).filter((c) => c.id !== id);
    await saveAdmin(admin, { message: `console: 删除分类 ${id}` });
  },

  /** 保存软件（新增/编辑，支持本地上传 APK/ZIP/MD 直下与 URL 跳转双模式） */
  async saveSoftware(item: ResourceButton): Promise<void> {
    const admin = await ensureAdmin();
    admin.software = admin.software || [];
    const softwares = admin.software;
    const existing = softwares.find((s) => s.id === item.id);
    const raw = (item._raw as Record<string, unknown>) || {};
    const cleanUrl = (item._url ?? "").trim();
    const cleanApkUrl = (item._apkUrl ?? "").trim();
    const primaryLink = cleanApkUrl || cleanUrl;
    const isFileExt = /\.(apk|zip|md)(\?.*)?$/i.test(primaryLink);
    const finalMode =
      item._mode === "file" || isFileExt || Boolean(cleanApkUrl)
        ? "file"
        : item._mode || "url";
    const finalApkUrl = finalMode === "file" ? cleanApkUrl || cleanUrl : cleanApkUrl;
    const finalUrl = finalMode === "file" ? cleanUrl : cleanUrl || cleanApkUrl;

    if (existing) {
      existing.title = item.name.trim();
      existing.desc = (item._desc ?? existing.desc ?? "").trim();
      existing.url = finalUrl;
      existing.apkUrl = finalApkUrl;
      existing.author = (item._author ?? existing.author ?? "").trim();
      existing.badge = (item._badge ?? existing.badge ?? "").trim();
      existing.badgeType = (item._badgeType ?? existing.badgeType ?? "").trim();
      existing.tags = (item._tags ?? existing.tags ?? "").trim();
      existing.iconUrl = (item._iconUrl ?? existing.iconUrl ?? "").trim();
      existing.previewUrl = (item._previewUrl ?? existing.previewUrl ?? "").trim();
      existing.mode = finalMode;
    } else {
      softwares.unshift({
        ...raw,
        id: item.id || `sw_${Date.now()}`,
        type: "software",
        title: item.name.trim(),
        desc: (item._desc ?? "").trim(),
        url: finalUrl,
        author: (item._author ?? "").trim(),
        badge: (item._badge ?? "").trim(),
        badgeType: (item._badgeType ?? "").trim(),
        tags: (item._tags ?? "").trim(),
        apkUrl: finalApkUrl,
        previewUrl: (item._previewUrl ?? "").trim(),
        iconUrl: (item._iconUrl ?? "").trim(),
        mode: finalMode,
      });
    }
    await saveAdmin(admin, { message: `console: 软件「${item.name}」${existing ? "编辑" : "新增"}` });
  },

  /** 获取全部 Skill 技能库 */
  async getSkills(): Promise<SkillItem[]> {
    const admin = await ensureAdmin();
    return (admin.skills || []).map((sk) => ({
      id: sk.id,
      type: sk.type || "skill",
      promptType: sk.promptType || "skill",
      title: sk.title || "",
      desc: sk.desc || "",
      prompt: sk.prompt || "",
      url: sk.url || "",
      author: sk.author || "懒得找了",
      badge: sk.badge || "",
      tags: sk.tags || "",
      previewUrl: sk.previewUrl || "",
      mediaUrl: sk.mediaUrl || "",
      iconUrl: sk.iconUrl || "",
      mode: sk.mode || "file",
      _raw: sk,
    }));
  },

  /** 保存 Skill（新增/编辑，支持本地上传 ZIP/MD/APK 文件在本体直接下载） */
  async saveSkill(item: SkillItem): Promise<void> {
    const admin = await ensureAdmin();
    admin.skills = admin.skills || [];
    const skills = admin.skills;
    const existing = skills.find((s) => s.id === item.id);
    const raw = (item._raw as Record<string, unknown>) || {};
    const cleanUrl = (item.url || "").trim();
    const isFileExt = /\.(zip|md|apk)(\?.*)?$/i.test(cleanUrl);
    const finalMode =
      item.mode === "file" || isFileExt
        ? "file"
        : item.mode === "url"
        ? "url"
        : "file";

    if (existing) {
      existing.title = item.title.trim();
      existing.desc = (item.desc || "").trim();
      existing.promptType = (item.promptType || "skill").trim();
      existing.prompt = item.prompt || "";
      existing.url = cleanUrl;
      existing.author = (item.author || "懒得找了").trim();
      existing.badge = (item.badge || "").trim();
      existing.tags = (item.tags || "").trim();
      existing.previewUrl = (item.previewUrl || "").trim();
      existing.mediaUrl = (item.mediaUrl || "").trim();
      existing.iconUrl = (item.iconUrl || "").trim();
      existing.mode = finalMode;
    } else {
      skills.unshift({
        ...raw,
        id: item.id || `sk_${Date.now()}`,
        type: "skill",
        promptType: (item.promptType || "skill").trim(),
        title: item.title.trim(),
        desc: (item.desc || "").trim(),
        prompt: item.prompt || "",
        url: cleanUrl,
        author: (item.author || "懒得找了").trim(),
        badge: (item.badge || "").trim(),
        tags: (item.tags || "").trim(),
        previewUrl: (item.previewUrl || "").trim(),
        mediaUrl: (item.mediaUrl || "").trim(),
        iconUrl: (item.iconUrl || "").trim(),
        mode: finalMode,
      });
    }
    await saveAdmin(admin, { message: `console: Skill「${item.title}」${existing ? "编辑" : "新增"}` });
  },

  /** 删除 Skill */
  async deleteSkill(id: string): Promise<void> {
    const admin = await ensureAdmin();
    admin.skills = (admin.skills || []).filter((s) => s.id !== id);
    await saveAdmin(admin, { message: `console: 删除 Skill ${id}` });
  },

  /** 删除软件 */
  async deleteSoftware(id: string): Promise<void> {
    const admin = await ensureAdmin();
    admin.software = (admin.software || []).filter((s) => s.id !== id);
    await saveAdmin(admin, { message: `console: 删除软件 ${id}` });
  },

  /** 更新 UI 文本 */
  async saveText(item: TextItem): Promise<void> {
    const admin = await ensureAdmin();
    const key = item._key || item.title;
    admin.uiText = admin.uiText || {};
    admin.uiText[key] = item.content;
    await saveAdmin(admin, { message: `console: UI文本「${key}」更新` });
  },

  /** 删除 UI 文本 */
  async deleteText(key: string): Promise<void> {
    const admin = await ensureAdmin();
    if (admin.uiText && key in admin.uiText) {
      delete admin.uiText[key];
      await saveAdmin(admin, { message: `console: 删除 UI文本「${key}」` });
      return;
    }
    throw new Error("文本不存在");
  },

  /** 保存设置 */
  async saveSettings(settings: Record<string, unknown>): Promise<void> {
    const admin = await ensureAdmin();
    admin.settings = { ...admin.settings, ...settings } as never;
    await saveAdmin(admin, { message: "console: 系统设置更新" });
  },

  /** 连接测试（可指定只读镜像） */
  async testConnection(): Promise<{ ok: boolean; via: string; version?: string }> {
    try {
      await loadAdmin(false);
      const s = getStore();
      return { ok: true, via: "api", version: s.admin?.version?.name };
    } catch {
      try {
        await loadAdmin(true);
        const s = getStore();
        return { ok: true, via: "mirror", version: s.admin?.version?.name };
      } catch (e) {
        return { ok: false, via: "none", version: (e as Error).message };
      }
    }
  },
};
