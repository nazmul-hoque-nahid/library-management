package com.example.library_management.dto;

import com.example.library_management.entity.BorrowRecord;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BorrowResponse {
    private Long borrowId;
    private Long userId;
    private Long librarianId;
    private LocalDateTime issuedAt;
    private LocalDateTime dueAt;
    private LocalDateTime returnedAt;
    private BorrowRecord.Status status;
    private Long copyId;

}
