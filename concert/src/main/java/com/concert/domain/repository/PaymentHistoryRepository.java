package com.concert.domain.repository;

import com.concert.domain.model.PaymentHistory;

public interface PaymentHistoryRepository {
    PaymentHistory save(PaymentHistory paymentHistory);
}
