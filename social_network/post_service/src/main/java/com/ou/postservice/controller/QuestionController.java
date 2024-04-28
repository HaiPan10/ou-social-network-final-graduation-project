package com.ou.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ou.postservice.service.interfaces.QuestionService;

@RestController
@RequestMapping("api/quests")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("stat/{questionId}")
    public ResponseEntity<?> stat(@PathVariable Long questionId) {
        try {
            return ResponseEntity.ok().body(questionService.stat(questionId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("stat/count/{questionId}")
    public ResponseEntity<?> countUnchoiceOption(@PathVariable Long questionId) {
        try {
            return ResponseEntity.ok().body(questionService.countUnchoiceOption(questionId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("text/{questionId}")
    public ResponseEntity<?> getText(@PathVariable Long questionId) {
        try {
            return ResponseEntity.ok().body(questionService.getText(questionId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
