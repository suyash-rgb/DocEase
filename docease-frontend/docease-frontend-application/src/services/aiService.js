import axios from 'axios';

const AI_SERVICE_BASE_URL = 'http://localhost:8080/ai-service';

// Create an axios instance with default config
const aiServiceClient = axios.create({
    baseURL: AI_SERVICE_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

export const getPrecautionsAndRemedies = async (symptoms) => {
    try {
        const response = await aiServiceClient.post('/symptom-precautions-and-remedies/generate', {
            symptoms
        });
        return response.data;
    } catch (error) {
        console.error('Error getting precautions and remedies:', error.response?.data || error.message);
        throw error;
    }
};

export const getMedicationInfo = async (medicines) => {
    try {
        const response = await aiServiceClient.post('/medication-info/get', {
            medicines
        });
        return response.data;
    } catch (error) {
        console.error('Error getting the medication info:', error.response?.data || error.message);
        throw error;
    }
};