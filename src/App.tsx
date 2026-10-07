import { useEffect } from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { Toaster } from "sonner";
import { AppLayout } from "@/components/layout/AppLayout";
import Dashboard from "@/pages/Dashboard";
import CardManagement from "@/pages/CardManagement";
import ButtonManagement from "@/pages/ButtonManagement";
import SkillManagement from "@/pages/SkillManagement";
import TextManagement from "@/pages/TextManagement";
import CategoryManagement from "@/pages/CategoryManagement";
import Settings from "@/pages/Settings";
import OperationLogs from "@/pages/OperationLogs";
import { loadAdmin } from "@/lib/store";

export default function App() {
  // 启动即尝试加载云端 admin-data（GitHub API 优先，失败自动降级只读镜像）
  useEffect(() => {
    void loadAdmin();
  }, []);

  return (
    <>
      <Routes>
        <Route element={<AppLayout />}>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/cards" element={<CardManagement />} />
          <Route path="/buttons" element={<ButtonManagement />} />
          <Route path="/skills" element={<SkillManagement />} />
          <Route path="/texts" element={<TextManagement />} />
          <Route path="/categories" element={<CategoryManagement />} />
          <Route path="/settings" element={<Settings />} />
          <Route path="/logs" element={<OperationLogs />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Route>
      </Routes>
      <Toaster
        position="top-right"
        toastOptions={{
          style: {
            background: "#FBF9F4",
            border: "1px solid #E8E5DE",
            color: "#202322",
            borderRadius: "12px",
          },
        }}
      />
    </>
  );
}
