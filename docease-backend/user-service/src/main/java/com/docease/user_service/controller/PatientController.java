package com.docease.user_service.controller;

// src/main/java/com/docease/user_service/controller/PatientController.java

import com.docease.user_service.DTO.PatientRegistrationDTO;
import com.docease.user_service.DTO.PatientResponse;
import com.docease.user_service.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * POST /api/patient/register
     * Registers a new patient
     */
    @PostMapping("/register")
    public ResponseEntity<PatientResponse> register(
            @Valid @RequestBody PatientRegistrationDTO request
    ) {
        PatientResponse response = patientService.register(request);
        return ResponseEntity.ok(response);
    }
}
