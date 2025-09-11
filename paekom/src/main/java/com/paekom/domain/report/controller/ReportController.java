package com.paekom.domain.report.controller;

import com.paekom.domain.report.dto.*;
import com.paekom.domain.report.service.ReportService;
import com.paekom.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ApiResponse<ReportCreateResponse> createReport(@RequestBody ReportCreateRequest request) throws Exception {
        ReportCreateResponse response = reportService.createReport(request);
        return ApiResponse.success(response);
    }

    @GetMapping("/{reportId}")
    public ApiResponse<ReportDetailResponse> getReport(@PathVariable Integer reportId) throws Exception {
        ReportDetailResponse response = reportService.getReport(reportId);
        return ApiResponse.success(response);
    }
}