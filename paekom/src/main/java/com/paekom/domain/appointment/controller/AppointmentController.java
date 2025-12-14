package com.paekom.domain.appointment.controller;

import com.paekom.domain.appointment.dto.AppointmentRequest;
import com.paekom.domain.appointment.dto.AppointmentResponse;
import com.paekom.domain.appointment.entity.Appointment;
import com.paekom.domain.appointment.service.AppointmentService;
import com.paekom.domain.appointment.service.WebrtcSessionService;
import com.paekom.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    // 서비스
    private final AppointmentService service;
    private final WebrtcSessionService webrtcSessionService;

    // 예약 생성
    @PostMapping
    public ApiResponse<String> createAppointment(@RequestBody AppointmentRequest request) {
       service.createAppointment(request);
       return ApiResponse.success("created appointment successfully");
    }

    // 예약 목록 조회
    @GetMapping("/list")
    public ApiResponse<List<AppointmentResponse>> getAppointment() {
        List<AppointmentResponse> list = service.getAppointments();
        return ApiResponse.success(list);
    }

    // 예약 목록 삭제(상태 수정)
    @PutMapping("/{id}/cancel")
    public ApiResponse<String> deleteAppointment(@PathVariable Integer id) {
        service.cancelAppointment(id);
        return ApiResponse.success("deleted appointment successfully");
    }

    // 상담 시작
    @PutMapping("/{id}/start")
    public ApiResponse<Map<String, Integer>> startAppointment(@PathVariable Integer id) {
        Appointment appointment = service.startedAppointment(id);
        Integer sessionId = webrtcSessionService.createWebRTCSession(appointment);
        return ApiResponse.success(Map.of("sessionId", sessionId));
    }

    // 상담 종료
    @PutMapping("/{id}/complete")
    public ApiResponse<String> completeAppointment(@PathVariable Integer id) {
        Appointment appointment = service.completedAppointment(id);
        webrtcSessionService.endWebRTCSession(appointment);
        return ApiResponse.success("completed appointment successfully");
    }
}
