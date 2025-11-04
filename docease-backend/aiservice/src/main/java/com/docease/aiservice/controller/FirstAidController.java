package com.docease.aiservice.controller;

import com.docease.aiservice.DTO.FirstAidResponseDTO;
import com.docease.aiservice.service.FirstAidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-service/first-aid")
public class FirstAidController {

    @Autowired
    private FirstAidService service;

    @GetMapping
    public ResponseEntity<FirstAidResponseDTO> search(@RequestParam String q) {
        return ResponseEntity.ok(service.search(q));
    }
}
