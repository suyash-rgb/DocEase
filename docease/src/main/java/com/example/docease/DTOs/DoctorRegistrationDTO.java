package com.example.docease.DTOs;

import java.math.BigDecimal;
import java.util.List;

public class DoctorRegistrationDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String specialization;
    private List<DoctorScheduleDTO> schedules;
    private BigDecimal consultationFee;
    private String profileDescription;

    private String medicalLicense;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<DoctorScheduleDTO> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<DoctorScheduleDTO> schedules) {
        this.schedules = schedules;
    }

    public String getMedicalLicense() {
        return medicalLicense;
    }

    public void setMedicalLicense(String medicalLicense) {
        this.medicalLicense = medicalLicense;
    }
}