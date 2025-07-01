package com.concert.domain.service;

import com.concert.domain.model.*;
import com.concert.domain.repository.PaymentHistoryRepository;
import com.concert.domain.repository.ReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock private ReservationRepository reservationRepository;
    @Mock private PaymentHistoryRepository paymentHistoryRepository;
    @Mock private BalanceService balanceService;
    @Mock private QueueService queueService;
    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("모든 조건이 충족될 때 결제가 성공적으로 처리된다.")
    void processPayment_succeeds() {
        //given
        Long userId = 1L;
        Long reservationId = 1L;
        Long seatId = 5L;
        String tokenValue = "active-token";

        Concert concert = new Concert("2직준비 콘서트", "김형준");
        ConcertSchedule concertSchedule = new ConcertSchedule(concert, LocalDate.of(2025, 6, 26));
        Seat seat = new Seat(concertSchedule, 5, 50000, Seat.SeatStatus.RESERVED);

        User user = new User(userId, "최영민", 100000);
        Reservation reservation = Reservation.builder()
                .user(user)
                .seat(seat)
                .status(Reservation.ReservationStatus.PENDING_PAYMENT)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();

        ReflectionTestUtils.setField(reservation, "id", reservationId);
        ReflectionTestUtils.setField(seat, "id", seatId);

        given(reservationRepository.findByIdAndUserId(reservationId, userId)).willReturn(Optional.of(reservation));
        doNothing().when(balanceService).useBalance(anyLong(), anyLong());
        doNothing().when(queueService).expireToken(anyString());
        given(paymentHistoryRepository.save(any(PaymentHistory.class))).willAnswer(inv -> inv.getArgument(0));

        //when
        PaymentHistory result = paymentService.processPayment(userId, reservationId, tokenValue);

        //then
        verify(balanceService).useBalance(userId, reservation.getPrice());
        verify(queueService).expireToken(tokenValue);
        verify(paymentHistoryRepository).save(any(PaymentHistory.class));
        assertThat(reservation.getStatus()).isEqualTo(Reservation.ReservationStatus.COMPLETED);
        assertThat(seat.getStatus()).isEqualTo(Seat.SeatStatus.SOLD);
    }

}