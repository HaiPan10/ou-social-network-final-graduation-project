package com.ou.social_network.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.ou.social_network.filter.CustomAccessDeniedHandler;
import com.ou.social_network.filter.JwtTokenFilter;
import com.ou.social_network.filter.RestAuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    // @Autowired
    // private AuthenticationConfiguration config;

    @Autowired
    private CharacterEncodingFilter filter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() throws Exception {
        JwtTokenFilter jwtAuthenticationTokenFilter = new JwtTokenFilter();
        // jwtAuthenticationTokenFilter.setAuthenticationManager(authenticationManager(config));
        return jwtAuthenticationTokenFilter;
    }

    @Bean
    public RestAuthenticationEntryPoint restServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(passwordEncoder());
        return dao;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .formLogin(login -> login.loginPage("/")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/?error"))
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(mvc.pattern("/resources/**"),
                                mvc.pattern("/css/**"),
                                mvc.pattern("/img/**"),
                                mvc.pattern("/js/**"),
                                mvc.pattern("/styles/**"),
                                mvc.pattern("/vendor/**"),
                                mvc.pattern("/pages/index"),
                                mvc.pattern("/"))
                        .permitAll()
                        // .requestMatchers(mvc.pattern("/")).anonymous()
                        .requestMatchers(mvc.pattern("/admin/**")).hasAnyRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(handling -> handling.accessDeniedHandler((requests, reponse, ex) -> {
                    System.out.printf("[EXCEPTION] - %s\n", ex.getMessage());
                    // reponse.sendRedirect(requests.getContextPath() + "/logout");
                    reponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                    reponse.getWriter().write("Forbidden!!!");
                }))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc)
            throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers(mvc.pattern("/api/**")).disable())
                // .securityMatcher(mvc.pattern("/ws/**"))
                .securityMatcher(mvc.pattern("/api/**"))
                .httpBasic(basic -> basic.authenticationEntryPoint(restAuthenticationEntryPoint))
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests.requestMatchers(mvc.pattern("/api/accounts/login"),
                        mvc.pattern("/api/accounts/register"),
                        // "/api/email/verify/**",
                        mvc.pattern("/api/ws/**"),
                        mvc.pattern("/api/accounts/verify/**"))
                        .permitAll()
                        .requestMatchers(mvc.pattern("/admin/**")).hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(handling -> handling.accessDeniedHandler(customAccessDeniedHandler()))
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
