package com.plateful.backend.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plateful.backend.entity.User;
import com.plateful.backend.repository.UserRepository;
import graphql.com.google.common.base.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (Objects.equals(path, "/register")
                || Objects.equals(path, "/authentication/refresh")
                || Objects.equals(path, "/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            response.setStatus(401);
            return;
        }

        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");


        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            String id = decodedJWT.getSubject();
            String type = decodedJWT.getClaim("type").toString();

            if (!type.equals("\"authentication\"")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Authentication token type is required.");
            }

            User user = userRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getEmail(), null);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception exception) {

            Map<String, String> error = new HashMap<>();
            error.put("Error", "Authentication failed");
            error.put("Message", exception.getMessage());
            response.setStatus(401);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }

        filterChain.doFilter(request, response);
    }
}
