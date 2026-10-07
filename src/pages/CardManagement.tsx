import { PageHeader } from "@/components/common/PageHeader";
import { CardTableSection } from "@/components/dashboard/CardTableSection";

export default function CardManagement() {
  return (
    <div className="space-y-6">
      <PageHeader
        title="卡片管理"
        description="管理全部资源卡片，支持搜索、筛选与批量操作"
      />
      <CardTableSection
        title="全部卡片"
        description="共 1,286 张卡片，以下为当前筛选结果"
        pageSize={8}
        showFilters
        showHeaderActions
      />
    </div>
  );
}
