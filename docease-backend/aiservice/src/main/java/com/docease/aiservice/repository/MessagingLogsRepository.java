package com.docease.aiservice.repository;


import com.docease.aiservice.model.MessageLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessagingLogsRepository extends MongoRepository<MessageLog, String> {

}
