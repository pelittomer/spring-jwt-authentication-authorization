package com.online_store.backend.api.user.auth.service;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_store.backend.api.user.auth.dto.request.LoginDto;
import com.online_store.backend.api.user.auth.dto.request.RegisterDto;
import com.online_store.backend.api.user.auth.dto.response.AuthenticationResponse;
import com.online_store.backend.api.user.auth.dto.response.ErrorResponse;
import com.online_store.backend.api.user.auth.utils.AuthServiceUtils;
import com.online_store.backend.api.user.user.entities.Role;
import com.online_store.backend.api.user.user.entities.User;
import com.online_store.backend.api.user.user.repository.UserRepository;
import com.online_store.backend.module.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthServiceUtils authServiceUtils;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public String register(RegisterDto payload, Role roleParam) {
        Role role = authServiceUtils.mapRoleParamToEnum(roleParam);
        authServiceUtils.validateNewUser(payload);

        String hashedPassword = authServiceUtils.hashedPassword(payload.getPassword());

        User user = User.builder()
                .username(payload.getUsername())
                .email(payload.getEmail())
                .password(hashedPassword)
                .role(role)
                .build();
        userRepository.save(user);

        return "User '" + payload.getUsername() + "' registered successfully.";
    }

    public AuthenticationResponse login(LoginDto payload, Role roleParam) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword()));
        var user = userRepository.findByEmail(payload.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email:" + payload.getEmail()));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null || userEmail.trim().isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (!jwtService.isTokenValid(refreshToken, userDetails)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            var accessToken = jwtService.generateToken(userDetails);

            var authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            response.setContentType("application/json");
            response.setStatus(HttpStatus.OK.value());
            objectMapper.writeValue(response.getOutputStream(), authResponse);

        } catch (UsernameNotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            objectMapper.writeValue(response.getOutputStream(),
                    ErrorResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            objectMapper.writeValue(response.getOutputStream(),
                    ErrorResponse.builder().message("An unexpected error occurred: " + e.getMessage()).build());
        }
    }

}
