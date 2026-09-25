package com.isera.assetmanagement.security.service;

import com.isera.assetmanagement.exception.ResourceNotFoundException;
import com.isera.assetmanagement.user.entity.User;
import com.isera.assetmanagement.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal()
                == null) {

            throw new ResourceNotFoundException(
                    "Authenticated user could not be determined"
            );
        }

        String username =
                authentication.getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated user not found: "
                                        + username
                        )
                );
    }
}