package com.wtmsbackend.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtDecodedResponse {
    private Map<String, Object> header;
    private Map<String, Object> payload;
    private String signature;
}