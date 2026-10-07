import { useState } from "react";
import { Outlet } from "react-router-dom";
import { TooltipProvider } from "@/components/ui/tooltip";
import { Sidebar } from "./Sidebar";
import { MobileSidebar } from "./MobileSidebar";
import { Topbar } from "./Topbar";

export function AppLayout() {
  const [mobileOpen, setMobileOpen] = useState(false);

  return (
    <TooltipProvider delayDuration={200}>
      <div className="min-h-screen bg-paper">
        <Sidebar />
        <MobileSidebar open={mobileOpen} onOpenChange={setMobileOpen} />

        <div className="flex min-h-screen flex-col lg:pl-60">
          <Topbar onOpenSidebar={() => setMobileOpen(true)} />
          <main className="flex-1 overflow-x-hidden px-4 py-6 sm:px-6 lg:px-8">
            <div className="mx-auto w-full max-w-[1400px] animate-fade-in">
              <Outlet />
            </div>
          </main>
        </div>
      </div>
    </TooltipProvider>
  );
}
