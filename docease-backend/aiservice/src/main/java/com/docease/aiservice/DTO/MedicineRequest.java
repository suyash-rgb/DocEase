package com.docease.aiservice.DTO;

import jakarta.validation.constraints.NotBlank;

public class MedicineRequest {

    @NotBlank(message = "Medicines cannot be blank")
    private String medicines;

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }
}
