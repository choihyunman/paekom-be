package com.paekom.domain.stt.repository;

import com.paekom.domain.stt.entity.SttJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SttJobRepository extends JpaRepository<SttJob, Integer> {
    Optional<SttJob> findByAppointment_Id(Integer integer);
}