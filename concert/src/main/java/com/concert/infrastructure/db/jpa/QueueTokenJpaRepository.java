package com.concert.infrastructure.db.jpa;

import com.concert.domain.model.QueueToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface QueueTokenJpaRepository extends JpaRepository<QueueToken,Long> {
    Optional<QueueToken> findByTokenValue(String tokenValue);
    long countByStatus(QueueToken.TokenStatus status);
    long countByStatusAndCreatedAtBefore(QueueToken.TokenStatus status, LocalDateTime createdAt);
}
