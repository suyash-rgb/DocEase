package com.docease.aiservice.repository.firstaid;

import com.docease.aiservice.entity.firstaid.FirstAidPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FirstAidPatternRepository extends JpaRepository<FirstAidPattern, Long> {

    //Querying in Natural Language Mode for large dataset
//    @Query(value = """
//        SELECT p.group_id, MATCH(p.pattern) AGAINST(?1 IN NATURAL LANGUAGE MODE) AS score
//        FROM first_aid_patterns p
//        WHERE MATCH(p.pattern) AGAINST(?1 IN NATURAL LANGUAGE MODE)
//        HAVING score > 0.5
//        ORDER BY score DESC
//        LIMIT 1
//        """, nativeQuery = true)
//    Optional<Long> findBestGroupIdWithScore(String query);

    //Querying in Boolean Mode for small dataset
    @Query(value = """
    SELECT p.group_id
    FROM first_aid_patterns p
    WHERE MATCH(p.pattern) AGAINST(?1 IN BOOLEAN MODE)
      AND MATCH(p.pattern) AGAINST(?1 IN BOOLEAN MODE) > 1.0
    ORDER BY MATCH(p.pattern) AGAINST(?1 IN BOOLEAN MODE) DESC
    LIMIT 1
    """, nativeQuery = true)
    Optional<Long> findBestGroupId(String query);


}
