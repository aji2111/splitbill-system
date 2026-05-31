package com.splitbill.auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TokenValidationResponse {

    private String email;

    private UUID publicId;

    private boolean valid;
}


