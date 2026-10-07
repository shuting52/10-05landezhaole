import { LogActionBadge } from "@/components/common/StatusBadge";
import { cn } from "@/lib/utils";
import type { ActivityLog as ActivityLogType } from "@/types";

interface ActivityLogProps {
  logs: ActivityLogType[];
  className?: string;
}

export function ActivityLog({ logs, className }: ActivityLogProps) {
  return (
    <ul className={cn("space-y-1", className)}>
      {logs.map((log) => (
        <li
          key={log.id}
          className="flex items-start gap-3 rounded-xl px-2 py-2.5 transition-colors hover:bg-mist-soft/60"
        >
          <div
            className="mt-0.5 flex h-8 w-8 shrink-0 items-center justify-center rounded-full text-xs font-semibold text-white"
            style={{ backgroundColor: log.avatarColor }}
          >
            {log.operator.slice(0, 1)}
          </div>
          <div className="min-w-0 flex-1">
            <p className="text-sm leading-snug text-inkblack/85">
              <span className="font-medium text-inkblack">{log.operator}</span>{" "}
              <span className="text-inkblack/70">{log.content}</span>
            </p>
            <p className="mt-0.5 text-xs text-inkblack/45">{log.time}</p>
          </div>
          <LogActionBadge action={log.action} />
        </li>
      ))}
    </ul>
  );
}
