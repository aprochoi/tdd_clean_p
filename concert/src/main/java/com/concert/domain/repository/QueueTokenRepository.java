package com.concert.domain.repository;

import com.concert.domain.model.QueueToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface QueueTokenRepository {

    QueueToken save(QueueToken queueToken);

    // ACTIVE 상태의 토큰 개수 count
    long countByStatus(QueueToken.TokenStatus status);

    // 토큰 값으로 토큰 찾기
    Optional<QueueToken> findByTokenValue(String tokenValue);

    // 내 앞에 대기중인 사람이 몇 명인지 계산하기 위한 jpa
    long countByStatusAndCreatedAtBefore(QueueToken.TokenStatus status, LocalDateTime myCreatedAt);

}
