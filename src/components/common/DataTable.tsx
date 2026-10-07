import type { ReactNode } from "react";
import { cn } from "@/lib/utils";

export interface Column<T> {
  key: string;
  header: ReactNode;
  className?: string;
  render: (row: T) => ReactNode;
}

interface DataTableProps<T> {
  columns: Column<T>[];
  data: T[];
  rowKey: (row: T) => string;
  empty?: ReactNode;
  loading?: ReactNode;
  className?: string;
}

/**
 * 通用表格容器：统一表头样式、空状态与加载状态插槽。
 * 具体单元格渲染由各业务表格通过 columns 传入。
 */
export function DataTable<T>({
  columns,
  data,
  rowKey,
  empty,
  loading,
  className,
}: DataTableProps<T>) {
  if (loading) return <>{loading}</>;
  if (!data.length) return <>{empty}</>;

  return (
    <div className={cn("w-full overflow-x-auto", className)}>
      <table className="w-full border-collapse text-sm">
        <thead>
          <tr className="border-b border-border">
            {columns.map((col) => (
              <th
                key={col.key}
                className={cn(
                  "h-11 whitespace-nowrap px-4 text-left align-middle text-xs font-semibold uppercase tracking-wide text-inkblack/50",
                  col.className
                )}
              >
                {col.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row) => (
            <tr
              key={rowKey(row)}
              className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60"
            >
              {columns.map((col) => (
                <td
                  key={col.key}
                  className={cn("px-4 py-3.5 align-middle text-inkblack/85", col.className)}
                >
                  {col.render(row)}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
