package com.docease.user_service.service;


import com.docease.user_service.DTO.PatientRegistrationDTO;
import com.docease.user_service.DTO.PatientResponse;
import com.docease.user_service.entity.Patient;
import com.docease.user_service.entity.Role;
import com.docease.user_service.entity.User;
import com.docease.user_service.repository.PatientRepository;
import com.docease.user_service.repository.RoleRepository;
import com.docease.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PatientService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PatientResponse register(PatientRegistrationDTO request) {

        Objects.requireNonNull(request, "Request cannot be null");
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.username() == null || request.username().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // ---------- 2. Duplicate check ----------
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already registered");
        }

        // ---------- 3. Load PATIENT role ----------
        Role patientRole = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new RuntimeException("Role PATIENT not found"));

        // ---------- 4. Create User ----------
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(patientRole);

        user = userRepository.save(user);   // userId is now populated

        // ---------- 5. Create Patient ----------
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setDateOfBirth(request.dateOfBirth());
        patient.setGender(request.gender());
        patient.setPhone(request.phone());
        patient.setAddress(request.address());
        patient.setMedicalHistory(request.medicalHistory());

        patient = patientRepository.save(patient);

        // ---------- 6. Return DTO ----------
        return toResponse(patient);
    }

    // ---------- Entity → DTO ----------
    private PatientResponse toResponse(Patient patient) {
        User u = patient.getUser();
        return new PatientResponse(
                patient.getPatientId(),
                u.getUserId(),
                u.getUsername(),
                u.getEmail(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getPhone(),
                patient.getAddress(),
                patient.getMedicalHistory(),
                u.getCreatedAt()
        );
    }
}
