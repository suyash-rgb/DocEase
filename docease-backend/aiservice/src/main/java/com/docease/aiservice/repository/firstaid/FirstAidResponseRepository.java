package com.docease.aiservice.repository.firstaid;

import com.docease.aiservice.entity.firstaid.FirstAidResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FirstAidResponseRepository extends JpaRepository<FirstAidResponse, Long> {
    List<FirstAidResponse> findByGroup_GroupIdOrderByStepOrderAsc(Long groupId);
    List<FirstAidResponse> findByGroup_Tag_NameAndIsFallbackTrue(String tagName);
}
