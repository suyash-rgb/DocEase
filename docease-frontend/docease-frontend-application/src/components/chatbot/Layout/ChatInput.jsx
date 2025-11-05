import { motion } from 'framer-motion';
import { useRef } from 'react';

export default function ChatInput({
  onSubmit,
  userInput,
  setUserInput,
  isLoading,
  showInput,
  expectedInputMode,
  inputRef,
}) {
  // Dynamically set placeholder based on mode
  const getPlaceholder = () => {
    if (!showInput) return 'Choose an option above to start';
    switch (expectedInputMode) {
      case 'medication':
        return 'Enter a medicine name (e.g., Parasafe)';
      case 'symptom':
        return 'Enter your symptoms (e.g., fever, cough)';
      case 'lab':
        return 'Upload a lab test result';
      default:
        return 'Type your message...';
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.5 }}
      className="p-4 border-t bg-white/80 backdrop-blur-sm sticky bottom-0"
    >
      <form onSubmit={onSubmit} className="flex gap-2">
        <input
          ref={inputRef}
          value={userInput}
          onChange={(e) => setUserInput(e.target.value)}
          className="flex-1 border rounded px-3 py-2 text-sm bg-white"
          placeholder={getPlaceholder()}
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