package com.example.library_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AuthorResponse {
    private Long id;
    private String name;
    private String biography;
    private String country;
}
