import { useCallback, useEffect, useState } from "react";
import { CheckCircle2, Cloud, RefreshCw, Rocket, Save, Send } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { Separator } from "@/components/ui/separator";
import { PageHeader } from "@/components/common/PageHeader";
import { getStore, loadAdmin, subscribe, applyToDevice, publishRelease } from "@/lib/store";
import { getToken, setToken, getConfig, setConfig } from "@/lib/github";

interface ToggleRowProps {
  title: string;
  description: string;
  checked: boolean;
  onCheckedChange: (v: boolean) => void;
}

function ToggleRow({ title, description, checked, onCheckedChange }: ToggleRowProps) {
  return (
    <div className="flex items-center justify-between gap-4 py-3">
      <div className="min-w-0">
        <p className="text-sm font-medium text-inkblack">{title}</p>
        <p className="mt-0.5 text-xs text-inkblack/55">{description}</p>
      </div>
      <Switch checked={checked} onCheckedChange={onCheckedChange} />
    </div>
  );
}

export default function Settings() {
  // 云端连接
  const [token, setTokenInput] = useState(getToken());
  const [owner, setOwner] = useState(getConfig().owner);
  const [repo, setRepo] = useState(getConfig().repo);
  const [connecting, setConnecting] = useState(false);

  // 数据状态
  const [store, setStore] = useState(getStore());
  const [publishing, setPublishing] = useState(false);

  useEffect(() => {
    const un = subscribe((s) => setStore(s));
    return un;
  }, []);

  // 首次进入自动加载
  const initLoad = useCallback(async () => {
    if (!getStore().admin) {
      await loadAdmin();
    }
  }, []);
  useEffect(() => {
    void initLoad();
  }, [initLoad]);

  const handleConnect = async () => {
    setConnecting(true);
    setToken(token);
    setConfig({ owner, repo, branch: "main" });
    try {
      await loadAdmin(false);
      const s = getStore();
      toast.success(
        s.state === "readonly"
          ? `已连接（只读镜像）：v${s.admin?.version?.name ?? "-"}`
          : `已连接：v${s.admin?.version?.name ?? "-"}（${owner}/${repo}）`
      );
    } catch {
      try {
        await loadAdmin(true);
        const s = getStore();
        toast.success(`已连接（只读镜像）：v${s.admin?.version?.name ?? "-"}`);
      } catch (e) {
        toast.error(`连接失败：${(e as Error).message}`);
      }
    }
    setConnecting(false);
  };

  const handleApply = async () => {
    setPublishing(true);
    try {
      await applyToDevice();
      toast.success("✅ 已实时同步到本体软件（零延迟生效，不弹更新窗）");
    } catch (e) {
      toast.error(`同步失败：${(e as Error).message}`);
    }
    setPublishing(false);
  };

  const handlePublish = async () => {
    const admin = store.admin;
    if (!admin) {
      toast.error("请先连接加载数据");
      return;
    }
    const newName = (() => {
      const parts = (admin.version?.name || "1.0").split(".");
      parts[parts.length - 1] = String(Number(parts[parts.length - 1] || 0) + 1);
      return parts.join(".");
    })();
    const ok = window.confirm(
      `⚠️ 将发布新版本 v${newName}（versionCode ${admin.version?.code ?? 1} → ${(admin.version?.code ?? 1) + 1}）\n\n本体软件将立即弹出更新窗口。\n\n如果只是想保存内容并实时同步，请点「✅ 应用」（不升版本、不弹窗）。\n\n确定发布新版本吗？`
    );
    if (!ok) return;
    setPublishing(true);
    try {
      const r = await publishRelease();
      toast.success(`🚀 新版本 v${r.name} 发布成功！本体已触发更新弹窗`);
    } catch (e) {
      toast.error(`发布失败：${(e as Error).message}`);
    }
    setPublishing(false);
  };

  const v = store.admin?.version;
  const stateLabel = {
    idle: "未连接",
    loading: "加载中…",
    connected: "已连接（GitHub API）",
    readonly: "已连接（只读镜像）",
    error: "连接异常",
  }[store.state] ?? store.state;

  return (
    <div className="space-y-6">
      <PageHeader
        title="系统设置"
        description="云端连接、版本发布与基础配置（GitHub 仓库即数据中枢）"
      />

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        {/* 云端连接 */}
        <Card>
          <CardHeader className="border-b border-border pb-4">
            <div className="flex items-center gap-2">
              <Cloud className="h-4 w-4 text-cinnabar" />
              <h3 className="text-base font-semibold text-inkblack">云端连接</h3>
            </div>
            <p className="mt-0.5 text-sm text-inkblack/55">配置 GitHub 仓库与 Token（写操作需要）</p>
          </CardHeader>
          <CardContent className="space-y-4 pt-5">
            <div className="space-y-1.5">
              <Label htmlFor="gh-token">GitHub Token（PAT，仅存本机浏览器）</Label>
              <Input
                id="gh-token"
                type="password"
                placeholder="ghp_xxx / github_pat_xxx"
                value={token}
                onChange={(e) => setTokenInput(e.target.value)}
              />
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div className="space-y-1.5">
                <Label htmlFor="gh-owner">Owner</Label>
                <Input id="gh-owner" value={owner} onChange={(e) => setOwner(e.target.value)} />
              </div>
              <div className="space-y-1.5">
                <Label htmlFor="gh-repo">仓库</Label>
                <Input id="gh-repo" value={repo} onChange={(e) => setRepo(e.target.value)} />
              </div>
            </div>
            <div className="flex items-center justify-between rounded-lg border border-border bg-paper-soft px-3 py-2.5">
              <div className="flex items-center gap-2 text-sm">
                {store.state === "connected" || store.state === "readonly" ? (
                  <CheckCircle2 className="h-4 w-4 text-[#2F7D5B]" />
                ) : (
                  <span className="h-2 w-2 rounded-full bg-inkblack/25" />
                )}
                <span className="font-medium text-inkblack">{stateLabel}</span>
                {v && <span className="text-xs text-inkblack/55">· v{v.name} (code {v.code})</span>}
              </div>
              <div className="flex gap-2">
                <Button variant="outline" size="sm" onClick={() => void loadAdmin()}>
                  <RefreshCw className="h-3.5 w-3.5" />
                  刷新
                </Button>
                <Button size="sm" onClick={handleConnect} disabled={connecting}>
                  {connecting ? "连接中…" : "连接"}
                </Button>
              </div>
            </div>
            {store.error && (
              <p className="text-xs text-cinnabar">{store.error}</p>
            )}
          </CardContent>
        </Card>

        {/* 发布管理（写死规则8：应用同步不弹窗 / 发布才弹窗） */}
        <Card>
          <CardHeader className="border-b border-border pb-4">
            <div className="flex items-center gap-2">
              <Rocket className="h-4 w-4 text-cinnabar" />
              <h3 className="text-base font-semibold text-inkblack">发布管理</h3>
            </div>
            <p className="mt-0.5 text-sm text-inkblack/55">
              双模式：应用=实时同步不弹窗 · 发布=bump 版本弹更新窗
            </p>
          </CardHeader>
          <CardContent className="space-y-4 pt-5">
            <div className="rounded-lg border border-border bg-paper-soft p-3 text-sm">
              <div className="flex items-center justify-between">
                <span className="text-inkblack/55">当前版本</span>
                <span className="font-semibold text-inkblack">v{v?.name ?? "-"} (code {v?.code ?? "-"})</span>
              </div>
              <div className="mt-2 flex items-center justify-between">
                <span className="text-inkblack/55">模式</span>
                <span className={`rounded-full px-2 py-0.5 text-xs font-medium ${store.mode === "release" ? "bg-gold/15 text-[#9A7420]" : "bg-[#2F7D5B]/12 text-[#2F7D5B]"}`}>
                  {store.mode === "release" ? "发布模式" : "内容同步模式"}
                </span>
              </div>
              {store.mode === "release" && (
                <p className="mt-2 text-xs text-[#9A7420]">⚠️ 当前为发布模式：保存操作将触发更新弹窗</p>
              )}
            </div>
            <div className="grid grid-cols-2 gap-3">
              <Button className="bg-[#2F7D5B] hover:bg-[#2F7D5B]/90" onClick={handleApply} disabled={publishing || store.state === "idle"}>
                <Send className="h-4 w-4" />
                {publishing ? "推送中…" : "✅ 应用并实时同步"}
              </Button>
              <Button className="bg-cinnabar hover:bg-cinnabar/90" onClick={handlePublish} disabled={publishing || store.state === "idle"}>
                <Rocket className="h-4 w-4" />
                发布新版本
              </Button>
            </div>
            <p className="text-xs text-inkblack/55">
              「应用」只写内容（mode=content），本体数秒生效、不弹窗；「发布」递增版本号（mode=release），本体弹更新窗。
            </p>
          </CardContent>
        </Card>

        {/* 基础设置 */}
        <Card>
          <CardHeader className="border-b border-border pb-4">
            <h3 className="text-base font-semibold text-inkblack">基础设置</h3>
            <p className="mt-0.5 text-sm text-inkblack/55">后台的基本信息（保存后同步到本体）</p>
          </CardHeader>
          <CardContent className="space-y-4 pt-5">
            <div className="space-y-1.5">
              <Label htmlFor="site-name">应用名称（appName）</Label>
              <Input
                id="site-name"
                value={store.admin?.settings?.appName ?? "懒得找了"}
                onChange={(e) => {
                  const st = getStore();
                  if (st.admin) {
                    st.admin.settings = { ...st.admin.settings, appName: e.target.value } as never;
                  }
                }}
              />
            </div>
            <div className="space-y-1.5">
              <Label htmlFor="site-slogan">标语（slogan）</Label>
              <Input
                id="site-slogan"
                value={store.admin?.settings?.slogan ?? ""}
                onChange={(e) => {
                  const st = getStore();
                  if (st.admin) {
                    st.admin.settings = { ...st.admin.settings, slogan: e.target.value } as never;
                  }
                }}
              />
            </div>
            <Button
              variant="outline"
              onClick={() => {
                const st = getStore();
                if (!st.admin) {
                  toast.error("请先连接加载数据");
                  return;
                }
                void import("@/lib/api").then(({ api }) =>
                  api.saveSettings({ appName: st.admin!.settings?.appName, slogan: st.admin!.settings?.slogan }).then(() =>
                    toast.success("基础设置已同步到云端")
                  ).catch((e) => toast.error(`保存失败：${(e as Error).message}`))
                );
              }}
            >
              <Save className="h-4 w-4" />
              保存基础设置
            </Button>
          </CardContent>
        </Card>

        {/* 通知设置 */}
        <Card>
          <CardHeader className="border-b border-border pb-4">
            <h3 className="text-base font-semibold text-inkblack">通知设置</h3>
            <p className="mt-0.5 text-sm text-inkblack/55">选择需要接收的系统通知</p>
          </CardHeader>
          <CardContent className="divide-y divide-border pt-2">
            <ToggleRow
              title="审核结果通知"
              description="卡片审核通过或被驳回时通知我"
              checked={false}
              onCheckedChange={(v) => toast.info(v ? "已开启" : "已关闭")}
            />
            <ToggleRow
              title="下载量提醒"
              description="单张卡片下载量突破阈值时通知我"
              checked={false}
              onCheckedChange={(v) => toast.info(v ? "已开启" : "已关闭")}
            />
            <ToggleRow
              title="每周数据周报"
              description="每周一发送上周资源使用汇总"
              checked={false}
              onCheckedChange={(v) => toast.info(v ? "已开启" : "已关闭")}
            />
          </CardContent>
        </Card>
      </div>

      <Separator />
    </div>
  );
}
