package com.ou.adminservice.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class ServletSecurityConfig {
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        log.info(passwordEncoder.toString());
        dao.setPasswordEncoder(passwordEncoder);
        return dao;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
            throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider)
                .formLogin(login -> login.loginPage("/")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/?error"))
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/",
                                "/resources/**",
                                "/css/**",
                                "/img/**",
                                "/js/**",
                                "/styles/**",
                                "/vendor/**",
                                "/pages/index")
                        .permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(handling -> handling.accessDeniedHandler((requests, reponse, ex) -> {
                    System.out.printf("[EXCEPTION] - %s\n", ex.getMessage());
                    // reponse.sendRedirect(requests.getContextPath() + "/logout");
                    reponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                    reponse.getWriter().write("Forbidden!!!");
                }));
        return http.build();
    }
}
