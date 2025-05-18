package com.example.docease.services;

import com.example.docease.DTOs.PatientRegistrationDTO;
import com.example.docease.entities.Patient;
import com.example.docease.entities.Role;
import com.example.docease.entities.User;
import com.example.docease.repositories.PatientRepository;
import com.example.docease.repositories.RoleRepository;
import com.example.docease.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

@Service
public class PatientService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public PatientService(UserRepository userRepository, PatientRepository patientRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<?> registerPatient(PatientRegistrationDTO patientDTO) {
        // ✅ Check if username/email already exists
        if (userRepository.existsByUsername(patientDTO.getUsername()) || userRepository.existsByEmail(patientDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already in use.");
        }

        // ✅ Retrieve "PATIENT" role
        Role patientRole = roleRepository.findByName("PATIENT").orElseThrow(
                () -> new RuntimeException("Role not found!")
        );

        // ✅ Create User entry
        User user = new User();
        user.setUsername(patientDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(patientDTO.getPassword())); // Secure hashing
        user.setEmail(patientDTO.getEmail());
        user.setRole(patientRole);

        userRepository.save(user);

        // ✅ Convert String to `java.sql.Date`
        Date dateOfBirth = Date.valueOf(LocalDate.parse(patientDTO.getDateOfBirth()));


        // ✅ Create Patient entry
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setDateOfBirth(dateOfBirth);
        patient.setGender(patientDTO.getGender());

        patientRepository.save(patient);

        return ResponseEntity.ok(Map.of(
                "message", "Patient registered successfully!",
                "patientId", patient.getPatientId()
        ));
    }
}
