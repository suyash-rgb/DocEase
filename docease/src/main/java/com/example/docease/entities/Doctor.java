package com.example.docease.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doctorId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String specialization;
    private String availability;
    private BigDecimal consultationFee;
    private String profileDescription;
    private String phone;

    private String imageUrl;

    private String medicalLicense;

    public Doctor() {
    }

    public Doctor(Integer doctorId, User user, String specialization, String availability, BigDecimal consultationFee, String profileDescription, String phone, String imageUrl, String medicalLicense) {
        this.doctorId = doctorId;
        this.user = user;
        this.specialization = specialization;
        this.availability = availability;
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

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
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