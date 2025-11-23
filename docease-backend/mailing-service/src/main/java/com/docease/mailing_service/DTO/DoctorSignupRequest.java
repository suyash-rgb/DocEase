package com.docease.mailing_service.DTO;

public record DoctorSignupRequest(
        String fullName,
        String email,
        String phone,
        String specialization,
        String clinicName,
        String password
) {}