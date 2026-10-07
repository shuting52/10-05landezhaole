import { useEffect, useMemo, useState } from "react";
import { Pencil, Plus, Trash2 } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { PageHeader } from "@/components/common/PageHeader";
import { SearchInput } from "@/components/common/SearchInput";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingState } from "@/components/common/LoadingState";
import { ConfirmDialog } from "@/components/common/ConfirmDialog";
import { ButtonTypeBadge, EnabledBadge } from "@/components/common/StatusBadge";
import { api } from "@/lib/api";
import { formatNumber } from "@/lib/utils";
import type { ResourceButton } from "@/types";

export default function ButtonManagement() {
  const [buttons, setButtons] = useState<ResourceButton[]>([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");
  const [deleting, setDeleting] = useState<ResourceButton | null>(null);

  useEffect(() => {
    let active = true;
    (async () => {
      setLoading(true);
      const data = await api.getButtons();
      if (!active) return;
      setButtons(data);
      setLoading(false);
    })();
    return () => {
      active = false;
    };
  }, []);

  const filtered = useMemo(() => {
    const kw = keyword.trim().toLowerCase();
    if (!kw) return buttons;
    return buttons.filter((b) => b.name.toLowerCase().includes(kw));
  }, [buttons, keyword]);

  return (
    <div className="space-y-6">
      <PageHeader
        title="按钮管理"
        description="维护卡片中使用的按钮样式与跳转行为"
        actions={
          <Button onClick={() => toast.success("已打开新建按钮面板")}>
            <Plus className="h-4 w-4" />
            新建按钮
          </Button>
        }
      />

      <Card className="overflow-hidden">
        <CardHeader className="gap-4 border-b border-border pb-4">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h3 className="text-base font-semibold text-inkblack">按钮列表</h3>
              <p className="mt-0.5 text-sm text-inkblack/55">共 {buttons.length} 个按钮</p>
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
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      按钮名称
                    </th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      按钮类型
                    </th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      使用次数
                    </th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      状态
                    </th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      操作
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map((btn) => (
                    <tr
                      key={btn.id}
                      className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60"
                    >
                      <td className="px-4 py-3.5 font-medium text-inkblack">{btn.name}</td>
                      <td className="px-4 py-3.5">
                        <ButtonTypeBadge type={btn.type} />
                      </td>
                      <td className="px-4 py-3.5 text-right font-medium tabular-nums text-inkblack">
                        {formatNumber(btn.usageCount)}
                      </td>
                      <td className="px-4 py-3.5">
                        <EnabledBadge enabled={btn.status === "enabled"} />
                      </td>
                      <td className="px-4 py-3.5">
                        <div className="flex items-center justify-end gap-1">
                          <Button
                            variant="ghost"
                            size="icon-sm"
                            aria-label="编辑"
                            onClick={() => toast.info(`编辑按钮「${btn.name}」`)}
                          >
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

      <ConfirmDialog
        open={Boolean(deleting)}
        onOpenChange={(o) => !o && setDeleting(null)}
        title="删除按钮"
        description={
          <>
            确定要删除按钮「<span className="font-medium text-inkblack">{deleting?.name}</span>」吗？
          </>
        }
        confirmText="删除"
        onConfirm={() => deleting && toast.success(`已删除按钮「${deleting.name}」`)}
      />
    </div>
  );
}
