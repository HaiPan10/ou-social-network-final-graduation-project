package com.ou.postservice.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ou.postservice.pojo.AnswerOption;

public interface AnswerOptionRepositoryJPA extends JpaRepository<AnswerOption, Long>{
    
}
