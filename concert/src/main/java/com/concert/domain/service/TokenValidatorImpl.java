package com.concert.domain.service;

import com.concert.domain.model.QueueToken;
import com.concert.domain.repository.QueueTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component // Service가 아닌, 재사용 가능한 부품이므로 Component로 등록
@RequiredArgsConstructor
public class TokenValidatorImpl implements TokenValidator {
    private final QueueTokenRepository queueTokenRepository;

    @Override
    public void validate(String tokenValue) {
        QueueToken token = queueTokenRepository.findByTokenValue(tokenValue)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 토큰입니다."));

        if (!token.isActive()) {
            throw new RuntimeException("토큰이 활성 상태가 아닙니다.");
        }
    }
}
