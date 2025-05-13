package com.example.docease.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppointmentNotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public AppointmentNotificationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(String message) {
        kafkaTemplate.send("appointment_notifications", message);
    }
}