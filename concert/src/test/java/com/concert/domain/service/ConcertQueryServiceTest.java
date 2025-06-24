package com.concert.domain.service;

import com.concert.domain.model.Concert;
import com.concert.domain.model.ConcertSchedule;
import com.concert.domain.model.Seat;
import com.concert.domain.repository.ConcertScheduleRepository;
import com.concert.domain.repository.SeatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConcertQueryServiceTest {

    @Mock
    private ConcertScheduleRepository concertScheduleRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private TokenValidator tokenValidator;
    @InjectMocks
    private ConcertQueryService concertQueryService;

    private final String validToken = "valid-active-token";
    private final LocalDate concertDate = LocalDate.of(2025, 7, 5);
    private final ConcertSchedule schedule = new ConcertSchedule(new Concert("형준이의 콘서트", "김형준"), concertDate);

    @Test
    @DisplayName("예약 가능한 날짜 목록을 성공적으로 조회")
    void getAvailableDates_shouldReturnDateList() {
        //given
        doNothing().when(tokenValidator).validate(validToken);
        given(concertScheduleRepository.findByConcertDateAfter(any(LocalDate.class))).willReturn(List.of(schedule));

        //when
        List<LocalDate> result = concertQueryService.getAvailableDates(validToken);

        //then
        assertThat(result).containsExactly(concertDate);
        verify(tokenValidator).validate(validToken);
    }

    @Test
    @DisplayName("특정 날짜의 예약 가능한 좌석 목록 조회 성공")
    void getAvailableSeats_withValidDate_shouldReturnSeatList() {
        //given
        doNothing().when(tokenValidator).validate(validToken);
        Seat seat1 = new Seat(schedule, 1, 40000, Seat.SeatStatus.AVAILABLE);
        Seat seat2 = new Seat(schedule, 2, 40000, Seat.SeatStatus.AVAILABLE);

        given(concertScheduleRepository.findByConcertDate(concertDate)).willReturn(Optional.of(schedule));
        given(seatRepository.findAllByConcertScheduleAndStatus(schedule, Seat.SeatStatus.AVAILABLE))
                .willReturn(List.of(seat1, seat2));

        //when
        List<Seat> result = concertQueryService.getAvailableSeats(validToken, concertDate);

        //then
        assertThat(result).hasSize(2);
        verify(tokenValidator).validate(validToken);
    }

}