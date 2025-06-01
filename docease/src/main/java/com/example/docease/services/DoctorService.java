package com.example.docease.services;

import com.example.docease.DTOs.DoctorProfileUpdateDTO;
import com.example.docease.DTOs.DoctorRegistrationDTO;
import com.example.docease.entities.Doctor;
import com.example.docease.entities.Role;
import com.example.docease.entities.User;
import com.example.docease.repositories.DoctorRepository;
import com.example.docease.repositories.RoleRepository;
import com.example.docease.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        //doctor.setAvailability(doctorDTO.getAvailability());
        doctor.setConsultationFee(doctorDTO.getConsultationFee());
        doctor.setProfileDescription(doctorDTO.getProfileDescription());

        doctorRepository.save(doctor);

        return ResponseEntity.ok(Map.of(
                "message", "Doctor registered successfully!",
                "doctorId", doctor.getDoctorId()
        ));
    }

    public ResponseEntity<?> updateDoctorProfile(DoctorProfileUpdateDTO updateDTO, String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null || !user.getRole().getName().equals("DOCTOR")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized action.");
        }

        Doctor doctor = doctorRepository.findByUser(user).orElse(null);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor profile not found.");
        }


//        if (updateDTO.getAvailability() != null) {
//            doctor.setAvailability(updateDTO.getAvailability());
//        }
        if (updateDTO.getConsultationFee() != null) {
            doctor.setConsultationFee(updateDTO.getConsultationFee());
        }
        if (updateDTO.getProfileDescription() != null) {
            doctor.setProfileDescription(updateDTO.getProfileDescription());
        }
        if (updateDTO.getPhone() != null) {
            doctor.setPhone(updateDTO.getPhone());
        }

        doctorRepository.save(doctor);

        return ResponseEntity.ok(Map.of("message", "Profile updated successfully!"));
    }
}
