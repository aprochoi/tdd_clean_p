package com.concert.presentation.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * 모든 API 요청과 응답을 로깅하는 필터
 * - 모든 요청 URI, Method, 처리 시간, 응답 상태 코드를 기록한다.
 */
@Slf4j
@Component
public class RequestLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 요청 및 응답을 여러번 읽을 수 있도록 Wrapper 클래스로 감싸준다.
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse)  response);

        long startTime = System.currentTimeMillis();

        // 다음 필터 또는 서블릿으로 요청 및 응답을 전달한다.
        // 이 라인을 기준으로 이전은 '요청', 이후는 '응답' 처리 로직이 위치한다.
        chain.doFilter(requestWrapper, responseWrapper);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // 로그 메세지 생성
        String requestURI = requestWrapper.getRequestURI();
        String method = requestWrapper.getMethod();
        int statusCode = responseWrapper.getStatus();

        log.info("[Request/Response] {} {} - {}ms, Status: {}", method, requestURI, executionTime, statusCode);

        // Wrapper에 캐싱된 응답 본문을 실제 응답 객체에 복사하여 클라이언트에게 전달
        responseWrapper.copyBodyToResponse();
    }
}
