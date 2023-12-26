package com.backend.expenseecho.repository;

import com.backend.expenseecho.model.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    List<Profile> findByUserId(Integer userId);
    Optional<Profile> findByNameAndUserId(String name, Integer userId);
}
