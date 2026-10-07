import { Badge } from "@/components/ui/badge";
import type { ButtonType, CardStatus, LogActionType } from "@/types";

const cardStatusMap: Record<CardStatus, { label: string; variant: "success" | "warning" | "muted" }> = {
  published: { label: "已发布", variant: "success" },
  reviewing: { label: "审核中", variant: "warning" },
  draft: { label: "草稿", variant: "muted" },
};

const buttonTypeMap: Record<ButtonType, { label: string; variant: "default" | "gold" | "ink" | "outline" }> = {
  download: { label: "下载按钮", variant: "default" },
  link: { label: "跳转按钮", variant: "ink" },
  copy: { label: "复制按钮", variant: "gold" },
  contact: { label: "联系按钮", variant: "outline" },
};

const logActionMap: Record<LogActionType, { label: string; variant: "success" | "gold" | "default" | "danger" | "ink" }> = {
  create: { label: "创建", variant: "success" },
  update: { label: "修改", variant: "gold" },
  publish: { label: "发布", variant: "default" },
  delete: { label: "删除", variant: "danger" },
  review: { label: "审核", variant: "ink" },
};

export function CardStatusBadge({ status }: { status: CardStatus }) {
  const cfg = cardStatusMap[status];
  return <Badge variant={cfg.variant}>{cfg.label}</Badge>;
}

export function ButtonTypeBadge({ type }: { type: ButtonType }) {
  const cfg = buttonTypeMap[type];
  return <Badge variant={cfg.variant}>{cfg.label}</Badge>;
}

export function LogActionBadge({ action }: { action: LogActionType }) {
  const cfg = logActionMap[action];
  return <Badge variant={cfg.variant}>{cfg.label}</Badge>;
}

export function EnabledBadge({ enabled }: { enabled: boolean }) {
  return (
    <Badge variant={enabled ? "success" : "muted"}>{enabled ? "启用" : "停用"}</Badge>
  );
}
