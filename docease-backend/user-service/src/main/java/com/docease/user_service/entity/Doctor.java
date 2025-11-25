// user-service → src/main/java/com/docease/user_service/entity/Doctor.java
package com.docease.user_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Integer doctorId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String specialization;

    @Column(name = "consultation_fee", precision = 10, scale = 2)
    private BigDecimal consultationFee = BigDecimal.ZERO;

    @Column(name = "profile_description", columnDefinition = "TEXT")
    private String profileDescription;

    private String phone;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "medical_license")
    private String medicalLicense;

    // ======== NEW COLUMNS ADDED BELOW ========

    @Column(nullable = false, length = 100)
    private String city = "Mumbai";  // default city

    @Column(name = "clinic_name", length = 255)
    private String clinicName = "Private Clinic";

    @Column()
    private Double rating = 4.50;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "experience_years")
    private Integer experienceYears = 0;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppointmentSlot> appointmentSlots;

    public List<AppointmentSlot> getAppointmentSlots() { return appointmentSlots; }
    public void setAppointmentSlots(List<AppointmentSlot> appointmentSlots) { this.appointmentSlots = appointmentSlots; }

    // =========================================

    public Doctor() {}

    // Getters & Setters for new fields
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }

    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }

    // Existing getters/setters (keep all)
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public BigDecimal getConsultationFee() { return consultationFee; }
    public void setConsultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; }

    public String getProfileDescription() { return profileDescription; }
    public void setProfileDescription(String profileDescription) { this.profileDescription = profileDescription; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getMedicalLicense() { return medicalLicense; }
    public void setMedicalLicense(String medicalLicense) { this.medicalLicense = medicalLicense; }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor doctor)) return false;
        return Objects.equals(doctorId, doctor.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", name='Dr. " + (user != null ? user.getUsername() : "N/A") + '\'' +
                ", specialization='" + specialization + '\'' +
                ", city='" + city + '\'' +
                ", clinicName='" + clinicName + '\'' +
                ", fee=" + consultationFee +
                ", rating=" + rating +
                '}';
    }
}