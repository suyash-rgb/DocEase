package com.docease.aiservice.DTO;

import java.math.BigDecimal;
import java.util.List;

public record AvailableDoctorResponse(
        Integer doctorId,
        String name,
        String specialization,
        String clinicName,
        String city,
        BigDecimal fee,
        Double rating,
        Integer totalReviews,
        String availableSlots  // e.g., ["10:00 AM", "04:30 PM"]
) {}
