import { Link } from "react-router-dom";
import {
  ArrowUpRight,
  ClipboardCheck,
  Download,
  Layers,
  TrendingUp,
  Users,
  type LucideIcon,
} from "lucide-react";
import { cn } from "@/lib/utils";
import type { StatItem } from "@/types";

const iconMap: Record<StatItem["icon"], LucideIcon> = {
  layers: Layers,
  download: Download,
  users: Users,
  clipboard: ClipboardCheck,
};

const toneMap: Record<
  StatItem["tone"],
  { card: string; iconWrap: string; icon: string; value: string; accent: string }
> = {
  cinnabar: {
    card: "bg-gradient-to-br from-[#FBEDEA] to-paper-soft border-[#F0D6D1]",
    iconWrap: "bg-cinnabar/12",
    icon: "text-cinnabar",
    value: "text-cinnabar",
    accent: "text-cinnabar",
  },
  gold: {
    card: "bg-gradient-to-br from-[#FAF3E2] to-paper-soft border-[#EEDFBE]",
    iconWrap: "bg-gold/15",
    icon: "text-[#A87C22]",
    value: "text-[#A87C22]",
    accent: "text-[#A87C22]",
  },
  ink: {
    card: "bg-gradient-to-br from-[#E9F0EF] to-paper-soft border-[#D3E0DE]",
    iconWrap: "bg-ink/10",
    icon: "text-ink",
    value: "text-ink",
    accent: "text-ink",
  },
  orange: {
    card: "bg-gradient-to-br from-[#FBEDE6] to-paper-soft border-[#F1D8C9]",
    iconWrap: "bg-[#E76F61]/15",
    icon: "text-[#C4553F]",
    value: "text-[#C4553F]",
    accent: "text-[#C4553F]",
  },
};

export function StatCard({ item }: { item: StatItem }) {
  const Icon = iconMap[item.icon];
  const tone = toneMap[item.tone];

  return (
    <div
      className={cn(
        "relative flex h-full flex-col overflow-hidden rounded-2xl border p-5 shadow-soft transition-shadow hover:shadow-card",
        tone.card
      )}
    >
      {/* 右上角极淡回纹装饰 */}
      <div className="pattern-fret pointer-events-none absolute -right-2 -top-2 h-20 w-20 opacity-60" />

      <div className="relative flex items-start justify-between">
        <div className={cn("flex h-11 w-11 items-center justify-center rounded-xl", tone.iconWrap)}>
          <Icon className={cn("h-5 w-5", tone.icon)} />
        </div>
        <div className="flex h-8 w-8 items-center justify-center rounded-lg border border-black/5 bg-paper-soft/70">
          <span className={cn("font-serif text-sm font-bold", tone.accent)}>印</span>
        </div>
      </div>

      <div className="relative mt-4">
        <p className="text-sm text-inkblack/55">{item.label}</p>
        <p className={cn("mt-1 font-serif text-3xl font-bold tracking-tight", tone.value)}>
          {item.value}
        </p>
      </div>

      <div className="relative mt-3 flex items-center gap-1.5 text-xs">
        {item.action ? (
          <Link
            to={item.action.to}
            className={cn(
              "inline-flex items-center gap-1 font-medium transition-opacity hover:opacity-80",
              tone.accent
            )}
          >
            {item.delta}
            <ArrowUpRight className="h-3.5 w-3.5" />
          </Link>
        ) : (
          <>
            <span className={cn("inline-flex items-center gap-0.5 font-medium", tone.accent)}>
              <TrendingUp className="h-3.5 w-3.5" />
              {item.delta}
            </span>
            <span className="text-inkblack/45">{item.deltaLabel}</span>
          </>
        )}
      </div>
    </div>
  );
}
