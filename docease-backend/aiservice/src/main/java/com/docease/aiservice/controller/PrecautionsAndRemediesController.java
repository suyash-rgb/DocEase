package com.docease.aiservice.controller;

import com.docease.aiservice.DTO.SymptomRequest;
import com.docease.aiservice.service.PrecautionsAndRemediesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-service/symptom-precautions-and-remedies")
public class PrecautionsAndRemediesController {

    @Autowired
    private PrecautionsAndRemediesService precautionsAndRemediesService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateAdvice(@Valid @RequestBody SymptomRequest symptomRequest){
        String advice = precautionsAndRemediesService.generateAdvice(symptomRequest);
        return ResponseEntity.ok(advice);
    }

}
