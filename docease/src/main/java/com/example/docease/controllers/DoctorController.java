package com.example.docease.controllers;

import com.example.docease.DTOs.DoctorProfileUpdateDTO;
import com.example.docease.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateDoctorProfile(@RequestBody DoctorProfileUpdateDTO updateDTO, Principal principal) {
        return doctorService.updateDoctorProfile(updateDTO, principal.getName());
    }





}
