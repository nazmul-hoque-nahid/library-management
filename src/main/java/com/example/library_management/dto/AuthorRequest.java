package com.example.library_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AuthorRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 3,max = 40,message = "name length must be between 3 and 40")
    @Pattern(regexp = "^[a-zA-Z ]+$",message = "Name only contain latter with space")
    private String name;
    @NotBlank(message = "Biography is required")
    @Size(max =1000 )
    private String biography;
    private LocalDate dob;
    private String country;
}
