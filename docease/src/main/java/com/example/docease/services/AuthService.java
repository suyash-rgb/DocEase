package com.example.docease.services;

import com.example.docease.DTOs.LoginRequest;
import com.example.docease.entities.User;
import com.example.docease.jwt.JwtUtil;
import com.example.docease.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;  // Sends OTP

    @Autowired
    private OtpService otpService;  // Stores & validates OTP

    @Autowired
    private JwtUtil jwtUtil;


    public ResponseEntity<?> authenticateWithMFA(LoginRequest loginRequest, String role) {
        User user = userRepository.findByUsernameAndRole_Name(loginRequest.getUsername(), role).orElse(null);
        if (user == null || !user.getPasswordHash().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String otp = otpService.generateOtp(user.getEmail());

        try {
            emailService.sendOtp(user.getEmail(), otp);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP. Please try again.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }

        return ResponseEntity.ok("OTP sent to email. Please verify.");
    }

    public ResponseEntity<?> authenticateWithoutMFA(LoginRequest loginRequest) {
        User user = userRepository.findByUsernameAndRole_Name(loginRequest.getUsername(), "PATIENT").orElse(null);
        if (user == null || !user.getPasswordHash().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername(), "PATIENT");
        return ResponseEntity.ok(Map.of("token", token));
    }

    public ResponseEntity<?> initiateForgotPassword(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            User user = userRepository.findByUsername(username).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            // Generate temporary password reset token
            String resetToken = jwtUtil.generatePasswordResetToken(username);

            // Send email with reset instructions
            emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

            return ResponseEntity.ok(Map.of("message", "Password reset link sent to email."));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token.");
        }
    }
}
