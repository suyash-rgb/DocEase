package com.docease.user_service.controller;

import com.docease.user_service.DTO.DoctorRegistrationDTO;
import com.docease.user_service.entity.Doctor;
import com.docease.user_service.exception.DuplicateEmailException;
import com.docease.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Doctor> register(@Valid @RequestBody DoctorRegistrationDTO dto) {
        try {
            Doctor doctor = userService.registerDoctor(dto);
            return ResponseEntity.ok(doctor);
        } catch (DuplicateEmailException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body((Doctor) error);
        }
    }

}
