package com.concert.presentation.controller;

import com.concert.domain.model.PaymentHistory;
import com.concert.domain.model.Reservation;
import com.concert.domain.model.User;
import com.concert.domain.service.BalanceService;
import com.concert.domain.service.PaymentService;
import com.concert.domain.service.SeatReservationService;
import com.concert.presentation.auth.TokenResolver;
import com.concert.presentation.dto.ApiRequest;
import com.concert.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {
    private final SeatReservationService seatReservationService;
    private final BalanceService balanceService;
    private final PaymentService paymentService;
    private final TokenResolver tokenResolver;

    @PostMapping("/reservations")
    public ResponseEntity<ApiResponse.SeatReservationResponse> reserveSeat(
            @RequestHeader("Authorization") String tokenValue,
            @RequestBody ApiRequest.SeatReservationRequest request) {

        Long userId = tokenResolver.getUserId(tokenValue);
        Reservation reservation = seatReservationService.reserveSeat(tokenValue, userId, request.seatId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse.SeatReservationResponse(reservation.getId(), reservation.getSeat().getStatus().name(), reservation.getExpiresAt())
        );
    }

    @PatchMapping("/balance/charge")
    public ResponseEntity<ApiResponse.BalanceResponse> chargeBalance(
            @RequestHeader("Authorization") String tokenValue,
            @RequestBody ApiRequest.BalanceChargeRequest request) {
        Long userId = tokenResolver.getUserId(tokenValue);
        User user = balanceService.chargeBalance(userId, request.amount());
        return ResponseEntity.ok(new ApiResponse.BalanceResponse(user.getId(), user.getBalance()));
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse.BalanceResponse> getBalance(
            @RequestHeader("Authorization") String tokenValue) {
        Long userId = tokenResolver.getUserId(tokenValue);
        User user = balanceService.getBalance(userId);
        return ResponseEntity.ok(new ApiResponse.BalanceResponse(user.getId(), user.getBalance()));
    }

    @PostMapping("/payments")
    public ResponseEntity<ApiResponse.PaymentResponse> processPayment(
            @RequestHeader("Authorization") String tokenValue,
            @RequestBody ApiRequest.PaymentRequest request) {

        Long userId = tokenResolver.getUserId(tokenValue);
        PaymentHistory payment = paymentService.processPayment(userId, request.reservationId(), tokenValue);
        User user = balanceService.getBalance(userId);

        return ResponseEntity.ok(new ApiResponse.PaymentResponse(payment.getId(), payment.getPaidAmount(), user.getBalance()));
    }
}
