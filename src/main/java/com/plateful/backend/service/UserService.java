package com.plateful.backend.service;

import com.plateful.backend.dto.RegisterRequestDto;
import com.plateful.backend.dto.UserDto;
import com.plateful.backend.entity.User;
import com.plateful.backend.entity.UserByEmail;
import com.plateful.backend.repository.UserByEmailRepository;
import com.plateful.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private UserByEmailRepository userByEmailRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userByEmailRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDto registerNewUser(RegisterRequestDto registerRequestDto) {
        if (!userByEmailRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            User newUser = User.builder()
                    .id(UUID.randomUUID())
                    .email(registerRequestDto.getEmail())
                    .username(registerRequestDto.getUsername())
                    .password(passwordEncoder.encode(registerRequestDto.getPassword())).build();
            User savedUser = userRepository.save(newUser);
            userByEmailRepository.save(modelMapper.map(newUser, UserByEmail.class));
            return modelMapper.map(savedUser, UserDto.class);
        }
        throw null;
    }

    public UserByEmail loadByEmail(String email) throws UsernameNotFoundException {
        return userByEmailRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}
