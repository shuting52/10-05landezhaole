import { useEffect, useState } from "react";
import { toast } from "sonner";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { categoryOptions as mockCategoryOptions } from "@/data/mockData";
import { getStore } from "@/lib/store";
import type { ButtonType, CardStatus, ResourceCard } from "@/types";

// 真实分类（从云端 admin-data 读取）；未连接时退回 mock
function useRealCategoryOptions(): string[] {
  const s = getStore();
  const real = s.admin?.home?.categories?.map((c) => c.name).filter(Boolean) || [];
  return real.length > 0 ? real : mockCategoryOptions;
}

interface CardFormDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  card?: ResourceCard | null;
  onSubmit?: (data: CardFormValues) => void;
}

export interface CardFormValues {
  name: string;
  description: string;
  buttonType: ButtonType;
  status: CardStatus;
  category: string;
}

const emptyForm: CardFormValues = {
  name: "",
  description: "",
  buttonType: "download",
  status: "draft",
  category: "",
};

export function CardFormDialog({ open, onOpenChange, card, onSubmit }: CardFormDialogProps) {
  const isEdit = Boolean(card);
  const [form, setForm] = useState<CardFormValues>(emptyForm);
  const [error, setError] = useState("");
  const categoryOptions = useRealCategoryOptions();

  useEffect(() => {
    if (open) {
      setError("");
      setForm(
        card
          ? {
              name: card.name,
              description: card.description,
              buttonType: card.buttonType,
              status: card.status,
              category: card.category,
            }
          : { ...emptyForm, category: categoryOptions[0] || "" }
      );
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open, card]);

  const handleSubmit = () => {
    if (!form.name.trim()) {
      setError("请填写卡片名称");
      return;
    }
    if (!form.description.trim()) {
      setError("请填写卡片描述");
      return;
    }
    onSubmit?.(form);
    toast.success(isEdit ? `已保存《${form.name}》` : `已创建《${form.name}》`);
    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle>{isEdit ? "编辑卡片" : "新建卡片"}</DialogTitle>
          <DialogDescription>
            {isEdit ? "修改卡片信息后保存，变更将记录到操作日志。" : "填写卡片基础信息，创建后可继续上传素材。"}
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4">
          <div className="space-y-1.5">
            <Label htmlFor="card-name">卡片名称</Label>
            <Input
              id="card-name"
              value={form.name}
              placeholder="例如：春节祝福卡"
              onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
            />
          </div>

          <div className="space-y-1.5">
            <Label htmlFor="card-desc">卡片描述</Label>
            <Textarea
              id="card-desc"
              value={form.description}
              placeholder="简要说明适用场景"
              onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
            />
          </div>

          <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
            <div className="space-y-1.5">
              <Label>按钮类型</Label>
              <Select
                value={form.buttonType}
                onValueChange={(v) => setForm((f) => ({ ...f, buttonType: v as ButtonType }))}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="download">下载按钮</SelectItem>
                  <SelectItem value="link">跳转按钮</SelectItem>
                  <SelectItem value="copy">复制按钮</SelectItem>
                  <SelectItem value="contact">联系按钮</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-1.5">
              <Label>状态</Label>
              <Select
                value={form.status}
                onValueChange={(v) => setForm((f) => ({ ...f, status: v as CardStatus }))}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="published">已发布</SelectItem>
                  <SelectItem value="reviewing">审核中</SelectItem>
                  <SelectItem value="draft">草稿</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-1.5">
              <Label>分类</Label>
              <Select
                value={form.category}
                onValueChange={(v) => setForm((f) => ({ ...f, category: v }))}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {categoryOptions.map((c) => (
                    <SelectItem key={c} value={c}>
                      {c}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>

          {error && <p className="text-xs text-cinnabar">{error}</p>}
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            取消
          </Button>
          <Button onClick={handleSubmit}>{isEdit ? "保存修改" : "创建卡片"}</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
