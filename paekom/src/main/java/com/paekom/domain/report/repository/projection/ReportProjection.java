package com.paekom.domain.report.repository.projection;

public interface ReportProjection {
    Integer getId();
    String getSummary();
    java.sql.Timestamp getCreatedAt();
    Integer getSessionId();
}
