package com.concert.domain.service;

import com.concert.domain.model.PaymentHistory;
import com.concert.domain.model.Reservation;
import com.concert.domain.repository.PaymentHistoryRepository;
import com.concert.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final ReservationRepository reservationRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final BalanceService balanceService;
    private final QueueService queueService;

    @Transactional
    public PaymentHistory processPayment(Long userId, Long reservationId, String tokenValue) {
        // 1.예약 정보 조회 및 검증 (본인의 예약이 맞는지 확인)
        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않는 예약 정보입니다."));

        // 2. 예약 상태 검증 (결제 대기 상태인지, 만료되지 않았는지)
        reservation.validateForPayment();

        // 3. 잔액 차감
        balanceService.useBalance(userId, reservation.getPrice());

        // 4. 좌석 및 예약 상태 변경
        reservation.getSeat().sell();
        reservation.complete();

        // 5. 결제 내역 생성 및 저장
        PaymentHistory paymentHistory = PaymentHistory.builder()
                .userId(userId)
                .reservationId(reservationId)
                .paidAmount(reservation.getPrice())
                .paidAt(LocalDateTime.now())
                .build();
        paymentHistoryRepository.save(paymentHistory);

        // 6. 대기열 토큰 만료 처리
        queueService.expireToken(tokenValue);

        return paymentHistory;
    }
}
