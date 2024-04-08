package com.ou.accountservice.service.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ou.accountservice.pojo.User;


public interface UserService {
    User create(User user);
    User uploadAvatar(MultipartFile uploadAvatar, Long userId) throws IOException, Exception;
    User retrieve(Long userId) throws Exception;
    User uploadCover(MultipartFile uploadCover, Long userId) throws IOException, Exception;
    User updateUser(User user, Long userId);
    List<User> list(List<Long> listUserId);
    List<User> list();
}
