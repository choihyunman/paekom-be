package com.paekom.domain.stt.service;

import com.paekom.domain.file.entity.FileMetadata;
import com.paekom.domain.file.repository.FileMetadataRepository;
import com.paekom.domain.stt.entity.SttJob;
import com.paekom.domain.stt.entity.SttStatus;
import com.paekom.domain.stt.repository.SttJobRepository;
import com.paekom.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SttService {

    private final S3Uploader s3Uploader;
    private final FileMetadataRepository fileMetadataRepository;
    private final SttJobRepository sttJobRepository;
    private final SttClient sttClient;

    public SttJob createAndRunStt(MultipartFile file) {
        // 1. S3 업로드
        String s3Key = s3Uploader.uploadFile(file);

        // 2. 파일 메타데이터 저장
        FileMetadata metadata = fileMetadataRepository.save(
                FileMetadata.builder()
                        .s3Key(s3Key)
                        .originalFilename(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .size(file.getSize())
                        .build()
        );

        // 3. STT Job 저장 (QUEUED)
        SttJob job = SttJob.builder()
                .file(metadata)
                .status(SttStatus.QUEUED)
                .createdAt(LocalDateTime.now())
                .build();
        sttJobRepository.save(job);

        try {
            job.setStatus(SttStatus.RUNNING);
            job.setStartedAt(LocalDateTime.now());

            // 4. 파이썬 서버 호출
            String transcript = sttClient.requestStt(s3Key);
            job.setTranscript(transcript);

            job.setStatus(SttStatus.DONE);
            job.setFinishedAt(LocalDateTime.now());
        } catch (Exception e) {
            job.setStatus(SttStatus.FAILED);
            job.setErrorMessage(e.getMessage());
        }

        return sttJobRepository.save(job);
    }

    public SttJob getSttJob(Integer id) {
        return sttJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("STT Job not found: " + id));
    }
}