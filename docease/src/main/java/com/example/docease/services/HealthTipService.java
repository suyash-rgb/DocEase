package com.example.docease.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class HealthTipService {

        @Value("${GEMINI_API_URL}")
        private String geminiApiUrl;

        @Value("${GEMINI_API_KEY}")
        private String geminiApiKey;

        private final WebClient webClient;

        public HealthTipService(WebClient.Builder webClientBuilder) {
            this.webClient = webClientBuilder.baseUrl(geminiApiUrl).build();
        }

    public String generateHealthTip() {
        String prompt = "Generate a short, practical health tip.";

        try {
            Map<String, Object> requestBody = Map.of(
                    "contents", new Object[]{
                            Map.of("parts", new Object[]{
                                    Map.of("text", prompt)
                            })
                    }
            );

            String response = webClient.post()
                    .uri(geminiApiUrl + geminiApiKey)  // Correctly appends the full API URL
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode jsonResponse = new ObjectMapper().readTree(response);
            return jsonResponse.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }
    }

}
