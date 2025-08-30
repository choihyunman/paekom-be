package com.paekom.global.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        // RuntimeException의 message 필드에 ErrorCode의 message를 전달
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
