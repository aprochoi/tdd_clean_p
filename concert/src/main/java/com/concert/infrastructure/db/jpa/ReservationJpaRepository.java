package com.concert.infrastructure.db.jpa;

import com.concert.domain.model.PaymentHistory;
import com.concert.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByIdAndUserId(Long reservationId, Long userId);
}
