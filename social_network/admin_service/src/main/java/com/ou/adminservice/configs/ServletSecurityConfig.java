package com.ou.adminservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ServletSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .formLogin(login -> login.loginPage("/")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/?error"))
                .logout(logout -> logout.logoutSuccessUrl("/"));

        http.authorizeHttpRequests(request -> request
                .requestMatchers("/",
                        "/resources/**",
                        "/css/**",
                        "/img/**",
                        "/js/**",
                        "/styles/**",
                        "/vendor/**",
                        "/pages/index")
                .permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
