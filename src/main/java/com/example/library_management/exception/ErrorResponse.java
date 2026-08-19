package com.example.library_management.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorResponse {
    private Integer status;
    private String message;
    private LocalDateTime atTime;

    public ErrorResponse(int status,String message,LocalDateTime now) {
        this.status=status;
        this.message=message;
        this.atTime=now;
    }
}
