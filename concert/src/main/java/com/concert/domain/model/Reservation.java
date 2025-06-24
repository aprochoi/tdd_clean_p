package com.concert.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private int price; // 예약 시점의 가격

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt; // 결제 만료 시간

    public enum ReservationStatus { PENDING_PAYMENT, COMPLETED, CANCELLED, EXPIRED }

    @Builder
    public Reservation(User user, Seat seat, ReservationStatus status, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.user = user;
        this.seat = seat;
        this.price = seat.getPrice();
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
