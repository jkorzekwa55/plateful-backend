package com.plateful.backend.security;

import com.plateful.backend.jwt.JwtConfig;
import com.plateful.backend.jwt.JwtUsernameAndPasswordAuthentication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class JwtUsernameAndPasswordAuthenticationFilterConfigurer extends AbstractHttpConfigurer<JwtUsernameAndPasswordAuthenticationFilterConfigurer, HttpSecurity> {
    public static JwtUsernameAndPasswordAuthenticationFilterConfigurer jwtUsernameAndPasswordAuthenticationFilterConfigurer() {
        return new JwtUsernameAndPasswordAuthenticationFilterConfigurer();
    }

    @Override
    public void configure(HttpSecurity http) {
        ApplicationContext context = http.getSharedObject(ApplicationContext.class);
        JwtConfig jwtConfig = context.getBean(JwtConfig.class);

        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        JwtUsernameAndPasswordAuthentication filter = new JwtUsernameAndPasswordAuthentication(authenticationManager, jwtConfig);

        filter.setFilterProcessesUrl("/login");

        http.addFilter(filter);
    }
}
