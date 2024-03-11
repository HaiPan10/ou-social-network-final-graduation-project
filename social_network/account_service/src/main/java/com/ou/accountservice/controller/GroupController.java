package com.ou.accountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ou.accountservice.pojo.InvitationGroup;
import com.ou.accountservice.service.interfaces.GroupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping()
    public InvitationGroup getInvitationGroup(@RequestParam Long invitationGroupId) {
        try {
            return groupService.retrieve(invitationGroupId);
        } catch (Exception e) {
            return null;
        }
    }
    
}
