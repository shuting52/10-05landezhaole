import { useEffect, useState } from "react";
import { ArrowDown, ArrowUp, Plus } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { PageHeader } from "@/components/common/PageHeader";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingState } from "@/components/common/LoadingState";
import { EnabledBadge } from "@/components/common/StatusBadge";
import { api } from "@/lib/api";
import { formatNumber } from "@/lib/utils";
import type { Category } from "@/types";

export default function CategoryManagement() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let active = true;
    (async () => {
      setLoading(true);
      const data = await api.getCategories();
      if (!active) return;
      setCategories(data);
      setLoading(false);
    })();
    return () => {
      active = false;
    };
  }, []);

  const move = (id: string, dir: -1 | 1) => {
    setCategories((prev) => {
      const sorted = [...prev].sort((a, b) => a.order - b.order);
      const idx = sorted.findIndex((c) => c.id === id);
      const target = idx + dir;
      if (target < 0 || target >= sorted.length) return prev;
      [sorted[idx], sorted[target]] = [sorted[target], sorted[idx]];
      return sorted.map((c, i) => ({ ...c, order: i + 1 }));
    });
    toast.success("排序已更新");
  };

  return (
    <div className="space-y-6">
      <PageHeader
        title="分类管理"
        description="维护卡片分类，调整展示顺序与启用状态"
        actions={
          <Button onClick={() => toast.success("已打开新增分类面板")}>
            <Plus className="h-4 w-4" />
            新增分类
          </Button>
        }
      />

      <Card className="overflow-hidden">
        <CardHeader className="border-b border-border pb-4">
          <h3 className="text-base font-semibold text-inkblack">分类列表</h3>
          <p className="mt-0.5 text-sm text-inkblack/55">共 {categories.length} 个分类</p>
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
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      分类名称
                    </th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      卡片数量
                    </th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      排序
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
                  {[...categories]
                    .sort((a, b) => a.order - b.order)
                    .map((cat) => (
                      <tr
                        key={cat.id}
                        className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60"
                      >
                        <td className="px-4 py-3.5 font-medium text-inkblack">{cat.name}</td>
                        <td className="px-4 py-3.5 text-right font-medium tabular-nums text-inkblack">
                          {formatNumber(cat.cardCount)}
                        </td>
                        <td className="px-4 py-3.5">
                          <div className="flex items-center gap-1.5">
                            <span className="w-6 text-center text-inkblack/60">{cat.order}</span>
                            <Button
                              variant="ghost"
                              size="icon-sm"
                              aria-label="上移"
                              onClick={() => move(cat.id, -1)}
                            >
                              <ArrowUp className="h-3.5 w-3.5" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="icon-sm"
                              aria-label="下移"
                              onClick={() => move(cat.id, 1)}
                            >
                              <ArrowDown className="h-3.5 w-3.5" />
                            </Button>
                          </div>
                        </td>
                        <td className="px-4 py-3.5">
                          <EnabledBadge enabled={cat.status === "enabled"} />
                        </td>
                        <td className="px-4 py-3.5 text-right">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => toast.info(`编辑分类「${cat.name}」`)}
                          >
                            编辑
                          </Button>
                        </td>
                      </tr>
                    ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
