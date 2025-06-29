package com.online_store.backend.api.user.auth.utils;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.online_store.backend.api.user.auth.dto.request.RegisterDto;
import com.online_store.backend.api.user.auth.exceptions.UserAlreadyExistsException;
import com.online_store.backend.api.user.user.entities.Role;
import com.online_store.backend.api.user.user.entities.User;
import com.online_store.backend.api.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthServiceUtils {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Role mapRoleParamToEnum(Role roleParam) {
        return roleParam == Role.CUSTOMER ? Role.CUSTOMER : Role.ADMIN;
    }

    public void validateNewUser(RegisterDto payload) {
        Optional<User> existingUser = userRepository.findByUsernameOrEmail(payload.getUsername(), payload.getEmail());
        if (existingUser.isPresent()) {
            if (existingUser.get().getUsername().equals(payload.getUsername())) {
                throw new UserAlreadyExistsException(
                        "User with username '" + payload.getEmail() + "'' already exists.");
            } else if (existingUser.get().getEmail().equals(payload.getEmail())) {
                throw new UserAlreadyExistsException("User with email '" + payload.getEmail() + "'' already exists.");
            }
        }
    }

    public String hashedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
