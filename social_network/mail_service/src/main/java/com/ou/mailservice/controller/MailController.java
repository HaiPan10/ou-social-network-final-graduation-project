package com.ou.mailservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import com.ou.mailservice.service.interfaces.MailService;


@RestController
// @CrossOrigin(origins = "http://localhost:3000")
// @CrossOrigin(origins = "http://34.101.76.209:80")
// @CrossOrigin(origins = "http://ousocialnetwork.id.vn/")
// @CrossOrigin(origins = "*")
@RequestMapping("api/email")
public class MailController {
    @Autowired
    private MailService mailService;

    @GetMapping(path = "/verify")
    public void SendVerificationEmail(@RequestHeader HttpHeaders headers) throws Exception {
        try {
            Long accountId = Long.parseLong(headers.getFirst("AccountID"));
            mailService.sendVerificationEmail(accountId);
        } catch (Exception e) {
            return ;
        }
    }
}
