package com.example.library_management.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BorrowRequest {
    private Long librarianId;
    private Long userId;
    private Long bookCopyId;
}
