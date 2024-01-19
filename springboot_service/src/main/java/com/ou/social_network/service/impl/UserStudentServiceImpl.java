package com.ou.social_network.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ou.social_network.pojo.UserStudent;
import com.ou.social_network.repository.repositoryJPA.UserStudentRepositoryJPA;
import com.ou.social_network.service.interfaces.UserStudentService;

@Service
public class UserStudentServiceImpl implements UserStudentService {
    @Autowired
    private UserStudentRepositoryJPA userStudentRepositoryJPA;

    @Override
    public UserStudent create(UserStudent userStudent) throws Exception {
        if (userStudentRepositoryJPA.findByStudentIdentical(userStudent.getStudentIdentical()).isPresent()) {
            throw new Exception("Mã số sinh viên đã được sử dụng!");
        }
        return userStudentRepositoryJPA.save(userStudent);
    }
}
