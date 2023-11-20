package com.akinovapp.bankapp.controller;

import com.akinovapp.bankapp.dto.Response;
import com.akinovapp.bankapp.dto.TransactionRequest;
import com.akinovapp.bankapp.dto.UserRequest;
import com.akinovapp.bankapp.service.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankSearch")
public class UserController {

    private final UserServiceImpl userService;

    //Class constructor
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    //1) Method to register user
    @PostMapping("/registerUser")
    public Response registerUser(@RequestBody UserRequest userRequest){
        return userService.registerUser(userRequest);
    }

    //2) Method to list all users in the repository
    @GetMapping("/allUsers")
    public List<Response> allUsers(){
        return userService.allUsers();
    }

    //3) Method to fetch User by id
    @GetMapping("/fetchUserById/{id}")
    public Response fetchUser(@PathVariable(name = "id") Long id){
        return userService.fetchUser(id);
    }

    //4) Method for balance enquiry
    @GetMapping("/balanceEnquiry/{accountNumber}")
    public Response balanceEnquiry(@PathVariable(name = "accountNumber") String accountNumber){
        return userService.balanceEnquiry(accountNumber);
    }

    //5) Method to find user details...name enquiry (Though...not necessary; similar to method (4)
    @GetMapping("/nameEnquiry/{accountNumber}")
    public Response nameEnquiry(@PathVariable(name = "accountNumber") String accountNumber){
        return userService.nameEnquiry(accountNumber);
    }

    //6) Method to credit user account balance
    @PutMapping("/creditTransaction")
    public Response credit(@RequestBody TransactionRequest transactionRequest){
        return userService.credit(transactionRequest);
    }

    //7) Method to debit user account balance
    @PutMapping("/debitTransaction")
    public Response debit(@RequestBody TransactionRequest transactionRequest){
        return userService.debit(transactionRequest);
    }
}
