package com.concert.presentation.dto;

public final class ApiRequest {
    public record TokenIssuanceRequest(Long userId) {}
    public record SeatReservationRequest(Long seatId) {}
    public record BalanceChargeRequest(long amount) {}
    public record PaymentRequest(Long reservationId) {}
}
