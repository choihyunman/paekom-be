package com.paekom.domain.report.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class AiReportResponse {
    private String summary;
    private List<String> issues;
    private String emotion;
    private Map<String, Object> evidence;
    private String overall_assessment;
}