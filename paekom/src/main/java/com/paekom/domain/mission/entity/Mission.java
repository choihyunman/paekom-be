package com.paekom.domain.mission.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) // 기존 객체 복사 후 일부 수정할 수 있음
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionCategory category;

    @Lob
    private String memo;

    @Lob
    private String feedback;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Mission 전체 업데이트 (PUT 용도)
     */
    public void update(String title, String content, MissionCategory category, String memo, String feedback) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.memo = memo;
        this.feedback = feedback;
    }

    /**
     * Mission 부분 업데이트 (PATCH 용도)
     */
    public void patch(String title, String content, MissionCategory category, String memo, String feedback) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (category != null) this.category = category;
        if (memo != null) this.memo = memo;
        if (feedback != null) this.feedback = feedback;
    }

    /**
     * 편의 메서드: feedback만 갱신
     */
    public void updateFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category=" + category +
                ", memo='" + memo + '\'' +
                ", feedback='" + (feedback != null ? feedback.substring(0, Math.min(30, feedback.length())) + "..." : null) + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mission mission)) return false;
        return id != null && id.equals(mission.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
