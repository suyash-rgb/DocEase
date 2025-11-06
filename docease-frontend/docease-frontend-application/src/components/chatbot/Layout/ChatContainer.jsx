import { motion } from 'framer-motion';
import QuickActions from '../../QuickActions';

export default function ChatContainer({ children }) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, y: 20 }}
      transition={{ duration: 0.3 }}
      className="fixed bottom-6 right-6 border border-green-300 shadow-lg rounded-lg w-96 z-50 h-[600px] flex flex-col bg-white overflow-hidden"
    >
      {/* Background Image Layer */}
      <div className="absolute inset-0 z-0">
        <div className="absolute inset-0 bg-white/80" />
        <div 
          className="absolute inset-0 bg-[url('/logo.png')] bg-no-repeat bg-center bg-contain opacity-15"
        />
      </div>

      {/* Content Layer */}
      <div className="relative z-10 flex flex-col h-full">
        {children}
      </div>
    </motion.div> 
  );
}