package com.concert.presentation.advice;

import com.concert.presentation.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * [400 Bad Request] 잘못된 입력 값으로 인한 예외 처리
     * ex) 존재하지 않는 ID로 조회, 유효성 검증 실패
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("handleIllegalArgumentException: {}", e.getMessage());
        final ErrorResponse response = new ErrorResponse("BAD_REQUEST", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * [409 Conflict] 비즈니스 로직 상 상태가 맞지 않아 발생하는 예외 처리
     * ex) 이미 예약된 좌석을 다시 예약하려는 시도
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        log.warn("handleMethodArgumentNotValidException: {}", errorMsg);
        final ErrorResponse response = new ErrorResponse("INVALID_INPUT_VALUE", errorMsg);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * [409 Conflict] 비즈니스 로직 상 상태가 맞지 않아 발생하는 예외 처리
     * ex) 이미 예약된 좌석을 다시 예약하려는 시도
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        log.warn("handleIllegalStateException: {}", e.getMessage());
        final ErrorResponse response = new ErrorResponse("CONFLICT", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * [404 Not Found] 요청한 리소스를 찾을 수 없을 때 발생하는 예외 처리
     * ex) 존재하지 않는 URL 접근, 정적 리소스 없음
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("handleNoResourceFoundException: {}", e.getMessage());
        final ErrorResponse response = new ErrorResponse("NOT_FOUND", "요청한 리소스를 찾을 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * [500 Internal Server Error] 위에서 처리하지 못한 모든 예외 처리
     * 서버의 문제로 인해 발생하는 예외를 최종적으로 처리합니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled Exception: {}", e.getMessage());
        final ErrorResponse response = new ErrorResponse("INTERNAL_SERVER_ERROR", "서버에 예상치 못한 오류가 발생했습니다. 관리자에게 문의해주세요.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
