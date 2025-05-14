package com.example.docease.repositories;

import com.example.docease.entities.HealthTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthTipRepository extends JpaRepository<HealthTip, Long> {

}
