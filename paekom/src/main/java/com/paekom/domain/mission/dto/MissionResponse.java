package com.paekom.domain.mission.dto;

import com.paekom.domain.mission.entity.MissionCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MissionResponse {
    private Integer missionId;
    private String title;
    private String content;
    private MissionCategory category;
    private String memo;
    private String feedback;
    private LocalDateTime createdAt;
}