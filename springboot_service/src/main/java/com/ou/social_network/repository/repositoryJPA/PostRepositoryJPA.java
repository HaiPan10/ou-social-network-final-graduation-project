package com.ou.social_network.repository.repositoryJPA;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ou.social_network.pojo.Post;

public interface PostRepositoryJPA extends JpaRepository<Post, Long> {
    @Query("SELECT COUNT(*) FROM Post p " +
            "JOIN p.userId u " +
            "WHERE LOWER(CONCAT(u.id, u.lastName, ' ', u.firstName)) " +
            "LIKE %?1% ")
    Long countPosts(String kw);

    @Query("SELECT p FROM Post p " +
            "JOIN p.userId u " +
            "WHERE LOWER(CONCAT(u.id, u.lastName, ' ', u.firstName)) " +
            "LIKE %?1% ")
    List<Post> search(String kw, Pageable pageable);

    @Query("SELECT " +
            "    CASE " +
            "        WHEN :byMonth = true THEN FUNCTION('MONTH', p.createdAt) " +
            "        WHEN :byQuarter = true THEN FUNCTION('QUARTER', p.createdAt) " +
            "        ELSE FUNCTION('YEAR', p.createdAt) " +
            "    END AS datePart, " +
            "    COUNT(p.id) " +
            "FROM Post p " +
            "LEFT JOIN p.postSurvey ps " +
            "LEFT JOIN p.postInvitation pi " +
            "WHERE (:year IS NULL OR FUNCTION('YEAR', p.createdAt) = :year) " +
            "   AND ps.id IS NULL " +
            "   AND pi.id IS NULL " +
            "GROUP BY datePart")
    List<Object[]> stat(@Param("byMonth") boolean byMonth,
                        @Param("byQuarter") boolean byQuarter,
                        @Param("year") Integer year);
}
