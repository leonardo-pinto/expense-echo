package com.backend.expenseecho.seed;

import com.backend.expenseecho.model.entities.Budget;
import com.backend.expenseecho.model.entities.Category;
import com.backend.expenseecho.model.entities.Profile;
import com.backend.expenseecho.model.entities.User;
import com.backend.expenseecho.repository.BudgetRepository;
import com.backend.expenseecho.repository.CategoryRepository;
import com.backend.expenseecho.repository.ProfileRepository;
import com.backend.expenseecho.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryDataLoader implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
        loadProfileData();
        loadCategoryData();
        loadBudgetData();
    }

    private void loadCategoryData() {
        if (categoryRepository.count() == 0) {
            Category category1 = new Category("Groceries");
            Category category2 = new Category("Rent");
            categoryRepository.save(category1);
            categoryRepository.save(category2);
        }
    }

    private void loadUserData() {
        User user1 = new User("Leonardo", "Pinto", "mail@mail.com", "123456789");
        user1.setPassword(encoder.encode(user1.getPassword()));

        User user2 = new User("Gabriela", "Bittencourt", "mail2@mail.com", "6454789");
        user2.setPassword(encoder.encode(user2.getPassword()));

        userRepository.save(user1);
        userRepository.save(user2);
    }

    private void loadProfileData() {
        Optional<User> user1 = userRepository.findById(1);
        Profile profile1 = new Profile("Zizi", "", user1.get());

        Profile profile2 = new Profile("Gabi 07", "", user1.get());
        profileRepository.save(profile1);
        profileRepository.save(profile2);
    }

    private void loadBudgetData() {
        Optional<User> user1 = userRepository.findById(1);

        Budget budget1 = new Budget();
        budget1.setName("Main budget");
        budget1.setUser(user1.get());

        budgetRepository.save(budget1);

        Budget budget2 = new Budget();
        budget2.setName("Second budget");
        budget2.setUser(user1.get());

        budgetRepository.save(budget2);
    }

}
