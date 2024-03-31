package com.ou.apigateway.filter;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LogFilter implements WebFilter{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpHeaders headers = exchange.getResponse().getHeaders();
        List<String> header = headers.get("Access-Control-Allow-Origin");
        if(header != null) {
            log.info(String.valueOf(header.size()));
            for (String h : header) {
                log.info(h);
            }
        }
        
        return chain.filter(exchange);
    }
}
