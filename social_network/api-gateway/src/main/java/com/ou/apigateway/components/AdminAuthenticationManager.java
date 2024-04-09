package com.ou.apigateway.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component("adminAuthenticationManager")
@Slf4j
public class AdminAuthenticationManager extends AbstractUserDetailsReactiveAuthenticationManager{

    public AdminAuthenticationManager(PasswordEncoder passwordEncoder)
    {
        super();
        setPasswordEncoder(passwordEncoder);
    }

    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    @Override
    protected Mono<UserDetails> retrieveUser(String username) {
        log.info("Start authenticate admin page");
        return userDetailsService.findByUsername(username);
    }

}
