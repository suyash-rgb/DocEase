package com.example.docease.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String specialization;
    private String availability;
    private BigDecimal consultationFee;
    private String profileDescription;
    private String phone;
}