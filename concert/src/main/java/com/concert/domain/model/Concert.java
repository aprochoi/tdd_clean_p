package com.concert.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String artist;

    // Concert 하나는 여러개의 ConcertSchedule을 가진다 (1:N)
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<ConcertSchedule> schedules = new ArrayList<>();

    public Concert(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }
}
