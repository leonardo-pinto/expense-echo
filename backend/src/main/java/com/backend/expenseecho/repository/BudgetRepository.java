package com.backend.expenseecho.repository;

import com.backend.expenseecho.model.entities.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findByUserId(Integer userId);
    Optional<Budget> findByNameAndUserId(String name, Integer userId);

}
