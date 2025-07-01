package com.concert.presentation.controller;

import com.concert.domain.model.QueueToken;
import com.concert.domain.service.QueueService;
import com.concert.presentation.dto.ApiRequest;
import com.concert.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final QueueService queueService;

    @PostMapping
    public ResponseEntity<ApiResponse.TokenIssuanceResponse> issueToken(@RequestBody ApiRequest.TokenIssuanceRequest request) {
        QueueToken token = queueService.issueToken(request.userId());
        return ResponseEntity.ok(new ApiResponse.TokenIssuanceResponse(token.getTokenValue(), token.getStatus().name(), token.getExpiresAt()));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse.QueueStatusResponse> getQueueStatus(@RequestHeader("Authorization") String tokenValue) {
        QueueService.QueueStatus status = queueService.getQueueStatus(tokenValue);
        return ResponseEntity.ok(new ApiResponse.QueueStatusResponse(status.status().name(), status.rank()));
    }
}
