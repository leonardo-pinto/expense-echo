package com.backend.expenseecho.seed;

import com.backend.expenseecho.model.entities.Category;
import com.backend.expenseecho.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryDataLoader implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCategoryData();
    }

    private void loadCategoryData(){
        if (categoryRepository.count() == 0) {
            Category category1 = new Category("Groceries");
            Category category2 = new Category("Rent");
            categoryRepository.save(category1);
            categoryRepository.save(category2);
        }
    }
}
