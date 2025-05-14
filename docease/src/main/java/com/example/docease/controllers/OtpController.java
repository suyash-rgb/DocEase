package com.example.docease.controllers;

import com.example.docease.auth.OtpGenerator;
import com.example.docease.services.EmailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class OtpController {

    private final EmailService emailService;

    public OtpController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send-otp")
    public String sendOtp(@RequestParam String email) throws Exception {
        String otp = OtpGenerator.generateOtp();
        emailService.sendOtp(email, otp);
        return "OTP sent to: " + email;
    }
}
