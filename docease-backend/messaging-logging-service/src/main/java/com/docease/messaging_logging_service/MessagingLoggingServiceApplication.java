package com.docease.messaging_logging_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MessagingLoggingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagingLoggingServiceApplication.class, args);
	}

}
