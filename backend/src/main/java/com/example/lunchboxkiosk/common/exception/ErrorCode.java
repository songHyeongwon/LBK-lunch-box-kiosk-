package com.example.lunchboxkiosk.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 4XX COMMON
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized access."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden access."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found."),
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "Invalid value. value: {0}"),

    // 5XX COMMON
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."),

    // Custom
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "Menu not found with id: {0}"),
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "Brand not found with id: {0}"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category not found with id: {0}"),
    REDIS_KEY_NOT_FOUND(HttpStatus.NOT_FOUND, "Redis key not found. key(s): {0}");

    private final HttpStatus httpStatus;
    private final String defaultMessage;
    @Setter
    private String message;

    public void setArgs(String ... args) {
        if (args == null || args.length == 0) {
            this.message = defaultMessage;
        } else {
            this.message = MessageFormat.format(defaultMessage, (Object[]) args);
        }
    }
}
