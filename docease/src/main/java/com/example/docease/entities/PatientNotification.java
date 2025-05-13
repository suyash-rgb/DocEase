package com.example.docease.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "patient_notifications")
public class PatientNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "tip_id")
    private HealthTip tip;

    private String message;
    private Boolean isRead;
    private Date createdAt;
}
