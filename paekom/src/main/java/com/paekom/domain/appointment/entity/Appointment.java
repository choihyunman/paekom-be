package com.paekom.domain.appointment.entity;

import com.paekom.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointments")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 상담사
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", nullable = false)
    private User counselor;

    // 청년(내담자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "youth_id", nullable = false)
    private User youth;

    @Column(nullable = false)
    private LocalDateTime scheduledStartAt;

    @Column(nullable = false)
    private LocalDateTime scheduledEndAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(length = 64, nullable = false)
    private String meetingRoomCode;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 연관관계
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WebrtcSession> webrtcSessions = new ArrayList<>();
}
