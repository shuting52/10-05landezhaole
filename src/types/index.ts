// 全局类型定义

export type CardStatus = "published" | "reviewing" | "draft";
export type ButtonType = "download" | "link" | "copy" | "contact";
export type LogActionType = "create" | "update" | "publish" | "delete" | "review";

export interface ResourceCard {
  id: string;
  name: string;
  description: string;
  buttonType: ButtonType;
  size: string;
  downloads: number;
  status: CardStatus;
  updatedAt: string;
  category: string;
  // 真实数据扩展字段（映射回 admin-data 用）
  _raw?: unknown;
  _url?: string;
  _categoryId?: string;
  _subcatId?: string;
  _badge?: string;
  _badgeType?: string;
}

export interface ResourceButton {
  id: string;
  name: string;
  type: ButtonType;
  usageCount: number;
  status: "enabled" | "disabled";
  updatedAt: string;
  // 真实数据扩展字段
  _raw?: unknown;
  _url?: string;
  _desc?: string;
}

export interface TextItem {
  id: string;
  title: string;
  category: string;
  content: string;
  usageCount: number;
  updatedAt: string;
  // 真实数据扩展字段
  _key?: string;
}

export interface Category {
  id: string;
  name: string;
  cardCount: number;
  order: number;
  status: "enabled" | "disabled";
}

export interface ActivityLog {
  id: string;
  operator: string;
  avatarColor: string;
  action: LogActionType;
  content: string;
  time: string;
  ip?: string;
}

export interface StatItem {
  id: string;
  label: string;
  value: string;
  delta: string;
  deltaLabel: string;
  trend: "up" | "down";
  icon: "layers" | "download" | "users" | "clipboard";
  tone: "cinnabar" | "gold" | "ink" | "orange";
  action?: { label: string; to: string };
}

export interface Paginated<T> {
  items: T[];
  total: number;
  page: number;
  pageSize: number;
}

export interface CardQuery {
  keyword?: string;
  status?: CardStatus | "all";
  category?: string | "all";
  page?: number;
  pageSize?: number;
}
