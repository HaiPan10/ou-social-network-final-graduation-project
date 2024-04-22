package com.ou.accountservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ou.accountservice.pojo.InvitationGroup;
import com.ou.accountservice.repository.repositoryJPA.InvitationGroupRepositoryJPA;
import com.ou.accountservice.service.interfaces.InvitationGroupService;

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
