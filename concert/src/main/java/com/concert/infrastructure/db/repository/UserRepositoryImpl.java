package com.concert.infrastructure.db.repository;

import com.concert.domain.model.User;
import com.concert.domain.repository.UserRepository;
import com.concert.infrastructure.db.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpa;

    @Override
    public Optional<User> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public User save(User user) {
        return jpa.save(user);
    }
}
