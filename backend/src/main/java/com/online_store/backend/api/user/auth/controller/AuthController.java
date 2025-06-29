package com.online_store.backend.api.user.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.user.auth.dto.request.LoginDto;
import com.online_store.backend.api.user.auth.dto.request.RegisterDto;
import com.online_store.backend.api.user.auth.dto.response.AuthenticationResponse;
import com.online_store.backend.api.user.auth.service.AuthService;
import com.online_store.backend.api.user.user.entities.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/{role}/sign-in")
    public ResponseEntity<AuthenticationResponse> signIn(@Valid @RequestBody LoginDto payload,
            @PathVariable Role role) {
        AuthenticationResponse response = authService.login(payload, role);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{role}/sign-up")
    public ResponseEntity<String> signUp(
            @Valid @RequestBody RegisterDto payload,
            @PathVariable Role role) {
        String registrationMessage = authService.register(payload, role);

        return new ResponseEntity<>(registrationMessage, HttpStatus.CREATED);
    }

    @GetMapping("/refresh")
    public void refresh(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

}
