package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.exception.BadRequestException;
import com.backend.expenseecho.exception.ResourceNotFoundException;
import com.backend.expenseecho.exception.UnauthorizedException;
import com.backend.expenseecho.model.dto.Budget.BudgetResponse;
import com.backend.expenseecho.model.dto.Budget.CreateBudgetRequest;
import com.backend.expenseecho.model.dto.Budget.UpdateBudgetRequest;
import com.backend.expenseecho.model.entities.Budget;
import com.backend.expenseecho.model.entities.User;
import com.backend.expenseecho.repository.BudgetRepository;
import com.backend.expenseecho.repository.UserRepository;
import com.backend.expenseecho.service.BudgetService;
import com.backend.expenseecho.utils.mapper.BudgetMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private BudgetRepository budgetRepository;
    private UserRepository userRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BudgetResponse create(CreateBudgetRequest request, String userId) {
        User user = userRepository
                .findById(Integer.parseInt(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User account not found."));

        if(budgetNameAlreadyExists(request.getName(), user.getId())) {
            throw new BadRequestException("Budget name already exists.");
        }

        Budget budget = BudgetMapper.INSTANCE.convert(request);
        budget.setUser(user);
        Budget createdBudget = budgetRepository.save(budget);

        return new BudgetResponse(createdBudget);
    }

    @Override
    public List<BudgetResponse> getAllByUserId(String userId) {
        List<Budget> budgets = budgetRepository.findByUserId(Integer.parseInt(userId));
        return budgets.stream().map(BudgetResponse::new).toList();
    }

    @Override
    public BudgetResponse update(String id, UpdateBudgetRequest request, String userId) {
        Budget budget = budgetRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found."));

        if (budgetNameAlreadyExists(request.getName(), Integer.parseInt(userId))) {
            throw new BadRequestException("Budget name already exists.");
        }

        if (!budget.getUser().getId().toString().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to perform this action.");
        }

        budget.setName(request.getName());
        budgetRepository.save(budget);
        return new BudgetResponse(budget);
    }

    @Override
    public void delete(String id, String userId) {
        Budget budget = budgetRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found."));

        if (!budget.getUser().getId().toString().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to perform this action.");
        }

        budgetRepository.delete(budget);
    }

    private Boolean budgetNameAlreadyExists(String budgetName, int userId) {
        return budgetRepository.findByNameAndUserId(budgetName, userId).isPresent();
    }
}
