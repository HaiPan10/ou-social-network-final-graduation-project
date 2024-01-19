package com.ou.social_network.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ou.social_network.pojo.GroupUser;
import com.ou.social_network.pojo.InvitationGroup;
import com.ou.social_network.pojo.User;
import com.ou.social_network.repository.repositoryJPA.GroupRepositoryJPA;
import com.ou.social_network.service.interfaces.GroupService;

@Service
@Transactional
public class GroupServiceImpl implements GroupService{
    // @Autowired
    // private GroupRepository groupRepository;

    @Autowired
    private GroupRepositoryJPA groupRepositoryJPA;

    @Override
    public InvitationGroup create(InvitationGroup group) {
        return groupRepositoryJPA.save(group);
    }

    @Override
    public void addUsers(Long groupId, List<User> users) throws Exception {
        InvitationGroup group = groupRepositoryJPA.findById(groupId).get();
        List<GroupUser> groupList = Optional.ofNullable(group.getGroupUsers()).orElse(new ArrayList<GroupUser>());
        users.forEach(u -> {
            GroupUser groupUser = new GroupUser();
            groupUser.setUserId(u);
            groupUser.setGroupId(group);
            groupList.add(groupUser);
        });
        group.setGroupUsers(groupList);
        groupRepositoryJPA.save(group);
    }

    @Override
    public List<Object[]> getUsers(Long groupId) {
        return groupRepositoryJPA.getUsers(groupId);
    }
    
}
