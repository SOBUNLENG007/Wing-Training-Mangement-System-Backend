package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.UserRequest;
import com.wtmsbackend.dto.request.UserUpdateRequest;
import com.wtmsbackend.dto.response.UserResponse;
import com.wtmsbackend.models.Until.Role;
import com.wtmsbackend.models.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserById(Integer id);

    UserResponse getUserByEmail(String email);

    UserResponse createUser(UserRequest request);

    UserResponse updateUser(Integer id, UserUpdateRequest request);

    UserResponse updateRole(Integer id, Role role);

    void deleteUser(Integer id);

    // Add this new signature
    List<UserResponse> getUsersByDepartment(Integer departmentId);

    void userResetPassword(User user, String oldPassword, String newPassword);

    // Add this method to fetch the User entity directly
    User getUserEntityById(Integer id);

    /**
     * Get all users by role
     */
    List<UserResponse> getUsersByRole(Role role);
}