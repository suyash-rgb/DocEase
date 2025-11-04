package com.docease.aiservice.entity.firstaid;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "first_aid_tags")
public class FirstAidTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public FirstAidTag() {
    }

    public FirstAidTag(Long tagId, String name, String description, LocalDateTime createdAt) {
        this.tagId = tagId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
