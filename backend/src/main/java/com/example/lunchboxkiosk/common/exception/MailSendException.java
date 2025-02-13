package com.example.lunchboxkiosk.common.exception;

import com.example.lunchboxkiosk.common.exception.common.CustomException;
import lombok.Getter;

@Getter
public class MailSendException extends CustomException {
  public MailSendException(ErrorCode errorCode, String... args) {
    super(errorCode, args);
  }
}
