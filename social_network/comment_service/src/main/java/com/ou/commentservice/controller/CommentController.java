// package com.ou.commentservice.controller;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PatchMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.ou.commentservice.configs.JwtService;
// import com.ou.commentservice.pojo.Comment;
// import com.ou.commentservice.pojo.User;
// import com.ou.commentservice.service.interfaces.CommentService;
// import com.ou.commentservice.utils.ValidationUtils;

// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.validation.Valid;


// @RestController
// // @CrossOrigin(origins = "http://localhost:3000")
// // @CrossOrigin(origins = "http://ousocialnetwork.id.vn/")
// // @CrossOrigin(origins = "*")
// @RequestMapping("api/comments")
// public class CommentController {
//     @Autowired
//     private CommentService commentService;

//     // @Autowired
//     // private WebAppValidator webAppValidator;

//     // @InitBinder()
//     // public void initBinderWeb(WebDataBinder binder) {
//     //     binder.setValidator(webAppValidator);
//     // }

//     @Autowired
//     private JwtService jwtService;

//     @PostMapping(path = "/{postId}")
//     public ResponseEntity<Object> create(@Valid @RequestBody Comment comment,
//             @PathVariable Long postId, HttpServletRequest httpServletRequest, BindingResult bindingResult) throws Exception {
//         try {
//             // System.out.println("[DEBUG] - START VALIDATION");
//             // webAppValidator.validate(comment, bindingResult);
//             if (bindingResult.hasErrors()) {
//                 return ResponseEntity.badRequest().body(ValidationUtils.getInvalidMessage(bindingResult));
//             }
//             Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
//             return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(comment, postId, userId));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         } 
//     }

//     @PostMapping(path = "{postId}/{commentId}")
//     public ResponseEntity<Object> create(@Valid @RequestBody Comment comment, @PathVariable Long postId,
//             @PathVariable Long commentId, HttpServletRequest httpServletRequest, BindingResult bindingResult) throws Exception {
//         try {
//             // System.out.println("[DEBUG] - START VALIDATION");
//             // webAppValidator.validate(comment, bindingResult);
//             if (bindingResult.hasErrors()) {
//                 return ResponseEntity.badRequest().body(ValidationUtils.getInvalidMessage(bindingResult));
//             }
//             Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
//             return ResponseEntity
//                 .status(HttpStatus.CREATED)
//                 .body(commentService.create(comment, postId, userId, commentId));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         } 
//     }

//     @GetMapping(path = "{postId}")
//     ResponseEntity<Object> loadComment(@PathVariable Long postId) {
//         try {
//             return ResponseEntity.ok(commentService.loadComment(postId));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @GetMapping(path = "{postId}/{commentId}")
//     ResponseEntity<Object> loadComment(@PathVariable Long postId, @PathVariable Long commentId) {
//         try {
//             return ResponseEntity.ok(commentService.loadComment(postId, commentId));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @PatchMapping()
//     ResponseEntity<Object> editComment(@Valid @RequestBody Comment comment, HttpServletRequest httpServletRequest, BindingResult bindingResult) {
//         try {
//             // webAppValidator.validate(comment, bindingResult);
//             if (bindingResult.hasErrors()) {
//                 return ResponseEntity.badRequest().body(ValidationUtils.getInvalidMessage(bindingResult));
//             }
//             Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
//             comment.setUserId(new User(userId));
//             return ResponseEntity.ok(commentService.editComment(comment));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @DeleteMapping(path = "{commentId}")
//     ResponseEntity<Object> delete(@PathVariable Long commentId, HttpServletRequest httpServletRequest) {
//         try {
//             Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
//             return ResponseEntity.status(HttpStatus.NO_CONTENT).body(commentService.delete(commentId, userId));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }
// }
