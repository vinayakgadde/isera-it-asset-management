package com.isera.assetmanagement.security.filter;

import com.isera.assetmanagement.security.jwt.JwtService;
import com.isera.assetmanagement.security.service.CustomUserDetailsService;
import com.isera.assetmanagement.user.entity.User;
import com.isera.assetmanagement.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService,
            UserRepository userRepository
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String token =
                authHeader.substring(7);

        try {

            String username =
                    jwtService.extractUsername(token);

            Long tokenVersion =
                    jwtService.extractTokenVersion(token);

            if (username != null
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                User user =
                        userRepository
                                .findByUsername(username)
                                .orElse(null);

                if (user == null) {

                    filterChain.doFilter(
                            request,
                            response
                    );

                    return;
                }

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(
                                        username
                                );

                boolean valid =
                        jwtService.isTokenValid(
                                token,
                                userDetails.getUsername(),
                                user.getTokenVersion()
                        );

                if (valid) {

                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );
                }
            }

        } catch (Exception exception) {

            /*
             * Invalid, expired, malformed or
             * revoked-version JWT.
             *
             * Continue without authentication.
             * A protected endpoint will then return 401.
             */
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}