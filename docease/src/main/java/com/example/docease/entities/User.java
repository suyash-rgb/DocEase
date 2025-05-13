package com.example.docease.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    private Boolean mfaEnabled;
    private String mfaSecret;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}