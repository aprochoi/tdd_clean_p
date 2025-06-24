package com.concert.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueueToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String tokenValue;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    public enum TokenStatus {
        WAITING, ACTIVE, EXPIRED
    }

    @Builder
    public QueueToken(Long userId, String tokenValue, TokenStatus status, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.userId = userId;
        this.tokenValue = tokenValue;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public void activate() {
        this.status = TokenStatus.ACTIVE;
        this.expiresAt = LocalDateTime.now().plusMinutes(10); //활성화 시 10분 유효시간 부여
    }

    public boolean isActive() {
        return this.status == TokenStatus.ACTIVE && LocalDateTime.now().isBefore(this.expiresAt);
    }

    public void expire() {
        this.status = TokenStatus.EXPIRED;
    }
}
