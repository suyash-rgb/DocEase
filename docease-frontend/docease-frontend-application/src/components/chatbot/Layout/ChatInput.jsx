import { motion } from 'framer-motion';
import { useRef } from 'react';

export default function ChatInput({ onSubmit, userInput, setUserInput, isLoading, showInput }) {
  const inputRef = useRef(null);

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.5 }}
      className="p-4 border-t bg-white/80 backdrop-blur-sm sticky bottom-0"
    >
      <form
        onSubmit={onSubmit}
        className="flex gap-2"
      >
        <input
          ref={inputRef}
          value={userInput}
          onChange={(e) => setUserInput(e.target.value)}
          className="flex-1 border rounded px-3 py-2 text-sm bg-white"
          placeholder={showInput ? 'Enter your symptoms (e.g., fever, cough, sore throat)' : 'Choose an option above to start'}
          disabled={!showInput}
        />
        <button
          type="submit"
          disabled={isLoading || !showInput}
          className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 disabled:bg-green-300 transition-colors"
        >
          Send
        </button>
      </form>
    </motion.div>
  );
}