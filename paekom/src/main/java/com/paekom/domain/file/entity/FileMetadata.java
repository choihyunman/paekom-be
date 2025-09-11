package com.paekom.domain.file.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // INT로 통일

    @Column(name = "s3_key", nullable = false, length = 512, unique = true)  // 🔥 수정
    private String s3Key; // S3에 저장된 파일 키 (경로)

    @Column(nullable = false, length = 255)
    private String originalFilename; // 사용자가 업로드한 원본 이름

    @Column(length = 100)
    private String contentType; // MIME 타입 (예: image/png, audio/wav)

    private Long size; // 파일 크기 (bytes) → INT로 맞춤 (최대 2GB까지 커버됨)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder.Default
    private Boolean isDeleted = false; // 논리 삭제 여부
}