package com.example.library_management.dto;

import com.example.library_management.entity.BookCopy;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookCopyResponse {
    private Long id;
    private String copyNumber;
    private LocalDateTime acquiredDate;
    private BookCopy.Status status;
    private Long bookId;
}
