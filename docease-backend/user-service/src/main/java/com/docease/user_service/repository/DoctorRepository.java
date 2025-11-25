package com.docease.user_service.repository;

import com.docease.user_service.DTO.AvailableDoctorResponse;
import com.docease.user_service.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Query(value = """
    SELECT 
        d.doctor_id AS doctorId,
        CONCAT('Dr. ', u.username) AS name,
        d.specialization AS specialization,
        COALESCE(d.clinic_name, 'Private Clinic') AS clinicName,
        d.city AS city,
        d.consultation_fee AS fee,
        COALESCE(d.rating, 4.5) AS rating,
        COALESCE(d.total_reviews, 0) AS totalReviews,
        GROUP_CONCAT(
            DATE_FORMAT(s.start_time, '%l:%i %p') 
            ORDER BY s.start_time 
            SEPARATOR ', '
        ) AS availableSlots
    FROM appointment_slots s
    JOIN doctors d ON s.doctor_id = d.doctor_id
    JOIN users u ON d.user_id = u.user_id
    WHERE d.specialization = :specialist
      AND d.city = :city
      AND DATE(s.start_time) = :date
      AND s.slot_status = 'available'
    GROUP BY d.doctor_id, u.username, d.specialization, d.clinic_name, d.city, d.consultation_fee, d.rating, d.total_reviews
    ORDER BY d.consultation_fee ASC
    """,
            nativeQuery = true)
    List<AvailableDoctorResponse> findAvailableDoctors(
            @Param("specialist") String specialist,
            @Param("city") String city,
            @Param("date") LocalDate date
    );

}
