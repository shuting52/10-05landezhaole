import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/utils";

const badgeVariants = cva(
  "inline-flex items-center gap-1 rounded-full border px-2.5 py-0.5 text-xs font-medium transition-colors whitespace-nowrap",
  {
    variants: {
      variant: {
        default: "border-transparent bg-cinnabar/10 text-cinnabar",
        gold: "border-transparent bg-gold/12 text-[#9A7420]",
        ink: "border-transparent bg-ink/10 text-ink",
        outline: "border-border bg-paper-soft text-inkblack/70",
        muted: "border-transparent bg-mist text-inkblack/60",
        success: "border-transparent bg-[#2F7D5B]/12 text-[#2F7D5B]",
        warning: "border-transparent bg-gold/15 text-[#9A7420]",
        danger: "border-transparent bg-cinnabar/12 text-cinnabar",
      },
    },
    defaultVariants: { variant: "default" },
  }
);

export interface BadgeProps
  extends React.HTMLAttributes<HTMLSpanElement>,
    VariantProps<typeof badgeVariants> {}

function Badge({ className, variant, ...props }: BadgeProps) {
  return <span className={cn(badgeVariants({ variant }), className)} {...props} />;
}

export { Badge, badgeVariants };
