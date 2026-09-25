package com.isera.assetmanagement.user.service.impl;

import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.exception.ResourceNotFoundException;
import com.isera.assetmanagement.role.entity.Role;
import com.isera.assetmanagement.role.repository.RoleRepository;
import com.isera.assetmanagement.user.dto.UserRequest;
import com.isera.assetmanagement.user.dto.UserResponse;
import com.isera.assetmanagement.user.entity.User;
import com.isera.assetmanagement.user.repository.UserRepository;
import com.isera.assetmanagement.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new DuplicateResourceException(
                    "Username already exists: "
                            + request.getUsername()
            );
        }

        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists: "
                            + request.getEmail()
            );
        }

        Set<Role> roles =
                loadRoles(request.getRoleIds());

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        user.setActive(
                request.getActive()
        );

        user.setRoles(roles);

        user.setTokenVersion(0L);

        User savedUser =
                userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(
            Long id,
            UserRequest request
    ) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: "
                                                + id
                                )
                        );

        if (!user.getUsername().equals(
                request.getUsername())
                && userRepository.existsByUsername(
                request.getUsername())) {

            throw new DuplicateResourceException(
                    "Username already exists: "
                            + request.getUsername()
            );
        }

        if (!user.getEmail().equals(
                request.getEmail())
                && userRepository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists: "
                            + request.getEmail()
            );
        }

        Set<Role> roles =
                loadRoles(request.getRoleIds());

        user.setUsername(
                request.getUsername()
        );

        user.setEmail(
                request.getEmail()
        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        user.setActive(
                request.getActive()
        );

        user.setRoles(roles);

        /*
         * Invalidate all existing access tokens
         * when user information/security
         * permissions are changed.
         */
        user.setTokenVersion(
                user.getTokenVersion() + 1
        );

        User updatedUser =
                userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: "
                                                + id
                                )
                        );

        userRepository.delete(user);
    }

    private Set<Role> loadRoles(
            Set<Long> roleIds
    ) {

        Set<Role> roles =
                new HashSet<>();

        for (Long roleId : roleIds) {

            Role role =
                    roleRepository.findById(
                            roleId
                    ).orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Role not found with id: "
                                            + roleId
                            )
                    );

            roles.add(role);
        }

        return roles;
    }

    private UserResponse mapToResponse(
            User user
    ) {

        UserResponse response =
                new UserResponse();

        response.setId(
                user.getId()
        );

        response.setUsername(
                user.getUsername()
        );

        response.setEmail(
                user.getEmail()
        );

        response.setFirstName(
                user.getFirstName()
        );

        response.setLastName(
                user.getLastName()
        );

        response.setActive(
                user.getActive()
        );

        Set<String> roleNames =
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(
                                Collectors.toSet()
                        );

        response.setRoles(roleNames);

        response.setCreatedAt(
                user.getCreatedAt()
        );

        response.setUpdatedAt(
                user.getUpdatedAt()
        );

        return response;
    }
}