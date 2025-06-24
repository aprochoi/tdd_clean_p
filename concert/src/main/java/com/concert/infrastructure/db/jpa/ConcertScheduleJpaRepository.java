package com.concert.infrastructure.db.jpa;

import com.concert.domain.model.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
    List<ConcertSchedule> findByConcertDateAfter(LocalDate date);
    Optional<ConcertSchedule> findByConcertDate(LocalDate date);
}
