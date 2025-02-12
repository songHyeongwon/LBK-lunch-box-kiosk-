package com.example.lunchboxkiosk.model.dto.response;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponseDto {

    private final int status;
    private final String error;
    private final String message;
    private final String timestamp;

    public ErrorResponseDto(ErrorCode errorCode) {
        this.status = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.message = errorCode.getMessage();
        this.timestamp = LocalDateTime.now().toString();
    }
}
