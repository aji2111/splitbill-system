package com.splitbill.expense_service.security;
import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication
        .WebAuthenticationDetailsSource;
import org.springframework.stereotype
        .Component;
import org.springframework.web.filter
        .OncePerRequestFilter;

import jakarta.servlet
        .FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    ) throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        if (
                authHeader == null
                || !authHeader.startsWith(
                        "Bearer "
                )
        ) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String token =
                authHeader.substring(7);

        boolean valid =
                jwtUtil.validateToken(token);

        if (valid) {

            String email =
                    jwtUtil.extractEmail(token);

            UsernamePasswordAuthenticationToken
                    authentication =

                    new UsernamePasswordAuthenticationToken(

                            email,

                            null,

                            List.of(
                                    new SimpleGrantedAuthority(
                                            "USER"
                                    )
                            )
                    );

            authentication.setDetails(

                    new WebAuthenticationDetailsSource()

                            .buildDetails(request)
            );

            SecurityContextHolder

                    .getContext()

                    .setAuthentication(authentication);
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}


