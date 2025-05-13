package com.example.docease.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String action;
    private String details;
    private Date logDate;
}