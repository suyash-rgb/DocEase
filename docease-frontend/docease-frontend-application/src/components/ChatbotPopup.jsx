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
import { getPrecautionsAndRemedies, getMedicationInfo } from '../services/aiService';

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
  const inputRef = useRef(null);

  useEffect(() => {
    const timer = setTimeout(() => setVisible(true), 2000);
    return () => clearTimeout(timer);
  }, []);

  const pushBotMessage = (message) => {
    setMessages((prev) => [
      ...prev,
      {
        type: "bot",
        content: {
          symptoms: {
            info: {
              precautions: [],
              remedies: [message],
            },
          },
          disclaimer:
            "This is an automated response. Please consult with a healthcare professional for personalized advice.",
        },
      },
    ]);
  };

  const handleQuickAction = async (action) => {
    setShowQuickActions(false);
    setShowWelcome(false);

    switch (action) {
      case 'firstAid':
        setMessages(prev => [...prev, {
          type: 'bot',
          content: {
            symptoms: {
              firstAid: {
                precautions: [],
                remedies: ['Basic first aid: ensure safety, stop bleeding with pressure, immobilize fractures, call emergency services if severe.'],
              },
            },
            disclaimer: 'First aid tips are general. For emergencies call local services.',
          },
        }]);
        setShowInput(false);
        setTimeout(() => setShowQuickActions(true), 220);
        break;

      case 'symptomChecker':
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        setShowQuickActions(true);
        break;

      // ... other cases
      case 'specialist':
        pushBotMessage(
          'Specialist Recommender: Feature coming soon!'
        );
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        // Re-show quick actions after message
        setTimeout(() => setShowQuickActions(true), 220);
        break;

      case 'medication':
        pushBotMessage('Enter a medicine name to get general medication guidance.');
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        // Re-show quick actions after message
        setTimeout(() => setShowQuickActions(true), 220);
        break;

      case 'lab':
        pushBotMessage('Lab Test Explainer: enter the name of the test or paste key values (e.g., HbA1c, CBC) for a short explainer.');
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        // Re-show quick actions after message
        setTimeout(() => setShowQuickActions(true), 220);
        break;

      case 'talkDoctor':
        pushBotMessage('Talk to a Doctor: You can request a teleconsultation or share symptoms and I will guide you how to reach a clinician.');
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        // Re-show quick actions after message
        setTimeout(() => setShowQuickActions(true), 220);
        break;

      default:
        // Re-show quick actions if no action taken
        setShowQuickActions(true);
        break;
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!userInput.trim()) return;

    const input = userInput.trim();
    setMessages(prev => [...prev, { type: 'user', content: input }]);
    setUserInput('');
    setIsLoading(true);
    setShowQuickActions(false);

    try {
      const lastMessage = messages[messages.length - 1]?.content;
      const isMedicationRequest = lastMessage === 'Enter a medicine name to get general medication guidance.';
      
      let response = isMedicationRequest
        ? await handleMedicationRequest(input)
        : await handleSymptomRequest(input);

      setMessages(prev => [...prev, { type: 'bot', content: response }]);
    } catch (err) {
      const errorMessage = 'Sorry, I had trouble processing your request. Please try again.';
      setError(errorMessage);
    } finally {
      setIsLoading(false);
      setTimeout(() => setShowQuickActions(true), 220);
    }
  };

  const handleMedicationRequest = async (input) => {
    const response = await getMedicationInfo(input);
    const medicineName = Object.keys(response.medicines)[0];
    const medicineInfo = response.medicines[medicineName];
    
    return {
      symptoms: {
        [medicineName]: {
          precautions: [medicineInfo.precautions],
          remedies: [
            `Uses: ${medicineInfo.uses}`,
            `Dosage:`,
            `- Adults: ${medicineInfo.dosage.adults}`,
            `- Children: ${medicineInfo.dosage.kids}`
          ]
        }
      },
      disclaimer: 'This information is for general guidance only. Always consult with a healthcare professional before taking any medication.'
    };
  };

  const handleSymptomRequest = (input) => getPrecautionsAndRemedies(input);

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
              <div className="flex-1 overflow-y-auto px-4 py-3 bg-white/50">
                {showWelcome && <WelcomeMessage />}

                {messages.map((message, index) => (
                  message.type === 'user' 
                    ? <UserMessage key={index} content={message.content} />
                    : <BotMessage key={index} content={message.content} />
                ))}

                {isLoading && <LoadingIndicator />}
                {error && <Disclaimer text={error} type="error" />}

                {showQuickActions && !isLoading && (
                  <motion.div
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    className="mt-4"
                  >
                    {messages.length > 0 && (
                      <div className="inline-block bg-gray-100 rounded-lg p-3 w-full">
                        <div className="text-xs text-gray-600 mb-2">
                          What would you like to do next?
                        </div>
                        <QuickActions onAction={handleQuickAction} />
                      </div>
                    )}
                    {messages.length === 0 && <QuickActions onAction={handleQuickAction} />}
                  </motion.div>
                )}
              </div>

              <ChatInput 
                onSubmit={handleSubmit}
                userInput={userInput}
                setUserInput={setUserInput}
                isLoading={isLoading}
                showInput={showInput}
                inputRef={inputRef}
              />
            </div>
          )}
        </ChatContainer>
      )}
    </AnimatePresence>
  );
}