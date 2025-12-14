package com.paekom.domain.report.dto;

import lombok.Getter;

@Getter
public class ReportCreateRequest {
    private Integer sttId; // STT 결과 ID
    private Integer bookingId;
}