package com.ou.postservice.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ou.postservice.pojo.Reaction;

public interface ReactionRepositoryJPA extends JpaRepository<Reaction, Long>{
    
}
