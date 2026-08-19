package com.example.library_management.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
public class BookRequest {
    @NotBlank(message = "title is required")
    private String title;
    @NotBlank(message = "isbn is required")
    private String isbn;
    @NotBlank(message = "language required")
    private String language;
    @NotBlank(message = "edition is required")
    private String edition;
    @NotNull(message = "Total copy is required")
    private Long publisherId;
    @NotEmpty
    private Set<Long> authorIds;
    @NotEmpty
    private Set<Long> categoryIds;
}
