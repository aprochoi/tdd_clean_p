package com.concert.infrastructure.db.repository;

import com.concert.domain.model.ConcertSchedule;
import com.concert.domain.repository.ConcertScheduleRepository;
import com.concert.infrastructure.db.jpa.ConcertScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository {
    private final ConcertScheduleJpaRepository jpa;

    @Override
    public List<ConcertSchedule> findByConcertDateAfter(LocalDate date) {
        return jpa.findByConcertDateAfter(date);
    }

    @Override
    public Optional<ConcertSchedule> findByConcertDate(LocalDate date) {
        return jpa.findByConcertDate(date);
    }
}
