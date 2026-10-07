import { useCallback, useEffect, useState } from "react";
import { Download, Plus } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { SearchInput } from "@/components/common/SearchInput";
import { FilterButton } from "@/components/common/FilterButton";
import { Pagination } from "@/components/common/Pagination";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingState } from "@/components/common/LoadingState";
import { ConfirmDialog } from "@/components/common/ConfirmDialog";
import { CardFormDialog, type CardFormValues } from "@/components/common/CardFormDialog";
import { CardManagementTable } from "@/components/dashboard/CardManagementTable";
import { api } from "@/lib/api";
import { categoryOptions } from "@/data/mockData";
import type { CardStatus, ResourceCard } from "@/types";

interface CardTableSectionProps {
  title?: string;
  description?: string;
  pageSize?: number;
  showFilters?: boolean;
  showHeaderActions?: boolean;
}

export function CardTableSection({
  title = "卡片管理",
  description = "管理已创建的资源卡片",
  pageSize = 6,
  showFilters = true,
  showHeaderActions = true,
}: CardTableSectionProps) {
  const [keyword, setKeyword] = useState("");
  const [status, setStatus] = useState<CardStatus | "all">("all");
  const [category, setCategory] = useState<string>("all");
  const [page, setPage] = useState(1);

  const [items, setItems] = useState<ResourceCard[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);

  const [formOpen, setFormOpen] = useState(false);
  const [editing, setEditing] = useState<ResourceCard | null>(null);
  const [deleting, setDeleting] = useState<ResourceCard | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    const res = await api.getCards({ keyword, status, category, page, pageSize });
    setItems(res.items);
    setTotal(res.total);
    setLoading(false);
  }, [keyword, status, category, page, pageSize]);

  useEffect(() => {
    void load();
  }, [load]);

  // 筛选条件变化时回到第一页
  useEffect(() => {
    setPage(1);
  }, [keyword, status, category]);

  const handleCreate = () => {
    setEditing(null);
    setFormOpen(true);
  };

  const handleEdit = (card: ResourceCard) => {
    setEditing(card);
    setFormOpen(true);
  };

  const handleSubmit = async (values: CardFormValues) => {
    // 真实写入：保存到 admin-data.json 并同步（mode=content 不弹窗）
    try {
      const { api } = await import("@/lib/api");
      if (editing) {
        await api.saveCard({ ...editing, name: values.name, description: values.description });
        toast.success(`已保存《${values.name}》并同步到本体`);
      } else {
        await api.saveCard({
          id: `site_${Date.now()}`,
          name: values.name,
          description: values.description,
          buttonType: "link",
          size: "-",
          downloads: 0,
          status: "published",
          updatedAt: "-",
          category: values.category || "全部",
        });
        toast.success(`已新建《${values.name}》并同步到本体`);
      }
      setFormOpen(false);
      void load();
    } catch (e) {
      toast.error(`保存失败：${(e as Error).message}`);
    }
  };

  const handleDelete = async () => {
    if (deleting) {
      try {
        const { api } = await import("@/lib/api");
        await api.deleteCard(deleting.id);
        toast.success(`已删除《${deleting.name}》并同步到本体`);
        setDeleting(null);
        void load();
      } catch (e) {
        toast.error(`删除失败：${(e as Error).message}`);
      }
    }
  };

  return (
    <Card className="overflow-hidden">
      <CardHeader className="gap-4 border-b border-border pb-4">
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h3 className="text-base font-semibold text-inkblack">{title}</h3>
            <p className="mt-0.5 text-sm text-inkblack/55">{description}</p>
          </div>
          {showHeaderActions && (
            <div className="flex flex-wrap items-center gap-2">
              <FilterButton
                active={status !== "all" || category !== "all"}
                onClick={() => toast.info("筛选面板：可按状态与分类组合筛选")}
              />
              <Button variant="outline" onClick={() => toast.success("已导出卡片列表（CSV）")}>
                <Download className="h-4 w-4" />
                导出
              </Button>
              <Button onClick={handleCreate}>
                <Plus className="h-4 w-4" />
                新建卡片
              </Button>
            </div>
          )}
        </div>

        {showFilters && (
          <div className="flex flex-col gap-2 sm:flex-row sm:items-center">
            <SearchInput
              value={keyword}
              onChange={setKeyword}
              placeholder="搜索卡片名称或描述"
            />
            <div className="flex items-center gap-2">
              <Select value={status} onValueChange={(v) => setStatus(v as CardStatus | "all")}>
                <SelectTrigger className="w-[130px]">
                  <SelectValue placeholder="状态" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">全部状态</SelectItem>
                  <SelectItem value="published">已发布</SelectItem>
                  <SelectItem value="reviewing">审核中</SelectItem>
                  <SelectItem value="draft">草稿</SelectItem>
                </SelectContent>
              </Select>
              <Select value={category} onValueChange={setCategory}>
                <SelectTrigger className="w-[140px]">
                  <SelectValue placeholder="分类" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">全部分类</SelectItem>
                  {categoryOptions.map((c) => (
                    <SelectItem key={c} value={c}>
                      {c}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>
        )}
      </CardHeader>

      <CardContent className="p-0">
        {loading ? (
          <LoadingState label="正在加载卡片…" />
        ) : items.length === 0 ? (
          <EmptyState
            title="没有匹配的卡片"
            description="试试更换关键词，或调整状态与分类筛选。"
            action={
              <Button variant="outline" size="sm" onClick={handleCreate}>
                <Plus className="h-4 w-4" />
                新建卡片
              </Button>
            }
          />
        ) : (
          <>
            {/* 桌面端表格 */}
            <div className="hidden md:block">
              <CardManagementTable cards={items} onEdit={handleEdit} onDelete={setDeleting} />
            </div>
            {/* 小屏卡片列表 */}
            <div className="space-y-3 p-4 md:hidden">
              {items.map((card) => (
                <MobileCardItem
                  key={card.id}
                  card={card}
                  onEdit={() => handleEdit(card)}
                  onDelete={() => setDeleting(card)}
                />
              ))}
            </div>
            <Pagination
              page={page}
              pageSize={pageSize}
              total={total}
              onPageChange={setPage}
            />
          </>
        )}
      </CardContent>

      <CardFormDialog
        open={formOpen}
        onOpenChange={setFormOpen}
        card={editing}
        onSubmit={handleSubmit}
      />
      <ConfirmDialog
        open={Boolean(deleting)}
        onOpenChange={(o) => !o && setDeleting(null)}
        title="删除卡片"
        description={
          <>
            确定要删除《<span className="font-medium text-inkblack">{deleting?.name}</span>》吗？此操作无法撤销。
          </>
        }
        confirmText="删除"
        onConfirm={handleDelete}
      />
    </Card>
  );
}

function MobileCardItem({
  card,
  onEdit,
  onDelete,
}: {
  card: ResourceCard;
  onEdit: () => void;
  onDelete: () => void;
}) {
  return (
    <div className="rounded-xl border border-border bg-paper-soft p-4 shadow-soft">
      <div className="flex items-start justify-between gap-3">
        <div className="min-w-0">
          <p className="truncate font-medium text-inkblack">{card.name}</p>
          <p className="mt-0.5 line-clamp-2 text-xs text-inkblack/55">{card.description}</p>
        </div>
        <CardStatusBadgeInline status={card.status} />
      </div>
      <div className="mt-3 flex flex-wrap items-center gap-x-4 gap-y-1 text-xs text-inkblack/60">
        <span>{card.size}</span>
        <span>下载 {card.downloads.toLocaleString("en-US")}</span>
        <span>{card.updatedAt}</span>
      </div>
      <div className="mt-3 flex items-center gap-2">
        <Button variant="outline" size="sm" className="flex-1" onClick={onEdit}>
          编辑
        </Button>
        <Button
          variant="ghost"
          size="sm"
          className="flex-1 text-cinnabar hover:bg-cinnabar/10 hover:text-cinnabar"
          onClick={onDelete}
        >
          删除
        </Button>
      </div>
    </div>
  );
}

function CardStatusBadgeInline({ status }: { status: ResourceCard["status"] }) {
  const map = {
    published: { label: "已发布", cls: "bg-[#2F7D5B]/12 text-[#2F7D5B]" },
    reviewing: { label: "审核中", cls: "bg-gold/15 text-[#9A7420]" },
    draft: { label: "草稿", cls: "bg-mist text-inkblack/60" },
  } as const;
  const cfg = map[status];
  return (
    <span className={`shrink-0 rounded-full px-2.5 py-0.5 text-xs font-medium ${cfg.cls}`}>
      {cfg.label}
    </span>
  );
}
