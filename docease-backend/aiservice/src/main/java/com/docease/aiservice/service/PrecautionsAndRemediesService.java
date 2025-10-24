package com.docease.aiservice.service;

import com.docease.aiservice.DTO.SymptomRequest;
import com.docease.aiservice.service.MessageLoggingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class PrecautionsAndRemediesService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    private MessageLoggingService messageLoggingService;

    public PrecautionsAndRemediesService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateAdvice(SymptomRequest symptomRequest){
        //Build the prompt
        String prompt = buildPrompt(symptomRequest);

        //Craft a request
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        // Do request and get response
        String response = webClient.post()
                .uri(geminiApiUrl+geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //Extract response
        String advice = extractResposneContent(response);

        //Log the conversation
        messageLoggingService.logConversation(symptomRequest.getSymptoms(), advice);

        return advice;
    }

    private String extractResposneContent(String response){
        try{
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
        } catch (JsonMappingException e) {
            return "Error processing request: " + e.getMessage();
        } catch (JsonProcessingException e) {
            return "Error processing request: " + e.getMessage();
        }
    }

    private String buildPrompt(SymptomRequest symptomRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a concise JSON response with precautions and home remedies for the following symptoms: ")
                .append(symptomRequest.getSymptoms())
                .append(". Include only precautions and remedies specific to the listed symptoms. Exclude general precautions. Structure the response as JSON with a 'symptoms' object containing 'precautions' and 'remedies' arrays for each symptom, and a 'disclaimer' field at the end. Keep under 250 words. Example format: {\"symptoms\": {\"fever\": {\"precautions\": [], \"remedies\": []}, \"cough\": {\"precautions\": [], \"remedies\": []}}, \"disclaimer\": \"Please remember that this is not a substitute for professional medical advice. If your symptoms are severe, persistent, or worsening, you should seek medical attention from a doctor or other qualified healthcare provider.\"}");
        return prompt.toString();
    }
}
