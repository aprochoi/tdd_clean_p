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

/**
 * 예약 및 결제 관련 API 컨트롤러
 * - 좌석 예약, 잔액 충전 및 조회, 결제 처리를 담당합니다.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {
    private final SeatReservationService seatReservationService;
    private final BalanceService balanceService;
    private final PaymentService paymentService;
    private final TokenResolver tokenResolver;

    /**
     * 특정 좌석을 예약합니다.
     * @param tokenValue 대기열 토큰
     * @param request 좌석 ID를 포함한 요청 객체
     * @return 생성된 예약 정보
     */
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

    /**
     * 사용자의 잔액을 충전합니다.
     * @param tokenValue 대기열 토큰
     * @param request 충전할 금액을 포함한 요청 객체
     * @return 충전 후 잔액 정보
     */
    @PatchMapping("/balance/charge")
    public ResponseEntity<ApiResponse.BalanceResponse> chargeBalance(
            @RequestHeader("Authorization") String tokenValue,
            @RequestBody ApiRequest.BalanceChargeRequest request) {
        Long userId = tokenResolver.getUserId(tokenValue);
        User user = balanceService.chargeBalance(userId, request.amount());
        return ResponseEntity.ok(new ApiResponse.BalanceResponse(user.getId(), user.getBalance()));
    }

    /**
     * 사용자의 현재 잔액을 조회합니다.
     * @param tokenValue 대기열 토큰
     * @return 현재 잔액 정보
     */
    @GetMapping("/balance")
    public ResponseEntity<ApiResponse.BalanceResponse> getBalance(
            @RequestHeader("Authorization") String tokenValue) {
        Long userId = tokenResolver.getUserId(tokenValue);
        User user = balanceService.getBalance(userId);
        return ResponseEntity.ok(new ApiResponse.BalanceResponse(user.getId(), user.getBalance()));
    }

    /**
     * 예약된 좌석에 대한 결제를 진행합니다.
     * @param tokenValue 대기열 토큰
     * @param request 예약 ID를 포함한 요청 객체
     * @return 결제 내역 정보
     */
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
