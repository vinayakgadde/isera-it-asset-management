package com.isera.assetmanagement.user.service;

import com.isera.assetmanagement.user.dto.UserRequest;
import com.isera.assetmanagement.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);
}