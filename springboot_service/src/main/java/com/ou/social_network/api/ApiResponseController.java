package com.ou.social_network.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ou.social_network.configs.JwtService;
import com.ou.social_network.pojo.Response;
import com.ou.social_network.pojo.User;
import com.ou.social_network.service.interfaces.ResponseService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/responses")
public class ApiResponseController {
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
