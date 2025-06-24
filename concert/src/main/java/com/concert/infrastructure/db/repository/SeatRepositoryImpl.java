package com.concert.infrastructure.db.repository;

import com.concert.domain.model.ConcertSchedule;
import com.concert.domain.model.Seat;
import com.concert.domain.repository.SeatRepository;
import com.concert.infrastructure.db.jpa.SeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {
    private final SeatJpaRepository jpa;

    @Override
    public List<Seat> findAllByConcertScheduleAndStatus(ConcertSchedule concertSchedule, Seat.SeatStatus status) {
        return jpa.findAllByConcertScheduleAndStatus(concertSchedule, status);
    }

    @Override
    public Optional<Seat> findById(Long id) {
        return jpa.findByIdWithLock(id);
    }

    @Override
    public Seat save(Seat seat) {
        return jpa.save(seat);
    }
}
