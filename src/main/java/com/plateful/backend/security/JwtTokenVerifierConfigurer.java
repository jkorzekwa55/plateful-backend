package com.plateful.backend.security;

import com.plateful.backend.jwt.JwtConfig;
import com.plateful.backend.jwt.JwtTokenVerifier;
import com.plateful.backend.jwt.JwtUsernameAndPasswordAuthentication;
import com.plateful.backend.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class JwtTokenVerifierConfigurer extends AbstractHttpConfigurer<JwtUsernameAndPasswordAuthenticationFilterConfigurer, HttpSecurity> {
    public static JwtTokenVerifierConfigurer jwtTokenVerifierConfigurer() {
        return new JwtTokenVerifierConfigurer();
    }

    @Override
    public void configure(HttpSecurity http) {
        ApplicationContext context = http.getSharedObject(ApplicationContext.class);
        JwtConfig jwtConfig = context.getBean(JwtConfig.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        JwtTokenVerifier filter = new JwtTokenVerifier(jwtConfig, userRepository);

        http.addFilterAfter(filter, JwtUsernameAndPasswordAuthentication.class);
    }
}
