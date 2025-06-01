package com.example.docease.services;

import com.example.docease.DTOs.DoctorScheduleDTO;
import com.example.docease.entities.Doctor;
import com.example.docease.entities.DoctorSchedule;
import com.example.docease.repositories.DoctorRepository;
import com.example.docease.repositories.DoctorScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;

    public DoctorScheduleService(DoctorScheduleRepository doctorScheduleRepository, DoctorRepository doctorRepository) {
        this.doctorScheduleRepository = doctorScheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    // 🔹 Create a new schedule slot
    public ResponseEntity<?> createSchedule(DoctorScheduleDTO scheduleDTO, String username) {
        Doctor doctor = doctorRepository.findByUser_Username(username).orElse(null);
        if (doctor == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Doctor not found.");

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(scheduleDTO.getDayOfWeek());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());
        schedule.setActive(true);

        doctorScheduleRepository.save(schedule);
        return ResponseEntity.ok(Map.of("message", "Schedule created successfully!"));
    }

    // 🔹 Update an existing schedule slot
    public ResponseEntity<?> updateSchedule(Integer scheduleId, DoctorScheduleDTO scheduleDTO, String username) {
        DoctorSchedule schedule = doctorScheduleRepository.findById(scheduleId).orElse(null);
        if (schedule == null || !schedule.getDoctor().getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or schedule not found.");
        }

        schedule.setDayOfWeek(scheduleDTO.getDayOfWeek());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());

        doctorScheduleRepository.save(schedule);
        return ResponseEntity.ok(Map.of("message", "Schedule updated successfully!"));
    }

    // 🔹 Delete a schedule slot
    public ResponseEntity<?> deleteSchedule(Integer scheduleId, String username) {
        DoctorSchedule schedule = doctorScheduleRepository.findById(scheduleId).orElse(null);
        if (schedule == null || !schedule.getDoctor().getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or schedule not found.");
        }

        doctorScheduleRepository.delete(schedule);
        return ResponseEntity.ok(Map.of("message", "Schedule deleted successfully!"));
    }

    // 🔹 Bulk schedule creation
    public ResponseEntity<?> bulkCreateSchedules(List<DoctorScheduleDTO> schedules, String username) {
        Doctor doctor = doctorRepository.findByUser_Username(username).orElse(null);
        if (doctor == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Doctor not found.");

        List<DoctorSchedule> newSchedules = schedules.stream().map(dto -> {
            DoctorSchedule schedule = new DoctorSchedule();
            schedule.setDoctor(doctor);
            schedule.setDayOfWeek(dto.getDayOfWeek());
            schedule.setStartTime(dto.getStartTime());
            schedule.setEndTime(dto.getEndTime());
            schedule.setActive(true);
            return schedule;
        }).toList();

        doctorScheduleRepository.saveAll(newSchedules);
        return ResponseEntity.ok(Map.of("message", "Bulk schedule created successfully!"));
    }
}
