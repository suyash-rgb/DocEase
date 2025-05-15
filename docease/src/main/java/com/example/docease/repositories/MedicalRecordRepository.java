package com.example.docease.repositories;

import com.example.docease.entities.Doctor;
import com.example.docease.entities.MedicalRecord;
import com.example.docease.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository <MedicalRecord, Integer>{
    List<MedicalRecord> findByPatient(Patient patient);
    List<MedicalRecord> findByDoctor(Doctor doctor);

}
