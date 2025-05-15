package com.example.docease.repositories;

import com.example.docease.entities.HealthTip;
import com.example.docease.entities.Patient;
import com.example.docease.entities.PatientNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientNotificationRepository extends JpaRepository<PatientNotification, Integer> {

    List<PatientNotification> findByPatient(Patient patient);

}
