import { useLocation } from "react-router-dom";
import { Bell, CheckCircle2, HelpCircle, Menu, Search } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { pageTitles } from "./navConfig";
import { UserMenu } from "./UserMenu";
import { useEffect, useState } from "react";
import { getStore, subscribe } from "@/lib/store";

interface TopbarProps {
  onOpenSidebar: () => void;
}

export function Topbar({ onOpenSidebar }: TopbarProps) {
  const location = useLocation();
  const title = pageTitles[location.pathname] ?? "总览";
  const [conn, setConn] = useState(getStore().state);

  useEffect(() => subscribe((s) => setConn(s.state)), []);

  const connLabel =
    conn === "connected"
      ? "云端已连接"
      : conn === "readonly"
        ? "只读模式"
        : conn === "loading"
          ? "连接中…"
          : conn === "error"
            ? "连接异常"
            : "未连接";

  return (
    <header className="sticky top-0 z-20 border-b border-border bg-paper/85 backdrop-blur-md">
      <div className="flex h-16 items-center gap-3 px-4 sm:px-6">
        <Button
          variant="ghost"
          size="icon"
          className="lg:hidden"
          onClick={onOpenSidebar}
          aria-label="打开菜单"
        >
          <Menu className="h-5 w-5" />
        </Button>

        <h1 className="shrink-0 text-base font-semibold text-inkblack sm:text-lg">{title}</h1>

        <div className="relative ml-auto hidden max-w-sm flex-1 md:block">
          <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-inkblack/40" />
          <Input
            placeholder="搜索卡片、按钮或文字"
            className="h-9 pl-9"
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                const value = (e.target as HTMLInputElement).value.trim();
                toast.info(value ? `正在搜索「${value}」` : "请输入搜索关键词");
              }
            }}
          />
        </div>

        <div className="ml-auto flex items-center gap-1 md:ml-0">
          {(conn === "connected" || conn === "readonly") && (
            <span className="mr-1 hidden items-center gap-1.5 rounded-full bg-[#2F7D5B]/10 px-2.5 py-1 text-xs font-medium text-[#2F7D5B] sm:flex">
              <CheckCircle2 className="h-3.5 w-3.5" />
              {connLabel}
            </span>
          )}
          {(conn === "error" || conn === "idle") && (
            <span className="mr-1 hidden items-center gap-1.5 rounded-full bg-gold/15 px-2.5 py-1 text-xs font-medium text-[#9A7420] sm:flex">
              <span className="h-2 w-2 rounded-full bg-gold" />
              {connLabel}
            </span>
          )}
          <Button
            variant="ghost"
            size="icon"
            className="relative"
            aria-label="通知"
            onClick={() => toast.info("暂无新通知")}
          >
            <Bell className="h-5 w-5" />
            <span className="absolute right-2 top-2 h-2 w-2 rounded-full bg-cinnabar ring-2 ring-paper" />
          </Button>
          <Button
            variant="ghost"
            size="icon"
            aria-label="帮助"
            onClick={() => toast.info("帮助中心即将上线")}
          >
            <HelpCircle className="h-5 w-5" />
          </Button>
          <div className="mx-1 hidden h-6 w-px bg-border sm:block" />
          <UserMenu />
        </div>
      </div>
    </header>
  );
}
