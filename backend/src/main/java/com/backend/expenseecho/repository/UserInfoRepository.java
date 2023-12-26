package com.backend.expenseecho.repository;

import com.backend.expenseecho.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
