package com.example.lunchboxkiosk.common.exception.common;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode, String ... args) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorCode.setArgs(args);
    }
}
