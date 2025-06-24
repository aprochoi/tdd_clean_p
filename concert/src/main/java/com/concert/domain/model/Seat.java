package com.concert.domain.model;

import com.concert.domain.service.QueueService;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이 좌석이 어떤 공연 스케쥴에 속하는지에 대한 정보 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_schedule_id")
    private ConcertSchedule concertSchedule;

    private int seatNumber;

    private int price;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Version
    private Long version;

    public enum SeatStatus { AVAILABLE, RESERVED, SOLD }

    public Seat(ConcertSchedule concertSchedule, int seatNumber, int price, SeatStatus status) {
        this.concertSchedule = concertSchedule;
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = status;
    }

    public boolean isAvailable() {
        return this.status == SeatStatus.AVAILABLE;
    }

    public void reserve() {
        if (!isAvailable()) {
            throw new IllegalStateException("이미 예약되었거나 판매된 좌석입니다.");
        }
        this.status = SeatStatus.RESERVED;
    }
}
