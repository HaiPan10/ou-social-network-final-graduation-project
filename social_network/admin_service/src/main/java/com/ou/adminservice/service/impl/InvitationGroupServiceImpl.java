package com.ou.adminservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.adminservice.pojo.InvitationGroup;
import com.ou.adminservice.service.interfaces.InvitationGroupService;

@Service
public class InvitationGroupServiceImpl implements InvitationGroupService{

    @Autowired
    private WebClient.Builder builder;

    @Override
    public List<InvitationGroup> list() {
        return builder.build().get()
            .uri("http://account-service/api/groups")
            .retrieve()
            .bodyToFlux(InvitationGroup.class)
            .collectList()
            .block();
    }

}
