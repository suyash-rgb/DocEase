const AI_SERVICE_BASE_URL = 'http://localhost:8080/ai-service';

export const getPrecautionsAndRemedies = async (symptoms) => {
    try {
        const response = await fetch(`${AI_SERVICE_BASE_URL}/symptom-precautions-and-remedies/generate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ symptoms }),
        });

        if (!response.ok) {
            throw new Error('Failed to get precautions and remedies');
        }

        return await response.json();
    } catch (error) {
        console.error('Error getting precautions and remedies:', error);
        throw error;
    }
};