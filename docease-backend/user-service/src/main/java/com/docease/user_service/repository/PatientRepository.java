package com.docease.user_service.repository;

import com.docease.user_service.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    //Optional<Patient> findByEmail(String email);
    Optional<Patient> findByPhone(String phone);
    //boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
