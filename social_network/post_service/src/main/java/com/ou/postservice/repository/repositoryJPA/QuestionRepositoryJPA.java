package com.ou.postservice.repository.repositoryJPA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ou.postservice.pojo.Question;

public interface QuestionRepositoryJPA extends JpaRepository<Question, Long>{
    @Query("SELECT qo.id, qo.value, COUNT(ao.id) " +
           "FROM QuestionOption qo " +
           "LEFT JOIN qo.answerOptions ao " +
           "WHERE qo.questionId.id = ?1 " +
           "GROUP BY qo.id, qo.value")
    List<Object[]> stat(Long questionId);

    @Query("SELECT COUNT(*) FROM Answer a "
        + "LEFT JOIN AnswerOption ao on a.id = ao.answerId.id "
        + "WHERE a.questionId.id = ?1 and ao.id IS NULL")
    Integer countUnchoiceOption(Long questionId);
    
    @Query("SELECT questionText FROM Question q WHERE q.id = ?1")
    String getText(Long id);
}
