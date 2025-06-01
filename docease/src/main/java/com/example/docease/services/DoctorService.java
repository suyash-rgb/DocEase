package com.example.docease.services;

import com.example.docease.DTOs.DoctorProfileUpdateDTO;
import com.example.docease.DTOs.DoctorRegistrationDTO;
import com.example.docease.entities.Doctor;
import com.example.docease.entities.DoctorSchedule;
import com.example.docease.entities.Role;
import com.example.docease.entities.User;
import com.example.docease.repositories.DoctorRepository;
import com.example.docease.repositories.DoctorScheduleRepository;
import com.example.docease.repositories.RoleRepository;
import com.example.docease.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DoctorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> registerDoctor(DoctorRegistrationDTO doctorDTO) {
        // ✅ Check if username/email already exists
        if (userRepository.existsByUsername(doctorDTO.getUsername()) || userRepository.existsByEmail(doctorDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already in use.");
        }

        // ✅ Retrieve "DOCTOR" role
        Role doctorRole = roleRepository.findByName("DOCTOR").orElseThrow(
                () -> new RuntimeException("Role not found!")
        );

        // ✅ Create User entry
        User user = new User();
        user.setUsername(doctorDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(doctorDTO.getPassword())); // Secure hashing
        user.setEmail(doctorDTO.getEmail());
        user.setRole(doctorRole);

        userRepository.save(user);

        // ✅ Create Doctor entry
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(doctorDTO.getSpecialization());
        doctor.setConsultationFee(doctorDTO.getConsultationFee());
        doctor.setProfileDescription(doctorDTO.getProfileDescription());
        doctor.setMedicalLicense(doctorDTO.getMedicalLicense()); // ✅ Added Medical License Field

        doctorRepository.save(doctor);

        // ✅ Store Doctor Schedules (Replacing Availability)
        List<DoctorSchedule> schedules = doctorDTO.getSchedules().stream()
                .map(dto -> new DoctorSchedule(doctor, dto.getDayOfWeek(), dto.getStartTime(), dto.getEndTime()))
                .toList();

        doctorScheduleRepository.saveAll(schedules);

        return ResponseEntity.ok(Map.of(
                "message", "Doctor registered successfully!",
                "doctorId", doctor.getDoctorId()
        ));
    }

    public ResponseEntity<?> updateDoctorProfile(DoctorProfileUpdateDTO profileDTO, String username) {
        Doctor doctor = doctorRepository.findByUser_Username(username).orElse(null);
        if (doctor == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Doctor not found.");

        // ✅ Update profile details
        doctor.setConsultationFee(profileDTO.getConsultationFee());
        doctor.setProfileDescription(profileDTO.getProfileDescription());
        doctor.setPhone(profileDTO.getPhone());

        // ✅ Update schedules (bulk delete & insert new ones)
        doctorScheduleRepository.deleteByDoctor(doctor);
        List<DoctorSchedule> newSchedules = profileDTO.getSchedules().stream()
                .map(dto -> new DoctorSchedule(doctor, dto.getDayOfWeek(), dto.getStartTime(), dto.getEndTime()))
                .toList();
        doctorScheduleRepository.saveAll(newSchedules);

        doctorRepository.save(doctor);
        return ResponseEntity.ok("Profile updated successfully!");
    }
}
