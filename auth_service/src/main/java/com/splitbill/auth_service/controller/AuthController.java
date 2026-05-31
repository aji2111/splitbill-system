package com.splitbill.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.splitbill.auth_service.dto.request.LoginRequest;
import com.splitbill.auth_service.dto.request.RegisterRequest;
import com.splitbill.auth_service.dto.response.AuthResponse;
import com.splitbill.auth_service.dto.response.BaseResponse;
import com.splitbill.auth_service.dto.response.TokenValidationResponse;
import com.splitbill.auth_service.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication API",
        description = "API for authentication and authorization"
)
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Object>> register(

            @Valid
            @RequestBody
            RegisterRequest request
    ) {

        authService.register(request);

        BaseResponse<Object> response =
                BaseResponse.builder()
                        .message("Register success")
                        .data(null)
                        .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(

            @Valid
            @RequestBody
            LoginRequest request
    ) {

        AuthResponse authResponse =
                authService.login(request);

        BaseResponse<AuthResponse> response =
                BaseResponse.<AuthResponse>builder()
                        .message("Login success")
                        .data(authResponse)
                        .build();

        return ResponseEntity.ok(response);
    }



@Operation(summary = "Validate JWT token")
@GetMapping("/validate")
public ResponseEntity<
        BaseResponse<TokenValidationResponse>
        > validateToken(

        @RequestParam("token")
        String token
) {

    TokenValidationResponse validationResponse =
            authService.validateToken(token);

    BaseResponse<TokenValidationResponse>
            response =

            BaseResponse
                    .<TokenValidationResponse>builder()

                    .message("Token valid")

                    .data(validationResponse)

                    .build();

    return ResponseEntity.ok(response);
}



}

