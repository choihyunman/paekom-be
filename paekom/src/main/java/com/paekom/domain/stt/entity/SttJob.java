package com.paekom.domain.stt.entity;

import com.paekom.domain.appointment.entity.WebrtcSession;
import com.paekom.domain.file.entity.FileMetadata;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "stt_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SttJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // INT로 통일

    // WebRTC 세션 연관관계 (nullable 허용 → 세션 없는 STT도 가능)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webrtc_session_id", nullable = true)
    private WebrtcSession webrtcSession;

    // 업로드된 파일 참조 (STT Job의 필수 조건)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileMetadata file;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SttStatus status = SttStatus.QUEUED;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    @Column(length = 255)
    private String errorMessage;

    @Lob
    private String transcript;   // STT 결과 텍스트 (MEDIUMTEXT 매핑)

    @CreationTimestamp
    private LocalDateTime createdAt;
}