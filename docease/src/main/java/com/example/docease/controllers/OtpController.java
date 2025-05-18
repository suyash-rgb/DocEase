package com.example.docease.controllers;

import com.example.docease.entities.User;
import com.example.docease.jwt.JwtUtil;
import com.example.docease.repositories.UserRepository;
import com.example.docease.services.OtpService;
import com.example.docease.util.OtpGenerator;
import com.example.docease.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class OtpController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public OtpController(EmailService emailService, OtpService otpService) {
        this.emailService = emailService;
        this.otpService = otpService;
    }


    @GetMapping("/send-otp")
    public String sendOtp(@RequestParam String email) throws Exception {
        String otp = otpService.generateOtp(email);
        emailService.sendOtp(email, otp);
        return "OTP sent to: " + email;
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        if (otpService.validateOtp(email, otp)) {

            // Find user and generate JWT token
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().getName());
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
        }
    }

}
