import { motion } from 'framer-motion';

export default function WelcomeMessage() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="mb-3"
    >
      <div className="inline-block p-2 rounded-lg bg-gray-100 text-gray-800">
        <div className="flex items-start gap-3">
          <div className="w-10 h-10 rounded-full bg-green-100 flex items-center justify-center">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" className="text-green-700">
              <path d="M12 2v20M2 12h20" stroke="#16A34A" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </div>
          <div>
            <div className="text-sm font-semibold text-green-700">Welcome to DocEase</div>
            <div className="text-xs text-gray-600">How can I be of assistance today?</div>
          </div>
        </div>
      </div>
    </motion.div>
  );
}