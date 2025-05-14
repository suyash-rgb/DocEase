package com.example.docease.repositories;

import com.example.docease.entities.Patient;
import com.example.docease.entities.SymptomQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymptomQueryRepository extends JpaRepository<SymptomQuery, Long> {
    List<SymptomQuery> findByPatient(Patient patient);

}
