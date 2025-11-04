package com.docease.aiservice.entity.firstaid;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "first_aid_response_groups")
public class FirstAidResponseGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private FirstAidTag tag;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public FirstAidResponseGroup() {
    }

    public FirstAidResponseGroup(Long groupId, FirstAidTag tag, String name, String description, LocalDateTime createdAt) {
        this.groupId = groupId;
        this.tag = tag;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public FirstAidTag getTag() {
        return tag;
    }

    public void setTag(FirstAidTag tag) {
        this.tag = tag;
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