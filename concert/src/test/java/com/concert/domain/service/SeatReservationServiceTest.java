package com.concert.domain.service;

import com.concert.domain.model.Reservation;
import com.concert.domain.model.Seat;
import com.concert.domain.model.User;
import com.concert.domain.repository.ReservationRepository;
import com.concert.domain.repository.SeatRepository;
import com.concert.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SeatReservationServiceTest {

    @Mock
    private TokenValidator tokenValidator;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private SeatReservationService seatReservationService;

    @Test
    @DisplayName("활성화된 토큰과 예약 가능한 좌석으로 예약 시 임시 배정 성공")
    void reserveSeat_WithValidTokenAndAvailableSeat_ShouldSucceed() {
        //given
        String validToken = "valid-token";
        Long userId = 1L, seatId = 1L;
        User user = new User("최영민", 0); // 현재 가진 돈은 없지만 임시 배정이므로 성공함
        Seat availableSeat = new Seat(null, 1, 50000, Seat.SeatStatus.AVAILABLE);

        doNothing().when(tokenValidator).validate(validToken);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(seatRepository.findById(seatId)).willReturn(Optional.of(availableSeat));
        given(reservationRepository.save(any(Reservation.class))).willAnswer(inv -> inv.getArgument(0));

        //when
        Reservation result = seatReservationService.reserveSeat(validToken, userId, seatId);

        //then
        assertThat(result.getStatus()).isEqualTo(Reservation.ReservationStatus.PENDING_PAYMENT);
        assertThat(result.getExpiresAt()).isAfter(LocalDateTime.now().plusMinutes(4)); // 4분보다 이후인지? (5분의 만료시간 확인)
        assertThat(availableSeat.getStatus()).isEqualTo(Seat.SeatStatus.RESERVED); // 좌석 정보가 예약 상태로 되었는지
        verify(seatRepository).save(availableSeat);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("이미 예약된 좌석으로 예약 시 예외 발생")
    void reserveSeat_WithReservedSeat_ShouldThrowException() {
        //given
        String validToken = "valid-token";
        Long userId = 1L, seatId = 1L;
        User user = new User("최영민", 0);
        Seat reservedSet = new Seat(null, 1, 50000, Seat.SeatStatus.RESERVED);

        doNothing().when(tokenValidator).validate(validToken);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(seatRepository.findById(seatId)).willReturn(Optional.of(reservedSet));

        //when & then
        assertThrows(IllegalStateException.class, () -> seatReservationService.reserveSeat(validToken, userId, seatId));
    }
}