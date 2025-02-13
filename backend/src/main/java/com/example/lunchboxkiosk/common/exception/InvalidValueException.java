package com.example.lunchboxkiosk.common.exception;

import com.example.lunchboxkiosk.common.exception.common.CustomException;
import lombok.Getter;

@Getter
public class InvalidValueException extends CustomException {

    public InvalidValueException(ErrorCode errorCode, String... args) {
        super(errorCode, args);
    }
}
