package com.example.docease.controllers;

import com.example.docease.services.OtpService;
import com.example.docease.util.OtpGenerator;
import com.example.docease.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class OtpController {

    private final EmailService emailService;
    private final OtpService otpService;

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
            otpService.invalidateOtp(email);
            return ResponseEntity.ok("OTP verification successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
        }
    }

}
