package com.example.library_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateRequest {
    @Email(message = "Invalid email format")
    private String email;
    @Size(min = 3,max = 40,message = "name length must be between 3 and 40")
    @Pattern(regexp = "^[a-zA-Z ]+$",message = "Name only contain latter with space")
    private String name;
    @Size(max = 500,message = "too long")
    private String address;
}
