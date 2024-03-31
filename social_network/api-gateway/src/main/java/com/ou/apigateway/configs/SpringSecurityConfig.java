package com.ou.apigateway.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.cors.reactive.CorsWebFilter;

import com.netflix.discovery.converters.Auto;
import com.ou.apigateway.components.AuthenticationManager;
import com.ou.apigateway.components.SecurityContextHolder;
import com.ou.apigateway.filter.CustomFilter;
import com.ou.apigateway.filter.LogFilter;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity()
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

//     @Autowired
//     private CorsWebFilter corsWebFilter;

    @Autowired
    private LogFilter logFilter;

    // @Bean
    // public SecurityWebFilterChain adminFilterChain(ServerHttpSecurity http) {
    //     http.csrf(csrf -> csrf.disable())
    //             // .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
    //             //         .authenticationEntryPoint(
    //             //                 (swe, e) -> Mono
    //             //                         .fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
    //             //         .accessDeniedHandler(
    //             //                 (swe, e) -> Mono
    //             //                         .fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))))
    //             .authorizeExchange(
    //                     authorizeExchangeSpec -> authorizeExchangeSpec
    //                             .pathMatchers(HttpMethod.POST, "/").permitAll()
    //                             .pathMatchers(
    //                                     "/",
    //                                     "/resources/**",
    //                                     "/css/**",
    //                                     "/img/**",
    //                                     "/js/**",
    //                                     "/styles/**",
    //                                     "/vendor/**",
    //                                     "/pages/index",
    //                                     "/admin/**")
    //                             .permitAll());
    //     return http.build();
    // }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http.csrf(csrf -> csrf.disable())
                .formLogin(login -> login.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint(
                                (swe, e) -> Mono
                                        .fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler(
                                (swe, e) -> Mono
                                        .fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))))
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextHolder)
                .authorizeExchange(
                        authorizeExchangeSpec -> authorizeExchangeSpec
                                .pathMatchers(
                                        "/api/accounts/login",
                                        "/api/accounts/register",
                                        "/api/accounts/verify/**",
                                        "/api/ws/**",
                                        "/**")
                                .permitAll()
                                .anyExchange().authenticated());

        http
                .addFilterBefore(logFilter, SecurityWebFiltersOrder.CORS)
        //     .addFilterAfter(corsWebFilter, SecurityWebFiltersOrder.ANONYMOUS_AUTHENTICATION)
                .addFilterAfter(customFilter, SecurityWebFiltersOrder.ANONYMOUS_AUTHENTICATION);
        return http.build();
    }
}
