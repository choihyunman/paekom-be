package com.paekom.domain.report.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paekom.domain.appointment.entity.Appointment;
import com.paekom.domain.appointment.service.AppointmentService;
import com.paekom.domain.report.dto.*;
import com.paekom.domain.report.entity.Emotion;
import com.paekom.domain.report.entity.Report;
import com.paekom.domain.report.repository.ReportRepository;
import com.paekom.domain.stt.entity.SttJob;
import com.paekom.domain.stt.repository.SttJobRepository;
import com.paekom.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final WebClient webClient;
    private final ReportRepository reportRepository;
    private final SttJobRepository sttJobRepository;
    private final ObjectMapper objectMapper;
    private final AppointmentService appointmentService;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    /**
     * 리포트 생성
     */
    public ReportCreateResponse createReport(ReportCreateRequest request) throws Exception {
        // webrtc 세션 찾기
        Integer appointmentId = request.getBookingId();
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        // 1. STT 텍스트 조회
        String transcript = getTranscriptBySttId(request.getSttId());

        // 2. Python 서버 호출 → ApiResponse<AiReportResponse>로 받기
        ApiResponse<AiReportResponse> wrapper = webClient.post()
                .uri(aiServerUrl + "/api/ai/report")
                .bodyValue(Map.of("transcript", transcript))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<AiReportResponse>>() {})
                .block();

        if (wrapper == null || wrapper.getData() == null) {
            throw new RuntimeException("AI 리포트 생성 실패");
        }

        AiReportResponse aiResponse = wrapper.getData();

        // 3. Emotion 매핑 (null/invalid 대비)
        Emotion emotion = null;
        if (aiResponse.getEmotion() != null && !aiResponse.getEmotion().isBlank()) {
            try {
                emotion = Emotion.valueOf(aiResponse.getEmotion().toUpperCase());
            } catch (IllegalArgumentException e) {
                emotion = Emotion.NEUTRAL; // fallback
            }
        } else {
            emotion = Emotion.NEUTRAL;
        }

        // 4. Report 저장 (issues, evidence → JSON 문자열로 저장)
        Report report = Report.builder()
                .summary(aiResponse.getSummary())
                .issues(objectMapper.writeValueAsString(aiResponse.getIssues()))
                .emotion(emotion)
                .evidence(objectMapper.writeValueAsString(aiResponse.getEvidence()))
                .overallAssessment(aiResponse.getOverall_assessment())
                .transcriptFulltext(transcript)
                .createdAt(LocalDateTime.now())
                .appointment(appointment)
                .build();

        Report saved = reportRepository.save(report);
        return new ReportCreateResponse(saved.getId());
    }

    /**
     * 리포트 상세 조회
     */
    public ReportDetailResponse getReport(Integer reportId) throws Exception {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NoSuchElementException("리포트가 존재하지 않습니다."));

        return ReportDetailResponse.builder()
                .summary(report.getSummary())
                .issues(objectMapper.readValue(report.getIssues(), List.class))
                .emotion(report.getEmotion())
                .evidence(objectMapper.readValue(report.getEvidence(), Map.class))
                .overallAssessment(report.getOverallAssessment())
                .createdAt(report.getCreatedAt())
                .build();
    }

    private String getTranscriptBySttId(Integer sttId) {
        SttJob sttJob = sttJobRepository.findById(sttId)
                .orElseThrow(() -> new NoSuchElementException("STT Job이 존재하지 않습니다."));
        return sttJob.getTranscript();
    }

    public List<ReportsResponseDto> getReports() {
        return reportRepository.findByAllOrderByCreatedAtDesc()
                .stream()
                .map(p -> new ReportsResponseDto(
                        p.getId(),
                        p.getSummary(),
                        p.getCreatedAt().toLocalDateTime(),
                        p.getSessionId())
                ).toList();
    }
}