package com.ou.postservice.repository.repositoryJPA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ou.postservice.pojo.PostInvitation;

public interface PostInvitationRepositoryJPA extends JpaRepository<PostInvitation, Long>{
    @Query("SELECT " +
           "CASE WHEN :byMonth = true THEN FUNCTION('MONTH', p.createdAt) " +
           "     WHEN :byQuarter = true THEN FUNCTION('QUARTER', p.createdAt) " +
           "     ELSE FUNCTION('YEAR', p.createdAt) END, " +
           "COUNT(pt.id) " +
           "FROM PostInvitation pt " +
           "JOIN pt.post p " +
           "WHERE (:year IS NULL OR FUNCTION('YEAR', p.createdAt) = :year) " +
           "GROUP BY " +
           "CASE WHEN :byMonth = true THEN FUNCTION('MONTH', p.createdAt) " +
           "     WHEN :byQuarter = true THEN FUNCTION('QUARTER', p.createdAt) " +
           "     ELSE FUNCTION('YEAR', p.createdAt) END")
    List<Object[]> stat(@Param("year") Integer year,
                        @Param("byMonth") boolean byMonth,
                        @Param("byQuarter") boolean byQuarter);
}
