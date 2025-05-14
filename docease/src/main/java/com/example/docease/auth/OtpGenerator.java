package com.example.docease.auth;

import java.security.SecureRandom;

public class OtpGenerator {
    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}

