package com.ou.postservice.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ou.postservice.pojo.QuestionOption;

public interface QuestionOptionRepositoryJPA extends JpaRepository<QuestionOption, Long>{
    
}
