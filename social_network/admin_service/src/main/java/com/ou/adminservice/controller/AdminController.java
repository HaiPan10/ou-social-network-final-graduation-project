package com.ou.adminservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ou.adminservice.pojo.Account;
import com.ou.adminservice.service.interfaces.AccountService;
import com.ou.adminservice.service.interfaces.PostService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PostService postService;
    // @Autowired
    // private PostSurveyService postSurveyService;
    // @Autowired
    // private PostInvitationService postInvitationService;
    // @Autowired
    // private GroupService groupService;
    // @Autowired
    // private QuestionService questionService;

    @PatchMapping("accounts/update_status")
    public ResponseEntity<?> update(@RequestBody Map<String, String> request) {
        try {
            accountService.verifyAccount(Long.parseLong(request.get("id")), request.get("status"));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @DeleteMapping(path = "posts/delete/{postId}")
    // ResponseEntity<Object> delete(@PathVariable Long postId) {
    //     try {
    //         return ResponseEntity.status(HttpStatus.NO_CONTENT).body(postService.delete(postId, Long.valueOf(1)));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @PostMapping("posts/upload_survey")
    // public ResponseEntity<?> uploadSurvey(@RequestBody Post post) {
    //     try {
    //         return ResponseEntity.ok().body(postService.uploadPostSurvey(post, Long.valueOf(1)));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    @GetMapping(path = "accounts/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok().body(accountService.list());
    }

    // @PostMapping(path = "posts/upload_invitation")
    // public ResponseEntity<?> uploadInvitation(@RequestBody Post post) {
    //     try {
    //         return ResponseEntity.ok().body(postService.uploadPostInvitation(post, Long.valueOf(1)));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    @GetMapping(path = "statistics/users")
    public ResponseEntity<?> numberOfUsers(@RequestParam Map<String, String> params) {
        try {
            Object[][] result = accountService.stat(params);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @GetMapping(path = "stat/post")
    // public ResponseEntity<?> statPost(@RequestParam Map<String, String> params) {
    //     try {
    //         return ResponseEntity.ok().body(postService.stat(params));
    //     } catch (Exception e) {
    //         System.out.println("[ERROR] - " + e.getMessage());
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "stat/post_survey")
    // public ResponseEntity<?> statPostSurvey(@RequestParam Map<String, String> params) {
    //     try {
    //         return ResponseEntity.ok().body(postSurveyService.stat(params));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "stat/post_invitation")
    // public ResponseEntity<?> statPostInvitation(@RequestParam Map<String, String> params) {
    //     try {
    //         return ResponseEntity.ok().body(postInvitationService.stat(params));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "invitation_groups/{id}")
    // public ResponseEntity<?> getUsers(@PathVariable Long id) {
    //     try {
    //         return ResponseEntity.ok().body(groupService.getUsers(id));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "stat/question/{id}")
    // public ResponseEntity<?> statQuestion(@PathVariable Long id) {
    //     try {
    //         return ResponseEntity.ok().body(questionService.stat(id));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "stat/question/get_total/{id}")
    // public ResponseEntity<?> getTotal(@PathVariable Long id) {
    //     try {
    //         return ResponseEntity.ok().body(questionService.countUnchoiceOption(id));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }
}
