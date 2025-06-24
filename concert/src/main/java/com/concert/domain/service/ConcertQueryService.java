package com.concert.domain.service;

import com.concert.domain.model.ConcertSchedule;
import com.concert.domain.model.Seat;
import com.concert.domain.repository.ConcertScheduleRepository;
import com.concert.domain.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertQueryService {
    private final ConcertScheduleRepository concertScheduleRepository;
    private final SeatRepository seatRepository;
    private final TokenValidator tokenValidator;

    public List<LocalDate> getAvailableDates(String tokenValue) {
        tokenValidator.validate(tokenValue); // 1. 토큰 검증

        // 2. 날짜 조회 로직
        return concertScheduleRepository.findByConcertDateAfter(LocalDate.now().minusDays(1))
                .stream()
                .map(ConcertSchedule::getConcertDate)
                .collect(Collectors.toList());
    }

    public List<Seat> getAvailableSeats(String tokenValue, LocalDate date) {
        tokenValidator.validate(tokenValue); // 1. 토큰 검증

        // 2. 스케줄 조회
        ConcertSchedule schedule = concertScheduleRepository.findByConcertDate(date)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 공연이 존재하지 않습니다."));

        // 3. 좌석 조회
        return seatRepository.findAllByConcertScheduleAndStatus(schedule, Seat.SeatStatus.AVAILABLE);
    }
}
