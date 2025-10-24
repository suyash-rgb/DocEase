package com.docease.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
public class LabTestExplainerService {

    private final WebClient webClient;
    private final MessageLoggingService messageLoggingService;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public LabTestExplainerService(WebClient.Builder webClientBuilder, MessageLoggingService messageLoggingService) {
        this.webClient = webClientBuilder.build();
        this.messageLoggingService = messageLoggingService;
    }

    public String explainLabTest(MultipartFile image) throws IOException {
        // Build the prompt
        String prompt = "Analyze this lab test report image. Explain the results in simple terms for a non-medical user. Draw conclusions about what the results might indicate, including any abnormalities or normal ranges. Suggest next steps if needed, like consulting a doctor. Structure the response as JSON: {\"explanation\": \"\", \"conclusions\": [], \"next_steps\": []}. Keep under 300 words.";

        // Convert image to base64
        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        String mimeType = image.getContentType();

        // Craft a request with image
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

        // Do request and get response
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Extract response
        String explanation = extractResponseContent(response);

        // Log the conversation (input as prompt since image can't be logged directly)
        messageLoggingService.logConversation("Lab test image analysis: " + prompt, explanation);

        return explanation;
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText()
                    .replaceAll("^```json\\n|\\n```$", "")
                    .trim();
        } catch (Exception e) {
            return "{\"error\": \"Error processing request: " + e.getMessage() + "\"}";
        }
    }
}
