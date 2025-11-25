package com.docease.aiservice.service;

import com.docease.aiservice.DTO.LogRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
public class LabTestExplainerService {

    private final WebClient externalWebClient;           // For Gemini
    private final WebClient.Builder internalWebClientBuilder; // For internal logging

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${app.logging.endpoint}")
    private String loggingUrl;


    private final ObjectMapper objectMapper = new ObjectMapper();

    public LabTestExplainerService(
            @Qualifier("externalWebClientBuilder") WebClient.Builder externalBuilder,
            @Qualifier("internalWebClientBuilder") WebClient.Builder internalBuilder) {
        this.externalWebClient = externalBuilder.build();
        this.internalWebClientBuilder = internalBuilder;
    }

    public String explainLabTest(MultipartFile image) throws IOException {
        String prompt = """
                Analyze this lab test report image carefully.
                Explain each test result in simple, non-medical language.
                Highlight any values that are high, low, or abnormal.
                Draw clear conclusions about what the results might indicate.
                Suggest next steps (e.g., consult doctor, repeat test, lifestyle changes).
                Respond in valid JSON format:
                {
                  "explanation": "Your hemoglobin is normal at 14.2 g/dL...",
                  "abnormal_results": ["High cholesterol: 240 mg/dL (normal <200)"],
                  "conclusions": ["Possible early diabetes risk", "Liver function normal"],
                  "next_steps": ["Consult physician within 1 week", "Repeat fasting glucose test"]
                }
                Keep under 300 words. Be accurate and helpful.
                """;

        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        String mimeType = image.getContentType() != null ? image.getContentType() : "image/jpeg";

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt),
                                Map.of("inlineData", Map.of(
                                        "mimeType", mimeType,
                                        "data", base64Image
                                ))
                        })
                }
        );

        // Call Gemini Vision API
        String rawResponse = externalWebClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String explanation = extractResponseContent(rawResponse);

        // LOG ASYNC to centralized service (fire-and-forget)
        logConversationAsync("Lab test image uploaded for analysis", explanation);

        return explanation;
    }

    private void logConversationAsync(String input, String output) {
        LogRequest logRequest = new LogRequest(input, output);

        internalWebClientBuilder.build()
                .post()
                .uri(loggingUrl)
                .header("Content-Type", "application/json")
                .bodyValue(logRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(throwable -> {
                    System.err.println("Failed to send log to messaging-logging-service: " + throwable.getMessage());
                    return Mono.empty();
                })
                .subscribe(); // Fire-and-forget
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
            // Remove markdown fences if present
            return text.replaceAll("^```json\\n|\\n```$", "").trim();
        } catch (Exception e) {
            return "{\"error\": \"Failed to parse AI response: " + e.getMessage() + "\"}";
        }
    }
}