package com.online_store.backend.api.user.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDto {
    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Please provide a valid email address.")
    @Size(max = 50, message = "Email cannot exceed 100 characters.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 6, max = 30, message = "Password must be at least 6 characters long.")
    private String password;
}
