package com.ou.social_network.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private ApplicationContext applicationContext;
    // @Autowired
    // CloudinaryService uploadFileService;
    // @Autowired
    // private PostService postService;
    // @Autowired
    // private PostSurveyService postSurveyService;
    // @Autowired
    // private PostInvitationService postInvitationService;
    // @Autowired
    // private AccountService accountService;
    // @Autowired
    // private InvitationGroupService invitationGroupService;
    // @Autowired
    // private GroupService groupService;
    // @Autowired
    // private QuestionService questionService;
    // @Autowired
    // private PostSurveyService postSurveyService;
    // @Autowired
    // private PostInvitationService postInvitationService;

    @GetMapping("beans")
    public ResponseEntity<String> retrives() {
        StringBuilder sb = new StringBuilder();
        System.out.println("[DEUBG] - Bean count: " + applicationContext.getBeanDefinitionCount());
        for (String bean : applicationContext.getBeanDefinitionNames()) {
            sb.append(bean + "\n");
        }
        return ResponseEntity.ok().body(sb.toString());
    }

    // @GetMapping("/cloudinary/delete")
    // public ResponseEntity<Object> deleteImage() throws IOException {
    // return ResponseEntity.ok().body(uploadFileService.deleteImage());
    // }

    // @PostMapping(path = "posts")
    // public ResponseEntity<?> uploadSurvey(@RequestBody Post post, BindingResult bindingResult) {
    //     try{
    //         System.out.println("[DEBUG] - " + post);
    //         Post p = postService.uploadPostSurvey(post, 1);
    //         return ResponseEntity.ok().body(p);
    //     }
    //     catch(Exception e){
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "accounts")
    // public ResponseEntity<?> list() {
    //     return ResponseEntity.ok().body(accountService.list());
    // }

    // @GetMapping(path = "accounts/{id}")
    // public ResponseEntity<?> list(Integer id) {
    //     try {
    //         return ResponseEntity.ok().body(postService.retrieve(id));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "posts/{id}")
    // public ResponseEntity<?> getPost(@PathVariable Integer id) {
    //     try {
    //         return ResponseEntity.ok().body(postService.retrieve(id));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "postSurvey/{id}")
    // public ResponseEntity<?> retrievePostSurvey(@PathVariable Integer id) {
    //     try {
    //         return ResponseEntity.ok().body(postSurveyService.retrieve(id));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @PostMapping(path = "postInvitation")
    // public ResponseEntity<?> uploadPostInvitation(@RequestBody Post postInvitation) {
    //     try {
    //         return ResponseEntity.ok().body(postService.uploadPostInvitation(postInvitation, 1));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "statistics")
    // public ResponseEntity<?> numberOfUsers(@RequestParam Map<String, String> params){
    //     try {
    //         return ResponseEntity.ok().body(accountService.stat(params));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "stat/post")
    // public ResponseEntity<?> statPost(@RequestParam Map<String, String> params){
    //     try {
    //         return ResponseEntity.ok().body(postService.stat(params));
    //     } catch (Exception e) {
    //         System.out.println("[ERROR] - " + e.getMessage());
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "stat/post_survey")
    // public ResponseEntity<?> statPostSurvey(@RequestParam Map<String, String> params){
    //     try {
    //         return ResponseEntity.ok().body(postSurveyService.stat(params));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "stat/post_invitation")
    // public ResponseEntity<?> statPostInvitation(@RequestParam Map<String, String> params){
    //     try {
    //         return ResponseEntity.ok().body(postInvitationService.stat(params));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "invitation_groups")
    // public ResponseEntity<?> getInvitationGroup(){
    //     try {
    //         return ResponseEntity.ok().body(invitationGroupService.list());
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "invitation_groups/{id}")
    // public ResponseEntity<?> getUsers(@PathVariable Integer id){
    //     try {
    //         return ResponseEntity.ok().body(groupService.getUsers(id));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "stat/question/{id}")
    // public ResponseEntity<?> statQuestion(@PathVariable Integer id){
    //     try {
    //         return ResponseEntity.ok().body(questionService.stat(id));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }

    // @GetMapping(path = "stat/question/get_total/{id}")
    // public ResponseEntity<?> getTotal(@PathVariable Integer id){
    //     try {
    //         return ResponseEntity.ok().body(questionService.countUnchoiceOption(id));
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }
}
