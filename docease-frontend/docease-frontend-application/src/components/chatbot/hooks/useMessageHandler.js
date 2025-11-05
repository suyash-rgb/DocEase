import { useCallback } from 'react';
import { sendMessage } from '../../../services/aiService';

export default function useMessageHandler({
  addMessage,
  setIsLoading,
  setError,
  setInputText,
  startChat
}) {
  const handleSendMessage = useCallback(async (text) => {
    if (!text.trim()) return;

    // Add user message
    const userMessage = {
      type: 'user',
      text: text.trim(),
      timestamp: new Date().toISOString()
    };
    addMessage(userMessage);
    setInputText('');
    setIsLoading(true);
    setError(null);

    try {
      const response = await sendMessage(text);
      
      // Add bot message
      const botMessage = {
        type: 'bot',
        text: response.data,
        timestamp: new Date().toISOString()
      };
      addMessage(botMessage);
      
      // Start chat if it hasn't started yet
      startChat();
    } catch (err) {
      console.error('Error sending message:', err);
      setError('Failed to send message. Please try again.');
    } finally {
      setIsLoading(false);
    }
  }, [addMessage, setInputText, setIsLoading, setError, startChat]);

  return {
    handleSendMessage
  };
}