import { motion } from 'framer-motion';

export default function LoadingIndicator() {
  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="text-center text-gray-500 py-2"
    >
      <div className="flex items-center justify-center gap-2">
        <div className="animate-pulse">DocEase Bot is thinking</div>
        <div className="flex gap-1">
          {[1, 2, 3].map((dot) => (
            <motion.span
              key={dot}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{
                duration: 0.5,
                repeat: Infinity,
                repeatType: "reverse",
                delay: dot * 0.2,
              }}
            >
              .
            </motion.span>
          ))}
        </div>
      </div>
    </motion.div>
  );
}