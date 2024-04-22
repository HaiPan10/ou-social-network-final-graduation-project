package com.ou.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingGlobalPreFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Debug the request coming first
        log.info("Pre-logging start");

        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        String token = httpHeaders.getFirst("Authorization");
        log.info("Token: " + token);
        return chain.filter(exchange);
    }
}