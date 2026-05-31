package com.splitbill.auth_service.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private UUID publicId;

    private String fullName;

    private String email;
}
