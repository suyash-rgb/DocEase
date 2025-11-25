package com.docease.user_service.controller;

import com.docease.user_service.DTO.AvailableDoctorResponse;
import com.docease.user_service.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/recommended/doctors")
public class RecommendedDoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/available")
    public List<AvailableDoctorResponse> getAvailableDoctors(
            @RequestParam String specialist,
            @RequestParam String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        if (date == null) date = tomorrow;

        return doctorRepository.findAvailableDoctors(specialist, city, date);
    }
}
