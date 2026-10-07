import { useEffect, useState } from "react";
import { Plus } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { PageHeader } from "@/components/common/PageHeader";
import { StatCard } from "@/components/dashboard/StatCard";
import { CardTableSection } from "@/components/dashboard/CardTableSection";
import { ActivityLog } from "@/components/dashboard/ActivityLog";
import { LoadingState } from "@/components/common/LoadingState";
import { CardFormDialog } from "@/components/common/CardFormDialog";
import { api } from "@/lib/api";
import { cn } from "@/lib/utils";
import type { ActivityLog as ActivityLogType, StatItem } from "@/types";

const dateRanges = ["今日", "近7天", "近30天"] as const;

export default function Dashboard() {
  const [range, setRange] = useState<(typeof dateRanges)[number]>("近7天");
  const [stats, setStats] = useState<StatItem[]>([]);
  const [logs, setLogs] = useState<ActivityLogType[]>([]);
  const [loading, setLoading] = useState(true);
  const [createOpen, setCreateOpen] = useState(false);

  useEffect(() => {
    let active = true;
    (async () => {
      setLoading(true);
      const [s, l] = await Promise.all([api.getStats(), api.getActivityLogs()]);
      if (!active) return;
      setStats(s);
      setLogs(l);
      setLoading(false);
    })();
    return () => {
      active = false;
    };
  }, []);

  return (
    <div className="space-y-6">
      <PageHeader
        title="总览"
        description="查看资源使用情况和最近操作"
        actions={
          <>
            <div className="flex items-center rounded-xl border border-border bg-paper-soft p-0.5 shadow-sm">
              {dateRanges.map((r) => (
                <button
                  key={r}
                  type="button"
                  onClick={() => {
                    setRange(r);
                    toast.success(`已切换到「${r}」`);
                  }}
                  className={cn(
                    "rounded-lg px-3 py-1.5 text-sm font-medium transition-colors",
                    range === r
                      ? "bg-cinnabar text-paper shadow-seal"
                      : "text-inkblack/60 hover:bg-mist-soft hover:text-inkblack"
                  )}
                >
                  {r}
                </button>
              ))}
            </div>
            <Button onClick={() => setCreateOpen(true)}>
              <Plus className="h-4 w-4" />
              新建卡片
            </Button>
          </>
        }
      />

      {/* 统计卡片 */}
      {loading ? (
        <Card>
          <LoadingState label="正在加载统计数据…" />
        </Card>
      ) : (
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {stats.map((item) => (
            <StatCard key={item.id} item={item} />
          ))}
        </div>
      )}

      {/* 卡片管理 + 最近操作 */}
      <div className="grid grid-cols-1 gap-6 xl:grid-cols-3">
        <div className="xl:col-span-2">
          <CardTableSection pageSize={6} />
        </div>

        <Card className="h-fit">
          <CardHeader className="border-b border-border pb-4">
            <h3 className="text-base font-semibold text-inkblack">最近操作</h3>
            <p className="mt-0.5 text-sm text-inkblack/55">团队近期的资源变更记录</p>
          </CardHeader>
          <CardContent className="p-3">
            {loading ? (
              <LoadingState label="正在加载操作记录…" />
            ) : (
              <ActivityLog logs={logs} />
            )}
          </CardContent>
        </Card>
      </div>

      <CardFormDialog open={createOpen} onOpenChange={setCreateOpen} />
    </div>
  );
}
