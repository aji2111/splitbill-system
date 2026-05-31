package com.splitbill.expense_service.security;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes()
        );
    }

    public boolean validateToken(
            String token
    ) {

        try {

            Jwts.parser()

                    .verifyWith(getKey())

                    .build()

                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public String extractEmail(
            String token
    ) {

        Claims claims = Jwts.parser()

                .verifyWith(getKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

        return claims.getSubject();
    }

    public UUID extractPublicId(
            String token
    ) {

        Claims claims = Jwts.parser()

                .verifyWith(getKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

        return UUID.fromString(
                claims.get(
                        "publicId",
                        String.class
                )
        );
    }
}


