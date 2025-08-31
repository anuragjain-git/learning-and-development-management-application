package com.vilt.narmada.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vilt.narmada.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
