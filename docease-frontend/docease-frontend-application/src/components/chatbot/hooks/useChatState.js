import { useState, useCallback } from 'react';

export default function useChatState() {
  const [messages, setMessages] = useState([]);
  const [inputText, setInputText] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [chatStarted, setChatStarted] = useState(false);

  const addMessage = useCallback((message) => {
    setMessages(prev => [...prev, message]);
  }, []);

  const clearMessages = useCallback(() => {
    setMessages([]);
    setChatStarted(false);
  }, []);

  const startChat = useCallback(() => {
    setChatStarted(true);
  }, []);

  return {
    messages,
    inputText,
    setInputText,
    isLoading,
    setIsLoading,
    error,
    setError,
    chatStarted,
    addMessage,
    clearMessages,
    startChat
  };
}