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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private QueueTokenRepository queueTokenRepository;

    @InjectMocks
    private QueueService queueService;

    @Test
    @DisplayName("활성 사용자 수가 제한보다 적을 때, ACTIVE 상태의 토큰을 발급한다.")
    void issueToken_whenSlotsAvailable_shouldIssueActiveToken() {
        // given : 활성 유저가 50명이라고 가정한다.
        given(queueTokenRepository.countByStatus(QueueToken.TokenStatus.ACTIVE)).willReturn(50L);
        given(queueTokenRepository.save(any(QueueToken.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when : 토큰을 발급 하면
        QueueToken token = queueService.issueToken(1L);

        // then : ACTIVE 상태의 토큰이 발급된다.
        assertThat(token.getStatus()).isEqualTo(QueueToken.TokenStatus.ACTIVE);
        assertThat(token.getExpiresAt()).isNotNull();
        verify(queueTokenRepository).save(any(QueueToken.class)); // save가 호출되었는지 검증
    }

    @Test
    @DisplayName("대기 중인 토큰의 순번을 정확하게 조회한다.")
    void getQueueStatus_whenWaiting_shouldReturnRank() {
        // given : 특정 대기 토큰 정보 생성
        String testTokenValue = UUID.randomUUID().toString();
        LocalDateTime testCreatedAt = LocalDateTime.now();
        QueueToken testToken = new QueueToken(1L, testTokenValue, QueueToken.TokenStatus.WAITING, testCreatedAt, null);

        given(queueTokenRepository.findByTokenValue(testTokenValue)).willReturn(Optional.of(testToken));
        // 내 앞에 15명이 대기중이라고 가정한다.
        given(queueTokenRepository.countByStatusAndCreatedAtBefore(QueueToken.TokenStatus.WAITING, testCreatedAt)).willReturn(15L);

        // when : 내 상태를 조회하면
        QueueService.QueueStatus status =  queueService.getQueueStatus(testTokenValue);

        // then : 16번째다
        assertThat(status.status()).isEqualTo(QueueToken.TokenStatus.WAITING);
        assertThat(status.rank()).isEqualTo(16);
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 조회 시 예외 발생")
    void getQueueStatus_withInvalidToken_shouldThrowException() {
        //given
        String invalidToken = "invalidToken";
        given(queueTokenRepository.findByTokenValue(invalidToken)).willReturn(Optional.empty());

        //when & then
        assertThrows(IllegalArgumentException.class, () -> queueService.getQueueStatus(invalidToken));
    }





















}