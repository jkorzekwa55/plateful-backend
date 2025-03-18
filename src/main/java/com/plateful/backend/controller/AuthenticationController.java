package com.plateful.backend.controller;

import com.plateful.backend.dto.RegisterRequestDto;
import com.plateful.backend.dto.UserDto;
import com.plateful.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthenticationController {
    private UserService userService;

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(userService.registerNewUser(registerRequestDto));
    }
}
