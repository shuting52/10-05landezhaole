<div className="grid grid-cols-5 gap-2 p-3 bg-neutral-900 rounded-xl border border-neutral-800 text-center">
  {['按钮', '输入', '卡片', '开关', '加载', '列表', '导航', '表单', '弹窗', '设置'].map((t, idx) => (
    <div key={idx} className="flex flex-col items-center gap-1">
      <div className="w-9 h-9 rounded-xl bg-neutral-800 flex items-center justify-center text-xs font-bold text-amber-300 border border-amber-500/20">{idx + 1}</div>
      <span className="text-[10px] font-mono text-neutral-300">{t}</span>
    </div>
  ))}
</div>