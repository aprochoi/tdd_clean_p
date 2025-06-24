package com.concert.infrastructure.db.repository;

import com.concert.domain.model.QueueToken;
import com.concert.domain.repository.QueueTokenRepository;
import com.concert.infrastructure.db.jpa.QueueTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QueueTokenRepositoryImpl implements QueueTokenRepository {
    private final QueueTokenJpaRepository jpa;

    @Override
    public QueueToken save(QueueToken queueToken) {
        return jpa.save(queueToken);
    }

    @Override
    public long countByStatus(QueueToken.TokenStatus status) {
        return jpa.countByStatus(status);
    }

    @Override
    public Optional<QueueToken> findByTokenValue(String tokenValue) {
        return jpa.findByTokenValue(tokenValue);
    }

    @Override
    public long countByStatusAndCreatedAtBefore(QueueToken.TokenStatus status, LocalDateTime myCreatedAt) {
        return jpa.countByStatusAndCreatedAtBefore(status, myCreatedAt);
    }
}
