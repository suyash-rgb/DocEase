import { motion } from 'framer-motion';

export const formatBotResponse = (response) => {
  if (!response || typeof response !== "object") return String(response || "");

  const blocks = [];

  // ✅ Medication info rendering
  if (response.medicines && typeof response.medicines === "object") {
    for (const [medName, medData] of Object.entries(response.medicines)) {
      blocks.push(
        <div key={medName} className="mb-4">
          <h4 className="text-md font-semibold text-green-700 capitalize">{medName}</h4>
          <div className="ml-4">
            <p className="text-sm font-medium text-gray-800">Uses:</p>
            <p className="text-sm text-gray-700 mb-2">{medData.uses}</p>

            <p className="text-sm font-medium text-gray-800">Dosage:</p>
            <ul className="list-disc ml-5 text-sm text-gray-700">
              <li><strong>Adults:</strong> {medData.dosage?.adults}</li>
              <li><strong>Children:</strong> {medData.dosage?.kids}</li>
            </ul>

            <p className="text-sm font-medium text-gray-800 mt-2">Precautions:</p>
            <p className="text-sm text-gray-700">{medData.precautions}</p>
          </div>
        </div>
      );
    }
  }

  // ✅ Symptom checker rendering (unchanged)
  if (response.symptoms && typeof response.symptoms === "object") {
    for (const [symptom, data] of Object.entries(response.symptoms)) {
      blocks.push(
        <div key={symptom} className="mb-4">
          <h4 className="text-md font-semibold text-green-700 capitalize">{symptom}</h4>
          <div className="ml-4">
            {Array.isArray(data.precautions) && data.precautions.length > 0 && (
              <>
                <p className="text-sm font-medium text-gray-800">Precautions:</p>
                <ul className="list-disc ml-5 text-sm text-gray-700">
                  {data.precautions.map((item, idx) => (
                    <li key={`p-${symptom}-${idx}`}>{item}</li>
                  ))}
                </ul>
              </>
            )}
            {Array.isArray(data.remedies) && data.remedies.length > 0 && (
              <>
                <p className="text-sm font-medium text-gray-800 mt-2">Remedies:</p>
                <ul className="list-disc ml-5 text-sm text-gray-700">
                  {data.remedies.map((item, idx) => (
                    <li key={`r-${symptom}-${idx}`}>{item}</li>
                  ))}
                </ul>
              </>
            )}
          </div>
        </div>
      );
    }
  }

  // ✅ Disclaimer
  if (response.disclaimer) {
    blocks.push(
      <div key="disclaimer" className="mt-4 p-2 bg-yellow-50 border border-yellow-200 rounded-lg">
        <p className="text-xs text-gray-600 italic">{response.disclaimer}</p>
      </div>
    );
  }

  // Handle simple text messages
  if (blocks.length === 0) {
    if (response.prompt) {
      blocks.push(
        <p key="simple-message" className="text-sm text-gray-700">
          {response.prompt}
        </p>
      );
    } else if (typeof response === 'string') {
      blocks.push(
        <p key="simple-message" className="text-sm text-gray-700">
          {response}
        </p>
      );
    } else {
      blocks.push(
        <p key="simple-message" className="text-sm text-gray-700">
          {JSON.stringify(response)}
        </p>
      );
    }
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