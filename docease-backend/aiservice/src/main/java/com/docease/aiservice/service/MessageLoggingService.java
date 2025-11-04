package com.docease.aiservice.service;

import com.docease.aiservice.entity.MessageLog;
import com.docease.aiservice.repository.MessagingLogsRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageLoggingService {

    private final MessagingLogsRepository messageLogRepository;

    public MessageLoggingService(MessagingLogsRepository messageLogRepository) {
        this.messageLogRepository = messageLogRepository;
    }

    void logConversation(String input, String output){
        try{
            MessageLog log = new MessageLog(input, output);
            messageLogRepository.save(log);
        } catch(Exception e){
            throw new RuntimeException("Failed to save conversation log", e);
        }
    }
}
