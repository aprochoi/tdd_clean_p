package com.concert.presentation.controller;

import com.concert.domain.model.Seat;
import com.concert.domain.service.ConcertQueryService;
import com.concert.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 콘서트 관련 API 컨트롤러
 * - 예약 가능한 날짜 및 좌석 조회를 담당합니다.
 */
@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {
    private final ConcertQueryService concertQueryService;

    /**
     * 예약 가능한 콘서트 날짜 목록을 조회합니다.
     * @param tokenValue 대기열 토큰
     * @return 예약 가능한 날짜 리스트
     */
    @GetMapping("/dates")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@RequestHeader("Authorization") String tokenValue) {
        List<LocalDate> dates = concertQueryService.getAvailableDates(tokenValue);
        return ResponseEntity.ok(dates);
    }

    /**
     * 특정 날짜에 예약 가능한 좌석 목록을 조회합니다.
     * @param tokenValue 대기열 토큰
     * @param date 조회할 날짜
     * @return 예약 가능한 좌석 리스트
     */
    @GetMapping("/seats")
    public ResponseEntity<List<ApiResponse.AvailableSeatResponse>> getAvailableSeats(
            @RequestHeader("Authorization") String tokenValue, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Seat> seats = concertQueryService.getAvailableSeats(tokenValue, date);
        List<ApiResponse.AvailableSeatResponse> response = seats.stream()
                .map(seat -> new ApiResponse.AvailableSeatResponse(seat.getId(), seat.getSeatNumber(), seat.getPrice()))
                .toList();

        return ResponseEntity.ok(response);
    }

}
