package com.docease.aiservice.entity.firstaid;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// FirstAidPattern.java
@Entity
@Table(name = "first_aid_patterns")
public class FirstAidPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pattern_id")
    private Long patternId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private FirstAidResponseGroup group;

    @Column(name = "pattern", columnDefinition = "TEXT", nullable = false)
    private String pattern;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public FirstAidPattern() {
    }

    public FirstAidPattern(Long patternId, FirstAidResponseGroup group, String pattern, LocalDateTime createdAt) {
        this.patternId = patternId;
        this.group = group;
        this.pattern = pattern;
        this.createdAt = createdAt;
    }

    public Long getPatternId() {
        return patternId;
    }

    public void setPatternId(Long patternId) {
        this.patternId = patternId;
    }

    public FirstAidResponseGroup getGroup() {
        return group;
    }

    public void setGroup(FirstAidResponseGroup group) {
        this.group = group;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
