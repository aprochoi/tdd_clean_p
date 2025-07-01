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

@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {
    private final ConcertQueryService concertQueryService;

    @GetMapping("/dates")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@RequestHeader("Authorization") String tokenValue) {
        List<LocalDate> dates = concertQueryService.getAvailableDates(tokenValue);
        return ResponseEntity.ok(dates);
    }

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
