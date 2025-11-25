package com.docease.aiservice.service;

import com.docease.aiservice.DTO.SymptomRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.inject.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced; //comes from org.srpingframwork.cloud.*
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class PrecautionsAndRemediesService {

    private final WebClient externalWebClient;

    private final WebClient.Builder internalWebClientBuilder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${app.logging.endpoint}")
    private String loggingUrl;

    @Autowired
    private MessageLoggingService messageLoggingService;

    // Constructor injection
    public PrecautionsAndRemediesService(
            @Qualifier("externalWebClientBuilder") WebClient.Builder externalBuilder,
            @Qualifier("internalWebClientBuilder") WebClient.Builder internalBuilder) {
        this.externalWebClient = externalBuilder.build();
        this.internalWebClientBuilder = internalBuilder;
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
        String response = externalWebClient.post()
                .uri(geminiApiUrl+geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //Extract response
        String advice = extractResposneContent(response);

        //Log the conversation
        //messageLoggingService.logConversation(symptomRequest.getSymptoms(), advice);

        //LOG ASYNC
        logToCentralServiceAsync(symptomRequest.getSymptoms(), advice);

        return advice;
    }

    private void logToCentralServiceAsync(String input, String output) {
        Map<String, String> logRequest = Map.of(
                "input", input != null ? input : "",
                "output", output != null ? output : "No response generated"
        );

        internalWebClientBuilder.build()
                .post()
                .uri(loggingUrl)
                .header("Content-Type", "application/json")
                .bodyValue(logRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(throwable -> {
                    // Never break user experience
                    System.err.println("Failed to log to messaging-logging-service: " + throwable.getMessage());
                    return Mono.empty();
                })
                .subscribe(); // Fire-and-forget
    }

    private String extractResposneContent(String response){
        if(response==null||response.isBlank()){
            return "No response from AU model.";
        }
        try{
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String content = textNode.asText("AI response could not be parsed.");

            // Remove Markdown code fences and trim whitespace
            return content = content.replaceAll("^```json\\n|\\n```$", "").trim();
        } catch (JsonMappingException e) {
            return "Error processing request: " + e.getMessage();
        } catch (JsonProcessingException e) {
            return "Error processing request: " + e.getMessage();
        }  catch (Exception e) {
            return "Error parsing AI response: " + e.getMessage();
        }
    }

    private String buildPrompt(SymptomRequest symptomRequest) {
        return new StringBuilder()
                .append("Generate a concise JSON response with precautions and home remedies for the following symptoms: ")
                .append(symptomRequest.getSymptoms())
                .append(". Include only precautions and remedies specific to the listed symptoms. Exclude general precautions. Structure the response as JSON with a 'symptoms' object containing 'precautions' and 'remedies' arrays for each symptom, and a 'disclaimer' field at the end. Keep under 250 words. Example format: ")
                .append("{\"symptoms\": {\"fever\": {\"precautions\": [], \"remedies\": []}}, \"disclaimer\": \"This is not medical advice...\"}")
                .toString();
    }
}
