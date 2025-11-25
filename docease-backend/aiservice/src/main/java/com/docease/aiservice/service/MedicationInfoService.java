package com.docease.aiservice.service;

import com.docease.aiservice.DTO.MedicineRequest;
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
public class MedicationInfoService {

    private final WebClient externalWebClient;           // For Gemini API
    private final WebClient.Builder internalWebClientBuilder; // For internal logging service

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${app.logging.endpoint}")
    private String loggingUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MedicationInfoService(
            @Qualifier("externalWebClientBuilder") WebClient.Builder externalBuilder,
            @Qualifier("internalWebClientBuilder") WebClient.Builder internalBuilder) {
        this.externalWebClient = externalBuilder.build();
        this.internalWebClientBuilder = internalBuilder;
    }

    public String getMedicationInfo(MedicineRequest medicineRequest) {
        String prompt = buildPrompt(medicineRequest);

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

        String info = extractResponseContent(rawResponse);

        // LOG ASYNC — fire-and-forget to centralized logging service
        logConversationAsync(medicineRequest.getMedicines(), info);

        return info;
    }

    private void logConversationAsync(String input, String output) {
        LogRequest logRequest = new LogRequest(
                "Medication info request: " + (input != null ? input : "N/A"),
                output != null ? output : "No response generated"
        );

        internalWebClientBuilder.build()
                .post()
                .uri(loggingUrl)
                .header("Content-Type", "application/json")
                .bodyValue(logRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(throwable -> {
                    System.err.println("Failed to log medication query: " + throwable.getMessage());
                    return Mono.empty();
                })
                .subscribe(); // Fire-and-forget — never blocks user
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

            String text = textNode.asText("AI response parsing failed.");

            // Remove markdown code blocks if present
            return text.replaceAll("^```json\\n|\\n```$", "")
                    .replaceAll("^```\\n|\\n```$", "")
                    .trim();
        } catch (Exception e) {
            return "{\"error\": \"Failed to parse medication info: " + e.getMessage() + "\"}";
        }
    }

    private String buildPrompt(MedicineRequest medicineRequest) {
        return """
                Provide accurate, concise information for the following medicine(s): %s
                
                For each medicine, return a JSON object with:
                - "uses": main medical uses
                - "dosage": { "adults": "...", "children": "..." }
                - "side_effects": common ones
                - "precautions": who should avoid or consult doctor
                - "warning": any serious risks
                
                Example format:
                {
                  "medicines": {
                    "Paracetamol": {
                      "uses": "Pain relief and fever reduction",
                      "dosage": { "adults": "500-1000mg every 4-6 hours", "children": "10-15mg/kg every 6 hours" },
                      "side_effects": "Rare liver damage with overdose",
                      "precautions": "Avoid alcohol, do not exceed 4g/day",
                      "warning": "Overdose can cause liver failure"
                    }
                  }
                }
                Respond ONLY in valid JSON. Keep under 250 words.
                """.formatted(medicineRequest.getMedicines());
    }
}