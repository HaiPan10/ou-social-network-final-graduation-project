package com.ou.apigateway.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.ou.apigateway.configs.JwtService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SecurityContextHolder implements ServerSecurityContextRepository {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(jwtService.getAccessToken(exchange.getRequest()))
                .flatMap(token -> {
                    log.info(token);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(null, token, null);
                    return authenticationManager.authenticate(authentication)
                        .map(SecurityContextImpl::new);
                });
    }

}
