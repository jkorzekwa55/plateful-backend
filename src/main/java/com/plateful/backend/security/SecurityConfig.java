package com.plateful.backend.security;

import com.plateful.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.util.List;

import static com.plateful.backend.security.JwtTokenVerifierConfigurer.jwtTokenVerifierConfigurer;
import static com.plateful.backend.security.JwtUsernameAndPasswordAuthenticationFilterConfigurer.jwtUsernameAndPasswordAuthenticationFilterConfigurer;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("login", "register").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exc -> exc.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .with(jwtUsernameAndPasswordAuthenticationFilterConfigurer(), Customizer.withDefaults())
                .with(jwtTokenVerifierConfigurer(), Customizer.withDefaults());


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);

        return new ProviderManager(List.of(provider));
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        return userService;
    }


}
