import { useEffect, useState } from 'react';

export default function ChatbotPopup() {
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => setVisible(true), 2000);
    return () => clearTimeout(timer);
  }, []);

  if (!visible) return null;

  return (
    <div className="fixed bottom-6 right-6 bg-white border border-green-300 shadow-lg rounded-lg p-4 w-72 z-50">
      <h3 className="text-green-700 font-semibold mb-2">Need help?</h3>
      <p className="text-sm text-gray-600">Ask DocEase Bot anything about appointments or features.</p>
      <input
        className="mt-3 w-full border rounded px-2 py-1 text-sm"
        placeholder="Type your question..."
      />
    </div>
  );
}