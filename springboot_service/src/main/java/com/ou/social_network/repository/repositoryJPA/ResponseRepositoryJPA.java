package com.ou.social_network.repository.repositoryJPA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ou.social_network.pojo.Answer;
import com.ou.social_network.pojo.Response;

public interface ResponseRepositoryJPA extends JpaRepository<Response, Long>{
    @Query("SELECT a " +
           "FROM Answer a " +
           "WHERE a.questionId.id = ?1 " +
           "AND a.value IS NOT NULL")
    List<Answer> getTextAnswers(Long questionId);
}
