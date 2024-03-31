package com.ou.apigateway.configs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@PropertySource("classpath:configs.properties")
@Slf4j
public class ApplicationConfig {

    @Autowired
    private Environment environment;

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClient() {
        return WebClient.builder();
    }

    // @Bean
    // public CorsWebFilter corsWebFilter() {
    //     String clientHostName = environment.getProperty("CLIENT_HOSTNAME");
    //     CorsConfiguration corsConfig = new CorsConfiguration();
    //     // corsConfig.setAllowedOrigins(Arrays.asList(clientHostName));
    //     corsConfig.setMaxAge(3000L);
    //     corsConfig.setAllowedMethods(List.of("PUT", "GET", "POST", "DELETE",
    //             "OPTION"));
    //     corsConfig.setAllowedHeaders(List.of("*"));
    //     corsConfig.setAllowedOrigins(Collections.singletonList(clientHostName));

    //     corsConfig.setAllowCredentials(true);

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", corsConfig);

    //     return new CorsWebFilter(source);
    // }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/ws/**")
                        .uri("lb://realtime-service"))
                .build();
    }

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
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
                            httpHeaders.set("Access-Control-Allow-Origin", "http://localhost:3000");
                            httpHeaders.set("Access-Control-Allow-Credentials", "true");
                        } else {
                            log.info("Null");
                        }
                        log.info("Global Post Filter executed");
                    }));
        };
    }
}
