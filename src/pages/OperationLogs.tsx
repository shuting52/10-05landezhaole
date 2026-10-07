import { useEffect, useMemo, useState } from "react";
import { Download } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { PageHeader } from "@/components/common/PageHeader";
import { SearchInput } from "@/components/common/SearchInput";
import { Pagination } from "@/components/common/Pagination";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingState } from "@/components/common/LoadingState";
import { LogActionBadge } from "@/components/common/StatusBadge";
import { api } from "@/lib/api";
import type { ActivityLog, LogActionType } from "@/types";

const PAGE_SIZE = 8;

export default function OperationLogs() {
  const [logs, setLogs] = useState<ActivityLog[]>([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");
  const [action, setAction] = useState<LogActionType | "all">("all");
  const [page, setPage] = useState(1);

  useEffect(() => {
    let active = true;
    (async () => {
      setLoading(true);
      const data = await api.getOperationLogs();
      if (!active) return;
      setLogs(data);
      setLoading(false);
    })();
    return () => {
      active = false;
    };
  }, []);

  const filtered = useMemo(() => {
    const kw = keyword.trim().toLowerCase();
    return logs.filter((l) => {
      const matchKw =
        !kw ||
        l.operator.toLowerCase().includes(kw) ||
        l.content.toLowerCase().includes(kw) ||
        (l.ip ?? "").toLowerCase().includes(kw);
      const matchAction = action === "all" || l.action === action;
      return matchKw && matchAction;
    });
  }, [logs, keyword, action]);

  useEffect(() => {
    setPage(1);
  }, [keyword, action]);

  const paged = filtered.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  return (
    <div className="space-y-6">
      <PageHeader
        title="操作日志"
        description="记录后台所有关键操作，便于审计与追溯"
        actions={
          <Button variant="outline" onClick={() => toast.success("已导出操作日志（CSV）")}>
            <Download className="h-4 w-4" />
            导出日志
          </Button>
        }
      />

      <Card className="overflow-hidden">
        <CardHeader className="gap-4 border-b border-border pb-4">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h3 className="text-base font-semibold text-inkblack">日志列表</h3>
              <p className="mt-0.5 text-sm text-inkblack/55">共 {filtered.length} 条记录</p>
            </div>
            <div className="flex flex-col gap-2 sm:flex-row sm:items-center">
              <SearchInput
                value={keyword}
                onChange={setKeyword}
                placeholder="搜索操作人、内容或 IP"
              />
              <Select value={action} onValueChange={(v) => setAction(v as LogActionType | "all")}>
                <SelectTrigger className="w-[130px]">
                  <SelectValue placeholder="操作类型" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">全部类型</SelectItem>
                  <SelectItem value="create">创建</SelectItem>
                  <SelectItem value="update">修改</SelectItem>
                  <SelectItem value="publish">发布</SelectItem>
                  <SelectItem value="delete">删除</SelectItem>
                  <SelectItem value="review">审核</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardHeader>

        <CardContent className="p-0">
          {loading ? (
            <LoadingState label="正在加载日志…" />
          ) : paged.length === 0 ? (
            <EmptyState title="没有匹配的日志" description="试试更换关键词或操作类型。" />
          ) : (
            <>
              <div className="w-full overflow-x-auto">
                <table className="w-full min-w-[820px] border-collapse text-sm">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                        操作人
                      </th>
                      <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                        操作类型
                      </th>
                      <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                        操作内容
                      </th>
                      <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                        IP 地址
                      </th>
                      <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                        操作时间
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {paged.map((log) => (
                      <tr
                        key={log.id}
                        className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60"
                      >
                        <td className="px-4 py-3.5">
                          <div className="flex items-center gap-2.5">
                            <div
                              className="flex h-7 w-7 shrink-0 items-center justify-center rounded-full text-xs font-semibold text-white"
                              style={{ backgroundColor: log.avatarColor }}
                            >
                              {log.operator.slice(0, 1)}
                            </div>
                            <span className="font-medium text-inkblack">{log.operator}</span>
                          </div>
                        </td>
                        <td className="px-4 py-3.5">
                          <LogActionBadge action={log.action} />
                        </td>
                        <td className="px-4 py-3.5 text-inkblack/75">{log.content}</td>
                        <td className="whitespace-nowrap px-4 py-3.5 font-mono text-xs text-inkblack/60">
                          {log.ip}
                        </td>
                        <td className="whitespace-nowrap px-4 py-3.5 text-inkblack/60">
                          {log.time}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <Pagination
                page={page}
                pageSize={PAGE_SIZE}
                total={filtered.length}
                onPageChange={setPage}
              />
            </>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
