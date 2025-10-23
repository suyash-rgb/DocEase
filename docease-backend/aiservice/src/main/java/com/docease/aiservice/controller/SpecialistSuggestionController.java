package com.docease.aiservice.controller;

import com.docease.aiservice.DTO.SymptomRequest;
import com.docease.aiservice.service.SpecialistSuggestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-service/specialist-suggestion")
public class SpecialistSuggestionController {

    @Autowired
    private SpecialistSuggestionService specialistSuggestionService;

    @PostMapping("/suggest")
    public ResponseEntity<String> suggestSpecialist(@Valid @RequestBody SymptomRequest symptomRequest) {
        String suggestion = specialistSuggestionService.suggestSpecialist(symptomRequest);
        return ResponseEntity.ok(suggestion);
    }
}
