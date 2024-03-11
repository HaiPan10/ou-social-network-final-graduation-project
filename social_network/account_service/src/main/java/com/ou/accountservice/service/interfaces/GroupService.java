package com.ou.accountservice.service.interfaces;

import java.util.List;

import com.ou.accountservice.pojo.InvitationGroup;
import com.ou.accountservice.pojo.User;


public interface GroupService {
    InvitationGroup create(InvitationGroup group);
    void addUsers(Long groupId, List<User> users) throws Exception;
    List<Object[]> getUsers(Long groupId);
    InvitationGroup retrieve(Long id) throws Exception;
}
