package com.example.lunchboxkiosk.common.exception.common;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import com.example.lunchboxkiosk.common.exception.InvalidValueException;
import com.example.lunchboxkiosk.common.exception.MailSendException;
import com.example.lunchboxkiosk.common.exception.NotFoundException;
import com.example.lunchboxkiosk.model.dto.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(ErrorCode errorCode) {
        log.error(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponseDto(errorCode));
    }

    //    +------------------------------------------------------------------+
    //    |                        4xx Client Errors                         |
    //    +------------------------------------------------------------------+
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(NotFoundException e) {
        return buildErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ErrorResponseDto> handleException(InvalidValueException e) {
        return buildErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        errorCode.setMessage(e.getMessage());
        return buildErrorResponse(ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(NoResourceFoundException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        errorCode.setMessage(e.getMessage());
        return buildErrorResponse(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        errorCode.setMessage(e.getMessage());
        return buildErrorResponse(ErrorCode.BAD_REQUEST);
    }

    //    +------------------------------------------------------------------+
    //    |                        5xx Server Errors                         |
    //    +------------------------------------------------------------------+
    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ErrorResponseDto> handleMailSendException(MailSendException e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        errorCode.setMessage(e.getMessage());
        return buildErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
