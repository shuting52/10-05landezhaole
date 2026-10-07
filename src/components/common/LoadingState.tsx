import { Loader2 } from "lucide-react";
import { cn } from "@/lib/utils";

interface LoadingStateProps {
  label?: string;
  className?: string;
}

export function LoadingState({ label = "加载中…", className }: LoadingStateProps) {
  return (
    <div
      className={cn(
        "flex flex-col items-center justify-center gap-3 px-6 py-16 text-center",
        className
      )}
    >
      <Loader2 className="h-6 w-6 animate-spin text-cinnabar" />
      <p className="text-sm text-inkblack/55">{label}</p>
    </div>
  );
}

export function SkeletonRow({ className }: { className?: string }) {
  return <div className={cn("h-4 w-full animate-pulse rounded-md bg-mist", className)} />;
}
