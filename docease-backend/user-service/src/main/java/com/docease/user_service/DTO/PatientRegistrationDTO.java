package com.docease.user_service.DTO;

import java.time.LocalDate;

public record PatientRegistrationDTO(
        String username,
        String email,
        String password,
        LocalDate dateOfBirth,
        String gender,
        String phone,
        String address,
        String medicalHistory
) {}
