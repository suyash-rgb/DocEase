package com.docease.aiservice.repository.firstaid;

import com.docease.aiservice.entity.firstaid.FirstAidPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FirstAidPatternRepository extends JpaRepository<FirstAidPattern, Long> {

    @Query(value = "SELECT p.group_id FROM first_aid_patterns p " +
            "WHERE MATCH(p.pattern) AGAINST(?1 IN NATURAL LANGUAGE MODE) " +
            "ORDER BY MATCH(p.pattern) AGAINST(?1 IN NATURAL LANGUAGE MODE) DESC LIMIT 1",
            nativeQuery = true)
    Optional<Long> findBestGroupId(String query);
}
