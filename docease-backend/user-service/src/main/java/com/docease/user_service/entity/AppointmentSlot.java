// user-service → src/main/java/com/docease/user_service/entity/AppointmentSlot.java
package com.docease.user_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "appointment_slots")
public class AppointmentSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long slotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "slot_status", nullable = false)
    private String slotStatus = "available";

    private String source;

    @Column(name = "hold_until")
    private LocalDateTime holdUntil;

    @Column(columnDefinition = "JSON")
    private String extra; // or Map<String,Object> if using Hibernate 6+ JSON

    public AppointmentSlot() {
    }

    public AppointmentSlot(Long slotId, Doctor doctor, LocalDateTime startTime, LocalDateTime endTime, Integer durationMinutes, String slotStatus, String source, LocalDateTime holdUntil, String extra) {
        this.slotId = slotId;
        this.doctor = doctor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.slotStatus = slotStatus;
        this.source = source;
        this.holdUntil = holdUntil;
        this.extra = extra;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(String slotStatus) {
        this.slotStatus = slotStatus;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getHoldUntil() {
        return holdUntil;
    }

    public void setHoldUntil(LocalDateTime holdUntil) {
        this.holdUntil = holdUntil;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
