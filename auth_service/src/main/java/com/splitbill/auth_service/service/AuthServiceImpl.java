package com.splitbill.auth_service.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.splitbill.auth_service.dto.request.LoginRequest;
import com.splitbill.auth_service.dto.request.RegisterRequest;
import com.splitbill.auth_service.dto.response.AuthResponse;
import com.splitbill.auth_service.dto.response.TokenValidationResponse;
import com.splitbill.auth_service.entity.User;
import com.splitbill.auth_service.exception.BadRequestException;
import com.splitbill.auth_service.repository.UserRepository;
import com.splitbill.auth_service.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest request) {

        String email =
                request.getEmail().toLowerCase();

        boolean exists = userRepository
                .findByEmail(email)
                .isPresent();

        if (exists) {

            log.warn(
                    "Register failed. Email already exists: {}",
                    email
            );

            throw new BadRequestException(
                    "Email already registered"
            );
        }

        User user = User.builder()

                .fullName(request.getFullName())

                .email(email)

                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )

                .build();

        userRepository.save(user);

        log.info(
                "User registered successfully: {}",
                user.getEmail()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        String email =
                request.getEmail().toLowerCase();

        User user = userRepository

                .findByEmail(email)

                .orElseThrow(() -> {

                    log.warn(
                            "Login failed. Email not found: {}",
                            email
                    );

                    return new BadRequestException(
                            "Invalid email or password"
                    );
                });

        boolean matches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!matches) {

            log.warn(
                    "Login failed. Invalid password: {}",
                    email
            );

            throw new BadRequestException(
                    "Invalid email or password"
            );
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getPublicId()
        );

        log.info(
                "User login success: {}",
                user.getEmail()
        );

        return new AuthResponse(

                token,

                user.getPublicId(),

                user.getFullName(),

                user.getEmail()
        );
    }

    @Override
    public TokenValidationResponse validateToken(
            String token
    ) {

        boolean valid =
                jwtUtil.validateToken(token);

        if (!valid) {

            log.warn("Token validation failed");

            throw new BadRequestException(
                    "Invalid token"
            );
        }

        try {

            String email =
                    jwtUtil.extractEmail(token);

            UUID publicId =
                    jwtUtil.extractPublicId(token);

            log.info(
                    "Token validated successfully: {}",
                    email
            );

            return TokenValidationResponse.builder()

                    .email(email)

                    .publicId(publicId)

                    .valid(true)

                    .build();

        } catch (Exception e) {

            log.error(
                    "Token parsing failed: {}",
                    e.getMessage()
            );

            throw new BadRequestException(
                    "Token parsing failed"
            );
        }
    }
}

