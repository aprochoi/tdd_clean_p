package com.concert.domain.service;

import com.concert.domain.model.QueueToken;
import com.concert.domain.repository.QueueTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenValidatorImplTest {

    @Mock
    private QueueTokenRepository queueTokenRepository;

    @InjectMocks
    private TokenValidatorImpl tokenValidator;

    @Test
    @DisplayName("토큰이 존재하고 ACTIVE 상태이면 검증 통과")
    void validate_withActiveToken_shouldSucceed() {
        // given
        String activeTokenValue = "active-token";
        QueueToken activeToken = QueueToken.builder().status(QueueToken.TokenStatus.ACTIVE).expiresAt(LocalDateTime.now().plusMinutes(5)).build();
        given(queueTokenRepository.findByTokenValue(activeTokenValue)).willReturn(Optional.of(activeToken));

        // when&then (예외가 발생하면 안됨)
        assertDoesNotThrow(() -> tokenValidator.validate(activeTokenValue));
    }

    @Test
    @DisplayName("토큰이 존재하지 않으면 예외 발생")
    void validate_withNotExistentToken_shouldThrowException() {
        //given
        String invalidTokenValue = "invalid-token";
        given(queueTokenRepository.findByTokenValue(invalidTokenValue)).willReturn(Optional.empty());

        //when&then
        assertThrows(RuntimeException.class, () -> tokenValidator.validate(invalidTokenValue));

    }

    @Test
    @DisplayName("토큰이 WAITING 상태면 예외 발생")
    void validate_withWaitingToken_shouldThrowException() {
        //given
        String waitingTokenValue = "waiting-token";
        QueueToken waitingToken = QueueToken.builder().status(QueueToken.TokenStatus.WAITING).build();
        given(queueTokenRepository.findByTokenValue(waitingTokenValue)).willReturn(Optional.of(waitingToken));

        //when & then
        assertThrows(RuntimeException.class, () -> tokenValidator.validate(waitingTokenValue));
    }
}