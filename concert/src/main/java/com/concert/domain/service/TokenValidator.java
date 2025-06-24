package com.concert.domain.service;

public interface TokenValidator {
    /**
     * 토큰이 유효하고 ACTIVE 상태인지 검증한다.
     * 유효하지 않으면 예외 발생
     * @param tokenValue 검즐할 토큰 값
     */
    void validate(String tokenValue);
}
