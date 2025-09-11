package com.paekom.domain.report.entity;

import com.paekom.domain.appointment.entity.WebrtcSession;
import com.paekom.global.converter.StringListJsonConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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

    // List<String> → JSON 자동 변환
    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "JSON")
    private List<String> issues;

    @Lob
    private String overallAssessment;

    @Lob
    private String transcriptFulltext;

    @Lob
    private String evidence;

    @Column(length = 512)
    private String reportFileUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
}