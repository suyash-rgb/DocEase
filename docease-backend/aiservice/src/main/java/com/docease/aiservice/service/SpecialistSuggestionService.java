package com.docease.aiservice.service;

import com.docease.aiservice.DTO.AvailableDoctorResponse;
import com.docease.aiservice.DTO.SpecialistResponse;
import com.docease.aiservice.DTO.SymptomRequest;
import com.docease.aiservice.DTO.LogRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class SpecialistSuggestionService {

    private final WebClient externalWebClient;           // For Gemini API
    private final WebClient.Builder internalWebClientBuilder; // For internal logging

    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${app.logging.endpoint}")
    private String loggingUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //constructor injection
    public SpecialistSuggestionService(
            @Qualifier("externalWebClientBuilder") WebClient.Builder externalBuilder,
            @Qualifier("internalWebClientBuilder") WebClient.Builder internalBuilder) {
        this.externalWebClient = externalBuilder.build();
        this.internalWebClientBuilder = internalBuilder;
    }

    public SpecialistResponse suggestSpecialistDoctors(SymptomRequest request, String city) {
        // 1. Get specialist from Gemini
        String specialist = extractSpecialistFromGemini(request);

        // 2. Get real doctors from user-service
        List<AvailableDoctorResponse> doctors = doctorAvailabilityService.getAvailableDoctors(
                specialist,
                city,
                LocalDate.now().plusDays(1)
        );

        // 3. Async log
        logConversationAsync(request.getSymptoms(), specialist);

        // 4. Return full response
        return new SpecialistResponse(specialist, city, doctors);
    }

    private String extractSpecialistFromGemini(SymptomRequest request) {
        String prompt = """
                Based on these symptoms: "%s"
                Suggest ONLY ONE most appropriate medical specialist.
                Respond in strict JSON: {"specialist": "Cardiologist"}
                No explanation. Only JSON.
                """.formatted(request.getSymptoms());

        Map<String, Object> body = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        String raw = externalWebClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode node = objectMapper.readTree(raw)
                    .path("candidates").get(0)
                    .path("content").path("parts").get(0).path("text");
            String json = node.asText().replaceAll("```json|```", "").trim();
            return objectMapper.readTree(json).path("specialist").asText();
        } catch (Exception e) {
            return "General Physician"; // fallback
        }
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