import { useEffect, useState } from 'react';
import { getPrecautionsAndRemedies } from '../services/aiService';

export default function ChatbotPopup({ onClose }) {
  const [visible, setVisible] = useState(false);
  const [minimized, setMinimized] = useState(false);
  const [userInput, setUserInput] = useState('');
  const [messages, setMessages] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => setVisible(true), 2000);
    return () => clearTimeout(timer);
  }, []);

  const formatBotResponse = (response) => {
    const { symptoms } = response;
    const blocks = [];

    for (const [symptom, data] of Object.entries(symptoms)) {
      blocks.push(
        <div key={symptom} className="mb-4">
          <h4 className="text-md font-semibold text-green-700 capitalize">{symptom}</h4>

          <div className="ml-4">
            <p className="text-sm font-medium text-gray-800">Precautions:</p>
            <ul className="list-disc ml-5 text-sm text-gray-700">
              {data.precautions.map((item, idx) => (
                <li key={`p-${symptom}-${idx}`}>{item}</li>
              ))}
            </ul>

            <p className="text-sm font-medium text-gray-800 mt-2">Remedies:</p>
            <ul className="list-disc ml-5 text-sm text-gray-700">
              {data.remedies.map((item, idx) => (
                <li key={`r-${symptom}-${idx}`}>{item}</li>
              ))}
            </ul>
          </div>
        </div>
      );
    }

    return blocks;
  };

  if (!visible) return null;

  return (
    <div
      className={`fixed bottom-6 right-6 bg-white border border-green-300 shadow-lg rounded-lg w-96 z-50 transition-all duration-300 ${
        minimized ? 'h-16 overflow-hidden' : 'h-[32rem]'
      } flex flex-col justify-between`}
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
        <div className="flex flex-col h-full">
          {/* Messages Container */}
          <div className="flex-1 overflow-y-auto px-4 py-3">
            {messages.map((message, index) => (
              <div
                key={index}
                className={`mb-3 ${message.type === 'user' ? 'text-right' : 'text-left'}`}
              >
                <div
                  className={`inline-block p-2 rounded-lg ${
                    message.type === 'user'
                      ? 'bg-green-500 text-white'
                      : 'bg-gray-100 text-gray-800'
                  }`}
                >
                  {message.type === 'bot' ? formatBotResponse(message.content) : message.content}
                </div>
              </div>
            ))}
            {isLoading && (
              <div className="text-center text-gray-500">DocEase Bot is thinking...</div>
            )}
          </div>

          {/* Input Container */}
          <div className="p-4 border-t">
            <form
              onSubmit={async (e) => {
                e.preventDefault();
                if (!userInput.trim()) return;

                const symptoms = userInput.trim();
                setMessages((prev) => [...prev, { type: 'user', content: symptoms }]);
                setUserInput('');
                setIsLoading(true);

                try {
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
                value={userInput}
                onChange={(e) => setUserInput(e.target.value)}
                className="flex-1 border rounded px-3 py-2 text-sm"
                placeholder="Enter your symptoms (e.g., fever, cough, sore throat)"
              />
              <button
                type="submit"
                disabled={isLoading}
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