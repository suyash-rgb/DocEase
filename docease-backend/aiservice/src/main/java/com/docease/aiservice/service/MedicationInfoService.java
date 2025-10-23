package com.docease.aiservice.service;

import com.docease.aiservice.DTO.MedicineRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class MedicationInfoService {

    private final WebClient webClient;
    private final MessageLoggingService messageLoggingService;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public MedicationInfoService(WebClient.Builder webClientBuilder, MessageLoggingService messageLoggingService) {
        this.webClient = webClientBuilder.build();
        this.messageLoggingService = messageLoggingService;
    }

    public String getMedicationInfo(MedicineRequest medicineRequest) {
        // Build the prompt
        String prompt = buildPrompt(medicineRequest);

        // Craft a request
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        // Do request and get response
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Extract response
        String info = extractResponseContent(response);

        // Log the conversation
        messageLoggingService.logConversation(medicineRequest.getMedicines(), info);

        return info;
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            String content = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            // Remove Markdown code fences and trim whitespace
            content = content.replaceAll("^```json\\n|\\n```$", "").trim();
            return content;
        } catch (Exception e) {
            return "{\"error\": \"Error processing request: " + e.getMessage() + "\"}";
        }
    }

    private String buildPrompt(MedicineRequest medicineRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a concise JSON response with information for the following medicines: ")
                .append(medicineRequest.getMedicines())
                .append(". For each medicine, include: 'uses': string describing what it's used for, 'dosage': {'kids': string, 'adults': string}, 'precautions': string describing who should consult a doctor or avoid it. Structure as JSON: {\"medicines\": {\"medicine1\": {\"uses\": \"\", \"dosage\": {\"kids\": \"\", \"adults\": \"\"}, \"precautions\": \"\"}}} Keep the response under 200 words.");
        return prompt.toString();
    }
}