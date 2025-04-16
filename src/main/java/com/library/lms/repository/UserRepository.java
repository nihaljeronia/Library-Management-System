package com.library.lms.repository;

import com.library.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStatus(String status);
    Optional<User> findByEmail(String email);
}