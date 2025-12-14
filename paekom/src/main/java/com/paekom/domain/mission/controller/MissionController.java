package com.paekom.domain.mission.controller;

import com.paekom.domain.mission.dto.MissionRequest;
import com.paekom.domain.mission.dto.MissionResponse;
import com.paekom.domain.mission.dto.MissionsResponse;
import com.paekom.domain.mission.service.MissionService;
import com.paekom.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mission")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping
    public ApiResponse<MissionResponse> create(@RequestBody MissionRequest req) {
        return ApiResponse.success(missionService.createMission(req));
    }

    @GetMapping("/{missionId}")
    public ApiResponse<MissionResponse> get(@PathVariable Integer missionId) {
        return ApiResponse.success(missionService.getMission(missionId));
    }

    @PutMapping("/{missionId}")
    public ApiResponse<MissionResponse> update(
            @PathVariable Integer missionId,
            @RequestBody MissionRequest req) {
        return ApiResponse.success(missionService.updateMission(missionId, req));
    }

    @DeleteMapping("/{missionId}")
    public ApiResponse<Void> delete(@PathVariable Integer missionId) {
        missionService.deleteMission(missionId);
        return ApiResponse.success(null);
    }

    @GetMapping("/list")
    public ApiResponse<List<MissionsResponse>> getMissionList() {
        return ApiResponse.success(missionService.getMissions());
    }
}