package com.concert.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 콘서트에 속하는지에 대한 정보 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    private LocalDate concertDate;

    // ConcertSchedule 하나는 여러 개의 Seat을 가진다 (1:N)
    @OneToMany(mappedBy = "concertSchedule", cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();

    public ConcertSchedule(Concert concert, LocalDate concertDate) {
        this.concert = concert;
        this.concertDate = concertDate;
    }
}
