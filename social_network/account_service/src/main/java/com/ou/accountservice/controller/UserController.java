package com.ou.accountservice.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ou.accountservice.pojo.User;
import com.ou.accountservice.service.interfaces.UserService;

@RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @CrossOrigin(origins = "http://ousocialnetwork.id.vn/")
// @CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/update_avatar")
    public ResponseEntity<Object> updateAvatar(MultipartFile uploadAvatar, @RequestHeader HttpHeaders headers)
            throws Exception {
        try {
            Long userId = Long.parseLong(headers.getFirst("AccountID"));
            return ResponseEntity.ok().body(userService.uploadAvatar(uploadAvatar, userId));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/update_cover")
    public ResponseEntity<Object> updateCover(MultipartFile uploadCover, @RequestHeader HttpHeaders headers)
            throws Exception {
        try {
            Long userId = Long.parseLong(headers.getFirst("AccountID"));
            return ResponseEntity.ok().body(userService.uploadCover(uploadCover, userId));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(value = "/update_information")
    public ResponseEntity<Object> updateInformation(@RequestBody User user, @RequestHeader HttpHeaders headers) {
        try {
            Long userId = Long.parseLong(headers.getFirst("AccountID"));
            return ResponseEntity.ok().body(userService.updateUser(user, userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping()
    public User getUser(@RequestParam Long userId) {
        try {
            return userService.retrieve(userId);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/list-by-id")
    public List<User> listUser(@RequestParam List<Long> listUserId) {
        return userService.list(listUserId);
    }

    @PostMapping(value = "/upload_avatar/{userId}", consumes = { MediaType.ALL_VALUE, "multipart/form-data" })
    public ResponseEntity<Object> uploadAvatar(@PathVariable("userId") Long userId,
            @RequestPart MultipartFile file) throws Exception {
        try {

            return ResponseEntity.ok().body(userService.uploadAvatar(file, userId));

        } catch (IOException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/list")
    public ResponseEntity<?> listUser() {
        return ResponseEntity.ok().body(userService.list());
    }
}
