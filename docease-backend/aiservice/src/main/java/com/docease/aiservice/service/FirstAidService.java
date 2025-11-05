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
            return fallback();
        }

        String clean = query.trim().toLowerCase();

        Optional<Long> groupIdOpt = patternRepository.findBestGroupId(clean);
        if (groupIdOpt.isPresent()) {
            Long groupId = groupIdOpt.get();
            List<String> steps = responseRepository.findByGroup_GroupIdOrderByStepOrderAsc(groupId)
                    .stream()
                    .map(FirstAidResponse::getResponse)
                    .toList();

            FirstAidResponseGroup group = responseRepository.findById(groupId)
                    .map(FirstAidResponse::getGroup)
                    .orElse(null);

            return new FirstAidResponseDTO(
                    group.getTag().getName(),
                    group.getName(),
                    steps
            );
        }
        return fallback();

    }

    private FirstAidResponseDTO fallback() {
        List<String> steps = responseRepository.findByGroup_Tag_NameAndIsFallbackTrue("General")
                .stream()
                .map(FirstAidResponse::getResponse)
                .toList();

        if (steps.isEmpty()) {
            steps = List.of("Sorry, I couldn't help with that. Please try rephrasing.");
        }

        return new FirstAidResponseDTO("General", "Fallback", steps);
    }
}