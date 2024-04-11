package com.ou.adminservice.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ou.adminservice.pojo.User;
import com.ou.adminservice.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Override
    public User create(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public User uploadAvatar(MultipartFile uploadAvatar, Long userId) throws IOException, Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadAvatar'");
    }

}
