package com.ou.apigateway.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.ou.apigateway.components.AuthenticationManager;
import com.ou.apigateway.components.SecurityContextHolder;
import com.ou.apigateway.filter.CustomFilter;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextHolder securityContextHolder;

    @Autowired
    private CustomFilter customFilter;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http.csrf(csrf -> csrf.disable())
                .formLogin(login -> login.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        http.exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                .authenticationEntryPoint(
                        (swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                .accessDeniedHandler(
                        (swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))))
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextHolder)
                .authorizeExchange(
                        authorizeExchangeSpec -> authorizeExchangeSpec
                                .pathMatchers(
                                        "/api/accounts/login",
                                        "/api/accounts/register",
                                        "/api/accounts/verify/**")
                                .permitAll()
                                .pathMatchers("/admin/**")
                                .denyAll()
                                .anyExchange().authenticated());

        http.addFilterAfter(customFilter, SecurityWebFiltersOrder.ANONYMOUS_AUTHENTICATION);
        return http.build();
    }
}
