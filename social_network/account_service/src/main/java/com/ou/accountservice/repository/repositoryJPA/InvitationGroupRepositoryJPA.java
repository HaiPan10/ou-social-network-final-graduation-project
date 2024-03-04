package com.ou.accountservice.repository.repositoryJPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ou.accountservice.pojo.InvitationGroup;

public interface InvitationGroupRepositoryJPA extends JpaRepository<InvitationGroup, Long>{
    
}
