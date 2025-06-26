package com.concert.infrastructure.db.repository;

import com.concert.domain.model.Reservation;
import com.concert.domain.repository.ReservationRepository;
import com.concert.infrastructure.db.jpa.ReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {
    private final ReservationJpaRepository jpa;

    @Override
    public Reservation save(Reservation reservation) {
        return jpa.save(reservation);
    }

    @Override
    public Optional<Reservation> findByIdAndUserId(Long reservationId, Long userId) {
        return jpa.findByIdAndUserId(reservationId, userId);
    }
}
