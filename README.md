# 懒得找了 · 卡片资源管理后台

现代国潮 + 工具效率风的卡片资源管理后台 Dashboard。React 18 + TypeScript + Vite + Tailwind CSS + shadcn/ui 风格组件 + lucide-react + Recharts + react-router-dom + sonner。

## ✅ 已对接真实云端（GitHub 仓库即数据中枢）

本管理台已对接 `admin-data.json`（唯一真相源），配置 Token 后可直接管理真实数据并同步到本体 App：

- **数据源**：`shuting52/10-05landezhaole@main/admin-data.json`（本体 App 同源）
- **连接配置**：「系统设置」→ 云端连接 → 填入 GitHub Token（PAT，仅存本机浏览器 localStorage）
- **双模式发布**（写死规则8）：
  - `✅ 应用并实时同步` → mode=content，只写内容不 bump 版本，本体 60s 轮询实时生效，**不弹更新窗**
  - `发布新版本` → mode=release，递增版本号，本体**弹更新窗**
- **只读兜底**：无 Token 时自动走 jsDelivr/raw 镜像链只读浏览

### 数据映射

| 管理台模块 | admin-data 真实结构 |
|---|---|
| 卡片管理 | `home.categories[].cards[]`（1819 张）|
| 分类管理 | `home.categories[]`（19 个）|
| 按钮管理 | `software[]`（软件库）|
| 文字管理 | `uiText` + settings/marquee/welcome 关键文案 |
| 总览 | 真实统计（卡片/软件/技能/版本）|
| 系统设置 | settings.* + version + autoRelease + Token 配置 |

## 快速开始

```bash
# 1. 安装依赖
npm install

# 2. 启动开发服务器
npm run dev
# 打开 http://localhost:5173

# 3. 生产构建
npm run build

# 4. 预览构建产物
npm run preview

# 5. 仅做类型检查
npm run typecheck
```

## 目录结构

```
src/
├── components/
│   ├── layout/        AppLayout / Sidebar / MobileSidebar / Topbar / UserMenu / navConfig
│   ├── dashboard/     StatCard / CardManagementTable / CardTableSection / ActivityLog
│   ├── common/        PageHeader / StatusBadge / SearchInput / FilterButton / EmptyState
│   │                  Pagination / LoadingState / ConfirmDialog / CardFormDialog / DataTable
│   └── ui/            button / input / badge / card / dialog / dropdown-menu / select
│                      switch / label / separator / tooltip / table / textarea
├── pages/             Dashboard / CardManagement / ButtonManagement / TextManagement
│                      CategoryManagement / Settings / OperationLogs
├── data/mockData.ts   本地 mock 数据（未连接时兜底）
├── types/
│   ├── index.ts       管理台模型类型
│   └── admin.ts       admin-data.json 真实结构类型
├── lib/
│   ├── github.ts      GitHub API 封装（token/GET/PUT/sha乐观锁/镜像链/CDN purge）
│   ├── mapper.ts      数据模型映射（admin-data ↔ 管理台模型）
│   ├── store.ts       全局状态 + 应用/发布双模式
│   ├── api.ts         数据访问层（真实 API，函数签名稳定）
│   └── utils.ts       cn / formatNumber 等工具
├── App.tsx            路由与 Toaster（启动自动加载云端数据）
├── main.tsx           入口
└── index.css          国潮色板与全局样式
```

## 色板

| 名称 | 色值 | 用途 |
| --- | --- | --- |
| 宣纸米白 | `#F7F3EA` | 页面背景 |
| 深黛青 | `#193B3D` | 左侧导航 |
| 朱砂红 | `#C63C32` | 主按钮 / 选中态 / 强调数字 |
| 辰砂浅红 | `#E76F61` | 浅色强调 |
| 鎏金 | `#C99A3D` | 次要强调 |
| 墨黑 | `#202322` | 正文 |
| 辅助浅灰 | `#E8E5DE` | 边框 / 分隔 |

## 对接规范

详见仓库根目录《管理工作台对接规范 V1》（数据结构字典 / GitHub API / 双模式发布 / CI / 规则速查）。
