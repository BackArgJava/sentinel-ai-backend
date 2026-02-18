package com.sentinel.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. The Bouncer looks at the HTTP Header called "Authorization"
        String authHeader = request.getHeader("Authorization");

        // 2. If the user has a token, and it starts with the word "Bearer "...
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // 3. We cut off the word "Bearer " to get the actual cryptographic token
            String token = authHeader.substring(7);

            // 4. The Scanner checks if the token is fake or expired
            if (jwtUtil.validateToken(token)) {

                // 5. It's real! Let's read the username written on it
                String username = jwtUtil.extractUsername(token);

                // 6. Tell Spring Security: "This user is verified. Open the door!"
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 7. Move to the next step in the chain (let them through, or block them if they had no token)
        filterChain.doFilter(request, response);
    }
}