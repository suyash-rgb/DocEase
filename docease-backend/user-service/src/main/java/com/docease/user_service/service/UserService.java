package com.docease.user_service.service;

import com.docease.user_service.DTO.DoctorRegistrationDTO;
import com.docease.user_service.entity.Doctor;
import com.docease.user_service.entity.Role;
import com.docease.user_service.entity.User;
import com.docease.user_service.exception.DuplicateEmailException;
import com.docease.user_service.repository.UserRepository;
import com.docease.user_service.repository.DoctorRepository;
import com.docease.user_service.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Doctor registerDoctor(DoctorRegistrationDTO dto) {
        Role doctorRole = roleRepository.findByName("DOCTOR")
                .orElseThrow(() -> new RuntimeException("Role DOCTOR not found – seed DB first"));

        User user = new User();
        user.setUsername(dto.getUsername());
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email '" + dto.getEmail() + "' is already registered");
        }
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(doctorRole);
        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setConsultationFee(
                dto.getConsultationFee() != null ? dto.getConsultationFee() : BigDecimal.ZERO
        );
        doctor.setProfileDescription(dto.getProfileDescription());
        doctor.setPhone(dto.getPhone());
        doctor.setImageUrl(dto.getImageUrl());
        doctor.setMedicalLicense(dto.getMedicalLicense());

        return doctorRepository.save(doctor);
    }

}
