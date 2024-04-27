package com.ou.adminservice.service.interfaces;

import java.util.List;

import com.ou.adminservice.pojo.InvitationGroup;
import com.ou.adminservice.pojo.User;


public interface GroupService {
    InvitationGroup create(InvitationGroup group);
    void addUsers(Long groupId, List<User> users) throws Exception;
    Object[][] getUsers(Long groupId);
    InvitationGroup retrieve(Long id) throws Exception;
}
