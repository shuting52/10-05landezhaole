import { useEffect, useMemo, useState } from "react";
import { Pencil, Plus } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { PageHeader } from "@/components/common/PageHeader";
import { SearchInput } from "@/components/common/SearchInput";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingState } from "@/components/common/LoadingState";
import { Tooltip, TooltipContent, TooltipTrigger } from "@/components/ui/tooltip";
import { api } from "@/lib/api";
import { formatNumber } from "@/lib/utils";
import type { TextItem } from "@/types";

export default function TextManagement() {
  const [texts, setTexts] = useState<TextItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");

  useEffect(() => {
    let active = true;
    (async () => {
      setLoading(true);
      const data = await api.getTexts();
      if (!active) return;
      setTexts(data);
      setLoading(false);
    })();
    return () => {
      active = false;
    };
  }, []);

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

  return (
    <div className="space-y-6">
      <PageHeader
        title="文字管理"
        description="维护卡片中使用的文案素材，支持分类与复用"
        actions={
          <Button onClick={() => toast.success("已打开新建文案面板")}>
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
              <p className="mt-0.5 text-sm text-inkblack/55">共 {texts.length} 条文案</p>
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
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      文案标题
                    </th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      文案分类
                    </th>
                    <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      内容预览
                    </th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      使用次数
                    </th>
                    <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">
                      操作
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map((t) => (
                    <tr
                      key={t.id}
                      className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60"
                    >
                      <td className="px-4 py-3.5 font-medium text-inkblack">{t.title}</td>
                      <td className="px-4 py-3.5">
                        <Badge variant="outline">{t.category}</Badge>
                      </td>
                      <td className="px-4 py-3.5">
                        <Tooltip>
                          <TooltipTrigger asChild>
                            <span className="block max-w-[320px] truncate text-inkblack/65">
                              {t.content}
                            </span>
                          </TooltipTrigger>
                          <TooltipContent className="max-w-sm">{t.content}</TooltipContent>
                        </Tooltip>
                      </td>
                      <td className="px-4 py-3.5 text-right font-medium tabular-nums text-inkblack">
                        {formatNumber(t.usageCount)}
                      </td>
                      <td className="px-4 py-3.5 text-right">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => toast.info(`编辑文案「${t.title}」`)}
                        >
                          <Pencil className="h-4 w-4" />
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
