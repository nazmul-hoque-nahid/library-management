package com.example.library_management.dto;

import com.example.library_management.entity.Fine;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class FineRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long borrowRecordId;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
    @NotBlank
    @Size(max = 300)
    private String reason;
    @NotNull
    private Fine.Category category;
}