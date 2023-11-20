package com.akinovapp.bankapp.service;

import com.akinovapp.bankapp.dto.TransactionDto;
import com.akinovapp.bankapp.entity.Transaction;
import com.akinovapp.bankapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;


@Service
public class TransactionImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    public TransactionImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .build();

        transactionRepository.save(transaction);
    }
}

