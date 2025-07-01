package com.concert.presentation.dto;

import java.time.LocalDateTime;

public final class ApiResponse {
    public record TokenIssuanceResponse(String token, String status, LocalDateTime expiresAt) {}
    public record QueueStatusResponse(String status, long rank) {}
    public record AvailableSeatResponse(Long seatId, int seatNumber, int price) {}
    public record SeatReservationResponse(Long reservationId, String seatStatus, LocalDateTime expiresAt) {}
    public record BalanceResponse(Long userId, long balance) {}
    public record PaymentResponse(Long paymentId, long paidAmount, long remainingBalance) {}
}
