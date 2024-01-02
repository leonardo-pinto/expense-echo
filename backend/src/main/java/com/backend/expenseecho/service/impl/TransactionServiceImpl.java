package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.exception.ResourceNotFoundException;
import com.backend.expenseecho.exception.UnauthorizedException;
import com.backend.expenseecho.model.dto.Transaction.*;
import com.backend.expenseecho.model.entities.Budget;
import com.backend.expenseecho.model.entities.Category;
import com.backend.expenseecho.model.entities.Profile;
import com.backend.expenseecho.model.entities.Transaction;
import com.backend.expenseecho.model.enums.TransactionType;
import com.backend.expenseecho.repository.BudgetRepository;
import com.backend.expenseecho.repository.CategoryRepository;
import com.backend.expenseecho.repository.ProfileRepository;
import com.backend.expenseecho.repository.TransactionRepository;
import com.backend.expenseecho.service.TransactionService;
import com.backend.expenseecho.utils.mapper.TransactionMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileRepository profileRepository;
    private final BudgetRepository budgetRepository;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository,
            ProfileRepository profileRepository,
            BudgetRepository budgetRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.profileRepository = profileRepository;
        this.budgetRepository = budgetRepository;
    }

    @Override
    public TransactionResponse create(CreateTransactionRequest request, String userId) {
        Transaction transaction = TransactionMapper.INSTANCE.convert(request);

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found."));

        if (!profile.getUser().getId().toString().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to perform this action.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
        Budget budget = budgetRepository.findById(request.getBudgetId())
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found."));

        transaction.setProfile(profile);
        transaction.setCategory(category);
        transaction.setBudget(budget);

        Transaction createdTransaction = transactionRepository.save(transaction);

        return TransactionMapper.INSTANCE.transactionToTransactionResponse(createdTransaction);
    }

    @Override
    public TransactionResponse update(String id, UpdateTransactionRequest request, String userId) {
        Transaction transaction = transactionRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found."));

        if (!request.getCategoryId().equals(transaction.getCategory().getId())) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
            transaction.setCategory(category);
        }
        if (!request.getProfileId().equals(transaction.getProfile().getId())) {
            Profile profile = profileRepository.findById(request.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found."));
            if (!profile.getUser().getId().toString().equals(userId)) {
                throw new UnauthorizedException("User is not authorized to perform this action.");
            }
            transaction.setProfile(profile);
        }

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transactionRepository.save(transaction);

        return TransactionMapper.INSTANCE.transactionToTransactionResponse(transaction);
    }

    @Override
    public TransactionResponse getById(String id, String userId) {
        Transaction transaction = transactionRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found."));

        if (!transaction.getProfile().getUser().getId().equals(Integer.parseInt(userId))) {
            throw new UnauthorizedException("User is not authorized to perform this action.");
        }
        return TransactionMapper.INSTANCE.transactionToTransactionResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getTransactionsByDateAndBudget(YearMonth date, int budgetId, String userId) {
        validateBudgetAndUserId(budgetId, Integer.parseInt(userId));

        List<Transaction> transactions = transactionRepository.findByBudgetIdAndDateInterval(budgetId, date.getMonthValue(), date.getYear());
        List<TransactionResponse> transactionResponseList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionResponseList.add(TransactionMapper.INSTANCE.transactionToTransactionResponse(transaction));
        }
        return transactionResponseList;
    }

    @Override
    public TransactionResultByDateAndTypeResponse getTransactionsResultByDateAndBudget(YearMonth date, int budgetId, String userId) {
        validateBudgetAndUserId(budgetId, Integer.parseInt(userId));

        List<TransactionResultByDateAndType> transactionsResult = transactionRepository.getTransactionResultByDateGroupByType(budgetId, date.getMonthValue(), date.getYear());
        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal totalIncome = BigDecimal.ZERO;

        if (!transactionsResult.isEmpty()) {
            for (TransactionResultByDateAndType transactionResult : transactionsResult) {
                if (transactionResult.getType() == TransactionType.EXPENSE) {
                    totalExpense = transactionResult.getTotalAmount();
                } else {
                    totalIncome = transactionResult.getTotalAmount();
                }
            }
        }
        return new TransactionResultByDateAndTypeResponse(totalExpense, totalIncome);
    }

    private void validateBudgetAndUserId(int budgetId, int userId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found."));

        if (!budget.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to perform this action.");
        }
    }
}
