package com.example.docease.controllers;

import com.example.docease.entities.HealthTip;
import com.example.docease.repositories.HealthTipRepository;
import com.example.docease.services.HealthTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/health-tips")
public class HealthTipController {

    @Autowired
    private HealthTipService healthTipService;

    private HealthTipRepository healthTipRepository;

    public HealthTipController(HealthTipService healthTipService, HealthTipRepository healthTipRepository) {
        this.healthTipService = healthTipService;
        this.healthTipRepository = healthTipRepository;
    }

    @GetMapping("/generate-health-tip")
    public ResponseEntity<HealthTip> generateHealthTip() {
        String tip = healthTipService.generateHealthTip();

        HealthTip healthTip = new HealthTip();
        healthTip.setTitle("Daily Health Tip");
        healthTip.setMessage(tip);
        healthTip.setTipDate(LocalDateTime.now());

        healthTipRepository.save(healthTip);

        return ResponseEntity.ok(healthTip);
    }
}