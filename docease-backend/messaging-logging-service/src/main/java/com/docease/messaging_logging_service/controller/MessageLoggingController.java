package com.docease.messaging_logging_service.controller;

import com.docease.messaging_logging_service.DTO.LogRequest;
import com.docease.messaging_logging_service.service.MessageLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messaging-logging-service")
public class MessageLoggingController {

    @Autowired
    private MessageLoggingService messageLoggingService;

    @PostMapping("/log-conversation")
    public void logConversation(@RequestBody LogRequest request) {
        messageLoggingService.logConversation(request.input(), request.output());
    }

}
