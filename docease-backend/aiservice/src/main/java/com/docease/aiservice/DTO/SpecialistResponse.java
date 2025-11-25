package com.docease.aiservice.DTO;

import java.util.List;

public record SpecialistResponse(
        String suggestedSpecialist,
        String city,
        List<AvailableDoctorResponse> doctorsAvailableTomorrow
) {}
