package com.docease.user_service.DTO;

import java.math.BigDecimal;

public interface AvailableDoctorResponse {
    Integer getDoctorId();
    String getName();
    String getSpecialization();
    String getClinicName();
    String getCity();
    BigDecimal getFee();
    Double getRating();
    Integer getTotalReviews();
    String getAvailableSlots();
}
