package com.concert.domain.repository;

import com.concert.domain.model.PaymentHistory;

import java.util.Optional;

public interface PaymentHistoryRepository {
    PaymentHistory save(PaymentHistory paymentHistory);
}
