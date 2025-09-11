package com.paekom.domain.mission.dto;

import com.paekom.domain.mission.entity.MissionCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionRequest {
    private String title;
    private String content;
    private MissionCategory category;
    private String memo;
}