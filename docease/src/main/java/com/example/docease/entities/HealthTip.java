package com.example.docease.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "health_tips")
public class HealthTip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tipId;

    private String title;
    private String message;
    private Date tipDate;
}