import { useCallback, useEffect, useMemo, useState } from "react";
import { Pencil, Plus, Trash2 } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { PageHeader } from "@/components/common/PageHeader";
import { SearchInput } from "@/components/common/SearchInput";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingState } from "@/components/common/LoadingState";
import { ConfirmDialog } from "@/components/common/ConfirmDialog";
import { Tooltip, TooltipContent, TooltipTrigger } from "@/components/ui/tooltip";
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
import { Textarea } from "@/components/ui/textarea";
import { api } from "@/lib/api";
import type { TextItem } from "@/types";

interface EditState {
  open: boolean;
  item: TextItem | null;
  isNew: boolean;
  keyName: string;
  content: string;
}

const emptyEdit: EditState = { open: false, item: null, isNew: false, keyName: "", content: "" };

export default function TextManagement() {
  const [texts, setTexts] = useState<TextItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");
  const [edit, setEdit] = useState<EditState>(emptyEdit);
  const [deleting, setDeleting] = useState<TextItem | null>(null);
  const [saving, setSaving] = useState(false);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const data = await api.getTexts();
      setTexts(data);
    } catch (e) {
      toast.error(`加载失败：${(e as Error).message}`);
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    void load();
  }, [load]);

  const filtered = useMemo(() => {
    const kw = keyword.trim().toLowerCase();
    if (!kw) return texts;
    return texts.filter(
      (t) =>
        t.title.toLowerCase().includes(kw) ||
        t.content.toLowerCase().includes(kw) ||
        t.category.toLowerCase().includes(kw)
    );
  }, [texts, keyword]);

  const openEdit = (item: TextItem) => {
    setEdit({
      open: true,
      item,
      isNew: false,
      keyName: item._key || item.title,
      content: item.content,
    });
  };

  const openCreate = () => {
    setEdit({ ...emptyEdit, open: true, isNew: true, keyName: "", content: "" });
  };

  const handleSave = async () => {
    if (!edit.keyName.trim()) {
      toast.error("请填写键名（key）");
      return;
    }
    setSaving(true);
    try {
      await api.saveText({
        id: `txt_${edit.keyName}`,
        title: edit.keyName,
        category: "UI 文本",
        content: edit.content,
        usageCount: 0,
        updatedAt: "-",
        _key: edit.keyName,
      });
      toast.success(`已保存「${edit.keyName}」并同步到本体`);
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
      await api.deleteText(deleting._key || deleting.title);
      toast.success(`已删除「${deleting.title}」并同步到本体`);
      setDeleting(null);
      void load();
    } catch (e) {
      toast.error(`删除失败：${(e as Error).message}`);
    }
  };

  return (
    <div className="space-y-6">
      <PageHeader
        title="文字管理"
        description="维护 UI 文案（uiText），保存后实时同步到本体"
        actions={
          <Button onClick={openCreate}>
            <Plus className="h-4 w-4" />
            新建文案
          </Button>
        }
      />

      <Card className="overflow-hidden">
        <CardHeader className="gap-4 border-b border-border pb-4">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h3 className="text-base font-semibold text-inkblack">文案列表</h3>
              <p className="mt-0.5 text-sm text-inkblack/55">共 {texts.length} 条（真实 admin-data.uiText 等）</p>
            </div>
            <SearchInput value={keyword} onChange={setKeyword} placeholder="搜索文案标题或内容" />
          </div>
        </CardHeader>

        <CardContent className="p-0">
          {loading ? (
            <LoadingState label="正在加载文案…" />
          ) : filtered.length === 0 ? (
            <EmptyState title="没有匹配的文案" description="试试更换关键词。" />
          ) : (
            <div className="w-full overflow-x-auto">
              <table className="w-full min-w-[760px] border-collapse text-sm">
                <thead>
                  <tr className="border-b border-border">
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">文案标题</th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">分类</th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">内容预览</th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">操作</th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map((t) => (
                    <tr key={t.id} className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60">
                      <td className="px-4 py-3.5 font-medium text-inkblack">{t.title}</td>
                      <td className="px-4 py-3.5">
                        <Badge variant="outline">{t.category}</Badge>
                      </td>
                      <td className="px-4 py-3.5">
                        <Tooltip>
                          <TooltipTrigger asChild>
                            <span className="block max-w-[320px] truncate text-inkblack/65">{t.content}</span>
                          </TooltipTrigger>
                          <TooltipContent className="max-w-sm">{t.content}</TooltipContent>
                        </Tooltip>
                      </td>
                      <td className="px-4 py-3.5">
                        <div className="flex items-center justify-end gap-1">
                          <Button variant="ghost" size="sm" onClick={() => openEdit(t)}>
                            <Pencil className="h-4 w-4" />
                            编辑
                          </Button>
                          <Button
                            variant="ghost"
                            size="icon-sm"
                            aria-label="删除"
                            className="text-cinnabar hover:bg-cinnabar/10 hover:text-cinnabar"
                            onClick={() => setDeleting(t)}
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
        <DialogContent className="max-w-lg">
          <DialogHeader>
            <DialogTitle>{edit.isNew ? "新建文案" : `编辑「${edit.item?.title}」`}</DialogTitle>
            <DialogDescription>保存后实时同步到本体（模式=内容同步，不弹更新窗）</DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div className="space-y-1.5">
              <Label>键名（key，对应 admin-data.uiText）</Label>
              <Input value={edit.keyName} onChange={(e) => setEdit((s) => ({ ...s, keyName: e.target.value }))} placeholder="如 splashSub / luckyTitle" disabled={!edit.isNew} />
            </div>
            <div className="space-y-1.5">
              <Label>内容</Label>
              <Textarea value={edit.content} onChange={(e) => setEdit((s) => ({ ...s, content: e.target.value }))} placeholder="文案内容" rows={4} />
            </div>
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
        title="删除文案"
        description={
          <>
            确定要删除「<span className="font-medium text-inkblack">{deleting?.title}</span>」吗？删除将同步到本体。
          </>
        }
        confirmText="删除"
        onConfirm={handleDelete}
      />
    </div>
  );
}
