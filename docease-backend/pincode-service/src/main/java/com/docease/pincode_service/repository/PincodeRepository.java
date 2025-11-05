package com.docease.pincode_service.repository;

import com.docease.pincode_service.entity.Pincode;
import com.docease.pincode_service.entity.PincodePrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PincodeRepository extends JpaRepository<Pincode, PincodePrimaryKey> {

    // Search in District OR OfficeName (case-insensitive, partial match)
    @Query("SELECT p FROM Pincode p WHERE " +
            "LOWER(p.pincodePrimaryKey.district) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(p.pincodePrimaryKey.officeName) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Pincode> searchByDistrictOrOfficeName(@Param("term") String term);

    // Optional: Exact match by pincode
    List<Pincode> findByPincodePrimaryKey_Pincode(int pincode);
}
