package com.paekom.domain.appointment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "webrtc_sessions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebrtcSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}