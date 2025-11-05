import { motion } from 'framer-motion';

export default function ChatHeader({ onClose, minimized, onMinimize }) {
  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ delay: 0.2 }}
      className="flex justify-between items-center px-4 py-2 border-b bg-white/50 backdrop-blur-sm"
    >
      <h3 className="text-green-700 font-semibold">DocEase Bot</h3>
      <div className="flex gap-2">
        <button onClick={onMinimize} title="Minimize" className="hover:bg-gray-100 p-1 rounded">
          {minimized ? '🔼' : '🔽'}
        </button>
        <button onClick={onClose} title="Close" className="hover:bg-gray-100 p-1 rounded">
          ❌
        </button>
      </div>
    </motion.div>
  );
}