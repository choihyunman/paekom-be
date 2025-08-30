package com.paekom.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     Response의 에러 코드에 맞춰 HttpStatus를 설정해주시기 바랍니다.

     // fail
     BAD_REQUEST(400)
     UNAUTHORIZED(401)
     PAYMENT_REQUIRED(402)
     FORBIDDEN(403)
     NOT_FOUND(404)
     METHOD_NOT_ALLOWED(405)
     INTERNAL_SERVER_ERROR(500)
     **/

    // Global Exception
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_HTTP_MESSAGE_BODY(HttpStatus.BAD_REQUEST,"HTTP 요청 바디의 형식이 잘못되었습니다."),
    UNSUPPORTED_HTTP_METHOD(HttpStatus.METHOD_NOT_ALLOWED,"지원하지 않는 HTTP 메서드입니다."),
    BIND_ERROR(HttpStatus.BAD_REQUEST, "요청 파라미터 바인딩에 실패했습니다."),
    ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 파라미터 타입이 일치하지 않습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
