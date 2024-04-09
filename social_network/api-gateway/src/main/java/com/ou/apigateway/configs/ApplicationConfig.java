package com.ou.apigateway.configs;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import com.ou.apigateway.pojo.Account;

import reactor.core.publisher.Mono;

@Configuration
@PropertySource("classpath:configs.properties")
public class ApplicationConfig implements WebFluxConfigurer {

    @Autowired
    private Environment environment;

    @Autowired
    private WebClient.Builder builder;

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClient() {
        return WebClient.builder();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
        WebFluxConfigurer.super.addResourceHandlers(registry);
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        String clientHostName = environment.getProperty("CLIENT_HOSTNAME");
        CorsConfiguration corsConfig = new CorsConfiguration();
        // corsConfig.setAllowedOrigins(Arrays.asList(clientHostName));
        corsConfig.setMaxAge(3000L);
        corsConfig.setAllowedMethods(List.of("PUT", "GET", "POST", "DELETE",
                "OPTION"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowedOrigins(Collections.singletonList(clientHostName));

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
                        .uri("lb://realtime-service"))
                .build();
    }

    @Bean
    public ReactiveUserDetailsService  userDetailsService() {
        return email -> builder.build().get()
                .uri("http://account-service/api/accounts/email",
                    uriBuilder -> uriBuilder.queryParam("email", email).build())
                .retrieve()
                .bodyToMono(Account.class)
                .flatMap(account -> {
                    if (account != null) {
                        // Create UserDetails using User.builder
                        Set<GrantedAuthority> authorities = new HashSet<>();
                        authorities.add(new SimpleGrantedAuthority(account.getRoleId().getName()));
                        return Mono.just(User.withUsername(account.getEmail())
                                .password(account.getPassword())
                                .authorities(authorities)
                                .build());
                    } else {
                        // Return empty UserDetails if account is not found
                        return Mono.empty();
                    }
                });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
