package com.ou.postservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ou.postservice.configs.JwtService;
import com.ou.postservice.pojo.Post;
import com.ou.postservice.pojo.User;
import com.ou.postservice.service.interfaces.PostService;
import com.ou.postservice.utils.ValidationUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @CrossOrigin(origins = "http://ousocialnetwork.id.vn/")
// @CrossOrigin(origins = "*")
@RequestMapping("api/posts")
public class PostController {
    @Autowired 
    private PostService postService;

    // @Autowired
    // private WebAppValidator webAppValidator;

    // @InitBinder()
    // public void initBinderWeb(WebDataBinder binder) {
    //     binder.setValidator(webAppValidator);
    // }
    @Autowired
    private JwtService jwtService;

    @PostMapping(path = "/upload")
    public ResponseEntity<Object> upLoadPost(String postContent,
     List<MultipartFile> images, boolean isActiveComment, HttpServletRequest httpServletRequest) throws Exception {
        try {
            Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            return ResponseEntity.status(HttpStatus.CREATED).body(postService.uploadPost(postContent, userId, images, isActiveComment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    ResponseEntity<Object> update(List<MultipartFile> images, @Valid Post post, boolean isEditImage,
     BindingResult bindingResult, HttpServletRequest httpServletRequest) throws Exception {
        // webAppValidator.validate(post, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getInvalidMessage(bindingResult));
        }
        try {
            Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            post.setUserId(userId);
            return ResponseEntity.ok(postService.update(post, images, isEditImage));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "{postId}")
    ResponseEntity<Object> delete(@PathVariable Long postId, HttpServletRequest httpServletRequest) {
        try {
            Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(postService.delete(postId, userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping()
    ResponseEntity<Object> loadNewFeed(HttpServletRequest httpServletRequest, @RequestParam Map<String, String> params) {
        try {
            Long currentUserId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            return ResponseEntity.ok(postService.loadNewFeed(currentUserId, params));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getPost(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        try {
            Long currentUserId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            return ResponseEntity.ok().body(postService.getDetail(id, currentUserId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/profile")
    public List<Post> loadPost(@RequestParam Long profileId,
     @RequestParam Long currentUserId, 
     @RequestParam Map<String, String> params) {
        try {
            return postService.loadPost(profileId, currentUserId, params);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @GetMapping()
    public Post getPost(@RequestParam Long postId) {
        try {
            return postService.retrieve(postId);
        } catch (Exception e) {
            return null;
        }
    }
    
}