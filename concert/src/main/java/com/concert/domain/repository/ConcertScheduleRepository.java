package com.concert.domain.repository;

import com.concert.domain.model.ConcertSchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConcertScheduleRepository {
    List<ConcertSchedule> findByConcertDateAfter(LocalDate date);
    Optional<ConcertSchedule> findByConcertDate(LocalDate date);
}
