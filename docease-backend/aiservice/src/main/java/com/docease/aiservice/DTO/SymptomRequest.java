package com.docease.aiservice.DTO;

import jakarta.validation.constraints.NotBlank;

public class SymptomRequest {

    @NotBlank()
    private String symptoms;

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }


}
