package com.docease.aiservice.entity.firstaid;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// FirstAidResponse.java
@Entity
@Table(name = "first_aid_responses")
public class FirstAidResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Long responseId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private FirstAidResponseGroup group;

    @Column(name = "response", columnDefinition = "TEXT", nullable = false)
    private String response;

    @Column(name = "step_order")
    private int stepOrder;

    @Column(name = "is_fallback")
    private boolean isFallback;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public FirstAidResponse() {
    }

    public FirstAidResponse(Long responseId, FirstAidResponseGroup group, String response, int stepOrder, boolean isFallback, LocalDateTime createdAt) {
        this.responseId = responseId;
        this.group = group;
        this.response = response;
        this.stepOrder = stepOrder;
        this.isFallback = isFallback;
        this.createdAt = createdAt;
    }

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public FirstAidResponseGroup getGroup() {
        return group;
    }

    public void setGroup(FirstAidResponseGroup group) {
        this.group = group;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(int stepOrder) {
        this.stepOrder = stepOrder;
    }

    public boolean isFallback() {
        return isFallback;
    }

    public void setFallback(boolean fallback) {
        isFallback = fallback;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
