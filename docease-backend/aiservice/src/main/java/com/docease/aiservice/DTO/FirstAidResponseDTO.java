package com.docease.aiservice.DTO;

import java.util.List;

public record FirstAidResponseDTO(String tag, String group, List<String> steps) {
}
