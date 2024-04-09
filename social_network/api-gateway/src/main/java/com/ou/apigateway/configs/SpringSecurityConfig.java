package com.ou.apigateway.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.cors.reactive.CorsWebFilter;
import com.ou.apigateway.filter.CustomFilter;
import com.ou.apigateway.filter.LogFilter;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity()
public class SpringSecurityConfig {

    @Autowired
    public ReactiveUserDetailsService userDetailsService;

    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Autowired
    private ServerSecurityContextRepository securityContextRepository;

    @Autowired
    @Qualifier("adminAuthenticationManager")
    private ReactiveAuthenticationManager adminAuthenticationManager;

    @Bean("adminSecurityContextRepository")
    public ServerSecurityContextRepository securityContextRepository() {
        return new WebSessionServerSecurityContextRepository();
    }

    @Autowired
    @Qualifier("adminSecurityContextRepository")
    private ServerSecurityContextRepository adminSecurityContextRepository;

    @Bean()
    public ServerLogoutHandler serverLogoutHandler() {
        return new DelegatingServerLogoutHandler(
                new SecurityContextServerLogoutHandler(),
                new WebSessionServerLogoutHandler());
    }

    @Autowired
    private ServerLogoutHandler logoutHandler;

    @Autowired
    private CustomFilter customFilter;

    @Autowired
    private CorsWebFilter corsWebFilter;

    @Autowired
    private LogFilter logFilter;

    @Bean
    @Order(1)
    public SecurityWebFilterChain adminFilterChain(ServerHttpSecurity http) {
        return http.csrf(csrf -> csrf.disable())
                .formLogin(login -> login.loginPage("/login")
                        .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler(
                                "/admin/dashboard")))
                .logout(logout -> logout
                        .logoutHandler(logoutHandler)
                        .logoutUrl("/logout"))
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint(
                                (swe, e) -> Mono
                                        .fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler(
                                (swe, e) -> Mono
                                        .fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))))
                .securityContextRepository(adminSecurityContextRepository)
                .authenticationManager(adminAuthenticationManager)
                .authorizeExchange(
                        authorizeExchangeSpec -> authorizeExchangeSpec
                                .pathMatchers(
                                        "/login",
                                        "/resources/**",
                                        "/css/**",
                                        "/img/**",
                                        "/js/**",
                                        "/styles/**",
                                        "/vendor/**",
                                        "/pages/index")
                                .permitAll()
                                .anyExchange().hasAnyRole("ADMIN"))
                .build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http.csrf(csrf -> csrf.disable())
                .formLogin(login -> login.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint(
                                (swe, e) -> Mono
                                        .fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler(
                                (swe, e) -> Mono
                                        .fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))))
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange(
                        authorizeExchangeSpec -> authorizeExchangeSpec
                                .pathMatchers(
                                        "/api/accounts/login",
                                        "/api/accounts/register",
                                        "/api/accounts/verify/**",
                                        "/api/ws/**")
                                .permitAll()
                                .anyExchange().authenticated())
                .addFilterBefore(logFilter, SecurityWebFiltersOrder.CORS)
                .addFilterAfter(corsWebFilter, SecurityWebFiltersOrder.ANONYMOUS_AUTHENTICATION)
                .addFilterAfter(customFilter, SecurityWebFiltersOrder.ANONYMOUS_AUTHENTICATION)
                .build();
    }
}
