package com.example.docease.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_tips")
public class HealthTip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tipId;

    private String title;
    private String message;
    private LocalDateTime tipDate;

    public HealthTip() {
    }

    public HealthTip(Integer tipId, String title, String message, LocalDateTime tipDate) {
        this.tipId = tipId;
        this.title = title;
        this.message = message;
        this.tipDate = tipDate;
    }

    public Integer getTipId() {
        return tipId;
    }

    public void setTipId(Integer tipId) {
        this.tipId = tipId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTipDate() {
        return tipDate;
    }

    public void setTipDate(LocalDateTime tipDate) {
        this.tipDate = tipDate;
    }
}