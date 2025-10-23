package com.docease.aiservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messaging_logs")
public class MessageLog {

    @Id
    private String id;  // Auto-generated ID

    private String input;    // User-provided input (e.g., symptoms etc)
    private String output;   // Generated response (e.g., advice etc)
    private LocalDateTime timestamp;  // When the log was created

    public MessageLog() {
    }

    public MessageLog(String input, String output) {
        this.input = input;
        this.output = output;
        this.timestamp = LocalDateTime.now();
    }

    public MessageLog(String input, String output, LocalDateTime timestamp) {
        this.input = input;
        this.output = output;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
