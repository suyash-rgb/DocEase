package com.docease.messaging_logging_service.service;

import com.docease.messaging_logging_service.entity.MessageLog;
import com.docease.messaging_logging_service.repository.MessagingLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageLoggingService {

    public final MessagingLogsRepository messagingLogsRepository;

    public MessageLoggingService(MessagingLogsRepository messagingLogsRepository) {
        this.messagingLogsRepository = messagingLogsRepository;
    }

    public void logConversation(String input, String output){
        try{
            MessageLog log = new MessageLog(input, output);
            System.out.println("Saving log...");
            messagingLogsRepository.save(log);
            System.out.println("Log saved! ID = " + log.getId());
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to save conversation log", e);
        }
    }
}
