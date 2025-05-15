package com.example.docease.repositories;

import com.example.docease.entities.Patient;
import com.example.docease.entities.Role;
import com.example.docease.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository <Patient, Integer>{
    Optional<Patient> findByUser(User user);
}
