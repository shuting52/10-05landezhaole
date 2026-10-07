import * as React from "react";
import { cn } from "@/lib/utils";

const Textarea = React.forwardRef<HTMLTextAreaElement, React.TextareaHTMLAttributes<HTMLTextAreaElement>>(
  ({ className, ...props }, ref) => (
    <textarea
      ref={ref}
      className={cn(
        "flex min-h-[80px] w-full rounded-xl border border-border bg-paper-soft px-3.5 py-2.5 text-sm text-inkblack shadow-sm transition-colors",
        "placeholder:text-inkblack/40 focus-visible:outline-none focus-visible:border-cinnabar/50 focus-visible:ring-2 focus-visible:ring-cinnabar/20",
        "disabled:cursor-not-allowed disabled:opacity-50",
        className
      )}
      {...props}
    />
  )
);
Textarea.displayName = "Textarea";

export { Textarea };
