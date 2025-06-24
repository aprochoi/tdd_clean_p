package com.concert.domain.repository;

import com.concert.domain.model.ConcertSchedule;
import com.concert.domain.model.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    List<Seat> findAllByConcertScheduleAndStatus(ConcertSchedule concertSchedule, Seat.SeatStatus status);

    // findById는 동시성 제어를 위해 Lock을 걸 수 있어야 한다.
    Optional<Seat> findById(Long id);

    Seat save(Seat seat);
}
