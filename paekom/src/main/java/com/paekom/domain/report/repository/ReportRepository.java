package com.paekom.domain.report.repository;

import com.paekom.domain.report.entity.Report;
import com.paekom.domain.report.repository.projection.ReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query(value = """
    SELECT 
    r.id AS id,
    SUBSTRING(r.summary, 1, 100) AS summary,
    r.created_at AS createdAt
    FROM reports r
    ORDER BY createdAt DESC 
""", nativeQuery = true)
    List<ReportProjection> findByAllOrderByCreatedAtDesc();
}
