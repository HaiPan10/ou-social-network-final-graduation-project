package com.ou.accountservice.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ou.accountservice.pojo.Role;
import com.ou.accountservice.repository.repositoryJPA.RoleRepositoryJPA;
import com.ou.accountservice.service.interfaces.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepositoryJPA roleRepositoryJPA;

    @Override
    public Role retrieve(Long id) throws Exception {
        Optional<Role> roleOptional = roleRepositoryJPA.findById(id);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        } else {
            throw new Exception("Role not found!");
        }
    }

}
