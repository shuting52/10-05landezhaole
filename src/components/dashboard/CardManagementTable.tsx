import { Eye, MoreHorizontal, Pencil, Trash2 } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Tooltip, TooltipContent, TooltipTrigger } from "@/components/ui/tooltip";
import { ButtonTypeBadge, CardStatusBadge } from "@/components/common/StatusBadge";
import { formatNumber } from "@/lib/utils";
import type { ResourceCard } from "@/types";

interface CardManagementTableProps {
  cards: ResourceCard[];
  onEdit: (card: ResourceCard) => void;
  onDelete: (card: ResourceCard) => void;
}

export function CardManagementTable({ cards, onEdit, onDelete }: CardManagementTableProps) {
  return (
    <div className="w-full overflow-x-auto">
      <table className="w-full min-w-[860px] border-collapse text-sm">
        <thead>
          <tr className="border-b border-border">
            <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
              名称
            </th>
            <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
              描述
            </th>
            <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
              按钮类型
            </th>
            <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
              大小
            </th>
            <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">
              下载量
            </th>
            <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
              状态
            </th>
            <th className="h-11 px-4 text-left text-xs font-semibold uppercase tracking-wide text-inkblack/50">
              更新时间
            </th>
            <th className="h-11 px-4 text-right text-xs font-semibold uppercase tracking-wide text-inkblack/50">
              操作
            </th>
          </tr>
        </thead>
        <tbody>
          {cards.map((card) => (
            <tr
              key={card.id}
              className="border-b border-border/70 transition-colors last:border-0 hover:bg-mist-soft/60"
            >
              <td className="px-4 py-3.5">
                <Tooltip>
                  <TooltipTrigger asChild>
                    <span className="block max-w-[160px] truncate font-medium text-inkblack">
                      {card.name}
                    </span>
                  </TooltipTrigger>
                  <TooltipContent>{card.name}</TooltipContent>
                </Tooltip>
              </td>
              <td className="px-4 py-3.5">
                <Tooltip>
                  <TooltipTrigger asChild>
                    <span className="block max-w-[200px] truncate text-inkblack/65">
                      {card.description}
                    </span>
                  </TooltipTrigger>
                  <TooltipContent>{card.description}</TooltipContent>
                </Tooltip>
              </td>
              <td className="px-4 py-3.5">
                <ButtonTypeBadge type={card.buttonType} />
              </td>
              <td className="whitespace-nowrap px-4 py-3.5 text-inkblack/70">{card.size}</td>
              <td className="whitespace-nowrap px-4 py-3.5 text-right font-medium tabular-nums text-inkblack">
                {formatNumber(card.downloads)}
              </td>
              <td className="px-4 py-3.5">
                <CardStatusBadge status={card.status} />
              </td>
              <td className="whitespace-nowrap px-4 py-3.5 text-inkblack/60">{card.updatedAt}</td>
              <td className="px-4 py-3.5">
                <div className="flex items-center justify-end gap-1">
                  <Tooltip>
                    <TooltipTrigger asChild>
                      <Button
                        variant="ghost"
                        size="icon-sm"
                        aria-label="查看"
                        onClick={() => toast.info(`查看《${card.name}》`)}
                      >
                        <Eye className="h-4 w-4" />
                      </Button>
                    </TooltipTrigger>
                    <TooltipContent>查看</TooltipContent>
                  </Tooltip>
                  <Tooltip>
                    <TooltipTrigger asChild>
                      <Button
                        variant="ghost"
                        size="icon-sm"
                        aria-label="编辑"
                        onClick={() => onEdit(card)}
                      >
                        <Pencil className="h-4 w-4" />
                      </Button>
                    </TooltipTrigger>
                    <TooltipContent>编辑</TooltipContent>
                  </Tooltip>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" size="icon-sm" aria-label="更多操作">
                        <MoreHorizontal className="h-4 w-4" />
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      <DropdownMenuItem onClick={() => toast.info(`查看《${card.name}》`)}>
                        <Eye />
                        查看详情
                      </DropdownMenuItem>
                      <DropdownMenuItem onClick={() => onEdit(card)}>
                        <Pencil />
                        编辑卡片
                      </DropdownMenuItem>
                      <DropdownMenuSeparator />
                      <DropdownMenuItem
                        className="text-cinnabar focus:bg-cinnabar/10 focus:text-cinnabar"
                        onClick={() => onDelete(card)}
                      >
                        <Trash2 />
                        删除卡片
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
