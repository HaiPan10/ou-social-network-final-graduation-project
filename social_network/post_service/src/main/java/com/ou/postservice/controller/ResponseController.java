package com.ou.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ou.postservice.configs.JwtService;
import com.ou.postservice.pojo.Response;
import com.ou.postservice.pojo.User;
import com.ou.postservice.service.interfaces.ResponseService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/responses")
public class ResponseController {
    @Autowired
    private ResponseService responseService;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Response response, HttpServletRequest httpServletRequest) throws Exception {
        try {
            Long userId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            response.setUserId(new User(userId));
            System.out.println(response);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseService.create(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    } 
}
