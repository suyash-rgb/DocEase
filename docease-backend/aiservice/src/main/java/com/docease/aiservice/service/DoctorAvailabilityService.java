package com.docease.aiservice.service;

import com.docease.aiservice.DTO.AvailableDoctorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

@Service
public class DoctorAvailabilityService {

    private final WebClient.Builder webClientBuilder;

    public DoctorAvailabilityService(
            @Qualifier("externalWebClientBuilder") WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public List<AvailableDoctorResponse> getAvailableDoctors(
            String specialist,
            String city,
            LocalDate date) {

        // Fix: Make date effectively final (or use a final wrapper)
        LocalDate searchDate = (date != null) ? date : LocalDate.now().plusDays(1);

        return webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")  // manual
                        .port(8081)
                        .path("/recommended/doctors/available")
                        .queryParam("specialist", specialist)
                        .queryParam("city", city)
                        .queryParam("date", searchDate.toString())  // ← toString() fixes lambda issue
                        .build())
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("No doctors found: " + body)))
                .onStatus(status -> status.value() >= 500,
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("user-service error: " + body)))
                .bodyToFlux(AvailableDoctorResponse.class)
                .collectList()
                .block(); // safe in sync context
    }
}
