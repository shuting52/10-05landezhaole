import { useNavigate } from "react-router-dom";
import { LogOut, Settings, User } from "lucide-react";
import { toast } from "sonner";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

export function UserMenu() {
  const navigate = useNavigate();

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <button
          type="button"
          className="flex items-center gap-2 rounded-xl border border-transparent px-1.5 py-1 transition-colors hover:border-border hover:bg-mist-soft focus:outline-none focus-visible:ring-2 focus-visible:ring-cinnabar/40"
        >
          <div className="flex h-8 w-8 items-center justify-center rounded-full bg-cinnabar text-sm font-semibold text-paper">
            管
          </div>
          <span className="hidden text-sm font-medium text-inkblack sm:inline">管理员</span>
        </button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="w-52">
        <DropdownMenuLabel>
          <div className="flex flex-col">
            <span className="text-sm font-medium text-inkblack">管理员</span>
            <span className="text-xs text-inkblack/50">admin@lazyfind.com</span>
          </div>
        </DropdownMenuLabel>
        <DropdownMenuSeparator />
        <DropdownMenuItem
          onClick={() => {
            navigate("/settings");
            toast.success("已进入个人设置");
          }}
        >
          <User />
          个人设置
        </DropdownMenuItem>
        <DropdownMenuItem
          onClick={() => {
            navigate("/settings");
            toast.success("已进入系统设置");
          }}
        >
          <Settings />
          系统设置
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem
          className="text-cinnabar focus:bg-cinnabar/10 focus:text-cinnabar"
          onClick={() => toast.info("演示环境：退出登录已模拟")}
        >
          <LogOut />
          退出登录
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
