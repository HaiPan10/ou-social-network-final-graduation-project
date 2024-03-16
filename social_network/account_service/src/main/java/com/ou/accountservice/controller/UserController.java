package com.ou.accountservice.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ou.accountservice.configs.JwtService;
import com.ou.accountservice.pojo.Account;
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
    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/update_avatar")
    public ResponseEntity<Object> updateAvatar(MultipartFile uploadAvatar, ServerHttpRequest httpServletRequest) throws Exception{
        try {
            Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            return ResponseEntity.ok().body(userService.uploadAvatar(uploadAvatar, userId));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/update_cover")
    public ResponseEntity<Object> updateCover(MultipartFile uploadCover, ServerHttpRequest httpServletRequest) throws Exception{
        try {
            Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            return ResponseEntity.ok().body(userService.uploadCover(uploadCover, userId));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(value = "/update_information")
    public ResponseEntity<Object> updateInformation(@RequestBody User user, ServerHttpRequest httpServletRequest){
        try {
            Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            return ResponseEntity.ok().body(userService.updateUser(user, userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Object> loadProfile(@PathVariable Long userId, ServerHttpRequest httpServletRequest, @RequestParam Map<String, String> params) {
        try {
            Long currentUserId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            return ResponseEntity.ok().body(userService.loadProfile(userId, currentUserId, params));
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

    @GetMapping("/list")
    public List<User> listUser(@RequestParam List<Long> listUserId) {
        return userService.list(listUserId);
    }
    
}
