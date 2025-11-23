package com.docease.user_service.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientResponse(
        Integer patientId,
        Integer userId,
        String userName,
        String email,
        LocalDate dateOfBirth,
        String gender,
        String phone,
        String address,
        String medicalHistory,
        LocalDateTime createdAt
) {}
