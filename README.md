# 懒得找了 · 卡片资源管理后台

现代国潮 + 工具效率风的卡片资源管理后台 Dashboard。React 18 + TypeScript + Vite + Tailwind CSS + shadcn/ui 风格组件 + lucide-react + Recharts + react-router-dom + sonner。

使用本地 mock 数据，不连接真实后端，无登录页，直接进入后台首页。

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
├── data/mockData.ts   本地 mock 数据
├── types/index.ts     全局类型定义
├── lib/
│   ├── utils.ts       cn / formatNumber 等工具
│   └── api.ts         数据访问层（未来替换真实 API 只需改这里）
├── App.tsx            路由与 Toaster
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

## 接入真实后端

所有数据请求都封装在 `src/lib/api.ts`，函数签名保持不变，把函数体替换为 `fetch` / `axios` 调用即可，页面组件无需改动。
