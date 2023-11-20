package com.akinovapp.bankapp.service;

import com.akinovapp.bankapp.dto.Response;
import com.akinovapp.bankapp.dto.TransactionRequest;
import com.akinovapp.bankapp.dto.UserRequest;

import java.util.List;

public interface UserService {

    Response registerUser(UserRequest userRequest);
    List<Response> allUsers();
    Response fetchUser(Long id);
    Response balanceEnquiry(String accountNumber);
    Response nameEnquiry(String accountNumber);
    Response credit(TransactionRequest transactionRequest);
    Response debit(TransactionRequest transactionRequest);

}

