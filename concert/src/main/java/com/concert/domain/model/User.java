package com.concert.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usr")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private long balance;

    public User(String name, long balance) {
        this.name = name;
        this.balance = balance;
    }

    public User(long id, String name, long balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public void charge(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야합니다.");
        }
        this.balance += amount;
    }

    public void use(long amount) {
        if (this.balance < amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }
        this.balance -= amount;
    }
}
