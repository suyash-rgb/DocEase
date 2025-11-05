import { motion } from 'framer-motion';

export default function UserMessage({ content }) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="mb-3 text-right"
    >
      <div className="inline-block p-2 rounded-lg bg-green-500 text-white">
        {content}
      </div>
    </motion.div>
  );
}