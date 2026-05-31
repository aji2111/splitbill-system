package com.splitbill.auth_service.service;

import com.splitbill.auth_service.dto.request.LoginRequest;
import com.splitbill.auth_service.dto.request.RegisterRequest;
import com.splitbill.auth_service.dto.response.AuthResponse;
import com.splitbill.auth_service.dto.response.TokenValidationResponse;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
    
    TokenValidationResponse validateToken( String token );
}
