package com.example.docease.repositories;

import com.example.docease.entities.Doctor;
import com.example.docease.entities.Invoice;
import com.example.docease.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByPatient(Patient patient);
    List<Invoice> findByDoctor(Doctor doctor);
    List<Invoice> findByPaymentStatus(String paymentStatus);

}
