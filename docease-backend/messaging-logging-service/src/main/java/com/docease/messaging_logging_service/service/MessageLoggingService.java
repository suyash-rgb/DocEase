package com.docease.messaging_logging_service.service;

import com.docease.messaging_logging_service.entity.MessageLog;
import com.docease.messaging_logging_service.repository.MessagingLogsRepository;

public class MessageLoggingService {

    private final MessagingLogsRepository messagingLogsRepository;

    public MessageLoggingService(MessagingLogsRepository messagingLogsRepository) {
        this.messagingLogsRepository = messagingLogsRepository;
    }

    void logConversation(String input, String output){
        try{
            MessageLog log = new MessageLog(input, output);
            messagingLogsRepository.save(log);
        } catch(Exception e){
            throw new RuntimeException("Failed to save conversation log", e);
        }
    }
}
