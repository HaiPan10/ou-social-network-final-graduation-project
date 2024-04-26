package com.ou.accountservice.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.accountservice.event.UploadAvatarEvent;
import com.ou.accountservice.pojo.User;
import com.ou.accountservice.repository.repositoryJPA.UserRepositoryJPA;
import com.ou.accountservice.service.interfaces.CloudinaryService;
import com.ou.accountservice.service.interfaces.UserService;


@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private Environment env;
    @Autowired
    private UserRepositoryJPA userRepositoryJPA;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public User create(User user) {
        return userRepositoryJPA.save(user);
    }

    @Override
    public User uploadAvatar(MultipartFile uploadAvatar, Long userId) throws Exception {
        try {
            String newUrl = cloudinaryService.uploadImage(uploadAvatar);
            User persistUser = retrieve(userId);
            String oldUrl = persistUser.getAvatar();
            persistUser.setAvatar(newUrl);
            User returnUser = userRepositoryJPA.save(persistUser);
            String defaultAvatar = this.env.getProperty("DEFAULT_AVATAR").toString();
            if (oldUrl != null && !oldUrl.equals(defaultAvatar)) {
                cloudinaryService.deleteImage(oldUrl);
            }
            applicationEventPublisher.publishEvent(
                new UploadAvatarEvent(this, "uploadAvatarTopic", userId, newUrl));
            return returnUser;
        } catch (IOException e) {
            throw new IOException("Fail to upload avatar");
        }
    }

    @Override
    public User uploadCover(MultipartFile uploadCover, Long userId) throws Exception {
        try {
            String newUrl = cloudinaryService.uploadImage(uploadCover);
            User persistUser = retrieve(userId);
            String oldUrl = persistUser.getCoverAvatar();
            String defaultCover = this.env.getProperty("DEFAULT_COVER").toString();
            persistUser.setCoverAvatar(newUrl);
            User returnUser = userRepositoryJPA.save(persistUser);
            if (!oldUrl.equals(defaultCover)) {
                cloudinaryService.deleteImage(oldUrl);
            }
            return returnUser;
        } catch (IOException e) {
            throw new IOException("Fail to upload avatar");
        }
    }

    @Override
    public User retrieve(Long userId) throws Exception {
        Optional<User> userOptional = userRepositoryJPA.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new Exception("Không tìm thấy người dùng");
        }
    }

    @Override
    public User updateUser(User user, Long userId) {
        User persistentUser = userRepositoryJPA.findById(userId).orElse(null);
        if(user.getDob() != null){
            persistentUser.setDob(user.getDob());
        }

        if(user.getFirstName() != null){
            persistentUser.setFirstName(user.getFirstName());
        }

        if(user.getLastName() != null){
            persistentUser.setLastName(user.getLastName());
        }

        return userRepositoryJPA.save(persistentUser);
    }

    @Override
    public List<User> list(List<Long> listUserId) {
        return userRepositoryJPA.list(listUserId);
    }

    @Override
    public List<User> list(){
        return userRepositoryJPA.findAll();
    }

}
