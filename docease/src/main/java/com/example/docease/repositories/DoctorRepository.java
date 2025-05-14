package com.example.docease.repositories;

import com.example.docease.entities.Doctor;
import com.example.docease.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository <Doctor, Long>{

    List<Doctor> findBySpecialization(String specialization);

}
