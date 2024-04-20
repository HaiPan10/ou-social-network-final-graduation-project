package com.ou.postservice.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ou.postservice.pojo.Post;
import com.ou.postservice.service.interfaces.PostInvitationService;
import com.ou.postservice.service.interfaces.PostService;
import com.ou.postservice.service.interfaces.PostSurveyService;
import com.ou.postservice.utils.ValidationUtils;

import jakarta.validation.Valid;

@RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @CrossOrigin(origins = "http://ousocialnetwork.id.vn/")
// @CrossOrigin(origins = "*")
@RequestMapping("api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private PostSurveyService postSurveyService;

    @Autowired
    private PostInvitationService postInvitationService;

    // @Autowired
    // private WebAppValidator webAppValidator;

    // @InitBinder()
    // public void initBinderWeb(WebDataBinder binder) {
    //     binder.setValidator(webAppValidator);
    // }

    @PostMapping(path = "/upload")
    public ResponseEntity<Object> upLoadPost(String postContent,
     List<MultipartFile> images, boolean isActiveComment, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            Long userId = Long.parseLong(headers.getFirst("AccountID"));
            return ResponseEntity.status(HttpStatus.CREATED).body(postService.uploadPost(postContent, userId, images, isActiveComment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    ResponseEntity<Object> update(List<MultipartFile> images, @Valid Post post, boolean isEditImage,
     BindingResult bindingResult, @RequestHeader HttpHeaders headers) throws Exception {
        // webAppValidator.validate(post, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getInvalidMessage(bindingResult));
        }
        try {
            Long userId = Long.parseLong(headers.getFirst("AccountID"));
            post.setUserId(userId);
            return ResponseEntity.ok(postService.update(post, images, isEditImage));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "{postId}")
    ResponseEntity<Object> delete(@PathVariable Long postId, @RequestHeader HttpHeaders headers) {
        try {
            Long userId = Long.parseLong(headers.getFirst("AccountID"));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(postService.delete(postId, userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping()
    ResponseEntity<Object> loadNewFeed(@RequestHeader HttpHeaders headers, @RequestParam Map<String, String> params) {
        try {
            Long currentUserId = Long.parseLong(headers.getFirst("AccountID"));
            return ResponseEntity.ok(postService.loadNewFeed(currentUserId, params));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getPost(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        try {
            Long currentUserId = Long.parseLong(headers.getFirst("AccountID"));
            return ResponseEntity.ok().body(postService.getDetail(id, currentUserId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Object> loadProfile(@PathVariable Long userId, @RequestHeader HttpHeaders headers, @RequestParam Map<String, String> params) {
        try {
            Long currentUserId = Long.parseLong(headers.getFirst("AccountID"));
            return ResponseEntity.ok().body(postService.loadProfile(userId, currentUserId, params));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping(path = "/fetch-post")
    public Post getPost(@RequestParam Long postId) {
        try {
            return postService.retrieve(postId);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping(path = "/stat/post")
    public ResponseEntity<?> stat(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.ok().body(postService.stat(params));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "stat/post_survey")
    public ResponseEntity<?> statPostSurvey(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.ok().body(postSurveyService.stat(params));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "stat/post_invitation")
    public ResponseEntity<?> statPostInvitation(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.ok().body(postInvitationService.stat(params));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}