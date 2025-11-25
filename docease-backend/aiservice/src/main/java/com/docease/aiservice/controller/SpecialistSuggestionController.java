package com.docease.aiservice.controller;

import com.docease.aiservice.DTO.SpecialistResponse;
import com.docease.aiservice.DTO.SymptomRequest;
import com.docease.aiservice.service.SpecialistSuggestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai-service/specialist-suggestion")
public class SpecialistSuggestionController {

    @Autowired
    private SpecialistSuggestionService specialistSuggestionService;

    @PostMapping("/suggest")
    public SpecialistResponse suggestSpecialist(
            @Valid @RequestBody SymptomRequest symptomRequest,
            @RequestHeader(value = "User-City", defaultValue = "Mumbai") String city) {

        return specialistSuggestionService.suggestSpecialistDoctors(symptomRequest, city);
    }
}
