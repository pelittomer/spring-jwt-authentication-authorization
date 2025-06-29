package com.online_store.backend.api.user.auth.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.user.auth.dto.request.RegisterDto;
import com.online_store.backend.api.user.auth.utils.AuthServiceUtils;
import com.online_store.backend.api.user.user.entities.Role;
import com.online_store.backend.api.user.user.entities.User;
import com.online_store.backend.api.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthServiceUtils authServiceUtils;

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

}
