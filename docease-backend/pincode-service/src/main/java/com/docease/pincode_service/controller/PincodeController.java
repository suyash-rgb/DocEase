package com.docease.pincode_service.controller;

import com.docease.pincode_service.DTO.PincodeOfficeDto;
import com.docease.pincode_service.entity.Pincode;
import com.docease.pincode_service.repository.PincodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pincode-service/pincode-records")
public class PincodeController {

    @Autowired
    private  PincodeRepository pincodeRepository;

    /**
     * Search post offices by city/district name (partial, case-insensitive)
     * Example: /api/pincode/search?city=Indore → returns all offices in Indore
     */
    @GetMapping("/search")
    public ResponseEntity<List<PincodeOfficeDto>> searchByCity(
            @RequestParam("city") String cityName
    ) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(List.of());
        }

        List<Pincode> results = pincodeRepository.searchByDistrictOrOfficeName(cityName.trim());

        List<PincodeOfficeDto> dtos = results.stream()
                .map(p -> new PincodeOfficeDto(
                        p.getOfficeName(),
                        p.getPincode(),
                        p.getDistrict(),
                        p.getDivisionName(),
                        p.getStateName()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }


    //Search by exact pincode
    @GetMapping("/by-pincode/{pincode}")
    public ResponseEntity<List<PincodeOfficeDto>> searchByPincode(@PathVariable int pincode) {
        List<Pincode> results = pincodeRepository.findByPincodePrimaryKey_Pincode(pincode);
        return ResponseEntity.ok(mapToDto(results));
    }

    private List<PincodeOfficeDto> mapToDto(List<Pincode> pincodes) {
        return pincodes.stream()
                .map(p -> new PincodeOfficeDto(
                        p.getOfficeName(),
                        p.getPincode(),
                        p.getDistrict(),
                        p.getDivisionName(),
                        p.getStateName()
                ))
                .toList();
    }
}
