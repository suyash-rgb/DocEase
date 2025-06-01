package com.example.docease.entities;

import com.example.docease.util.JsonConverter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "symptom_queries")
public class SymptomQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer queryId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(columnDefinition = "JSON") // ✅ Stores symptoms as a JSON array in MySQL
    @Convert(converter = JsonConverter.class) // Custom converter for JSON handling
    private JsonNode symptoms;

    private String recommendedSpecialist;
    private String guidance;
    private LocalDateTime queryDate;

    public SymptomQuery() {
    }

    public SymptomQuery(Integer queryId, Patient patient, JsonNode symptoms, String recommendedSpecialist, String guidance, LocalDateTime queryDate) {
        this.queryId = queryId;
        this.patient = patient;
        this.symptoms = symptoms;
        this.recommendedSpecialist = recommendedSpecialist;
        this.guidance = guidance;
        this.queryDate = queryDate;
    }

    public Integer getQueryId() {
        return queryId;
    }

    public void setQueryId(Integer queryId) {
        this.queryId = queryId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public JsonNode getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(JsonNode symptoms) {
        this.symptoms = symptoms;
    }

    public String getRecommendedSpecialist() {
        return recommendedSpecialist;
    }

    public void setRecommendedSpecialist(String recommendedSpecialist) {
        this.recommendedSpecialist = recommendedSpecialist;
    }

    public String getGuidance() {
        return guidance;
    }

    public void setGuidance(String guidance) {
        this.guidance = guidance;
    }

    public LocalDateTime getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(LocalDateTime queryDate) {
        this.queryDate = queryDate;
    }
}
