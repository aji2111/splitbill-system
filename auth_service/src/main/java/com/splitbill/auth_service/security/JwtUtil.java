package com.splitbill.auth_service.security;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey123456";

    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(
                    SECRET.getBytes()
            );

    private static final long EXPIRATION =
            1000 * 60 * 60 * 24;

    public String generateToken(
            String email,
            UUID publicId
    ) {

        return Jwts.builder()

                .subject(email)

                .claim(
                        "publicId",
                        publicId.toString()
                )

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION
                        )
                )

                .signWith(KEY)

                .compact();
    }

    public String extractEmail(String token) {

        Claims claims = Jwts.parser()

                .verifyWith(KEY)

                .build()

                .parseSignedClaims(token)

                .getPayload();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {


    try {

        Jwts.parser()

                .verifyWith(KEY)

                .build()

                .parseSignedClaims(token);

        return true;

    } catch (
            io.jsonwebtoken.JwtException
            | IllegalArgumentException e
    ) {

        return false;
    }
}


public UUID extractPublicId(String token) {

    Claims claims = Jwts.parser()

            .verifyWith(KEY)

            .build()

            .parseSignedClaims(token)

            .getPayload();

    String publicId =
            claims.get(
                    "publicId",
                    String.class
            );

    if (publicId == null) {

        throw new RuntimeException(
                "Public ID not found in token"
        );
    }

    return UUID.fromString(publicId);
}



}
