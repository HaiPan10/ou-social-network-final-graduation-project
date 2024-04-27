package com.ou.accountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ou.accountservice.pojo.InvitationGroup;
import com.ou.accountservice.service.interfaces.GroupService;
import com.ou.accountservice.service.interfaces.InvitationGroupService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private InvitationGroupService invitationGroupService;

    @GetMapping("{inviteGroupId}")
    public InvitationGroup getInvitationGroup(@PathVariable Long invitationGroupId) {
        try {
            return groupService.retrieve(invitationGroupId);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping()
    public ResponseEntity<?> list() {
        try {
            return ResponseEntity.ok().body(invitationGroupService.list());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("users/{invitationGroupId}")
    public ResponseEntity<?> getUsers(@PathVariable Long invitationGroupId) {
        try {
            return ResponseEntity.ok().body(groupService.getUsers(invitationGroupId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
