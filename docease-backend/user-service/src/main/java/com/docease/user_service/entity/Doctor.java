package com.docease.user_service.entity;


import jakarta.persistence.*;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Integer doctorId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String specialization;

    // CHANGE Double → BigDecimal
    @Column(name = "consultation_fee", precision = 10, scale = 2, nullable = false)
    private BigDecimal consultationFee = BigDecimal.ZERO;

    @Column(name = "profile_description")
    private String profileDescription;

    private String phone;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "medical_license")
    private String medicalLicense;

    public Doctor() {
    }

    public Doctor(Integer doctorId, User user, String specialization, BigDecimal consultationFee, String profileDescription, String phone, String imageUrl, String medicalLicense) {
        this.doctorId = doctorId;
        this.user = user;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.profileDescription = profileDescription;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.medicalLicense = medicalLicense;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMedicalLicense() {
        return medicalLicense;
    }

    public void setMedicalLicense(String medicalLicense) {
        this.medicalLicense = medicalLicense;
    }
}
