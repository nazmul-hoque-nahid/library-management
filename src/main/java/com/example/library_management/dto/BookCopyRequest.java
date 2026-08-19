package com.example.library_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookCopyRequest {
    @NotBlank(message = "Copy number is required")
    private String copyNumber;
    @NotNull(message = "Book is required")
    private Long bookId;
}
