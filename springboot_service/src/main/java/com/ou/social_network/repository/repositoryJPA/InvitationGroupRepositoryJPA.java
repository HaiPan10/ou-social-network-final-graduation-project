package com.ou.social_network.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ou.social_network.pojo.InvitationGroup;

public interface InvitationGroupRepositoryJPA extends JpaRepository<InvitationGroup, Long>{
    
}
