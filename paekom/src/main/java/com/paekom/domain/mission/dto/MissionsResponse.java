package com.paekom.domain.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionsResponse {
    private Integer id;
    private String title;
    private String content;
    private String category;
    private LocalDateTime createdAt;
}
