import { NavLink } from "react-router-dom";
import { HelpCircle, LifeBuoy } from "lucide-react";
import { cn } from "@/lib/utils";
import { navItems } from "./navConfig";

interface SidebarProps {
  onNavigate?: () => void;
}

export function SidebarContent({ onNavigate }: SidebarProps) {
  return (
    <div className="flex h-full flex-col bg-ink text-paper">
      {/* 品牌区 */}
      <div className="relative flex items-center gap-3 px-5 py-5">
        <div className="pattern-cloud pointer-events-none absolute inset-0 opacity-40" />
        <div className="relative flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-cinnabar shadow-seal">
          <span className="font-serif text-xl font-bold leading-none text-paper">懒</span>
        </div>
        <div className="relative min-w-0">
          <div className="truncate font-serif text-lg font-bold tracking-wide text-paper">
            懒得找了
          </div>
          <div className="truncate text-xs text-paper/55">资源管理后台</div>
        </div>
      </div>

      <div className="mx-5 h-px bg-paper/10" />

      {/* 菜单 */}
      <nav className="flex-1 space-y-1 overflow-y-auto px-3 py-4">
        {navItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.to}
              to={item.to}
              onClick={onNavigate}
              className={({ isActive }) =>
                cn(
                  "group flex items-center gap-3 rounded-xl px-3.5 py-2.5 text-sm font-medium transition-all duration-150",
                  isActive
                    ? "bg-cinnabar text-paper shadow-seal"
                    : "text-paper/70 hover:bg-paper/8 hover:text-paper"
                )
              }
            >
              {({ isActive }) => (
                <>
                  <Icon
                    className={cn(
                      "h-[18px] w-[18px] shrink-0 transition-colors",
                      isActive ? "text-paper" : "text-paper/55 group-hover:text-paper/90"
                    )}
                  />
                  <span className="truncate">{item.label}</span>
                </>
              )}
            </NavLink>
          );
        })}
      </nav>

      {/* 底部 */}
      <div className="space-y-3 px-3 pb-4">
        <div className="mx-2 h-px bg-paper/10" />
        <button
          type="button"
          className="flex w-full items-center gap-3 rounded-xl px-3.5 py-2.5 text-sm text-paper/70 transition-colors hover:bg-paper/8 hover:text-paper"
        >
          <LifeBuoy className="h-[18px] w-[18px] text-paper/55" />
          <span>帮助中心</span>
        </button>
        <div className="flex items-center gap-3 rounded-xl bg-paper/6 px-3 py-2.5">
          <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-gold text-sm font-semibold text-white">
            管
          </div>
          <div className="min-w-0 flex-1">
            <div className="truncate text-sm font-medium text-paper">管理员</div>
            <div className="truncate text-xs text-paper/50">超级管理员</div>
          </div>
          <HelpCircle className="h-4 w-4 shrink-0 text-paper/40" />
        </div>
        <div className="px-3 text-center text-[11px] text-paper/40">版本 v1.0.0</div>
      </div>
    </div>
  );
}

export function Sidebar() {
  return (
    <aside className="fixed inset-y-0 left-0 z-30 hidden w-60 shrink-0 lg:block">
      <SidebarContent />
    </aside>
  );
}
