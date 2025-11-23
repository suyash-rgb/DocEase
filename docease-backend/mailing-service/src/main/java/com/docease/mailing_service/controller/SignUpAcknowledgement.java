//package com.docease.mailing_service.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/email")
//public class SignUpAcknowledgement {
//
//    @Autowired
//    private EmailService emailService;
//
//    @PostMapping("/send-doctor-signup")
//    public ResponseEntity<String> sendDoctorSignupEmail(@RequestBody DoctorEmailRequest request) {
//        Doctor doctor = new Doctor();
//        doctor.setDoctorId(request.doctorId());
//        doctor.setFullName(request.fullName());
//        doctor.setEmail(request.email());
//        doctor.setPhone(request.phone());
//        doctor.setSpecialization(request.specialization());
//        doctor.setClinicName(request.clinicName());
//
//        emailService.sendDoctorSignupAcknowledgement(doctor);
//
//        return ResponseEntity.ok("Doctor signup email sent to: " + doctor.getEmail());
//    }
//
//}
