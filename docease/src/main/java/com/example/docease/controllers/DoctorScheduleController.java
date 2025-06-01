package com.example.docease.controllers;

import com.example.docease.DTOs.DoctorScheduleDTO;
import com.example.docease.services.DoctorScheduleService;
import com.example.docease.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/doctor-schedule")
public class DoctorScheduleController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorScheduleService doctorScheduleService;

    // ✅ Create a new schedule slot
    @PostMapping("/create")
    public ResponseEntity<?> createSchedule(@RequestBody DoctorScheduleDTO scheduleDTO, Principal principal) {
        return doctorScheduleService.createSchedule(scheduleDTO, principal.getName());
    }

    // ✅ Update an existing schedule slot
    @PutMapping("/update/{scheduleId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Integer scheduleId, @RequestBody DoctorScheduleDTO scheduleDTO, Principal principal) {
        return doctorScheduleService.updateSchedule(scheduleId, scheduleDTO, principal.getName());
    }

    // ✅ Delete a schedule slot
    @DeleteMapping("/delete/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Integer scheduleId, Principal principal) {
        return doctorScheduleService.deleteSchedule(scheduleId, principal.getName());
    }

    // ✅ Bulk create schedule slots for multiple days
    @PostMapping("/bulk-create")
    public ResponseEntity<?> bulkCreateSchedules(@RequestBody List<DoctorScheduleDTO> schedules, Principal principal) {
        return doctorScheduleService.bulkCreateSchedules(schedules, principal.getName());
    }


}
