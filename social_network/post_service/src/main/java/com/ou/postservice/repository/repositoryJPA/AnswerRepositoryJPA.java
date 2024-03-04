package com.ou.postservice.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ou.postservice.pojo.Answer;

public interface AnswerRepositoryJPA extends JpaRepository<Answer, Long>{
    
}
