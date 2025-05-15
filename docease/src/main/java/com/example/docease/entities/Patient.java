package com.example.docease.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String medicalHistory;
    private Date dateOfBirth;
    private String gender;
    private String phone;
    private String address;
}
