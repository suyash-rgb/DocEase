import { useEffect, useRef, useState } from 'react';
import { getPrecautionsAndRemedies, getMedicationInfo } from '../services/aiService';
import { motion, AnimatePresence } from 'framer-motion';

export default function ChatbotPopup({ onClose }) {
  const [visible, setVisible] = useState(false);
  const [minimized, setMinimized] = useState(false);
  const [userInput, setUserInput] = useState('');
  const [messages, setMessages] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [showInput, setShowInput] = useState(false);
  const inputRef = useRef(null);

  useEffect(() => {
    const timer = setTimeout(() => {
      setVisible(true);
    }, 2000);
    return () => clearTimeout(timer);
  }, []);

  const formatBotResponse = (response) => {
    if (!response || typeof response !== 'object') return String(response || '');
    const { symptoms, disclaimer } = response;
    const blocks = [];

    if (symptoms && typeof symptoms === 'object') {
      for (const [symptom, data] of Object.entries(symptoms)) {
        blocks.push(
          <div key={symptom} className="mb-4">
            <h4 className="text-md font-semibold text-green-700 capitalize">{symptom}</h4>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-800">Precautions:</p>
              <ul className="list-disc ml-5 text-sm text-gray-700">
                {Array.isArray(data.precautions)
                  ? data.precautions.map((item, idx) => <li key={`p-${symptom}-${idx}`}>{item}</li>)
                  : null}
              </ul>
              <p className="text-sm font-medium text-gray-800 mt-2">Remedies:</p>
              <ul className="list-disc ml-5 text-sm text-gray-700">
                {Array.isArray(data.remedies)
                  ? data.remedies.map((item, idx) => <li key={`r-${symptom}-${idx}`}>{item}</li>)
                  : null}
              </ul>
            </div>
          </div>
        );
      }
    }

    if (disclaimer) {
      blocks.push(
        <div key="disclaimer" className="mt-4 p-2 bg-yellow-50 border border-yellow-200 rounded-lg">
          <p className="text-xs text-gray-600 italic">{disclaimer}</p>
        </div>
      );
    }

    return blocks;
  };

  const pushBotMessage = (content) => {
    setMessages((prev) => [...prev, { type: 'bot', content }]);
  };

  const handleQuickAction = (action) => {
    switch (action) {
      case 'firstAid':
        pushBotMessage({
          symptoms: {
            firstAid: {
              precautions: [],
              remedies: [
                'Basic first aid: ensure safety, stop bleeding with pressure, immobilize fractures, call emergency services if severe.',
              ],
            },
          },
          disclaimer: 'First aid tips are general. For emergencies call local services.',
        });
        setShowInput(false);
        break;

      case 'symptomChecker':
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        break;

      case 'specialist':
        pushBotMessage(
          'Specialist Recommender: tell me more about the condition or symptoms and I can suggest which specialist you should consult.'
        );
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        break;

      case 'medication':
        pushBotMessage('Enter a medicine name to get general medication guidance.');
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        break;

      case 'lab':
        pushBotMessage('Lab Test Explainer: enter the name of the test or paste key values (e.g., HbA1c, CBC) for a short explainer.');
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        break;

      case 'talkDoctor':
        pushBotMessage('Talk to a Doctor: You can request a teleconsultation or share symptoms and I will guide you how to reach a clinician.');
        setShowInput(true);
        setTimeout(() => inputRef.current?.focus(), 50);
        break;

      default:
        break;
    }
  };

  return (
    <AnimatePresence>
      {visible && (
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: 20 }}
          transition={{ duration: 0.3 }}
          className={`fixed bottom-6 right-6 border border-green-300 shadow-lg rounded-lg w-96 z-50 ${
            minimized ? 'h-16 overflow-hidden' : 'h-[32rem]'
          } flex flex-col justify-between bg-white overflow-hidden`}
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
            {/* Header */}
            <motion.div 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: 0.2 }}
              className="flex justify-between items-center px-4 py-2 border-b bg-white/50 backdrop-blur-sm"
            >
              <h3 className="text-green-700 font-semibold">DocEase Bot</h3>
              <div className="flex gap-2">
                <button onClick={() => setMinimized(!minimized)} title="Minimize" className="hover:bg-gray-100 p-1 rounded">
                  {minimized ? '🔼' : '🔽'}
                </button>
                <button onClick={onClose} title="Close" className="hover:bg-gray-100 p-1 rounded">
                  ❌
                </button>
              </div>
            </motion.div>

            {/* Body */}
            {!minimized && (
              <div className="flex flex-col h-full relative">
                {/* Welcome / Quick Actions */}
                <motion.div 
                  initial={{ opacity: 0, y: -10 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: 0.3 }}
                  className="px-4 pt-4 pb-2 border-b bg-white/50 backdrop-blur-sm"
                >
                  <div className="flex items-start gap-3">
                    <div className="w-10 h-10 rounded-full bg-green-100 flex items-center justify-center">
                      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" className="text-green-700">
                        <path d="M12 2v20M2 12h20" stroke="#16A34A" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                      </svg>
                    </div>
                    <div>
                      <div className="text-sm font-semibold text-green-700">Welcome to DocEase</div>
                      <div className="text-xs text-gray-600">How can I be of assistance today?</div>
                    </div>
                  </div>

                  {/* Quick action buttons */}
                  <motion.div 
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ delay: 0.4 }}
                    className="mt-3 flex flex-wrap gap-2"
                  >
                    <button
                      onClick={() => handleQuickAction('firstAid')}
                      className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200 transition-colors"
                    >
                      First Aid Guidance
                    </button>
                    <button
                      onClick={() => handleQuickAction('symptomChecker')}
                      className="text-xs px-2 py-1 rounded bg-green-50 text-green-700 border border-green-100 hover:bg-green-100 transition-colors"
                    >
                      Symptoms Checker
                    </button>
                    <button
                      onClick={() => handleQuickAction('specialist')}
                      className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200 transition-colors"
                    >
                      Specialist Recommendation
                    </button>
                    <button
                      onClick={() => handleQuickAction('medication')}
                      className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200 transition-colors"
                    >
                      Medication Info
                    </button>
                    <button
                      onClick={() => handleQuickAction('lab')}
                      className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200 transition-colors"
                    >
                      Lab Test Explainer
                    </button>
                    <button
                      onClick={() => handleQuickAction('talkDoctor')}
                      className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200 transition-colors"
                    >
                      Talk to a Doctor
                    </button>
                  </motion.div>
                </motion.div>

                {/* Messages Container */}
                <div className="flex-1 overflow-y-auto px-4 py-3 bg-white/50">
                  {messages.map((message, index) => (
                    <motion.div
                      initial={{ opacity: 0, y: 10 }}
                      animate={{ opacity: 1, y: 0 }}
                      key={index}
                      className={`mb-3 ${message.type === 'user' ? 'text-right' : 'text-left'}`}
                    >
                      <div
                        className={`inline-block p-2 rounded-lg ${
                          message.type === 'user' ? 'bg-green-500 text-white' : 'bg-gray-100 text-gray-800'
                        }`}
                      >
                        {message.type === 'bot' ? formatBotResponse(message.content) : message.content}
                      </div>
                    </motion.div>
                  ))}

                  {isLoading && (
                    <motion.div
                      initial={{ opacity: 0 }}
                      animate={{ opacity: 1 }}
                      className="text-center text-gray-500"
                    >
                      DocEase Bot is thinking...
                    </motion.div>
                  )}
                </div>

                {/* Input Container */}
                <motion.div 
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: 0.5 }}
                  className="p-4 border-t bg-white/80 backdrop-blur-sm sticky bottom-0"
                >
                  <form
                    onSubmit={async (e) => {
                      e.preventDefault();
                      if (!userInput.trim()) return;

                      const input = userInput.trim();
                      setMessages((prev) => [...prev, { type: 'user', content: input }]);
                      setUserInput('');
                      setIsLoading(true);

                      try {
                        let response;
                        const lastMessage = messages.length > 0 ? messages[messages.length - 1].content : '';
                        const isMedicationRequest = lastMessage === 'Enter a medicine name to get general medication guidance.';
                        
                        console.log('Request type:', isMedicationRequest ? 'Medication' : 'Symptoms');
                        console.log('User input:', input);

                        if (isMedicationRequest) {
                          // Handle medication info request
                          console.log('Calling medication info API with input:', input);
                          response = await getMedicationInfo(input);
                          console.log('Medication API response:', response);
                          
                          // Get the first medicine info from the response
                          const medicineName = Object.keys(response.medicines)[0];
                          const medicineInfo = response.medicines[medicineName];
                          
                          const formattedResponse = {
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
                          setMessages((prev) => [...prev, { type: 'bot', content: formattedResponse }]);
                        } else {
                          // Handle symptoms request
                          response = await getPrecautionsAndRemedies(input);
                          console.log('Symptoms API response:', response);
                          setMessages((prev) => [...prev, { type: 'bot', content: response }]);
                        }
                      } catch (error) {
                        setMessages((prev) => [
                          ...prev,
                          {
                            type: 'bot',
                            content: {
                              symptoms: {
                                error: {
                                  precautions: [],
                                  remedies: ['Sorry, I had trouble processing your request. Please try again.'],
                                },
                              },
                              disclaimer:
                                'This is an error message. Please try again or contact support if the issue persists.',
                            },
                          },
                        ]);
                        console.error('Request failed:', error);
                      } finally {
                        setIsLoading(false);
                      }
                    }}
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
              </div>
            )}
          </div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}