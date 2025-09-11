package com.paekom.domain.mission.service;

import com.paekom.domain.mission.dto.*;
import com.paekom.domain.mission.entity.Mission;
import com.paekom.domain.mission.entity.MissionCategory;
import com.paekom.domain.mission.repository.MissionRepository;
import com.paekom.global.exception.ErrorCode;
import com.paekom.global.exception.NotFoundException;
import com.paekom.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final WebClient aiWebClient;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    // POST
    public MissionResponse createMission(MissionRequest req) {
        String feedback = callAiFeedback(req);

        Mission mission = Mission.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .category(req.getCategory())
                .memo(req.getMemo())
                .feedback(feedback)
                .build();
        missionRepository.save(mission);

        return toResponse(mission);
    }

    // GET
    public MissionResponse getMission(Integer id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MISSION_NOT_FOUND));
        return toResponse(mission);
    }

    // PUT
    public MissionResponse updateMission(Integer id, MissionRequest req) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MISSION_NOT_FOUND));

        String feedback = callAiFeedback(req);
        mission.update(req.getTitle(), req.getContent(), req.getCategory(), req.getMemo(), feedback);
        missionRepository.save(mission);

        return toResponse(mission);
    }

    // DELETE
    public void deleteMission(Integer id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MISSION_NOT_FOUND));
        missionRepository.delete(mission);
    }

    private MissionResponse toResponse(Mission mission) {
        return MissionResponse.builder()
                .missionId(mission.getId())
                .title(mission.getTitle())
                .content(mission.getContent())
                .category(mission.getCategory())
                .memo(mission.getMemo())
                .feedback(mission.getFeedback())
                .build();
    }

    // Python 서버 호출
    private String callAiFeedback(MissionRequest req) {
        ApiResponse<AiFeedbackResponse> aiRes = aiWebClient.post()
                .uri(aiServerUrl + "/api/ai/mission")
                .bodyValue(Map.of(
                        "title", req.getTitle(),
                        "content", req.getContent(),
                        "category", req.getCategory().name(),
                        "memo", req.getMemo()
                ))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<AiFeedbackResponse>>() {})
                .block();

        if (aiRes != null && "success".equals(aiRes.getStatus()) && aiRes.getData() != null) {
            return aiRes.getData().getFeedback();
        }
        return "피드백 생성 실패";
    }
}
