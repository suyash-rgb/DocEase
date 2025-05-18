package com.example.docease.controllers;

import com.example.docease.DTOs.DoctorRegistrationDTO;
import com.example.docease.DTOs.LoginRequest;
import com.example.docease.services.AuthService;
import com.example.docease.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateWithMFA(loginRequest, "ADMIN");
    }

    @PostMapping("/register-doctor")
    public ResponseEntity<?> registerDoctor(@RequestBody DoctorRegistrationDTO doctorDTO) {
        return doctorService.registerDoctor(doctorDTO);
    }

    @PostMapping("/login/doctor")
    public ResponseEntity<?> loginDoctor(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateWithMFA(loginRequest, "DOCTOR");
    }

    @PostMapping("/login/patient")
    public ResponseEntity<?> loginPatient(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateWithoutMFA(loginRequest);
    }
}
