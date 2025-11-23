package com.docease.messaging_logging_service.service;

import com.docease.messaging_logging_service.entity.MessageLog;
import com.docease.messaging_logging_service.repository.MessagingLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageLoggingService {

    @Autowired
    private  MessagingLogsRepository messagingLogsRepository;

    public void logConversation(String input, String output){
        try{
            MessageLog log = new MessageLog(input, output);
            messagingLogsRepository.save(log);
        } catch(Exception e){
            throw new RuntimeException("Failed to save conversation log", e);
        }
    }
}
