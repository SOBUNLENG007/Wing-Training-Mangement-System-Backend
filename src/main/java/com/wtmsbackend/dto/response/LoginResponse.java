package com.wtmsbackend.dto.response;

import com.wtmsbackend.security.JwtDecodedResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private UserResponse user;
    private String accessToken;
    private String refreshToken;
    private JwtDecodedResponse decodedToken;
}