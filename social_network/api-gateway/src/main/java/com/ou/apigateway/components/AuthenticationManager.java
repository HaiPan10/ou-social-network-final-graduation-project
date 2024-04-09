package com.ou.apigateway.components;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.ou.apigateway.configs.JwtService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("Start authenticate");
        String token = authentication.getCredentials().toString();
        return Mono.just(jwtService.isValidAccessToken(token))
                .map(authen -> {

                    if (authen) {
                        // Set the credentials is the user id for easy use
                        String roleName = jwtService.getRoleNameFromToken(token);
                        String email = jwtService.getEmailFromToken(token);
                        Long id = jwtService.getIdFromToken(token);
                        Set<GrantedAuthority> authorities = new HashSet<>();
                        authorities.add(new SimpleGrantedAuthority(roleName));
                        return new UsernamePasswordAuthenticationToken(email, id, authorities);
                    }
                    return new AnonymousAuthenticationToken("anonymous", "anonymous",
                            AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
                });
    }

}
