package com.ou.apigateway.components;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.ou.apigateway.configs.JwtService;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        return Mono.just(jwtService.isValidAccessToken(token))
                .switchIfEmpty(Mono.empty())
                .map(authen -> {
                    // Set the credentials is the user id for easy use
                    String roleName = jwtService.getRoleNameFromToken(token);
                    String email = jwtService.getEmailFromToken(token);
                    Long id = jwtService.getIdFromToken(token);
                    Set<GrantedAuthority> authorities = new HashSet<>();
                    authorities.add(new SimpleGrantedAuthority(roleName));
                    return new UsernamePasswordAuthenticationToken(email, id, authorities);
                });
    }

}
