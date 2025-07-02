package com.concert.config;

import com.concert.presentation.auth.TokenValidationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web MVC 설정을 위한 클래스
 * - Interceptor 등록 및 URL 패턴 설정 담당
 */
@Configurable
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenValidationInterceptor tokenValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidationInterceptor) // TokenValidationInterceptor 를 스프링에 등록
                // 1. 인터셉터를 적용할 URL 패텅 지정
                .addPathPatterns("/api/**")  // /api/**은 /api/로 시작하는 모든 하위 경로에 대해 인터셉터를 적용한다는 의미
                // 2. 인터셉터 적요에서 제외할 URL 패턴 지정
                .excludePathPatterns("/api/tokens"); // 토큰을 발급하는 API는 인터셉터가 가로채면 토큰이 없으므로 항상 실패하게 된다. 따라서 추가
    }
}
