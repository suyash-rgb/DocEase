import { motion } from 'framer-motion';

export default function Disclaimer({ text, type = 'info' }) {
  const bgColor = {
    info: 'bg-yellow-50 border-yellow-200',
    error: 'bg-red-50 border-red-200',
    success: 'bg-green-50 border-green-200',
  }[type];

  return (
    <motion.div
      initial={{ opacity: 0, y: 5 }}
      animate={{ opacity: 1, y: 0 }}
      className={`mt-4 p-2 ${bgColor} border rounded-lg`}
    >
      <p className="text-xs text-gray-600 italic">{text}</p>
    </motion.div>
  );
}