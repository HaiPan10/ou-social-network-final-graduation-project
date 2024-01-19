package com.ou.social_network.service.interfaces;

import java.util.List;

import com.ou.social_network.pojo.InvitationGroup;
import com.ou.social_network.pojo.User;


public interface GroupService {
    InvitationGroup create(InvitationGroup group);
    void addUsers(Long groupId, List<User> users) throws Exception;
    List<Object[]> getUsers(Long groupId);
}
