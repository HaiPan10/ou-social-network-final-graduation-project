package com.ou.social_network.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ou.social_network.pojo.QuestionOption;

public interface QuestionOptionRepositoryJPA extends JpaRepository<QuestionOption, Long>{
    
}
