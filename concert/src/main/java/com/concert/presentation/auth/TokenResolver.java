package com.concert.presentation.auth;

import org.springframework.stereotype.Component;

@Component
public class TokenResolver {

    public Long getUserId(String tokenValue) {
        // TODO : 실제로는 JWT 등을 사용해 토큰을 파싱하고 사용자 ID를 추출해야한다.
        // 현재는 그냥 1L 임시로 반환
        if (tokenValue == null || tokenValue.isEmpty()) {
            throw new IllegalArgumentException("tokenValue is null or empty");
        }
        return 1L;
    }
}
