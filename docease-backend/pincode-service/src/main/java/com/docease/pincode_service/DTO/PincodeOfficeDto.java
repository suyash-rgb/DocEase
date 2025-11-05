package com.docease.pincode_service.DTO;

public record PincodeOfficeDto(
        String officeName,
        int pincode,
        String district,
        String divisionName,
        String stateName
) {}
