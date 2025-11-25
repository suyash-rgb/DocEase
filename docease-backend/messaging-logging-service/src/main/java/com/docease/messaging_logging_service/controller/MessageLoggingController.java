package com.docease.messaging_logging_service.controller;

import com.docease.messaging_logging_service.DTO.LogRequest;
import com.docease.messaging_logging_service.service.MessageLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logging")
public class MessageLoggingController {

    private final MessageLoggingService messageLoggingService;

    public MessageLoggingController(MessageLoggingService messageLoggingService) {
        this.messageLoggingService = messageLoggingService;
    }

    @PostMapping("/conversation")
    public void logConversation(@RequestBody LogRequest request) {
        System.out.println("Repository in service: " + messageLoggingService.messagingLogsRepository);
        messageLoggingService.logConversation(request.input(), request.output());
    }

}
