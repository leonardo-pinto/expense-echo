package com.backend.expenseecho.repository;

import com.backend.expenseecho.model.dto.TransactionResultByDateAndType;
import com.backend.expenseecho.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    // Native SQL query
    @Query(value = "SELECT * FROM Transactions " +
            "WHERE budget_id = :budgetId AND MONTH(transaction_date) = :month AND YEAR(transaction_date) = :year ", nativeQuery = true)
    List<Transaction> findByBudgetIdAndDateInterval(@Param("budgetId") int budgetId, @Param("month") int month, @Param("year") int year);

    // JPQL query
    @Query("SELECT new com.backend.expenseecho.model.dto.TransactionResultByDateAndType(SUM(t.amount), t.type) " +
            "FROM Transaction t " +
            "WHERE t.budget.id = :budgetId AND FUNCTION('MONTH', t.date) = :month AND FUNCTION('YEAR', t.date) = :year " +
            "GROUP BY t.type")
    List<TransactionResultByDateAndType> getTransactionResultByDateGroupByType(@Param("budgetId") int budgetId, @Param("month") int month, @Param("year") int year);
}
