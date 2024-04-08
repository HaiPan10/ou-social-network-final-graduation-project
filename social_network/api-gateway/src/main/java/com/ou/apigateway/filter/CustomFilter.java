package com.ou.apigateway.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter implements WebFilter{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext().doOnNext(auth -> {
            if(auth != null && auth.getAuthentication().isAuthenticated()){
                String id = auth.getAuthentication().getCredentials().toString();
                log.info(String.format("Start to inject id %s in header", id));
                ServerHttpRequest request = exchange.getRequest();
                HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(request.getHeaders());
                if(httpHeaders.containsKey("AccountId")){
                    // Prevent the user intent to custom the account id
                    httpHeaders.set("AccountId", id);
                } else {
                    // Add new AccountId header
                    httpHeaders.add("AccountId", id);
                }

            }

        }).then(chain.filter(exchange));
    }

}
