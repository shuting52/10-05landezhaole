import { useCallback, useEffect, useMemo, useState } from "react";
import { Pencil, Plus, Trash2 } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { PageHeader } from "@/components/common/PageHeader";
import { SearchInput } from "@/components/common/SearchInput";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingState } from "@/components/common/LoadingState";
import { ConfirmDialog } from "@/components/common/ConfirmDialog";
import { ButtonTypeBadge } from "@/components/common/StatusBadge";
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
import type { ResourceButton } from "@/types";

interface FormState {
  open: boolean;
  editing: ResourceButton | null;
  name: string;
  url: string;
  desc: string;
}

const emptyForm: FormState = {
  open: false,
  editing: null,
  name: "",
  url: "",
  desc: "",
};

export default function ButtonManagement() {
  const [buttons, setButtons] = useState<ResourceButton[]>([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");
  const [deleting, setDeleting] = useState<ResourceButton | null>(null);
  const [form, setForm] = useState<FormState>(emptyForm);
  const [saving, setSaving] = useState(false);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const data = await api.getButtons();
      setButtons(data);
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
    if (!kw) return buttons;
    return buttons.filter((b) => b.name.toLowerCase().includes(kw));
  }, [buttons, keyword]);

  const openCreate = () => {
    setForm({ ...emptyForm, open: true, editing: null });
  };

  const openEdit = (btn: ResourceButton) => {
    const raw = (btn._raw as Record<string, unknown>) || {};
    setForm({
      open: true,
      editing: btn,
      name: btn.name,
      url: (btn._url as string) || (raw.url as string) || "",
      desc: (btn._desc as string) || (raw.desc as string) || "",
    });
  };

  const handleSave = async () => {
    if (!form.name.trim()) {
      toast.error("请填写软件名称");
      return;
    }
    setSaving(true);
    try {
      const item: ResourceButton = {
        id: form.editing?.id || `sw_${Date.now()}`,
        name: form.name.trim(),
        type: "download",
        usageCount: 0,
        status: "enabled",
        updatedAt: "-",
        _url: form.url.trim(),
        _desc: form.desc.trim(),
        _raw: form.editing?._raw,
      };
      await api.saveSoftware(item);
      toast.success(`已保存「${form.name}」并同步到本体`);
      setForm({ ...emptyForm, open: false });
      void load();
    } catch (e) {
      toast.error(`保存失败：${(e as Error).message}`);
    }
    setSaving(false);
  };

  const handleDelete = async () => {
    if (!deleting) return;
    try {
      await api.deleteSoftware(deleting.id);
      toast.success(`已删除「${deleting.name}」并同步到本体`);
      setDeleting(null);
      void load();
    } catch (e) {
      toast.error(`删除失败：${(e as Error).message}`);
    }
  };

  return (
    <div className="space-y-6">
      <PageHeader
        title="按钮管理"
        description="维护软件库入口（software[]），保存后实时同步到本体"
        actions={
          <Button onClick={openCreate}>
            <Plus className="h-4 w-4" />
            新建按钮
          </Button>
        }
      />

      <Card className="overflow-hidden">
        <CardHeader className="gap-4 border-b border-border pb-4">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h3 className="text-base font-semibold text-inkblack">软件库列表</h3>
              <p className="mt-0.5 text-sm text-inkblack/55">共 {buttons.length} 个（真实 admin-data.software）</p>
            </div>
            <SearchInput value={keyword} onChange={setKeyword} placeholder="搜索按钮名称" />
          </div>
        </CardHeader>

        <CardContent className="p-0">
          {loading ? (
            <LoadingState label="正在加载按钮…" />
          ) : filtered.length === 0 ? (
            <EmptyState title="没有匹配的按钮" description="试试更换关键词。" />
          ) : (
            <div className="w-full overflow-x-auto">
              <table className="w-full min-w-[720px] border-collapse text-sm">
                <thead>
                  <tr className="border-b border-border">
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">名称</th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">链接</th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">操作</th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map((btn) => (
                    <tr key={btn.id} className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60">
                      <td className="px-4 py-3.5">
                        <div className="flex items-center gap-2">
                          <span className="font-medium text-inkblack">{btn.name}</span>
                          <ButtonTypeBadge type={btn.type} />
                        </div>
                        {btn._desc && <p className="mt-0.5 text-xs text-inkblack/50">{btn._desc}</p>}
                      </td>
                      <td className="px-4 py-3.5">
                        {btn._url ? (
                          <a href={btn._url} target="_blank" rel="noreferrer" className="max-w-[280px] truncate text-cinnabar hover:underline">
                            {btn._url}
                          </a>
                        ) : (
                          <span className="text-inkblack/40">-</span>
                        )}
                      </td>
                      <td className="px-4 py-3.5">
                        <div className="flex items-center justify-end gap-1">
                          <Button variant="ghost" size="icon-sm" aria-label="编辑" onClick={() => openEdit(btn)}>
                            <Pencil className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="icon-sm"
                            aria-label="删除"
                            className="text-cinnabar hover:bg-cinnabar/10 hover:text-cinnabar"
                            onClick={() => setDeleting(btn)}
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
      <Dialog open={form.open} onOpenChange={(o) => setForm((f) => ({ ...f, open: o }))}>
        <DialogContent className="max-w-lg">
          <DialogHeader>
            <DialogTitle>{form.editing ? "编辑软件" : "新建软件"}</DialogTitle>
            <DialogDescription>保存后实时同步到本体（模式=内容同步，不弹更新窗）</DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div className="space-y-1.5">
              <Label>名称</Label>
              <Input value={form.name} onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))} placeholder="例如：Geek Uninstaller" />
            </div>
            <div className="space-y-1.5">
              <Label>链接</Label>
              <Input value={form.url} onChange={(e) => setForm((f) => ({ ...f, url: e.target.value }))} placeholder="https://…" />
            </div>
            <div className="space-y-1.5">
              <Label>描述</Label>
              <Textarea value={form.desc} onChange={(e) => setForm((f) => ({ ...f, desc: e.target.value }))} placeholder="简要介绍" />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setForm((f) => ({ ...f, open: false }))}>取消</Button>
            <Button onClick={handleSave} disabled={saving}>{saving ? "保存中…" : "保存"}</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <ConfirmDialog
        open={Boolean(deleting)}
        onOpenChange={(o) => !o && setDeleting(null)}
        title="删除按钮"
        description={
          <>
            确定要删除「<span className="font-medium text-inkblack">{deleting?.name}</span>」吗？删除将同步到本体。
          </>
        }
        confirmText="删除"
        onConfirm={handleDelete}
      />
    </div>
  );
}
