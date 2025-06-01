package com.example.docease.services;

import com.example.docease.util.OtpGenerator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, Long> otpExpiry = new HashMap<>();

    public String generateOtp(String email) {
        String otp = OtpGenerator.generateOtp();
        otpStorage.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() + (10 * 60 * 1000)); // Expiry in 10 mins
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) return false;
        if (System.currentTimeMillis() > otpExpiry.get(email)) return false; // Expired OTP
        return otp.equals(otpStorage.get(email)); //boolean result
    }

    public void invalidateOtp(String email) {
        otpStorage.remove(email);
        otpExpiry.remove(email);
    }
}
