import { useEffect, useState } from 'react';

export default function ChatbotPopup({onClose}) {
  const [visible, setVisible] = useState(false);
  const [minimized, setMinimized] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => setVisible(true), 2000);
    return () => clearTimeout(timer);
  }, []);

  if (!visible) return null;

  return (
    <div className={`fixed bottom-6 right-6 bg-white border border-green-300 shadow-lg rounded-lg w-80 z-50 transition-all duration-300 ${minimized ? 'h-16 overflow-hidden' : 'h-96'} flex flex-col justify-between`}>
      
      {/* Header */}
      <div className="flex justify-between items-center px-4 py-2 border-b">
        <h3 className="text-green-700 font-semibold">DocEase Bot</h3>
        <div className="flex gap-2">
          <button onClick={() => setMinimized(!minimized)} title="Minimize">
            {minimized ? '🔼' : '🔽'}
          </button>
          <button onClick={onClose} title="Close">❌</button>
        </div>
      </div>

      {/* Body */}
      {!minimized && (
        <div className="flex flex-col justify-end px-4 py-3 h-full">
          <p className="text-sm text-gray-600 mb-2">
            Ask DocEase Bot anything about appointments or features.
          </p>
          <input
            className="w-full border rounded px-2 py-1 text-sm"
            placeholder="Type your question..."
          />
        </div>
      )}

    </div>
  );
}