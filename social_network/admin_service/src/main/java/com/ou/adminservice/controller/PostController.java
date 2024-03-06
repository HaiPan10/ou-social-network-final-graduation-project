// package com.ou.adminservice.controller;

// import java.util.List;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.env.Environment;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.validation.ObjectError;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.multipart.MultipartFile;

// import com.ou.adminservice.pojo.InvitationGroup;
// import com.ou.adminservice.pojo.Post;
// import com.ou.adminservice.service.interfaces.AccountService;
// import com.ou.adminservice.service.interfaces.InvitationGroupService;
// import com.ou.adminservice.service.interfaces.PostService;
// import com.ou.adminservice.service.interfaces.QuestionService;
// import com.ou.adminservice.service.interfaces.ResponseService;

// import jakarta.validation.Valid;


// @Controller
// @RequestMapping("/admin/posts")
// public class PostController {
//     @Autowired
//     private PostService postService;
//     @Autowired
//     private Environment env;
//     @Autowired
//     private AccountService accountService;
//     @Autowired
//     private InvitationGroupService invitationGroupService;
//     @Autowired
//     private QuestionService questionService;
//     @Autowired
//     private ResponseService responseService;

//     @GetMapping
//     public String posts(Model model, @RequestParam Map<String, String> params) {
//         List<Post> posts = postService.search(params);
//         model.addAttribute("posts", posts);
//         Integer pageSize = Integer.parseInt(env.getProperty("POST_PAGE_SIZE"));
//         model.addAttribute("counter", Math.ceil(postService.countPosts(params) * 1.0 / pageSize));
//         int page;
//         if (params != null) {
//             String p = params.get("page");
//             if (p != null && !p.isEmpty()) {
//                 page = Integer.parseInt(p);
//             } else {
//                 page = 1;
//             }

//             String kw = params.get("kw");
//             if (kw != null) {
//                 model.addAttribute("kw", kw);
//             }

//         } else {
//             page = 1;
//         }
//         model.addAttribute("currentPage", page);
//         if (params != null && params.get("status") != null) {
//             model.addAttribute("status", params.get("status"));
//         }
//         return "pages/posts";
//     }

//     @GetMapping("{id}")
//     public String retrieve(@PathVariable(value = "id") Long postId, Model model,
//             @RequestParam Map<String, String> params) {
//         try {
//             Post post = postService.retrieve(postId);
//             model.addAttribute("post", post);
//             if (post.getPostSurvey() != null) {
//                 return "pages/postSurveyDetail";
//             } else if (post.getPostInvitation() != null) {
//                 System.out.println(post.getPostInvitation().getPostInvitationUsers());
//                 return "pages/postInvitationDetail";
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return "pages/postDetail";
//     }

//     @GetMapping("/upload")
//     public String uploadPost(Model model, @RequestParam(name = "status", required = false) String status) {
//         Post post = new Post();
//         model.addAttribute("post", post);
//         List<Object[]> accountList = accountService.list();
//         List<InvitationGroup> invitationGroups = invitationGroupService.list();
//         model.addAttribute("accountList", accountList);
//         model.addAttribute("invitationGroups", invitationGroups);
//         // if (status != null) {
//         // model.addAttribute("status", status);
//         // }
//         return "pages/uploadPost";
//     }

//     @PostMapping("/upload")
//     public String add(@ModelAttribute("post") @Valid Post post,
//             @RequestPart(value = "images", required = false) List<MultipartFile> images, BindingResult bindingResult)
//             throws Exception {
//         try {
//             if(bindingResult.hasErrors()){
//                 return "pages/uploadPost";
//             }
//             postService.uploadPost(post.getContent(), Long.valueOf(1), images, post.getIsActiveComment());
//             return "redirect:/admin/posts?status=success";
//         } catch (Exception e) {
//             bindingResult.addError(new ObjectError("exceptionError", e.getMessage()));
//             return "pages/uploadPost";
//         }
//     }

//     @GetMapping(path = "/survey_question/{id}")
//     public String statQuestion(Model model, @PathVariable Long id) {
//         List<Object[]> list = questionService.stat(id);
//         if (list.isEmpty()) {
//             model.addAttribute("listTextAnswer", responseService.getTextAnswers(id));
//         }
//         model.addAttribute("questionText", questionService.getText(id));
//         model.addAttribute("id", id);

//         return "pages/questionDetail";
//     }
// }
