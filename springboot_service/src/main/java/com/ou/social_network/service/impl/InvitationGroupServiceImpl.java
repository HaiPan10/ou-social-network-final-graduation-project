package com.ou.social_network.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ou.social_network.pojo.InvitationGroup;
import com.ou.social_network.repository.repositoryJPA.InvitationGroupRepositoryJPA;
import com.ou.social_network.service.interfaces.InvitationGroupService;

@Service
public class InvitationGroupServiceImpl implements InvitationGroupService {
    // @Autowired
    // private InvitationGroupRepository invitationGroupRepository;

    @Autowired
    private InvitationGroupRepositoryJPA invitationGroupRepositoryJPA;

    @Override
    public List<InvitationGroup> list() {
        return invitationGroupRepositoryJPA.findAll();
    }
    
}
