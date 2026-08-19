package com.example.library_management.dto;

import com.example.library_management.entity.BookCopy;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookCopyUpdateRequest {
    private String copyNumber;
    private BookCopy.Status status;
    private Long bookId;
}
