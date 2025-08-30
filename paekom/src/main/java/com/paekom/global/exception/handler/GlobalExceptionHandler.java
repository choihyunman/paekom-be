package com.paekom.global.exception.handler;

import com.paekom.global.exception.BaseException;
import com.paekom.global.exception.ErrorCode;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

import static com.paekom.global.exception.ErrorCode.*;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    public static final String MESSAGE_KEY = "message";

    // javax.validation.Valid or @Validated 으로 binding error 발생시 발생
    // 주로 @RequestBody, @RequestPart 어노테이션에서 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("요청 바디 검증 실패: {}", e.getMessage());
        Map<String, String> body = Map.of( MESSAGE_KEY,  INVALID_HTTP_MESSAGE_BODY.getMessage());
        return ResponseEntity
                .status(INVALID_HTTP_MESSAGE_BODY.getHttpStatus())
                .body(body);
    }

    // @ModelAttribute 으로 binding error 발생시 BindException 발생
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleBindException(BindException e) {
        log.warn("요청 파라미터 바인딩 실패: {}", e.getMessage());
        Map<String, String> body = Map.of( MESSAGE_KEY, BIND_ERROR.getMessage());
        return ResponseEntity
                .status(BIND_ERROR.getHttpStatus())
                .body(body);
    }

    // enum type 일치하지 않아 binding 못할 경우 발생
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("요청 파라미터 타입 불일치. 파라미터명: {}, 오류: {}", e.getName(), e.getMessage());
        Map<String, String> body = Map.of(MESSAGE_KEY, ARGUMENT_TYPE_MISMATCH.getMessage());
        return ResponseEntity
                .status(ARGUMENT_TYPE_MISMATCH.getHttpStatus())
                .body(body);
    }

    // 지원하지 않은 HTTP method 호출 할 경우 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("지원하지 않는 HTTP 메서드 호출: {}. 오류: {}", e.getMethod(), e.getMessage());
        Map<String, String> body = Map.of(MESSAGE_KEY, UNSUPPORTED_HTTP_METHOD.getMessage());
        return ResponseEntity
                .status(UNSUPPORTED_HTTP_METHOD.getHttpStatus())
                .body(body);
    }

    // request 값을 읽을 수 없을 때 발생
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("HTTP 메시지를 읽는 도중 오류 발생: {}", e.getMessage());
        Map<String, String> body = Map.of(MESSAGE_KEY, BAD_REQUEST_ERROR.getMessage());
        return ResponseEntity
                .status(BAD_REQUEST_ERROR.getHttpStatus())
                .body(body);
    }

    // 비즈니스 로직 에러
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, String>> handleBaseException(BaseException e, WebRequest request) {
        // 요청 URL 정보 가져오기
        String requestUrl = extractRequestUrl(request);
        String httpMethod = extractHttpMethod(request);

        // 현재 인증된 사용자 정보 가져오기
        String userName = "비로그인 사용자";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            Object principal = authentication.getPrincipal();
            if (principal instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) principal;
                userName = userPrincipal.getName();
            } else {
                userName = authentication.getName();
            }
        }
        log.error("비즈니스 로직 처리 중 오류 발생. 사용자: {}, URL: {}, 메서드: {}, 에러 코드: {}, 메시지: {}",
                userName, requestUrl, httpMethod, e.getErrorCode(), e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        Map<String, String> body = Map.of(
                MESSAGE_KEY, errorCode.getMessage()
        );
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(body);
    }

    // 요청 URL 추출 메서드
    private String extractRequestUrl(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            ServletWebRequest servletRequest = (ServletWebRequest) request;
            return servletRequest.getRequest().getRequestURL().toString();
        }
        return "Unknown URL";
    }

    // HTTP 메서드 추출 메서드
    private String extractHttpMethod(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            ServletWebRequest servletRequest = (ServletWebRequest) request;
            return servletRequest.getHttpMethod() != null ? servletRequest.getHttpMethod().toString() : "Unknown";
        }
        return "Unknown Method";
    }

}
