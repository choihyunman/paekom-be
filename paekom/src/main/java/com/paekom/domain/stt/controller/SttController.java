package com.paekom.domain.stt.controller;

import com.paekom.domain.stt.entity.SttJob;
import com.paekom.domain.stt.service.SttService;
import com.paekom.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/stt")
@RequiredArgsConstructor
public class SttController {

    private final SttService sttService;

    @PostMapping
    public ResponseEntity<?> uploadAndRequestStt(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bookingId") Integer appointmentId) {
        SttJob job = sttService.createAndRunStt(file, appointmentId);
        return ResponseEntity.ok(ApiResponse.success(new Data(job.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSttResult(@PathVariable Integer id) {
        SttJob job = sttService.getSttJob(id);
        return ResponseEntity.ok(ApiResponse.success(
                new SttResult(job.getId(), job.getStatus().name(), job.getTranscript())
        ));
    }

    // 컨트롤러 전용 응답 DTO
    record Data(Integer id) {}
    record SttResult(Integer id, String status, String transcript) {}
}