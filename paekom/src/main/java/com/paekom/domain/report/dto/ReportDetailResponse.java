package com.paekom.domain.report.dto;

import com.paekom.domain.report.entity.Emotion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ReportDetailResponse {
    private String summary;
    private List<String> issues;
    private Emotion emotion;
    private Map<String, Object> evidence;
    private String overallAssessment;
    private LocalDateTime createdAt;
}