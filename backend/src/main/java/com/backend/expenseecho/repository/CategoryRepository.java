package com.backend.expenseecho.repository;

import com.backend.expenseecho.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByUserIdOrIsDefaultTrue(int userId);

    @Query(
            value = "SELECT * FROM Categories c WHERE c.name = :name AND (c.user_id = :userId OR c.is_default = true)",
            nativeQuery = true
    )
    Optional<Category> findByNameAndUserIdOrIsDefaultTrue(@Param("name") String name, @Param("userId") int userId);
}
