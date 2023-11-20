package com.akinovapp.bankapp.service;

import com.akinovapp.bankapp.dto.TransactionDto;
import com.akinovapp.bankapp.entity.Transaction;

public interface TransactionService {
    void saveTransaction (TransactionDto transactionDto);
}
