package com.isera.assetmanagement.security.config;

import com.isera.assetmanagement.security.filter.JwtAuthenticationFilter;
import com.isera.assetmanagement.security.handler.RestAccessDeniedHandler;
import com.isera.assetmanagement.security.handler.RestAuthenticationEntryPoint;
import com.isera.assetmanagement.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JsonMapper jsonMapper;

    public SecurityConfig(
            CustomUserDetailsService customUserDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JsonMapper jsonMapper
    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jsonMapper = jsonMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        customUserDetailsService
                );

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authenticationProvider(authenticationProvider())

                .authorizeHttpRequests(auth -> auth

                        // =====================================================
                        // Swagger / OpenAPI
                        // =====================================================

                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        )
                        .permitAll()

                        // =====================================================
                        // Authentication
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/auth/**"
                        )
                        .permitAll()

                        // =====================================================
                        // Department Management
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/departments/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "IT_ADMIN"
                        )

                        // =====================================================
                        // Location Management
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/locations/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "IT_ADMIN"
                        )

                        // =====================================================
                        // Asset Category Management
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/asset-categories/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "IT_ADMIN"
                        )

                        // =====================================================
                        // Vendor Management
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/vendors/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "IT_ADMIN"
                        )

                        // =====================================================
                        // Employee Management
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/employees/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "IT_ADMIN"
                        )

                        // =====================================================
                        // Asset Management
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/assets/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "IT_ADMIN",
                                "IT_SUPPORT"
                        )

                        // =====================================================
                        // Asset Assignment
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/asset-assignments/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "IT_ADMIN",
                                "IT_SUPPORT"
                        )

                        // =====================================================
                        // Maintenance Management
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/maintenance/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "IT_ADMIN",
                                "IT_SUPPORT"
                        )

                        // =====================================================
                        // Audit Logs
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/audit-logs/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "IT_ADMIN"
                        )

                        // =====================================================
                        // User Management
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/users/**"
                        )
                        .hasRole("ADMIN")

                        // =====================================================
                        // Current User / Self-Service
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/me/**"
                        )
                        .authenticated()

                        // =====================================================
                        // Dashboard
                        // =====================================================

                        .requestMatchers(
                                "/api/v1/dashboard/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "IT_ADMIN",
                                "MANAGER"
                        )

                        // =====================================================
                        // Everything Else
                        // =====================================================

                        .anyRequest()
                        .authenticated()
                )

                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(
                                        new RestAuthenticationEntryPoint(
                                                jsonMapper
                                        )
                                )
                                .accessDeniedHandler(
                                        new RestAccessDeniedHandler(
                                                jsonMapper
                                        )
                                )
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}