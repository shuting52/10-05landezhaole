import {
  LayoutDashboard,
  Layers,
  MousePointerClick,
  Sparkles,
  Type,
  FolderTree,
  Settings as SettingsIcon,
  ScrollText,
  type LucideIcon,
} from "lucide-react";

export interface NavItem {
  label: string;
  to: string;
  icon: LucideIcon;
}

export const navItems: NavItem[] = [
  { label: "总览", to: "/dashboard", icon: LayoutDashboard },
  { label: "卡片管理", to: "/cards", icon: Layers },
  { label: "软件库管理", to: "/buttons", icon: MousePointerClick },
  { label: "Skill 技能库", to: "/skills", icon: Sparkles },
  { label: "文字管理", to: "/texts", icon: Type },
  { label: "分类管理", to: "/categories", icon: FolderTree },
  { label: "系统设置", to: "/settings", icon: SettingsIcon },
  { label: "操作日志", to: "/logs", icon: ScrollText },
];

export const pageTitles: Record<string, string> = {
  "/dashboard": "总览",
  "/cards": "卡片管理",
  "/buttons": "软件库管理",
  "/skills": "Skill 技能库",
  "/texts": "文字管理",
  "/categories": "分类管理",
  "/settings": "系统设置",
  "/logs": "操作日志",
};
