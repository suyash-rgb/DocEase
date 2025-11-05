import { motion } from 'framer-motion';

export const formatBotResponse = (response) => {
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

export default function BotMessage({ content }) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="mb-3 text-left"
    >
      <div className="inline-block p-2 rounded-lg bg-gray-100 text-gray-800">
        {formatBotResponse(content)}
      </div>
    </motion.div>
  );
}