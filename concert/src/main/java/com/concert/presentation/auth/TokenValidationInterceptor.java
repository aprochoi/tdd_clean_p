package com.concert.presentation.auth;

import com.concert.domain.service.TokenValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * API 요청 시 Authorization 헤더의 토큰을 검증하는 인터셉터
 * 토큰이 유효한 경우에만 Controller로 요청을 전달한다.
 */
@Component
@RequiredArgsConstructor
public class TokenValidationInterceptor implements HandlerInterceptor {
    private final TokenValidator tokenValidator;
    private final TokenResolver tokenResolver;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.Result Header에서 토큰 추출
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_TOKEN)) {
            // 토큰이 없거나 형식이 올바르지 않으면 예외 처리 (GlobalExceptionHandler가 처리)
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        String token = authorizationHeader.substring(BEARER_TOKEN.length());

        // 2. 토큰 유효성 검증 (기존 TokenValidator 사용)
        tokenValidator.validate(token);

        // 3. 토큰에서 UserId 추출하여 request에 저장
        // Controller에서 @RequestHeader 대신 request.getAttribute()로 편하게 사용이 가능
        Long userId = tokenResolver.getUserId(token);
        request.setAttribute("userId", userId);

        return true;
    }
}
