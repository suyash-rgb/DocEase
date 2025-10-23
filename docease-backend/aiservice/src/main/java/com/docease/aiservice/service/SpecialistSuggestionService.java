package com.docease.aiservice.service;

import com.docease.aiservice.DTO.SymptomRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class SpecialistSuggestionService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    private MessageLoggingService messageLoggingService;

    public SpecialistSuggestionService(WebClient.Builder webClientBuilder) {
        this.webClient = WebClient.builder().build();
    }

    public String suggestSpecialist(SymptomRequest symptomRequest){

        //Build the prompt
        String prompt = buildPrompt(symptomRequest);

        //Craft a reuqest
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        //Do request and get response
        String response = webClient.post()
                .uri(geminiApiUrl+geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //Extract response
        String suggestion = extractResponseContent(response);

        //Log the conversation
        messageLoggingService.logConversation(symptomRequest.getSymptoms(), suggestion);

        return suggestion;
    }

    private String extractResponseContent(String response){
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

            //Remove markdown code fences and trim whitespaces
            content = content.replaceAll("^```json\\n|\\n```$", "").trim();
            return content;
        } catch(Exception e){
            return "{\"error\": \"Error processing request: " + e.getMessage() + "\"}";
        }
    }

    private String buildPrompt(SymptomRequest symptomRequest){
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a concise JSON response suggesting medical specialists for the following symptoms: ")
                .append(symptomRequest.getSymptoms())
                .append("Provide a list of appropriate specialists (e.g., ENT specialist, general physician, endocrinologist, rheumatologist) based on the symptoms. Structure the response as JSON with a 'specialists' array. Keep the response under 50 words. Example format: {\"specialists\": [\"ENT specialist\", \"General physician\"]}");
        return prompt.toString();
    }






}
