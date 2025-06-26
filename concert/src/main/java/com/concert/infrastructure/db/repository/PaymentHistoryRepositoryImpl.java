package com.concert.infrastructure.db.repository;

import com.concert.domain.model.PaymentHistory;
import com.concert.domain.repository.PaymentHistoryRepository;
import com.concert.infrastructure.db.jpa.PaymentHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentHistoryRepositoryImpl implements PaymentHistoryRepository {
    private final PaymentHistoryJpaRepository jpa;

    @Override
    public PaymentHistory save(PaymentHistory paymentHistory) {
        return jpa.save(paymentHistory);
    }
}
