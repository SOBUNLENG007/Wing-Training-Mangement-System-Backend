package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.UserResetPasswordRequest;
import com.wtmsbackend.dto.request.UserUpdateRequest;
import com.wtmsbackend.dto.response.UserResponse;
import com.wtmsbackend.models.User;
import com.wtmsbackend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    private final UserService userService;

    @Operation(summary = "Get current user's profile")
    @GetMapping
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal User currentUser) {
        UserResponse userResponse = userService.getUserById(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .message("Profile fetched successfully")
                .success(true)
                .payload(userResponse)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Operation(summary = "Update current user's profile")
    @PutMapping
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal User currentUser,
                                           @Valid @RequestBody UserUpdateRequest request) {
        UserResponse userResponse = userService.updateUser(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .message("Profile updated successfully")
                .success(true)
                .payload(userResponse)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Operation(summary = "Change current user's password")
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal User currentUser,
                                            @Valid @RequestBody UserResetPasswordRequest request) {
        userService.userResetPassword(currentUser, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Password changed successfully")
                .success(true)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
