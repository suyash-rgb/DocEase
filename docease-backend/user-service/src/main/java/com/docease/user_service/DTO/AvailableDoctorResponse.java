package com.docease.user_service.DTO;

import java.math.BigDecimal;
import jakarta.persistence.*;

@SqlResultSetMapping(
        name = "AvailableDoctorMapping",
        classes = @ConstructorResult(
                targetClass = AvailableDoctorResponse.class,
                columns = {
                        @ColumnResult(name = "doctorId", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "specialization", type = String.class),
                        @ColumnResult(name = "clinicName", type = String.class),
                        @ColumnResult(name = "city", type = String.class),
                        @ColumnResult(name = "fee", type = BigDecimal.class),
                        @ColumnResult(name = "rating", type = Double.class),
                        @ColumnResult(name = "totalReviews", type = Integer.class),
                        @ColumnResult(name = "availableSlots", type = String.class)
                }
        )
)
public record AvailableDoctorResponse(
        Integer doctorId,
        String name,
        String specialization,
        String clinicName,
        String city,
        BigDecimal fee,
        Double rating,
        Integer totalReviews,
        String availableSlots
) {}