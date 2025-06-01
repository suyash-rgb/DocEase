package com.example.docease.DTOs;

import java.math.BigDecimal;
import java.util.List;

public class DoctorProfileUpdateDTO {
    private List<DoctorScheduleDTO> schedules;
    private BigDecimal consultationFee;
    private String profileDescription;
    private String phone;

    public List<DoctorScheduleDTO> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<DoctorScheduleDTO> schedules) {
        this.schedules = schedules;
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
}
