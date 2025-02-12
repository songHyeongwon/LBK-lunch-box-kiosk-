package com.example.lunchboxkiosk.common.exception;

import com.example.lunchboxkiosk.common.exception.common.CustomException;
import lombok.Getter;

@Getter
public class NotFoundException extends CustomException {

    public NotFoundException(ErrorCode errorCode, String... args) {
        super(errorCode, args);
    }
}
