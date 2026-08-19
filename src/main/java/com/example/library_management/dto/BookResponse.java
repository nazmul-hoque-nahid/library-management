package com.example.library_management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponse {
    private Long id;
    private String title;
    private String isbn;
    private String language;
    private String edition;
    private Integer availableCopies;
}
