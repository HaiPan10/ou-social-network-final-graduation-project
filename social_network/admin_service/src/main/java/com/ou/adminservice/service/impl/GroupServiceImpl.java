package com.ou.adminservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.adminservice.service.interfaces.GroupService;

@Service
public class GroupServiceImpl implements GroupService{

    @Autowired
    private WebClient.Builder builder;

    @Override
    public Object[][] getUsers(Long groupId) {
        return builder.build().get()
                .uri("http://account-service/api/groups/users",
                    uriBuilder -> uriBuilder.pathSegment("{groupId}").build(groupId))
                .retrieve()
                .bodyToMono(Object[][].class)
                .block();
    }

}
