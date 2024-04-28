package com.ou.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ou.postservice.pojo.Response;
import com.ou.postservice.service.interfaces.ResponseService;

@RestController
@RequestMapping("api/responses")
public class ResponseController {
    @Autowired
    private ResponseService responseService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Response response, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            Long userId = Long.parseLong(headers.getFirst("AccountID"));
            response.setUserId(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseService.create(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> list(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(responseService.getTextAnswers(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
