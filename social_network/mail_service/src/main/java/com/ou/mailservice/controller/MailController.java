package com.ou.mailservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ou.mailservice.configs.JwtService;
import com.ou.mailservice.service.interfaces.MailService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @CrossOrigin(origins = "http://34.101.76.209:80")
// @CrossOrigin(origins = "http://ousocialnetwork.id.vn/")
// @CrossOrigin(origins = "*")
@RequestMapping("api/email")
public class MailController {
    @Autowired
    private MailService mailService;
    @Autowired
    private JwtService jwtService;

    @GetMapping(path = "/verify")
    public void SendVerificationEmail(HttpServletRequest httpServletRequest) throws Exception {
        try {
            Long accountId = Long.parseLong(jwtService.getAccountId(httpServletRequest));
            mailService.sendVerificationEmail(accountId);
        } catch (Exception e) {
            return ;
        }
    }
}
