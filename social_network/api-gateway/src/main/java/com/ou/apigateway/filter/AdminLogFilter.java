package com.ou.apigateway.filter;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AdminLogFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // log.info("Admin log web filter");
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext -> SecurityContext.getAuthentication())
                .doOnNext(auth -> {
                    // log.info(auth.toString());
                })
                .then(chain.filter(exchange)
                        .then(Mono.fromRunnable(() -> {

                        })));
    }

}
