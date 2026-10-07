import { useState } from "react";
import { Save } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { Separator } from "@/components/ui/separator";
import { PageHeader } from "@/components/common/PageHeader";

interface ToggleRowProps {
  title: string;
  description: string;
  checked: boolean;
  onCheckedChange: (v: boolean) => void;
}

function ToggleRow({ title, description, checked, onCheckedChange }: ToggleRowProps) {
  return (
    <div className="flex items-center justify-between gap-4 py-3">
      <div className="min-w-0">
        <p className="text-sm font-medium text-inkblack">{title}</p>
        <p className="mt-0.5 text-xs text-inkblack/55">{description}</p>
      </div>
      <Switch checked={checked} onCheckedChange={onCheckedChange} />
    </div>
  );
}

export default function Settings() {
  const [siteName, setSiteName] = useState("懒得找了");
  const [subtitle, setSubtitle] = useState("资源管理后台");
  const [contact, setContact] = useState("admin@lazyfind.com");

  const [notifyReview, setNotifyReview] = useState(true);
  const [notifyDownload, setNotifyDownload] = useState(false);
  const [notifyWeekly, setNotifyWeekly] = useState(true);

  const [permPublish, setPermPublish] = useState(true);
  const [permDelete, setPermDelete] = useState(false);
  const [permExport, setPermExport] = useState(true);

  const handleSave = () => {
    toast.success("设置已保存");
  };

  return (
    <div className="space-y-6">
      <PageHeader
        title="系统设置"
        description="配置后台基础信息、品牌展示、通知与权限"
        actions={
          <Button onClick={handleSave}>
            <Save className="h-4 w-4" />
            保存设置
          </Button>
        }
      />

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        {/* 基础设置 */}
        <Card>
          <CardHeader className="border-b border-border pb-4">
            <h3 className="text-base font-semibold text-inkblack">基础设置</h3>
            <p className="mt-0.5 text-sm text-inkblack/55">后台的基本信息与联系方式</p>
          </CardHeader>
          <CardContent className="space-y-4 pt-5">
            <div className="space-y-1.5">
              <Label htmlFor="site-name">系统名称</Label>
              <Input id="site-name" value={siteName} onChange={(e) => setSiteName(e.target.value)} />
            </div>
            <div className="space-y-1.5">
              <Label htmlFor="site-subtitle">副标题</Label>
              <Input
                id="site-subtitle"
                value={subtitle}
                onChange={(e) => setSubtitle(e.target.value)}
              />
            </div>
            <div className="space-y-1.5">
              <Label htmlFor="site-contact">联系邮箱</Label>
              <Input
                id="site-contact"
                type="email"
                value={contact}
                onChange={(e) => setContact(e.target.value)}
              />
            </div>
          </CardContent>
        </Card>

        {/* 品牌设置 */}
        <Card>
          <CardHeader className="border-b border-border pb-4">
            <h3 className="text-base font-semibold text-inkblack">品牌设置</h3>
            <p className="mt-0.5 text-sm text-inkblack/55">品牌标识与主题色</p>
          </CardHeader>
          <CardContent className="space-y-5 pt-5">
            <div className="flex items-center gap-4">
              <div className="flex h-14 w-14 items-center justify-center rounded-xl bg-cinnabar shadow-seal">
                <span className="font-serif text-2xl font-bold text-paper">懒</span>
              </div>
              <div>
                <p className="text-sm font-medium text-inkblack">品牌印章</p>
                <p className="mt-0.5 text-xs text-inkblack/55">建议尺寸 512×512，支持 PNG / SVG</p>
                <Button
                  variant="outline"
                  size="sm"
                  className="mt-2"
                  onClick={() => toast.info("演示环境：暂不支持上传")}
                >
                  更换图标
                </Button>
              </div>
            </div>
            <Separator />
            <div className="space-y-2">
              <Label>主题色板</Label>
              <div className="flex flex-wrap gap-2">
                {[
                  { name: "宣纸米白", color: "#F7F3EA" },
                  { name: "深黛青", color: "#193B3D" },
                  { name: "朱砂红", color: "#C63C32" },
                  { name: "辰砂浅红", color: "#E76F61" },
                  { name: "鎏金", color: "#C99A3D" },
                  { name: "墨黑", color: "#202322" },
                ].map((c) => (
                  <div key={c.color} className="flex items-center gap-2 rounded-lg border border-border bg-paper-soft px-2.5 py-1.5">
                    <span
                      className="h-4 w-4 rounded-full border border-black/10"
                      style={{ backgroundColor: c.color }}
                    />
                    <span className="text-xs text-inkblack/70">{c.name}</span>
                  </div>
                ))}
              </div>
            </div>
          </CardContent>
        </Card>

        {/* 通知设置 */}
        <Card>
          <CardHeader className="border-b border-border pb-4">
            <h3 className="text-base font-semibold text-inkblack">通知设置</h3>
            <p className="mt-0.5 text-sm text-inkblack/55">选择需要接收的系统通知</p>
          </CardHeader>
          <CardContent className="divide-y divide-border pt-2">
            <ToggleRow
              title="审核结果通知"
              description="卡片审核通过或被驳回时通知我"
              checked={notifyReview}
              onCheckedChange={setNotifyReview}
            />
            <ToggleRow
              title="下载量提醒"
              description="单张卡片下载量突破阈值时通知我"
              checked={notifyDownload}
              onCheckedChange={setNotifyDownload}
            />
            <ToggleRow
              title="每周数据周报"
              description="每周一发送上周资源使用汇总"
              checked={notifyWeekly}
              onCheckedChange={setNotifyWeekly}
            />
          </CardContent>
        </Card>

        {/* 权限设置 */}
        <Card>
          <CardHeader className="border-b border-border pb-4">
            <h3 className="text-base font-semibold text-inkblack">权限设置</h3>
            <p className="mt-0.5 text-sm text-inkblack/55">控制当前角色的操作权限</p>
          </CardHeader>
          <CardContent className="divide-y divide-border pt-2">
            <ToggleRow
              title="允许发布卡片"
              description="可直接将卡片发布到线上"
              checked={permPublish}
              onCheckedChange={setPermPublish}
            />
            <ToggleRow
              title="允许删除资源"
              description="可删除卡片、按钮与文案"
              checked={permDelete}
              onCheckedChange={setPermDelete}
            />
            <ToggleRow
              title="允许导出数据"
              description="可导出卡片列表与操作日志"
              checked={permExport}
              onCheckedChange={setPermExport}
            />
          </CardContent>
        </Card>
      </div>

      <div className="flex justify-end">
        <Button onClick={handleSave}>
          <Save className="h-4 w-4" />
          保存设置
        </Button>
      </div>
    </div>
  );
}
