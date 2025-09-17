package com.paekom.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportsResponseDto {
    Integer reportId;
    String summary;
    LocalDateTime createdAt;

    public ReportsResponseDto(Integer reportId, String summary, LocalDateTime createdAt ) {
        this.createdAt = createdAt;
        this.summary = summary;
        this.reportId = reportId;
    }
}
