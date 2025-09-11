package com.paekom.domain.report.entity;

import com.paekom.domain.appointment.entity.WebrtcSession;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webrtc_session_id", nullable = true)
    private WebrtcSession webrtcSession;

    @Lob
    private String summary;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @Lob
    private String issues;   // JSON 문자열 그대로 저장

    @Lob
    private String overallAssessment;

    @Lob
    private String transcriptFulltext;

    @Lob
    private String evidence; // JSON 문자열 그대로 저장

    @Column(length = 512)
    private String reportFileUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
}