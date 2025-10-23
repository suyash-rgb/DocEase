package com.docease.aiservice.controller;

import com.docease.aiservice.DTO.MedicineRequest;
import com.docease.aiservice.service.MedicationInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-service/medication-info")
public class MedicineInfoController {

    @Autowired
    private MedicationInfoService medicationInfoService;

    @PostMapping("/get")
    public ResponseEntity<String> getMedicationInfo(@Valid @RequestBody MedicineRequest medicineRequest) {
        String info = medicationInfoService.getMedicationInfo(medicineRequest);
        return ResponseEntity.ok(info);
    }

}
