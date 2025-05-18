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
                String response = webClient.post()
                        .uri("/generate-tip") // Adjust URI as needed
                        .header("Authorization", "Bearer " + geminiApiKey)
                        .bodyValue(Map.of("prompt", prompt))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                JsonNode jsonResponse = new ObjectMapper().readTree(response);
                return jsonResponse.get("generatedText").asText();
            } catch (Exception e) {
                return e.getMessage();
            }
        }

}
