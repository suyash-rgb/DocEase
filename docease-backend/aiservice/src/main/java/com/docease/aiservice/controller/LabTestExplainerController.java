package com.docease.aiservice.controller;
import com.docease.aiservice.service.LabTestExplainerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ai-service/lab-test-explainer")
public class LabTestExplainerController {

    private final LabTestExplainerService labTestExplainerService;

    public LabTestExplainerController(LabTestExplainerService labTestExplainerService) {
        this.labTestExplainerService = labTestExplainerService;
    }

    @PostMapping("/explain")
    public ResponseEntity<String> explainLabTest(@RequestPart("image") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Image file is required\"}");
        }
        String explanation = labTestExplainerService.explainLabTest(image);
        return ResponseEntity.ok(explanation);
    }
}
