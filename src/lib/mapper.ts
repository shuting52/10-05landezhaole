// ============================================================
// 数据模型映射：管理台原型模型 ↔ admin-data.json 真实结构
// 对接规范 V1 §4 —— 让新管理台直接管理真实数据
// ============================================================

import type { AdminData, CardDto, CategoryDto, SoftwareDto } from "@/types/admin";
import type { Category, ResourceButton, ResourceCard, TextItem } from "@/types";

// ---------- 卡片：admin-data home.categories[].cards[] → ResourceCard ----------
export function cardToResourceCard(card: CardDto, category: CategoryDto): ResourceCard {
  return {
    id: card.id,
    name: card.title,
    description: card.desc || card.highlights || "",
    buttonType: "link",
    size: card.badgeType === "hot" ? "HOT" : card.badge ? card.badge : "-",
    downloads: 0,
    status: "published",
    updatedAt: "-",
    category: category.name,
    // 保留真实字段以便写回
    _raw: card,
    _categoryId: category.id,
    _subcatId: card.subcatId,
    _url: card.url,
  } as ResourceCard;
}

// ResourceCard → CardDto（写回）
export function resourceCardToCard(
  rc: ResourceCard,
  categoryId: string,
  existing: CardDto | null
): CardDto {
  const base = existing
    ? { ...existing }
    : {
        id: `site_${Date.now()}`,
        title: rc.name,
        url: rc._url || "",
        icon: "",
        fallbackText: (rc.name || "?").slice(0, 1).toUpperCase(),
        badge: "",
        badgeType: "",
        desc: rc.description || "",
        categoryId,
        subcatId: "all",
      };
  base.title = rc.name;
  base.desc = rc.description || base.desc;
  base.url = rc._url || base.url;
  base.categoryId = categoryId;
  return base;
}

// ---------- 分类：admin-data home.categories[] → Category ----------
export function categoryToModel(cat: CategoryDto, allCards: CardDto[]): Category {
  const cards = allCards.filter((c) => c.categoryId === cat.id);
  return {
    id: cat.id,
    name: cat.name,
    cardCount: cards.length,
    order: 0,
    status: "enabled",
  };
}

// ---------- 软件库：admin-data software[] → ResourceButton ----------
export function softwareToButton(s: SoftwareDto): ResourceButton {
  return {
    id: s.id,
    name: s.title,
    type: s.mode && s.mode !== "url" ? "download" : "link",
    usageCount: 0,
    status: "enabled",
    updatedAt: "-",
    _raw: s,
    _url: s.url || "",
    _desc: s.desc || "",
    _apkUrl: s.apkUrl || "",
    _author: s.author || "",
    _badge: s.badge || "",
    _badgeType: s.badgeType || "",
    _tags: s.tags || "",
    _iconUrl: s.iconUrl || "",
    _previewUrl: s.previewUrl || "",
    _mode: s.mode || (s.apkUrl ? "file" : "url"),
  } as ResourceButton;
}

// ---------- UI 文本：admin-data uiText / uiTextDefs / settings → TextItem ----------
export function uiTextToModel(key: string, value: string): TextItem {
  return {
    id: `txt_${key}`,
    title: key,
    category: "UI 文本",
    content: value,
    usageCount: 0,
    updatedAt: "-",
    _key: key,
  } as TextItem;
}

// ---------- 统计：真实 admin-data 统计 → StatItem ----------
export function buildStats(data: AdminData) {
  const categories = data.home?.categories || [];
  const allCards = categories.reduce((acc: CardDto[], c) => acc.concat(c.cards || []), []);
  return [
    {
      id: "cards",
      label: "站点卡片总数",
      value: allCards.length.toLocaleString("en-US"),
      delta: `${categories.length} 个分类`,
      deltaLabel: "覆盖 AI/开发/设计/娱乐/工具等",
      trend: "up" as const,
      icon: "layers" as const,
      tone: "cinnabar" as const,
      action: { label: "去管理", to: "/cards" },
    },
    {
      id: "software",
      label: "软件库",
      value: (data.software?.length || 0).toLocaleString("en-US"),
      delta: "内置工具与软件资源",
      deltaLabel: "",
      trend: "up" as const,
      icon: "download" as const,
      tone: "gold" as const,
      action: { label: "去管理", to: "/buttons" },
    },
    {
      id: "skills",
      label: "Skill 技能库",
      value: (data.skills?.length || 0).toLocaleString("en-US"),
      delta: "提示词与技能包",
      deltaLabel: "",
      trend: "up" as const,
      icon: "users" as const,
      tone: "ink" as const,
      action: { label: "去管理", to: "/skills" },
    },
    {
      id: "version",
      label: "当前版本",
      value: data.version?.name || "-",
      delta: `code ${data.version?.code ?? "-"}`,
      deltaLabel: data.autoRelease?.mode === "release" ? "发布模式" : "内容同步模式",
      trend: "up" as const,
      icon: "clipboard" as const,
      tone: "orange" as const,
      action: { label: "系统设置", to: "/settings" },
    },
  ];
}
