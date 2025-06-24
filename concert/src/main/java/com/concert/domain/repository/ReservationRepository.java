package com.concert.domain.repository;

import com.concert.domain.model.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
}
