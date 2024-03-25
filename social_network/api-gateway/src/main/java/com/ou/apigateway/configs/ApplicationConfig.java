package com.ou.apigateway.configs;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:configs.properties")
public class ApplicationConfig {

    @Autowired
    private Environment environment;

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClient() {
        return WebClient.builder();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        String clientHostName = environment.getProperty("CLIENT_HOSTNAME");
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList(clientHostName));
        corsConfig.setMaxAge(3000L);
        corsConfig.setAllowedMethods(List.of("PUT", "GET", "POST", "DELETE", "OPTION"));
        corsConfig.setAllowedHeaders(List.of("*"));

        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                    .path("/api/ws/**")
                    .uri("http://127.0.0.1:8080/api/ws"))
                .build();
    }
}
