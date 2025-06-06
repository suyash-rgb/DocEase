package com.example.docease.repositories;

import com.example.docease.entities.Doctor;
import com.example.docease.entities.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Integer> {
    List<DoctorSchedule> findByDoctorAndActiveTrue(Doctor doctor);

    List<DoctorSchedule> findByDoctor(Doctor doctor); // ✅ Retrieves schedules for a doctor

    void deleteByDoctor(Doctor doctor); // ✅ Bulk deletes schedules linked to a doctor

}