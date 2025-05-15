package com.example.docease.repositories;

import com.example.docease.entities.ChatMessage;
import com.example.docease.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findBySender(User sender);
    List<ChatMessage> findByReceiver(User receiver);

}
