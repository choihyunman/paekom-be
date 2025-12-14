package com.paekom.domain.mission.repository.projection;

public interface MissionProjection {
    Integer getId();
    String getTitle();
    String getContent();
    String getCategory();
    java.sql.Timestamp getCreatedAt();
}
