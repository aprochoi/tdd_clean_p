package com.concert.domain.repository;

import com.concert.domain.model.Reservation;

import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findByIdAndUserId(Long reservationId, Long userId);
}
