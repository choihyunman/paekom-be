package com.paekom.domain.file.repository;

import com.paekom.domain.file.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    Optional<FileMetadata> findByS3Key(String s3Key);
}
