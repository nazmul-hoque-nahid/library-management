package com.example.library_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @Email(message = "Invalid email format")
    @NotBlank(message = "email is required")
    private String email;
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    @Size(min=8,max=100)
    @NotBlank(message = "email is required")
    private String password;
}
