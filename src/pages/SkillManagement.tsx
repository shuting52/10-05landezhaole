import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { CloudUpload, Download, ExternalLink, Image as ImageIcon, Pencil, Plus, Sparkles, Trash2, Video } from "lucide-react";
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
import type { SkillItem } from "@/types";

interface SkillFormState {
  open: boolean;
  editing: SkillItem | null;
  title: string;
  desc: string;
  promptType: string;
  prompt: string;
  url: string;
  author: string;
  badge: string;
  tags: string;
  previewUrl: string;
  mediaUrl: string;
  iconUrl: string;
  mode: "file" | "url";
}

const emptySkillForm: SkillFormState = {
  open: false,
  editing: null,
  title: "",
  desc: "",
  promptType: "skill",
  prompt: "",
  url: "",
  author: "懒得找了",
  badge: "强烈推荐",
  tags: "Skill技能",
  previewUrl: "",
  mediaUrl: "",
  iconUrl: "",
  mode: "file",
};

export default function SkillManagement() {
  const [skills, setSkills] = useState<SkillItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");
  const [deleting, setDeleting] = useState<SkillItem | null>(null);
  const [form, setForm] = useState<SkillFormState>(emptySkillForm);
  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState<string | null>(null);

  const skillFileRef = useRef<HTMLInputElement | null>(null);
  const previewInputRef = useRef<HTMLInputElement | null>(null);
  const mediaInputRef = useRef<HTMLInputElement | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const data = await api.getSkills();
      setSkills(data);
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
    if (!kw) return skills;
    return skills.filter(
      (sk) =>
        sk.title.toLowerCase().includes(kw) ||
        sk.desc.toLowerCase().includes(kw) ||
        sk.tags.toLowerCase().includes(kw) ||
        sk.prompt.toLowerCase().includes(kw)
    );
  }, [skills, keyword]);

  const openCreate = () => {
    setForm({ ...emptySkillForm, open: true, editing: null });
  };

  const openEdit = (sk: SkillItem) => {
    setForm({
      open: true,
      editing: sk,
      title: sk.title,
      desc: sk.desc || "",
      promptType: sk.promptType || "skill",
      prompt: sk.prompt || "",
      url: sk.url || "",
      author: sk.author || "懒得找了",
      badge: sk.badge || "",
      tags: sk.tags || "",
      previewUrl: sk.previewUrl || "",
      mediaUrl: sk.mediaUrl || "",
      iconUrl: sk.iconUrl || "",
      mode: sk.mode === "url" ? "url" : "file",
    });
  };

  const handleUploadLocalFile = async (
    file: File | undefined,
    target: "skillFile" | "preview" | "media"
  ) => {
    if (!file) return;
    setUploading(`正在上传 ${file.name} 到云端仓库…`);
    try {
      const { rawUrl, fileName } = await ghUploadFile(file, "dist/uploads");
      setForm((f) => {
        if (target === "skillFile") {
          const baseTitle = fileName.replace(/\.[^/.]+$/, "");
          return {
            ...f,
            url: rawUrl,
            mode: "file",
            title: f.title.trim() ? f.title : baseTitle,
          };
        }
        if (target === "preview") {
          return { ...f, previewUrl: rawUrl };
        }
        return { ...f, mediaUrl: rawUrl };
      });
      toast.success(`文件「${file.name}」已上传，本体软件可直接下载`);
    } catch (e) {
      toast.error(`上传失败：${(e as Error).message}`);
    } finally {
      setUploading(null);
    }
  };

  const handleSave = async () => {
    if (!form.title.trim()) {
      toast.error("请填写 Skill 名称");
      return;
    }
    setSaving(true);
    try {
      const item: SkillItem = {
        id: form.editing?.id || `sk_${Date.now()}`,
        type: "skill",
        promptType: form.promptType || "skill",
        title: form.title.trim(),
        desc: form.desc.trim(),
        prompt: form.prompt,
        url: form.url.trim(),
        author: form.author.trim() || "懒得找了",
        badge: form.badge.trim(),
        tags: form.tags.trim(),
        previewUrl: form.previewUrl.trim(),
        mediaUrl: form.mediaUrl.trim(),
        iconUrl: form.iconUrl.trim(),
        mode: form.mode,
        _raw: form.editing?._raw,
      };
      await api.saveSkill(item);
      toast.success(`已保存 Skill「${form.title}」并实时同步到本体`);
      setForm({ ...emptySkillForm, open: false });
      void load();
    } catch (e) {
      toast.error(`保存失败：${(e as Error).message}`);
    }
    setSaving(false);
  };

  const handleDelete = async () => {
    if (!deleting) return;
    try {
      await api.deleteSkill(deleting.id);
      toast.success(`已删除 Skill「${deleting.title}」并同步到本体`);
      setDeleting(null);
      void load();
    } catch (e) {
      toast.error(`删除失败：${(e as Error).message}`);
    }
  };

  return (
    <div className="space-y-6">
      <PageHeader
        title="Skill 技能库管理"
        description="维护本体 Skill 技能包与提示词（skills[]），支持本地上传 .zip / .md / .apk 文件供本体直接下载"
        actions={
          <Button onClick={openCreate}>
            <Plus className="h-4 w-4" />
            新建 Skill / 上传技能包
          </Button>
        }
      />

      <Card className="overflow-hidden">
        <CardHeader className="gap-4 border-b border-border pb-4">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h3 className="text-base font-semibold text-inkblack">Skill 技能列表</h3>
              <p className="mt-0.5 text-sm text-inkblack/55">
                共 {skills.length} 个（真实 admin-data.skills · 文件形式在本体可直接下载）
              </p>
            </div>
            <SearchInput value={keyword} onChange={setKeyword} placeholder="搜索 Skill 标题、描述或标签" />
          </div>
        </CardHeader>

        <CardContent className="p-0">
          {loading ? (
            <LoadingState label="正在加载 Skill 技能库…" />
          ) : filtered.length === 0 ? (
            <EmptyState title="没有匹配的 Skill" description="点击右上角上传第一个 Skill 技能包。" />
          ) : (
            <div className="w-full overflow-x-auto">
              <table className="w-full min-w-[760px] border-collapse text-sm">
                <thead>
                  <tr className="border-b border-border">
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">Skill 名称与说明</th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">下发形式</th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">文件直链 / 跳转链接</th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">操作</th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map((sk) => {
                    const isFile = sk.mode !== "url";
                    return (
                      <tr key={sk.id} className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60">
                        <td className="px-4 py-3.5">
                          <div className="flex items-start gap-2.5">
                            <Sparkles className="mt-0.5 h-4 w-4 shrink-0 text-cinnabar" />
                            <div>
                              <div className="flex items-center gap-2">
                                <span className="font-medium text-inkblack">{sk.title}</span>
                                {sk.badge && (
                                  <span className="rounded bg-cinnabar/12 px-1.5 py-0.5 text-xs font-medium text-cinnabar">
                                    {sk.badge}
                                  </span>
                                )}
                              </div>
                              {sk.desc && <p className="mt-0.5 text-xs text-inkblack/60">{sk.desc}</p>}
                              {(sk.author || sk.tags) && (
                                <p className="mt-0.5 text-[11px] text-inkblack/45">
                                  {sk.author ? `作者: ${sk.author} ` : ""}
                                  {sk.tags ? `· 标签: ${sk.tags}` : ""}
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
                          {sk.url ? (
                            <a
                              href={sk.url}
                              target="_blank"
                              rel="noreferrer"
                              className="block max-w-[300px] truncate text-cinnabar hover:underline"
                            >
                              {sk.url}
                            </a>
                          ) : (
                            <span className="text-inkblack/40">-</span>
                          )}
                        </td>
                        <td className="px-4 py-3.5">
                          <div className="flex items-center justify-end gap-1">
                            <Button variant="ghost" size="icon-sm" aria-label="编辑" onClick={() => openEdit(sk)}>
                              <Pencil className="h-4 w-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="icon-sm"
                              aria-label="删除"
                              className="text-cinnabar hover:bg-cinnabar/10 hover:text-cinnabar"
                              onClick={() => setDeleting(sk)}
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
            <DialogTitle>{form.editing ? "编辑 Skill 技能" : "新建 Skill / 本地上传"}</DialogTitle>
            <DialogDescription>
              支持上传本地 .zip / .md / .apk 等技能包文件，上传后在本体软件上可直接点击下载到本地
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
                  本地文件直下 (ZIP/MD/APK)
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
              <div>
                <p className="text-xs font-semibold text-inkblack">本地 Skill 文件上传到云端仓库</p>
                <p className="text-[11px] text-inkblack/55">
                  选择本地 .zip / .md / .apk / 预览图 / 演示视频，自动推送到仓库并生成本体直下链接
                </p>
              </div>

              {uploading && (
                <div className="rounded-lg bg-cinnabar/10 px-3 py-1.5 text-xs font-medium text-cinnabar">
                  {uploading}
                </div>
              )}

              <input
                ref={skillFileRef}
                type="file"
                accept=".zip,.md,.apk,.7z,.rar,*/*"
                className="hidden"
                onChange={(e) => {
                  const file = e.target.files?.[0];
                  void handleUploadLocalFile(file, "skillFile");
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
              <input
                ref={mediaInputRef}
                type="file"
                accept="video/*,image/*"
                className="hidden"
                onChange={(e) => {
                  const file = e.target.files?.[0];
                  void handleUploadLocalFile(file, "media");
                  e.target.value = "";
                }}
              />

              <div className="flex flex-wrap gap-2">
                <Button
                  type="button"
                  size="sm"
                  disabled={Boolean(uploading)}
                  onClick={() => skillFileRef.current?.click()}
                >
                  <CloudUpload className="h-3.5 w-3.5" />
                  上传技能包 (.zip / .md / .apk)
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
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  disabled={Boolean(uploading)}
                  onClick={() => mediaInputRef.current?.click()}
                >
                  <Video className="h-3.5 w-3.5" />
                  上传演示视频
                </Button>
              </div>
            </div>

            <div className="space-y-1.5">
              <Label>Skill 名称 (title)</Label>
              <Input
                value={form.title}
                onChange={(e) => setForm((f) => ({ ...f, title: e.target.value }))}
                placeholder="例如：音乐节海报专业版"
              />
            </div>

            <div className="space-y-1.5">
              <Label>描述说明 (desc)</Label>
              <Textarea
                value={form.desc}
                onChange={(e) => setForm((f) => ({ ...f, desc: e.target.value }))}
                placeholder="例如：将压缩包丢给任意一款 AI Agent 即可"
              />
            </div>

            <div className="space-y-1.5">
              <Label>提示词内容 (prompt · 可选)</Label>
              <Textarea
                value={form.prompt}
                onChange={(e) => setForm((f) => ({ ...f, prompt: e.target.value }))}
                placeholder="若包含提示词正文可在此填写"
              />
            </div>

            <div className="space-y-1.5">
              <Label>技能包下载直链 / 跳转链接 (url · 本地上传后自动填入)</Label>
              <Input
                value={form.url}
                onChange={(e) => setForm((f) => ({ ...f, url: e.target.value }))}
                placeholder="https://raw.githubusercontent.com/.../xxx.zip"
              />
            </div>

            <div className="grid grid-cols-3 gap-3">
              <div className="space-y-1.5">
                <Label>作者 (author)</Label>
                <Input
                  value={form.author}
                  onChange={(e) => setForm((f) => ({ ...f, author: e.target.value }))}
                  placeholder="懒得找了"
                />
              </div>
              <div className="space-y-1.5">
                <Label>推荐角标 (badge)</Label>
                <Input
                  value={form.badge}
                  onChange={(e) => setForm((f) => ({ ...f, badge: e.target.value }))}
                  placeholder="如 强烈推荐"
                />
              </div>
              <div className="space-y-1.5">
                <Label>标签 (tags)</Label>
                <Input
                  value={form.tags}
                  onChange={(e) => setForm((f) => ({ ...f, tags: e.target.value }))}
                  placeholder="如 专为音乐海报设计"
                />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div className="space-y-1.5">
                <Label>预览图链接 (previewUrl)</Label>
                <Input
                  value={form.previewUrl}
                  onChange={(e) => setForm((f) => ({ ...f, previewUrl: e.target.value }))}
                  placeholder="https://…/preview.png"
                />
              </div>
              <div className="space-y-1.5">
                <Label>演示视频链接 (mediaUrl)</Label>
                <Input
                  value={form.mediaUrl}
                  onChange={(e) => setForm((f) => ({ ...f, mediaUrl: e.target.value }))}
                  placeholder="https://…/demo.mp4"
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
        title="删除 Skill"
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
