package com.ou.apigateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GatewayCorsFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    HttpHeaders headers = exchange.getResponse().getHeaders();
                    List<String> header = headers.get("Access-Control-Allow-Origin");
                    HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(exchange.getResponse().getHeaders());
                    if (header != null) {
                        log.info(String.valueOf(header.size()));
                        for (String h : header) {
                            log.info(h);
                        }
                        httpHeaders.set("Access-Control-Allow-Origin", "https://ousocialnetwork.id.vn");
                        httpHeaders.set("Access-Control-Allow-Credentials", "true");
                    }

                })
        );
    }
}
