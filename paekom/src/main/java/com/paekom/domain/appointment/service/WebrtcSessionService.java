package com.paekom.domain.appointment.service;

import com.paekom.domain.appointment.entity.Appointment;
import com.paekom.domain.appointment.entity.WebrtcSession;
import com.paekom.domain.appointment.repository.WebrtcSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WebrtcSessionService {

    private final WebrtcSessionRepository repository;

    // WebRTC Session 생성
    public void createWebRTCSession(Appointment appointment) {
        WebrtcSession session = WebrtcSession.builder()
                .appointment(appointment)
                .startedAt(LocalDateTime.now())
                .build();
        repository.save(session);
    }

    // WebRTC Session 종료
    public void endWebRTCSession(Appointment appointment) {
        WebrtcSession session = repository.findByAppointment(appointment);
        session.setEndedAt(LocalDateTime.now());
        repository.save(session);
    }

    // WebRTC Session 조회
    public WebrtcSession getWebRTCSession(Integer sessionId) {
        return repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("session을 찾을 수 없습니다."));
    }

}
