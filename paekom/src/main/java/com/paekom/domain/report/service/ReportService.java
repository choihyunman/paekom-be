package com.paekom.domain.report.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paekom.domain.report.dto.ReportCreateRequest;
import com.paekom.domain.report.dto.ReportCreateResponse;
import com.paekom.domain.report.dto.ReportDetailResponse;
import com.paekom.domain.report.entity.Emotion;
import com.paekom.domain.report.entity.Report;
import com.paekom.domain.report.repository.ReportRepository;
import com.paekom.domain.stt.entity.SttJob;
import com.paekom.domain.stt.repository.SttJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${ai.server.url}")
    private String aiServerUrl;

    /**
     * 리포트 생성 - STT 텍스트 기반 FastAPI 호출 후 DB 저장
     */
    public ReportCreateResponse createReport(ReportCreateRequest request) throws Exception {
        // 1. sttId로 DB에서 STT 텍스트 조회
        String transcript = getTranscriptBySttId(request.getSttId());

        // 2. WebClient로 FastAPI 호출
        JsonNode response = webClient.post()
                .uri(aiServerUrl + "/api/ai/report")
                .bodyValue(Map.of("transcript", transcript))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(); // 동기 처리

        if (response == null || !"success".equals(response.get("status").asText())) {
            throw new RuntimeException("AI 리포트 생성 실패");
        }

        JsonNode data = response.get("data");

        // 3. Report 엔티티 저장
        Report report = Report.builder()
                .summary(data.get("summary").asText())
                .issues(objectMapper.convertValue(data.get("issues"), List.class))
                .emotion(Emotion.valueOf(data.get("emotion").asText()))
                .evidence(data.get("evidence").toString())
                .overallAssessment(data.get("overall_assessment").asText())
                .transcriptFulltext(transcript)
                .createdAt(LocalDateTime.now())
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
                .issues(report.getIssues())
                .emotion(report.getEmotion())
                .evidence(objectMapper.readValue(report.getEvidence(), Map.class))
                .overallAssessment(report.getOverallAssessment())
                .createdAt(report.getCreatedAt())
                .build();
    }

    /**
     * STT 결과 텍스트 조회
     */
    private String getTranscriptBySttId(Integer sttId) {
        SttJob sttJob = sttJobRepository.findById(sttId)
                .orElseThrow(() -> new NoSuchElementException("STT Job이 존재하지 않습니다."));
        return sttJob.getTranscript();
    }
}