import { SlidersHorizontal } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface FilterButtonProps {
  onClick?: () => void;
  active?: boolean;
  label?: string;
  className?: string;
}

export function FilterButton({ onClick, active, label = "筛选", className }: FilterButtonProps) {
  return (
    <Button
      variant="outline"
      onClick={onClick}
      className={cn(active && "border-cinnabar/40 bg-cinnabar/5 text-cinnabar", className)}
    >
      <SlidersHorizontal className="h-4 w-4" />
      {label}
    </Button>
  );
}
