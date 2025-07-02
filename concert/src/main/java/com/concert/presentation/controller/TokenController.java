package com.concert.presentation.controller;

import com.concert.domain.model.QueueToken;
import com.concert.domain.service.QueueService;
import com.concert.presentation.dto.ApiRequest;
import com.concert.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 대기열 토큰 관련 API 컨트롤러
 * - 대기열 토큰 발급 및 상태 조회를 담당합니다.
 */
@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final QueueService queueService;

    /**
     * 사용자에게 대기열 토큰을 발급합니다.
     * @param request 사용자 ID를 포함한 요청 객체
     * @return 발급된 토큰 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse.TokenIssuanceResponse> issueToken(@RequestBody ApiRequest.TokenIssuanceRequest request) {
        QueueToken token = queueService.issueToken(request.userId());
        return ResponseEntity.ok(new ApiResponse.TokenIssuanceResponse(token.getTokenValue(), token.getStatus().name(), token.getExpiresAt()));
    }

    /**
     * 대기열 상태를 조회합니다. (현재 대기 순번 등)
     * @param tokenValue 대기열 토큰
     * @return 대기열 상태 정보 (상태, 대기 순번)
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse.QueueStatusResponse> getQueueStatus(@RequestHeader("Authorization") String tokenValue) {
        QueueService.QueueStatus status = queueService.getQueueStatus(tokenValue);
        return ResponseEntity.ok(new ApiResponse.QueueStatusResponse(status.status().name(), status.rank()));
    }
}
