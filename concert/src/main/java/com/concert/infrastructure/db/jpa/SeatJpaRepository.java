package com.concert.infrastructure.db.jpa;

import com.concert.domain.model.ConcertSchedule;
import com.concert.domain.model.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByConcertScheduleAndStatus(ConcertSchedule concertSchedule, Seat.SeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Seat> findById(long id);
}
