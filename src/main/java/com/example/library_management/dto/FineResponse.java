package com.example.library_management.dto;

import com.example.library_management.entity.Fine;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class FineResponse {
    private Long id;
    private Long userId;
    private Long borrowRecordId;
    private BigDecimal amount;
    private String reason;
    private Fine.Category category;
    private Fine.FineStatus fineStatus;
    private LocalDateTime createdAt;
}