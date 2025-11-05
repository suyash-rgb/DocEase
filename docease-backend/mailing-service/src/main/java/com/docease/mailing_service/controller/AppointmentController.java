//package com.docease.mailing_service.controller;
//
//import com.docease.mailing_service.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//public class AppointmentController {
//
//    @Autowired
//    private EmailService emailService;
//
//    @PostMapping("/book")
//    public ResponseEntity<?> book(@RequestBody AppointmentRequest req) {
//        Appointment appointment = appointmentService.book(req);
//        User user = userService.findById(req.getUserId());
//
//        emailService.sendAcknowledgement(user, appointment);
//
//        return ResponseEntity.ok("Booked & email sent!");
//    }
//}
