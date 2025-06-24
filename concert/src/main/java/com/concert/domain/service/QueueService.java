package com.concert.domain.service;

import com.concert.domain.model.QueueToken;
import com.concert.domain.repository.QueueTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final QueueTokenRepository queueTokenRepository;
    private static final long ACTIVE_USER_LIMIT = 100; // 동시 활성 사용자 수는 100명으로 제한

    /**
     * 신규 토큰 발급
     * - 활성 유저가 제한보다 적으면 ACTIVE 토큰 발급
     * - 활성 유저가 꽉 찼으면 WAITING 토큰 발급
     * @param userId 사용자 id
     * @return QueueToken
     */
    @Transactional
    public QueueToken issueToken(Long userId) {
        long activeCount = queueTokenRepository.countByStatus(QueueToken.TokenStatus.ACTIVE);

        QueueToken.TokenStatus status;
        LocalDateTime expiresAt = null;

        if (activeCount < ACTIVE_USER_LIMIT) {
            status = QueueToken.TokenStatus.ACTIVE;
            expiresAt = LocalDateTime.now().plusMinutes(10); // 활성 토큰은 10분 유효
        } else {
            status = QueueToken.TokenStatus.WAITING;
        }

        QueueToken newToken = QueueToken.builder()
                        .userId(userId)
                        .tokenValue(UUID.randomUUID().toString())
                        .status(status)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(expiresAt)
                        .build();

        return queueTokenRepository.save(newToken);
    }

    @Transactional(readOnly = true)
    public QueueStatus getQueueStatus(String tokenValue) {
        QueueToken token = queueTokenRepository.findByTokenValue(tokenValue)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (token.getStatus() == QueueToken.TokenStatus.ACTIVE) {
            return new QueueStatus(token.getStatus(), 0); // 활성 상태는 대기 순번 0
        }

        long rank = queueTokenRepository.countByStatusAndCreatedAtBefore(
                QueueToken.TokenStatus.WAITING,
                token.getCreatedAt()
        );

        return new QueueStatus(token.getStatus(), rank+1); // 순번은 내 앞사람 수 + 1
    }

    // 응답용 DTO
    public record QueueStatus(QueueToken.TokenStatus status, long rank) {}
}
