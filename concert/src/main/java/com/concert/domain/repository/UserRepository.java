package com.concert.domain.repository;

import com.concert.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    User save(User user);
}
