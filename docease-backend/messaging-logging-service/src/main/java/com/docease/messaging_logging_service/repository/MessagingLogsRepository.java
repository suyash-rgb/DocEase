package com.docease.messaging_logging_service.repository;

import com.docease.messaging_logging_service.entity.MessageLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessagingLogsRepository extends MongoRepository<MessageLog, String> {

}
