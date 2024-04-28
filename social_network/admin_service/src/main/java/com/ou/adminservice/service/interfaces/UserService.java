package com.ou.adminservice.service.interfaces;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ou.adminservice.pojo.User;

public interface UserService {
    User uploadAvatar(MultipartFile uploadAvatar, Long userId) throws IOException, Exception;
}
