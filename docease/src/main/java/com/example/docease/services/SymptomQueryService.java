package com.example.docease.services;

import com.example.docease.DTOs.SymptomQueryDTO;
import com.example.docease.entities.User;
import com.example.docease.entities.Doctor;
import com.example.docease.entities.Patient;
import com.example.docease.entities.SymptomQuery;
import com.example.docease.repositories.DoctorRepository;
import com.example.docease.repositories.PatientRepository;
import com.example.docease.repositories.SymptomQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SymptomQueryService {

    private final WebClient webClient;

    @Autowired
    private SymptomQueryRepository symptomQueryRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Value("${GEMINI_API_URL}")
    private String geminiApiUrl;

    @Value("${GEMINI_API_KEY}")
    private String geminiApiKey;


    //best approach to inject WebClient dependency is constructor injection
    public SymptomQueryService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(geminiApiUrl).build();
    }

    public ResponseEntity<?> processSymptomQuery(SymptomQueryDTO symptomDTO, String username) {
        Patient patient = patientRepository.findByUser_Username(username).orElse(null);
        if (patient == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Patient not found.");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode symptomsJson = objectMapper.valueToTree(symptomDTO.getSymptoms()); // ✅ Convert List to JSON

        // Get AI recommendation (Gemini API)
        String response = getGeminiRecommendation(symptomDTO.getSymptoms());
        JsonNode jsonResponse;
        try {
            jsonResponse = objectMapper.readTree(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing AI response.");
        }

        String recommendedSpecialist = jsonResponse.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
        String guidance = "AI-suggested home remedies for your symptoms: " + recommendedSpecialist;

        // ✅ Store symptom query with JSON-formatted symptoms
        SymptomQuery symptomQuery = new SymptomQuery();
        symptomQuery.setPatient(patient);
        symptomQuery.setSymptoms(symptomsJson);
        symptomQuery.setRecommendedSpecialist(recommendedSpecialist);
        symptomQuery.setGuidance(guidance);
        symptomQuery.setQueryDate(LocalDateTime.now());
        symptomQueryRepository.save(symptomQuery);

        return ResponseEntity.ok(Map.of(
                "recommendedSpecialist", recommendedSpecialist,
                "guidance", guidance,
                "availableDoctors", doctorRepository.findBySpecialization(recommendedSpecialist).stream()
                        .map(doctor -> doctor.getUser().getUsername()).toList()
        ));
    }

    private String getGeminiRecommendation(List<String> symptoms) {
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{Map.of("parts", new Object[]{Map.of("text", String.join(", ", symptoms))})}
        );

        try {
            return webClient.post()
                    .uri(geminiApiUrl + geminiApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            return "{\"error\": \"Failed to fetch specialist recommendation\"}";
        }
    }

    //to retrieve past symptom queries and recommendations
    public ResponseEntity<?> getSymptomQueryHistory(String username) {
        Patient patient = patientRepository.findByUser_Username(username).orElse(null);
        if (patient == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Patient not found.");

        List<SymptomQuery> history = symptomQueryRepository.findByPatient(patient);
        return ResponseEntity.ok(history);
    }
}
