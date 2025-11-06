import { useEffect, useRef, useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import ChatContainer from './chatbot/Layout/ChatContainer';
import ChatHeader from './chatbot/Layout/ChatHeader';
import ChatInput from './chatbot/Layout/ChatInput';
import BotMessage from './chatbot/Messages/BotMessage';
import UserMessage from './chatbot/Messages/UserMessage';
import WelcomeMessage from './chatbot/Messages/WelcomeMessage';
import LoadingIndicator from './chatbot/Utils/LoadingIndicator';
import Disclaimer from './chatbot/Utils/Disclaimer';
import QuickActions from './QuickActions';
import { getPrecautionsAndRemedies, getMedicationInfo, getFirstAidInfo } from '../services/aiService';

export default function ChatbotPopup({ onClose }) {
  const [visible, setVisible] = useState(false);
  const [minimized, setMinimized] = useState(false);
  const [userInput, setUserInput] = useState('');
  const [messages, setMessages] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [showInput, setShowInput] = useState(false);
  const [showQuickActions, setShowQuickActions] = useState(true);
  const [showWelcome, setShowWelcome] = useState(true);
  const [error, setError] = useState(null);
  const [expectedInputMode, setExpectedInputMode] = useState('none');
  const inputRef = useRef(null);

  useEffect(() => {
    const timer = setTimeout(() => setVisible(true), 300);
    console.log('Chatbot popup mounted');
    return () => clearTimeout(timer);
  }, []);

  const scrollToBottom = () => {
    const chatContainer = document.querySelector('.overflow-y-auto');
    if (chatContainer) {
      setTimeout(() => {
        chatContainer.scrollTo({ top: chatContainer.scrollHeight, behavior: 'smooth' });
      }, 100);
    }
  };

  const pushBotMessage = (message, mode = 'none') => {
    setMessages((prev) => [
      ...prev,
      {
        type: 'bot',
        content: message
      },
    ]);
    setExpectedInputMode(mode);
    console.debug('[pushBotMessage] mode set to:', mode);
    scrollToBottom();
  };

  const handleQuickAction = async (action) => {
    setShowQuickActions(false);
    setShowWelcome(false);
    scrollToBottom();

    switch (action) {
      case 'firstAid':
        setShowInput(true);
        setExpectedInputMode('firstAid');
        pushBotMessage('Enter your emergency details (e.g., burn, cut)', 'firstaid');
        setTimeout(() => inputRef.current?.focus(), 50);
        setTimeout(() => setShowQuickActions(true), 1000);
        break;

      case 'symptomChecker':
        pushBotMessage('Enter your symptoms (e.g., fever, cough)', 'symptom');
        setShowInput(true); 
        setTimeout(() => inputRef.current?.focus(), 50);
        break;

      case 'medication':
        pushBotMessage('Enter a medicine name to get general medication guidance.', 'medication');
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        break;

      case 'lab':
        pushBotMessage('Lab Test Explainer: Feature coming soon!', 'none');
        setShowInput(false);
        setTimeout(() => setShowQuickActions(true), 500);
        break;

      case 'specialist':
        pushBotMessage('Specialist Recommender: Feature coming soon!', 'none');
        setShowInput(false);
        setTimeout(() => setShowQuickActions(true), 500);
        break;

      case 'talkDoctor':
        pushBotMessage('Talk to a Doctor: Feature coming soon!', 'none');
        setShowInput(false);
        setTimeout(() => setShowQuickActions(true), 500);
        break;

      default:
        setShowQuickActions(true);
        break;
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.debug('[handleSubmit] mode:', expectedInputMode, 'input:', userInput);

    if (!userInput.trim()) return;

    const input = userInput.trim();
    setMessages((prev) => [...prev, { type: 'user', content: input }]);
    setUserInput('');
    setIsLoading(true);
    setShowQuickActions(false);

    try {
      let response;
      if (expectedInputMode === 'medication') {
        response = await handleMedicationRequest(input);
      }else if (expectedInputMode === 'firstAid') {
        response = await handleFirstAidRequest(input);
      } else {
        response = await handleSymptomRequest(input);
      }

      setMessages((prev) => [...prev, { type: 'bot', content: response }]);
      setExpectedInputMode('none');
      setShowInput(false);
      setShowQuickActions(true);
    } catch (err) {
      console.error('[handleSubmit] error:', err);
      setError('Sorry, I had trouble processing your request. Please try again.');
    } finally {
      setIsLoading(false);
      scrollToBottom();
    }
  };

  const handleMedicationRequest = async (input) => {
    const response = await getMedicationInfo(input);
    console.debug('[handleMedicationRequest] response:', response);

    if (!response || !response.medicines || Object.keys(response.medicines).length === 0) {
      return {
        medicines: {
          [input]: {
            uses: 'No information available.',
            dosage: {
              adults: 'Not available',
              kids: 'Not available',
            },
            precautions: 'No precautions listed.',
          },
        },
        disclaimer:
          'No medication data found. Please consult a healthcare professional before taking any medication.',
      };
    }

    return {
      medicines: response.medicines,
      disclaimer:
        'This information is for general guidance only. Always consult with a healthcare professional before taking any medication.',
    };
  };

  const handleSymptomRequest = async (input) => await getPrecautionsAndRemedies(input);
  
  const handleFirstAidRequest = async (input) => {
    const response = await getFirstAidInfo(input);
    console.debug("[handleFirstAidRequest] response:", response);

    return {
      firstAid: {
        tag: response.tag,
        group: response.group,
        steps: response.steps,
      },
      disclaimer:
        "This is general first aid guidance. For emergencies, contact local medical services immediately.",
    };
  };


  return (
    <AnimatePresence>
      {visible && (
        <ChatContainer>
          <ChatHeader
            onClose={onClose}
            minimized={minimized}
            onMinimize={() => setMinimized(!minimized)}
          />
          {!minimized && (
            <div className="flex flex-col h-full relative">
              <div className="flex-1 overflow-y-auto px-4 py-3 bg-white/50 scroll-smooth" style={{ height: 'calc(100% - 120px)' }}>
                <div className="space-y-4 pb-4 min-h-full">
                  {showWelcome && <WelcomeMessage />}
                  {messages.map((message, index) =>
                    message.type === 'user' ? (
                      <UserMessage key={index} content={message.content} />
                    ) : (
                      <BotMessage key={index} content={message.content} />
                    )
                  )}
                  {isLoading && <LoadingIndicator />}
                  {error && <Disclaimer text={error} type="error" />}
                  {showQuickActions && !isLoading && !showInput && (
                    <motion.div
                      initial={{ opacity: 0, y: 10 }}
                      animate={{ opacity: 1, y: 0 }}
                      className="mt-4 pb-2"
                    >
                      <div className="inline-block bg-gray-100 rounded-lg p-3 w-full">
                        {messages.length > 0 && (
                          <div className="text-xs text-gray-600 mb-2">
                            What would you like to do next?
                          </div>
                        )}
                        <QuickActions onAction={handleQuickAction} />
                      </div>
                    </motion.div>
                  )}
                </div>
              </div>
              <ChatInput
                onSubmit={handleSubmit}
                userInput={userInput}
                setUserInput={setUserInput}
                isLoading={isLoading}
                showInput={showInput}
                inputRef={inputRef}
                expectedInputMode={expectedInputMode}
              />
            </div>
          )}
        </ChatContainer>
      )}
    </AnimatePresence>
  );
}