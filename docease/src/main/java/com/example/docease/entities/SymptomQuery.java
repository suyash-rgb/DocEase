package com.example.docease.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "symptom_queries")
public class SymptomQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer queryId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private String symptoms;
    private String recommendedSpecialist;
    private String guidance;
    private LocalDateTime queryDate;
}
