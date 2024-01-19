package com.ou.social_network.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ou.social_network.pojo.Reaction;

public interface ReactionRepositoryJPA extends JpaRepository<Reaction, Long>{
    
}
