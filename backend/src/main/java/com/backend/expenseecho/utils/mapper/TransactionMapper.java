package com.backend.expenseecho.utils.mapper;

import com.backend.expenseecho.model.dto.Transaction.CreateTransactionRequest;
import com.backend.expenseecho.model.dto.Transaction.TransactionResponse;
import com.backend.expenseecho.model.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    Transaction convert(CreateTransactionRequest createTransactionRequest);
    TransactionResponse convert(Transaction transaction);

    default TransactionResponse transactionToTransactionResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transaction.getId());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setDescription(transaction.getDescription());
        transactionResponse.setType(transaction.getType().toString());
        transactionResponse.setCategoryName(transaction.getCategory().getName());
        transactionResponse.setProfileName(transaction.getProfile().getName());
        transactionResponse.setTransactionDate(transaction.getDate());

        return transactionResponse;
    }
}
