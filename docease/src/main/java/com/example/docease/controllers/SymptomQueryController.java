package com.example.docease.controllers;

import com.example.docease.DTOs.SymptomQueryDTO;
import com.example.docease.services.SymptomQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/symptom-query")
public class SymptomQueryController {

    @Autowired
    private SymptomQueryService symptomQueryService;


    // ✅ API to recommend a specialist based on symptoms
    @PostMapping("/recommend-specialist")
    public ResponseEntity<?> recommendSpecialist(@RequestBody SymptomQueryDTO symptomDTO, Principal principal) {
        return symptomQueryService.processSymptomQuery(symptomDTO, principal.getName());
    }

    // ✅ Fetch past symptom queries for a patient
    @GetMapping("/history")
    public ResponseEntity<?> getPatientSymptomHistory(Principal principal) {
        return symptomQueryService.getSymptomQueryHistory(principal.getName());
    }
}
