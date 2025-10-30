import { useEffect, useRef, useState } from 'react';
import { getPrecautionsAndRemedies } from '../services/aiService';

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
      // initial welcome message with quick actions shown separately in UI (not as a message)
    }, 2000);
    return () => clearTimeout(timer);
  }, []);

  // Format the bot response object (same as before)
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

  if (!visible) return null;

  // Helper to push bot messages
  const pushBotMessage = (content) => {
    setMessages((prev) => [...prev, { type: 'bot', content }]);
  };

  // Quick action handlers
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
        // Show input and focus — API will be called on submit
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
        pushBotMessage('Medication Info: enter a medicine name or describe the symptom to get general medication guidance.');
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
    <div
      className={`fixed bottom-6 right-6 border border-green-300 shadow-lg rounded-lg w-96 z-50 transition-all duration-300 ${
        minimized ? 'h-16 overflow-hidden' : 'h-[32rem]'
      } flex flex-col justify-between bg-white bg-[url('/logo.png')] bg-no-repeat bg-center bg-contain`}
    >
      {/* Header */}
      <div className="flex justify-between items-center px-4 py-2 border-b">
        <h3 className="text-green-700 font-semibold">DocEase Bot</h3>
        <div className="flex gap-2">
          <button onClick={() => setMinimized(!minimized)} title="Minimize">
            {minimized ? '🔼' : '🔽'}
          </button>
          <button onClick={onClose} title="Close">
            ❌
          </button>
        </div>
      </div>

      {/* Body */}
      {!minimized && (
        <div className="flex flex-col h-full relative">
          {/* Welcome / Quick Actions */}
          <div className="px-4 pt-4 pb-2 border-b">
            <div className="flex items-start gap-3">
              {/* small brand area */}
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
            <div className="mt-3 flex flex-wrap gap-2">
              <button
                onClick={() => handleQuickAction('firstAid')}
                className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200"
              >
                First Aid Guidance
              </button>
              <button
                onClick={() => handleQuickAction('symptomChecker')}
                className="text-xs px-2 py-1 rounded bg-green-50 text-green-700 border border-green-100 hover:bg-green-100"
              >
                Symptoms Checker
              </button>
              <button
                onClick={() => handleQuickAction('specialist')}
                className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200"
              >
                Specialist Recommendation
              </button>
              <button
                onClick={() => handleQuickAction('medication')}
                className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200"
              >
                Medication Info
              </button>
              <button
                onClick={() => handleQuickAction('lab')}
                className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200"
              >
                Lab Test Explainer
              </button>
              <button
                onClick={() => handleQuickAction('talkDoctor')}
                className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200"
              >
                Talk to a Doctor
              </button>
            </div>
          </div>

          {/* Messages Container */}
          <div className="flex-1 overflow-y-auto px-4 py-3">
            {messages.length === 0 && (
              <div className="text-center text-gray-500 text-sm"></div>
            )}

            {messages.map((message, index) => (
              <div key={index} className={`mb-3 ${message.type === 'user' ? 'text-right' : 'text-left'}`}>
                <div
                  className={`inline-block p-2 rounded-lg ${
                    message.type === 'user' ? 'bg-green-500 text-white' : 'bg-gray-100 text-gray-800'
                  }`}
                >
                  {message.type === 'bot' ? formatBotResponse(message.content) : message.content}
                </div>
              </div>
            ))}

            {isLoading && <div className="text-center text-gray-500">DocEase Bot is thinking...</div>}
          </div>

          {/* Input Container */}
          <div className="p-4 border-t bg-white sticky bottom-0">
            <form
              onSubmit={async (e) => {
                e.preventDefault();
                if (!userInput.trim()) return;

                const symptoms = userInput.trim();
                setMessages((prev) => [...prev, { type: 'user', content: symptoms }]);
                setUserInput('');
                setIsLoading(true);

                try {
                  // API call triggered on submit of Symptoms Checker flow
                  const response = await getPrecautionsAndRemedies(symptoms);
                  setMessages((prev) => [...prev, { type: 'bot', content: response }]);
                } catch (error) {
                  setMessages((prev) => [
                    ...prev,
                    {
                      type: 'bot',
                      content: {
                        symptoms: {
                          error: {
                            precautions: [],
                            remedies: ['Sorry, I had trouble processing your symptoms. Please try again.'],
                          },
                        },
                        disclaimer:
                          'This is an error message. Please try again or contact support if the issue persists.',
                      },
                    },
                  ]);
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
                className="flex-1 border rounded px-3 py-2 text-sm"
                placeholder={showInput ? 'Enter your symptoms (e.g., fever, cough, sore throat)' : 'Choose an option above to start'}
                disabled={!showInput}
              />
              <button
                type="submit"
                disabled={isLoading || !showInput}
                className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 disabled:bg-green-300"
              >
                Send
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}