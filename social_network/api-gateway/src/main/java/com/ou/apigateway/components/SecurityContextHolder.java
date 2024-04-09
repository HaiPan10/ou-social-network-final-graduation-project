package com.ou.apigateway.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.ou.apigateway.configs.JwtService;

import reactor.core.publisher.Mono;

@Component
public class SecurityContextHolder implements ServerSecurityContextRepository {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(jwtService.getAccessToken(exchange.getRequest()))
                .flatMap(token -> {
                    // log.info(token);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(null, token, null);
                    return authenticationManager.authenticate(authentication)
                        .map(SecurityContextImpl::new);
                });
    }

}
