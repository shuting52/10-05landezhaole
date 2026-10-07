import * as React from "react";
import { Slot } from "@radix-ui/react-slot";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/utils";

const buttonVariants = cva(
  "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-xl text-sm font-medium transition-all duration-150 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-cinnabar/50 focus-visible:ring-offset-2 focus-visible:ring-offset-paper disabled:pointer-events-none disabled:opacity-50 active:scale-[0.98] [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0",
  {
    variants: {
      variant: {
        default:
          "bg-cinnabar text-paper shadow-seal hover:bg-[#B3352C] hover:shadow-[0_4px_14px_rgba(198,60,50,0.34)]",
        gold: "bg-gold text-white shadow-[0_2px_8px_rgba(201,154,61,0.3)] hover:bg-[#B98A2F]",
        ink: "bg-ink text-paper hover:bg-ink-soft",
        outline:
          "border border-border bg-paper-soft text-inkblack hover:bg-mist-soft hover:border-ink/20",
        secondary: "bg-mist text-inkblack hover:bg-[#DEDAD1]",
        ghost: "text-inkblack/80 hover:bg-mist-soft hover:text-inkblack",
        destructive: "bg-cinnabar text-paper hover:bg-[#B3352C]",
        link: "text-cinnabar underline-offset-4 hover:underline",
      },
      size: {
        default: "h-10 px-4 py-2",
        sm: "h-8 rounded-lg px-3 text-xs",
        lg: "h-11 rounded-xl px-6",
        icon: "h-10 w-10",
        "icon-sm": "h-8 w-8 rounded-lg",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
);

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    const Comp = asChild ? Slot : "button";
    return (
      <Comp className={cn(buttonVariants({ variant, size, className }))} ref={ref} {...props} />
    );
  }
);
Button.displayName = "Button";

export { Button, buttonVariants };
