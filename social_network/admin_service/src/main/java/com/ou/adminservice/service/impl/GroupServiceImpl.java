package com.ou.adminservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.adminservice.pojo.InvitationGroup;
import com.ou.adminservice.pojo.User;
import com.ou.adminservice.service.interfaces.GroupService;

@Service
public class GroupServiceImpl implements GroupService{

    @Autowired
    private WebClient.Builder builder;

    @Override
    public InvitationGroup create(InvitationGroup group) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public void addUsers(Long groupId, List<User> users) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUsers'");
    }

    @Override
    public Object[][] getUsers(Long groupId) {
        return builder.build().get()
                .uri("http://account-service/api/groups/users",
                    uriBuilder -> uriBuilder.pathSegment("{groupId}").build(groupId))
                .retrieve()
                .bodyToMono(Object[][].class)
                .block();
    }

    @Override
    public InvitationGroup retrieve(Long id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retrieve'");
    }

}
