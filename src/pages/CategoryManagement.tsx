import { useCallback, useEffect, useState } from "react";
import { ArrowDown, ArrowUp, Pencil, Plus, Trash2 } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { PageHeader } from "@/components/common/PageHeader";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingState } from "@/components/common/LoadingState";
import { EnabledBadge } from "@/components/common/StatusBadge";
import { ConfirmDialog } from "@/components/common/ConfirmDialog";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { api } from "@/lib/api";
import { formatNumber } from "@/lib/utils";
import type { Category } from "@/types";

interface EditState {
  open: boolean;
  editing: Category | null;
  name: string;
}

const emptyEdit: EditState = { open: false, editing: null, name: "" };

export default function CategoryManagement() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [edit, setEdit] = useState<EditState>(emptyEdit);
  const [deleting, setDeleting] = useState<Category | null>(null);
  const [saving, setSaving] = useState(false);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const data = await api.getCategories();
      setCategories(data);
    } catch (e) {
      toast.error(`加载失败：${(e as Error).message}`);
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    void load();
  }, [load]);

  const move = (id: string, dir: -1 | 1) => {
    setCategories((prev) => {
      const sorted = [...prev].sort((a, b) => a.order - b.order);
      const idx = sorted.findIndex((c) => c.id === id);
      const target = idx + dir;
      if (target < 0 || target >= sorted.length) return prev;
      [sorted[idx], sorted[target]] = [sorted[target], sorted[idx]];
      return sorted.map((c, i) => ({ ...c, order: i + 1 }));
    });
    toast.success("排序已调整，请点「保存排序」同步到本体");
  };

  const handleSaveOrder = async () => {
    try {
      const { getStore, saveAdmin } = await import("@/lib/store");
      const st = getStore();
      if (!st.admin) throw new Error("请先连接加载数据");
      // 按本地排序重排 admin-data 的 categories 数组顺序（数组顺序即本体展示顺序）
      const sorted = [...categories].sort((a, b) => a.order - b.order);
      const orderMap = new Map(sorted.map((c, i) => [c.id, i]));
      const next = { ...st.admin };
      next.home = { ...next.home, categories: [...(next.home.categories || [])].sort((a, b) => {
        const ia = orderMap.get(a.id) ?? Number.MAX_SAFE_INTEGER;
        const ib = orderMap.get(b.id) ?? Number.MAX_SAFE_INTEGER;
        return ia - ib;
      }) };
      await saveAdmin(next, { message: "console: 分类排序更新" });
      toast.success("排序已保存并同步到本体");
      void load();
    } catch (e) {
      toast.error(`保存失败：${(e as Error).message}`);
    }
  };

  const openCreate = () => {
    setEdit({ ...emptyEdit, open: true, editing: null, name: "" });
  };

  const openEdit = (cat: Category) => {
    setEdit({ open: true, editing: cat, name: cat.name });
  };

  const handleSave = async () => {
    if (!edit.name.trim()) {
      toast.error("请填写分类名称");
      return;
    }
    setSaving(true);
    try {
      await api.saveCategory({
        id: edit.editing?.id || `cat_${Date.now()}`,
        name: edit.name.trim(),
        cardCount: edit.editing?.cardCount || 0,
        order: edit.editing?.order || categories.length + 1,
        status: edit.editing?.status || "enabled",
      });
      toast.success(`已保存分类「${edit.name}」并同步到本体`);
      setEdit({ ...emptyEdit, open: false });
      void load();
    } catch (e) {
      toast.error(`保存失败：${(e as Error).message}`);
    }
    setSaving(false);
  };

  const handleDelete = async () => {
    if (!deleting) return;
    try {
      await api.deleteCategory(deleting.id);
      toast.success(`已删除分类「${deleting.name}」（含其下卡片）并同步到本体`);
      setDeleting(null);
      void load();
    } catch (e) {
      toast.error(`删除失败：${(e as Error).message}`);
    }
  };

  return (
    <div className="space-y-6">
      <PageHeader
        title="分类管理"
        description="维护首页分类（home.categories），保存后实时同步到本体"
        actions={
          <Button onClick={openCreate}>
            <Plus className="h-4 w-4" />
            新增分类
          </Button>
        }
      />

      <Card className="overflow-hidden">
        <CardHeader className="border-b border-border pb-4">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-base font-semibold text-inkblack">分类列表</h3>
              <p className="mt-0.5 text-sm text-inkblack/55">共 {categories.length} 个分类（真实 admin-data.home.categories）</p>
            </div>
            <Button variant="outline" size="sm" onClick={handleSaveOrder}>
              保存排序
            </Button>
          </div>
        </CardHeader>

        <CardContent className="p-0">
          {loading ? (
            <LoadingState label="正在加载分类…" />
          ) : categories.length === 0 ? (
            <EmptyState title="暂无分类" description="点击右上角新增第一个分类。" />
          ) : (
            <div className="w-full overflow-x-auto">
              <table className="w-full min-w-[680px] border-collapse text-sm">
                <thead>
                  <tr className="border-b border-border">
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">分类名称</th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">卡片数量</th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">排序</th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">状态</th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">操作</th>
                  </tr>
                </thead>
                <tbody>
                  {[...categories].sort((a, b) => a.order - b.order).map((cat) => (
                    <tr key={cat.id} className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60">
                      <td className="px-4 py-3.5 font-medium text-inkblack">{cat.name}</td>
                      <td className="px-4 py-3.5 text-right font-medium tabular-nums text-inkblack">{formatNumber(cat.cardCount)}</td>
                      <td className="px-4 py-3.5">
                        <div className="flex items-center gap-1.5">
                          <span className="w-6 text-center text-inkblack/60">{cat.order}</span>
                          <Button variant="ghost" size="icon-sm" aria-label="上移" onClick={() => move(cat.id, -1)}>
                            <ArrowUp className="h-3.5 w-3.5" />
                          </Button>
                          <Button variant="ghost" size="icon-sm" aria-label="下移" onClick={() => move(cat.id, 1)}>
                            <ArrowDown className="h-3.5 w-3.5" />
                          </Button>
                        </div>
                      </td>
                      <td className="px-4 py-3.5">
                        <EnabledBadge enabled={cat.status === "enabled"} />
                      </td>
                      <td className="px-4 py-3.5">
                        <div className="flex items-center justify-end gap-1">
                          <Button variant="ghost" size="sm" onClick={() => openEdit(cat)}>
                            <Pencil className="h-4 w-4" />
                            编辑
                          </Button>
                          <Button
                            variant="ghost"
                            size="icon-sm"
                            aria-label="删除"
                            className="text-cinnabar hover:bg-cinnabar/10 hover:text-cinnabar"
                            onClick={() => setDeleting(cat)}
                          >
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>

      {/* 新建/编辑弹窗 */}
      <Dialog open={edit.open} onOpenChange={(o) => setEdit((e) => ({ ...e, open: o }))}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle>{edit.editing ? "编辑分类" : "新增分类"}</DialogTitle>
            <DialogDescription>保存后实时同步到本体（模式=内容同步，不弹更新窗）</DialogDescription>
          </DialogHeader>
          <div className="space-y-1.5">
            <Label>分类名称</Label>
            <Input value={edit.name} onChange={(e) => setEdit((s) => ({ ...s, name: e.target.value }))} placeholder="例如：AI 工具" />
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setEdit((e) => ({ ...e, open: false }))}>取消</Button>
            <Button onClick={handleSave} disabled={saving}>{saving ? "保存中…" : "保存"}</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <ConfirmDialog
        open={Boolean(deleting)}
        onOpenChange={(o) => !o && setDeleting(null)}
        title="删除分类"
        description={
          <>
            确定要删除分类「<span className="font-medium text-inkblack">{deleting?.name}</span>」吗？<span className="text-cinnabar">其下所有卡片将一并删除</span>，并同步到本体。
          </>
        }
        confirmText="删除"
        onConfirm={handleDelete}
      />
    </div>
  );
}
