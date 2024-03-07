package com.ou.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ou.postservice.configs.JwtService;
import com.ou.postservice.pojo.Reaction;
import com.ou.postservice.service.interfaces.PostReactionService;

import jakarta.servlet.http.HttpServletRequest;



@RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @CrossOrigin(origins = "http://ousocialnetwork.id.vn/")
// @CrossOrigin(origins = "*")
@RequestMapping("api/post_reactions")
public class PostReactionController {
    @Autowired 
    private PostReactionService postReactionService;

    @Autowired
    private JwtService jwtService;

    @GetMapping(path = "{postId}/{reactionId}")
    public ResponseEntity<Object> getReactionUsers(@PathVariable Long postId, @PathVariable Long reactionId) throws Exception {
        try {
            return ResponseEntity.ok(postReactionService.getReactionUsers(postId, reactionId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    } 
    
    @RequestMapping(path = "{postId}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Object> reaction(@PathVariable Long postId, HttpServletRequest httpServletRequest, @RequestBody Reaction reaction) throws Exception {
        try {
            HttpStatus responseStatus = HttpStatus.OK;
            if (httpServletRequest.getMethod().equals("POST")) {
                responseStatus = HttpStatus.CREATED;
            }
            Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            return ResponseEntity.status(responseStatus).body(postReactionService.reaction(postId, userId, reaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "{postId}")
    ResponseEntity<Object> delete(@PathVariable Long postId, HttpServletRequest httpServletRequest) {
        try {
            Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(postReactionService.delete(postId, userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}