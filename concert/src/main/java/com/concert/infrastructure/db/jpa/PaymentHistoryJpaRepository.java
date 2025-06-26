package com.concert.infrastructure.db.jpa;

import com.concert.domain.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentHistoryJpaRepository extends JpaRepository<PaymentHistory,Long> {
}
