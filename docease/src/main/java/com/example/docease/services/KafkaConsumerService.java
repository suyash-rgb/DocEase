package com.example.docease.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "appointment_notifications", groupId = "docease-group")
    public void listen(String message) {
        System.out.println("Received notification: " + message);
    }
}
