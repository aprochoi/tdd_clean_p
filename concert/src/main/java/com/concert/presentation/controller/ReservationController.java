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

    /**
     * 특정 좌석을 예약합니다.
     * @param userId @RequestAttribute를 통해 인터셉터에서 전달된 사용자 ID
     * @param request 좌석 ID를 포함한 요청 객체
     * @return 생성된 예약 정보
     */
    @PostMapping("/reservations")
    public ResponseEntity<ApiResponse.SeatReservationResponse> reserveSeat(
            @RequestAttribute("userId") Long userId,
            @RequestBody ApiRequest.SeatReservationRequest request) {

        Reservation reservation = seatReservationService.reserveSeat(userId, request.seatId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse.SeatReservationResponse(reservation.getId(), reservation.getSeat().getStatus().name(), reservation.getExpiresAt())
        );
    }

    /**
     * 사용자의 잔액을 충전합니다.
     * @param userId @RequestAttribute를 통해 인터셉터에서 전달된 사용자 ID
     * @param request 충전할 금액을 포함한 요청 객체
     * @return 충전 후 잔액 정보
     */
    @PatchMapping("/balance/charge")
    public ResponseEntity<ApiResponse.BalanceResponse> chargeBalance(
            @RequestAttribute("userId") Long userId,
            @RequestBody ApiRequest.BalanceChargeRequest request) {
        User user = balanceService.chargeBalance(userId, request.amount());
        return ResponseEntity.ok(new ApiResponse.BalanceResponse(user.getId(), user.getBalance()));
    }

    /**
     * 사용자의 현재 잔액을 조회합니다.
     * @param userId @RequestAttribute를 통해 인터셉터에서 전달된 사용자 ID
     * @return 현재 잔액 정보
     */
    @GetMapping("/balance")
    public ResponseEntity<ApiResponse.BalanceResponse> getBalance(
            @RequestAttribute("userId") Long userId) {
        User user = balanceService.getBalance(userId);
        return ResponseEntity.ok(new ApiResponse.BalanceResponse(user.getId(), user.getBalance()));
    }

    /**
     * 예약된 좌석에 대한 결제를 진행합니다.
     * @param userId @RequestAttribute를 통해 인터셉터에서 전달된 사용자 ID
     * @param request 예약 ID를 포함한 요청 객체
     * @return 결제 내역 정보
     */
    @PostMapping("/payments")
    public ResponseEntity<ApiResponse.PaymentResponse> processPayment(
            @RequestAttribute("userId") Long userId,
            @RequestBody ApiRequest.PaymentRequest request) {

        PaymentHistory payment = paymentService.processPayment(userId, request.reservationId());
        User user = balanceService.getBalance(userId);

        return ResponseEntity.ok(new ApiResponse.PaymentResponse(payment.getId(), payment.getPaidAmount(), user.getBalance()));
    }
}
