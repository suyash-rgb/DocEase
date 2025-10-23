package com.docease.aiservice.service;

import com.docease.aiservice.model.MessageLog;
import com.docease.aiservice.repository.MessagingLogsRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

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
