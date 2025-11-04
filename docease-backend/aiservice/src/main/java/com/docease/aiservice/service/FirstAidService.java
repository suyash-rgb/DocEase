package com.docease.aiservice.service;

import com.docease.aiservice.DTO.FirstAidResponseDTO;
import com.docease.aiservice.entity.firstaid.FirstAidResponse;
import com.docease.aiservice.entity.firstaid.FirstAidResponseGroup;
import com.docease.aiservice.repository.firstaid.FirstAidPatternRepository;
import com.docease.aiservice.repository.firstaid.FirstAidResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FirstAidService {

    @Autowired
    private FirstAidPatternRepository patternRepository;

    @Autowired
    private FirstAidResponseRepository responseRepository;

    public FirstAidResponseDTO search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return fallback("General");
        }

        Optional<Long> groupIdOpt = patternRepository.findBestGroupId(query.toLowerCase());
        if (groupIdOpt.isPresent()) {
            Long groupId = groupIdOpt.get();
            List<String> steps = responseRepository.findByGroup_GroupIdOrderByStepOrderAsc(groupId)
                    .stream().map(FirstAidResponse::getResponse).toList();

            FirstAidResponseGroup group = responseRepository.findById(groupId)
                    .map(FirstAidResponse::getGroup).orElse(null);

            return new FirstAidResponseDTO(
                    group.getTag().getName(),
                    group.getName(),
                    steps
            );
        }
        return fallback("Sprain");

    }

    private FirstAidResponseDTO fallback(String tagName) {
        List<String> steps = responseRepository.findByGroup_Tag_NameAndIsFallbackTrue(tagName)
                .stream().map(FirstAidResponse::getResponse).toList();
        return new FirstAidResponseDTO(tagName, "Fallback", steps);
    }
}