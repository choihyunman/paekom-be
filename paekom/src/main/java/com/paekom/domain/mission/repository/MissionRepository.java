package com.paekom.domain.mission.repository;

import com.paekom.domain.mission.entity.Mission;
import com.paekom.domain.mission.repository.projection.MissionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Integer> {
    @Query(value = """
        select 
            m.id as id
           , m.title as title
           , substring(m.content, 1, 50) as content
           , m.category as category
           , m.created_at as created_at
        from mission m
        order by created_at desc 
    """, nativeQuery = true)
    List<MissionProjection> findAllOrderByCreatedAtDesc();
}