package com.docease.aiservice.service;

import com.docease.aiservice.DTO.SymptomRequest;
import com.docease.aiservice.DTO.LogRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class SpecialistSuggestionService {

    private final WebClient externalWebClient;           // For Gemini API
    private final WebClient.Builder internalWebClientBuilder; // For internal logging

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${app.logging.endpoint}")
    private String loggingUrl;


    private final ObjectMapper objectMapper = new ObjectMapper();

    public SpecialistSuggestionService(
            @Qualifier("externalWebClientBuilder") WebClient.Builder externalBuilder,
            @Qualifier("internalWebClientBuilder") WebClient.Builder internalBuilder) {
        this.externalWebClient = externalBuilder.build();
        this.internalWebClientBuilder = internalBuilder;
    }

    public String suggestSpecialist(SymptomRequest symptomRequest) {
        String prompt = buildPrompt(symptomRequest);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        // Call Gemini API
        String rawResponse = externalWebClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String suggestion = extractResponseContent(rawResponse);

        // ASYNC LOGGING — fire-and-forget
        logConversationAsync(symptomRequest.getSymptoms(), suggestion);

        return suggestion;
    }

    private void logConversationAsync(String input, String output) {
        LogRequest logRequest = new LogRequest(
                "Specialist suggestion for symptoms: " + (input != null ? input : "N/A"),
                output != null ? output : "No suggestion generated"
        );

        internalWebClientBuilder.build()
                .post()
                .uri(loggingUrl)
                .header("Content-Type", "application/json")
                .bodyValue(logRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    System.err.println("Failed to log specialist suggestion: " + e.getMessage());
                    return Mono.empty();
                })
                .subscribe(); // Non-blocking, fire-and-forget
    }

    private String extractResponseContent(String response) {
        if (response == null || response.isBlank()) {
            return "{\"error\": \"No response from AI model.\"}";
        }

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String text = textNode.asText();
            return text.replaceAll("^```json\\n|\\n```$", "")
                    .replaceAll("^```\\n|\\n```$", "")
                    .trim();
        } catch (Exception e) {
            return "{\"error\": \"Failed to parse specialist suggestion: " + e.getMessage() + "\"}";
        }
    }

    private String buildPrompt(SymptomRequest symptomRequest) {
        return """
                Based on these symptoms: "%s"
                
                Suggest ONLY ONE most appropriate medical specialist.
                Examples:
                - Ear pain, hearing loss → ENT Specialist
                - Chest pain, shortness of breath → Cardiologist
                - Joint pain, swelling → Rheumatologist
                - Diabetes symptoms → Endocrinologist
                - Fever, cough, general illness → General Physician
                
                Respond in strict JSON format:
                {"specialist": "Cardiologist"}
                
                Keep response under 15 words. No explanation. Only JSON.
                """.formatted(symptomRequest.getSymptoms());
    }
}