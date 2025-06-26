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

    /**
     * 결제를 위해 예약 상태가 유효한지 검증하는 메소드
     */
    public void validateForPayment() {
        if (this.status != ReservationStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("결제 대기 상태의 예약만 결제할 수 있습니다.");
        }

        if (LocalDateTime.now().isAfter(this.expiresAt)) {
            throw new IllegalStateException("결제 가능 시간이 만료되었습니다.");
        }
    }

    /**
     * 예약 상태를 완료로 변경하는 메소드
     */
    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }
}
