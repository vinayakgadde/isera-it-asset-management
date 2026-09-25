package com.isera.assetmanagement.security.service;

import com.isera.assetmanagement.user.entity.User;
import com.isera.assetmanagement.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username
                        )
                );

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        user.getRoles()
                                .stream()
                                .map(role ->
                                        new SimpleGrantedAuthority(
                                                "ROLE_" + role.getName()
                                        )
                                )
                                .collect(Collectors.toSet())
                )
                .disabled(!user.getActive())
                .build();
    }
}