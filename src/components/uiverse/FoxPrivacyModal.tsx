import React, { useState } from 'react';

export function FoxPrivacyModal({
  isOpen = true,
  onClose = () => {},
  onAccept = () => {},
}: {
  isOpen?: boolean;
  onClose?: () => void;
  onAccept?: () => void;
}) {
  const [openOptions, setOpenOptions] = useState(false);
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4 z-50">
      <div className="relative w-full max-w-sm bg-white rounded-3xl p-7 text-center shadow-2xl overflow-hidden">
        <button onClick={onClose} className="absolute top-3 right-3 text-neutral-400 hover:text-neutral-700">✕</button>
        <h3 className="text-lg font-bold text-neutral-800 text-left mb-2">您的隐私对我们很重要</h3>
        <p className="text-xs text-neutral-600 text-left mb-3">我们处理您的个人信息以改进服务并提供个性化体验。</p>
        <button onClick={onAccept} className="w-full py-3 bg-gradient-to-r from-indigo-500 to-pink-500 text-white rounded-xl font-bold text-sm shadow-md">接受并继续</button>
      </div>
    </div>
  );
}
export default FoxPrivacyModal;