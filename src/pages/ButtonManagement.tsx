import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { CloudUpload, Download, ExternalLink, Image as ImageIcon, Pencil, Plus, Trash2 } from "lucide-react";
import { toast } from "sonner";
import { PageHeader } from "@/components/common/PageHeader";
import { SearchInput } from "@/components/common/SearchInput";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingState } from "@/components/common/LoadingState";
import { ConfirmDialog } from "@/components/common/ConfirmDialog";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
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
import { ghUploadFile } from "@/lib/github";
import type { ResourceButton } from "@/types";

interface FormState {
  open: boolean;
  editing: ResourceButton | null;
  name: string;
  url: string;
  apkUrl: string;
  desc: string;
  author: string;
  badge: string;
  tags: string;
  iconUrl: string;
  previewUrl: string;
  mode: "file" | "url";
}

const emptyForm: FormState = {
  open: false,
  editing: null,
  name: "",
  url: "",
  apkUrl: "",
  desc: "",
  author: "",
  badge: "最新版",
  tags: "软件 安装包 APK",
  iconUrl: "",
  previewUrl: "",
  mode: "file",
};

export default function ButtonManagement() {
  const [buttons, setButtons] = useState<ResourceButton[]>([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");
  const [deleting, setDeleting] = useState<ResourceButton | null>(null);
  const [form, setForm] = useState<FormState>(emptyForm);
  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState<string | null>(null);

  const pkgInputRef = useRef<HTMLInputElement | null>(null);
  const iconInputRef = useRef<HTMLInputElement | null>(null);
  const previewInputRef = useRef<HTMLInputElement | null>(null);

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
    return buttons.filter(
      (b) =>
        b.name.toLowerCase().includes(kw) ||
        (b._desc || "").toLowerCase().includes(kw) ||
        (b._tags || "").toLowerCase().includes(kw) ||
        (b._author || "").toLowerCase().includes(kw)
    );
  }, [buttons, keyword]);

  const openCreate = () => {
    setForm({ ...emptyForm, open: true, editing: null });
  };

  const openEdit = (btn: ResourceButton) => {
    const raw = (btn._raw as Record<string, unknown>) || {};
    const modeVal = ((btn._mode || (raw.mode as string) || (btn._apkUrl || raw.apkUrl ? "file" : "url")) === "url"
      ? "url"
      : "file") as "file" | "url";
    setForm({
      open: true,
      editing: btn,
      name: btn.name,
      url: (btn._url as string) || (raw.url as string) || "",
      apkUrl: (btn._apkUrl as string) || (raw.apkUrl as string) || "",
      desc: (btn._desc as string) || (raw.desc as string) || "",
      author: (btn._author as string) || (raw.author as string) || "",
      badge: (btn._badge as string) || (raw.badge as string) || "",
      tags: (btn._tags as string) || (raw.tags as string) || "",
      iconUrl: (btn._iconUrl as string) || (raw.iconUrl as string) || "",
      previewUrl: (btn._previewUrl as string) || (raw.previewUrl as string) || "",
      mode: modeVal,
    });
  };

  const handleUploadLocalFile = async (
    file: File | undefined,
    target: "pkg" | "icon" | "preview"
  ) => {
    if (!file) return;
    setUploading(`正在上传 ${file.name} 到云端仓库…`);
    try {
      const subFolder = target === "pkg" ? "auto" : "dist/uploads";
      const { rawUrl, fileName } = await ghUploadFile(file, subFolder);
      setForm((f) => {
        if (target === "pkg") {
          const baseTitle = fileName.replace(/\.[^/.]+$/, "");
          return {
            ...f,
            apkUrl: rawUrl,
            mode: "file",
            name: f.name.trim() ? f.name : baseTitle,
          };
        }
        if (target === "icon") {
          return { ...f, iconUrl: rawUrl };
        }
        return { ...f, previewUrl: rawUrl };
      });
      toast.success(`文件「${file.name}」已上传，本体软件可直接下载`);
    } catch (e) {
      toast.error(`上传失败：${(e as Error).message}`);
    } finally {
      setUploading(null);
    }
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
        type: form.mode === "file" ? "download" : "link",
        usageCount: 0,
        status: "enabled",
        updatedAt: "-",
        _url: form.url.trim(),
        _apkUrl: form.apkUrl.trim(),
        _desc: form.desc.trim(),
        _author: form.author.trim(),
        _badge: form.badge.trim(),
        _tags: form.tags.trim(),
        _iconUrl: form.iconUrl.trim(),
        _previewUrl: form.previewUrl.trim(),
        _mode: form.mode,
        _raw: form.editing?._raw,
      };
      await api.saveSoftware(item);
      toast.success(`已保存「${form.name}」并实时同步到本体`);
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
        title="软件库管理"
        description="维护本体软件库入口（software[]），支持本地上传 APK/ZIP/MD 文件供本体直接下载安装"
        actions={
          <Button onClick={openCreate}>
            <Plus className="h-4 w-4" />
            新建软件 / 上传本地包
          </Button>
        }
      />

      <Card className="overflow-hidden">
        <CardHeader className="gap-4 border-b border-border pb-4">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h3 className="text-base font-semibold text-inkblack">软件库列表</h3>
              <p className="mt-0.5 text-sm text-inkblack/55">
                共 {buttons.length} 个（真实 admin-data.software · 文件形式在本体可直接下载）
              </p>
            </div>
            <SearchInput value={keyword} onChange={setKeyword} placeholder="搜索软件名称、描述或标签" />
          </div>
        </CardHeader>

        <CardContent className="p-0">
          {loading ? (
            <LoadingState label="正在加载软件库…" />
          ) : filtered.length === 0 ? (
            <EmptyState title="没有匹配的软件" description="试试更换关键词或点击右上角上传软件。" />
          ) : (
            <div className="w-full overflow-x-auto">
              <table className="w-full min-w-[760px] border-collapse text-sm">
                <thead>
                  <tr className="border-b border-border">
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">软件信息</th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">下发形式</th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">文件直链 / 跳转链接</th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">操作</th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map((btn) => {
                    const isFile = (btn._mode || "url") !== "url" || Boolean(btn._apkUrl);
                    const link = btn._apkUrl || btn._url || "";
                    return (
                      <tr key={btn.id} className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60">
                        <td className="px-4 py-3.5">
                          <div className="flex items-center gap-2.5">
                            {btn._iconUrl ? (
                              <img
                                src={btn._iconUrl}
                                alt={btn.name}
                                className="h-9 w-9 rounded-lg border border-border object-cover"
                              />
                            ) : null}
                            <div>
                              <div className="flex items-center gap-2">
                                <span className="font-medium text-inkblack">{btn.name}</span>
                                {btn._badge && (
                                  <span className="rounded bg-gold/15 px-1.5 py-0.5 text-xs font-medium text-[#9A7420]">
                                    {btn._badge}
                                  </span>
                                )}
                              </div>
                              {btn._desc && <p className="mt-0.5 text-xs text-inkblack/55">{btn._desc}</p>}
                              {(btn._tags || btn._author) && (
                                <p className="mt-0.5 text-[11px] text-inkblack/45">
                                  {btn._author ? `作者: ${btn._author} ` : ""}
                                  {btn._tags ? `· 标签: ${btn._tags}` : ""}
                                </p>
                              )}
                            </div>
                          </div>
                        </td>
                        <td className="px-4 py-3.5">
                          {isFile ? (
                            <span className="inline-flex items-center gap-1 rounded-full bg-[#2F7D5B]/12 px-2.5 py-0.5 text-xs font-medium text-[#2F7D5B]">
                              <Download className="h-3 w-3" />
                              本体直接下载 (file)
                            </span>
                          ) : (
                            <span className="inline-flex items-center gap-1 rounded-full bg-ink/10 px-2.5 py-0.5 text-xs font-medium text-ink">
                              <ExternalLink className="h-3 w-3" />
                              网页跳转 (url)
                            </span>
                          )}
                        </td>
                        <td className="px-4 py-3.5">
                          {link ? (
                            <a
                              href={link}
                              target="_blank"
                              rel="noreferrer"
                              className="block max-w-[300px] truncate text-cinnabar hover:underline"
                            >
                              {link}
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
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>

      {/* 新建/编辑弹窗 */}
      <Dialog open={form.open} onOpenChange={(o) => setForm((f) => ({ ...f, open: o }))}>
        <DialogContent className="max-h-[90vh] max-w-xl overflow-y-auto">
          <DialogHeader>
            <DialogTitle>{form.editing ? "编辑软件" : "新建软件 / 本地上传"}</DialogTitle>
            <DialogDescription>
              支持上传本地 .apk / .zip / .md 等文件，上传后在本体软件上可直接点击下载安装
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-4">
            {/* 下发模式切换 */}
            <div className="space-y-1.5">
              <Label>下发形式（决定本体软件点击行为）</Label>
              <div className="grid grid-cols-2 gap-2.5">
                <button
                  type="button"
                  onClick={() => setForm((f) => ({ ...f, mode: "file" }))}
                  className={`flex items-center justify-center gap-1.5 rounded-xl border px-3 py-2 text-xs font-medium transition ${
                    form.mode === "file"
                      ? "border-cinnabar bg-cinnabar text-paper"
                      : "border-border bg-paper-soft text-inkblack hover:bg-mist-soft"
                  }`}
                >
                  <Download className="h-3.5 w-3.5" />
                  本地文件直下 (APK/ZIP/MD)
                </button>
                <button
                  type="button"
                  onClick={() => setForm((f) => ({ ...f, mode: "url" }))}
                  className={`flex items-center justify-center gap-1.5 rounded-xl border px-3 py-2 text-xs font-medium transition ${
                    form.mode === "url"
                      ? "border-ink bg-ink text-paper"
                      : "border-border bg-paper-soft text-inkblack hover:bg-mist-soft"
                  }`}
                >
                  <ExternalLink className="h-3.5 w-3.5" />
                  网页链接跳转
                </button>
              </div>
            </div>

            {/* 本地文件上传区 */}
            <div className="rounded-xl border border-border bg-paper-soft p-3.5 space-y-2.5">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-xs font-semibold text-inkblack">本地文件上传到云端仓库</p>
                  <p className="text-[11px] text-inkblack/55">
                    选择本地 .apk / .zip / .md / 图标 / 截图，自动推送到仓库并填入本体直下链接
                  </p>
                </div>
              </div>

              {uploading && (
                <div className="rounded-lg bg-cinnabar/10 px-3 py-1.5 text-xs font-medium text-cinnabar">
                  {uploading}
                </div>
              )}

              <input
                ref={pkgInputRef}
                type="file"
                accept=".apk,.zip,.md,.7z,.rar,*/*"
                className="hidden"
                onChange={(e) => {
                  const file = e.target.files?.[0];
                  void handleUploadLocalFile(file, "pkg");
                  e.target.value = "";
                }}
              />
              <input
                ref={iconInputRef}
                type="file"
                accept="image/*"
                className="hidden"
                onChange={(e) => {
                  const file = e.target.files?.[0];
                  void handleUploadLocalFile(file, "icon");
                  e.target.value = "";
                }}
              />
              <input
                ref={previewInputRef}
                type="file"
                accept="image/*"
                className="hidden"
                onChange={(e) => {
                  const file = e.target.files?.[0];
                  void handleUploadLocalFile(file, "preview");
                  e.target.value = "";
                }}
              />

              <div className="flex flex-wrap gap-2">
                <Button
                  type="button"
                  size="sm"
                  disabled={Boolean(uploading)}
                  onClick={() => pkgInputRef.current?.click()}
                >
                  <CloudUpload className="h-3.5 w-3.5" />
                  上传软件文件 (.apk / .zip / .md)
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  disabled={Boolean(uploading)}
                  onClick={() => iconInputRef.current?.click()}
                >
                  <ImageIcon className="h-3.5 w-3.5" />
                  上传软件图标
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  disabled={Boolean(uploading)}
                  onClick={() => previewInputRef.current?.click()}
                >
                  <ImageIcon className="h-3.5 w-3.5" />
                  上传预览图
                </Button>
              </div>
            </div>

            <div className="space-y-1.5">
              <Label>软件名称 (title)</Label>
              <Input
                value={form.name}
                onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
                placeholder="例如：PixelLab_2.1.6"
              />
            </div>

            <div className="space-y-1.5">
              <Label>描述说明 (desc)</Label>
              <Textarea
                value={form.desc}
                onChange={(e) => setForm((f) => ({ ...f, desc: e.target.value }))}
                placeholder="简要介绍软件功能"
              />
            </div>

            <div className="space-y-1.5">
              <Label>文件下载直链 (apkUrl · 本地上传后自动填入，本体直接下载)</Label>
              <Input
                value={form.apkUrl}
                onChange={(e) => setForm((f) => ({ ...f, apkUrl: e.target.value }))}
                placeholder="https://raw.githubusercontent.com/.../xxx.apk"
              />
            </div>

            <div className="space-y-1.5">
              <Label>网页跳转链接 (url · 链接跳转模式使用)</Label>
              <Input
                value={form.url}
                onChange={(e) => setForm((f) => ({ ...f, url: e.target.value }))}
                placeholder="https://…"
              />
            </div>

            <div className="grid grid-cols-3 gap-3">
              <div className="space-y-1.5">
                <Label>作者 (author)</Label>
                <Input
                  value={form.author}
                  onChange={(e) => setForm((f) => ({ ...f, author: e.target.value }))}
                  placeholder="可选"
                />
              </div>
              <div className="space-y-1.5">
                <Label>角标 (badge)</Label>
                <Input
                  value={form.badge}
                  onChange={(e) => setForm((f) => ({ ...f, badge: e.target.value }))}
                  placeholder="如 最新版"
                />
              </div>
              <div className="space-y-1.5">
                <Label>标签 (tags)</Label>
                <Input
                  value={form.tags}
                  onChange={(e) => setForm((f) => ({ ...f, tags: e.target.value }))}
                  placeholder="如 软件 安装包 APK"
                />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div className="space-y-1.5">
                <Label>图标链接 (iconUrl)</Label>
                <Input
                  value={form.iconUrl}
                  onChange={(e) => setForm((f) => ({ ...f, iconUrl: e.target.value }))}
                  placeholder="https://…/icon.png"
                />
              </div>
              <div className="space-y-1.5">
                <Label>预览图链接 (previewUrl)</Label>
                <Input
                  value={form.previewUrl}
                  onChange={(e) => setForm((f) => ({ ...f, previewUrl: e.target.value }))}
                  placeholder="https://…/preview.png"
                />
              </div>
            </div>
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={() => setForm((f) => ({ ...f, open: false }))}>
              取消
            </Button>
            <Button onClick={handleSave} disabled={saving || Boolean(uploading)}>
              {saving ? "保存中…" : "保存并同步"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <ConfirmDialog
        open={Boolean(deleting)}
        onOpenChange={(o) => !o && setDeleting(null)}
        title="删除软件"
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
