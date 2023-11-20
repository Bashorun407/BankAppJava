package com.akinovapp.bankapp.service;

import com.akinovapp.bankapp.dto.*;
import com.akinovapp.bankapp.email.emailDto.EmailDetails;
import com.akinovapp.bankapp.email.emailService.EmailService;

import com.akinovapp.bankapp.entity.User;
import com.akinovapp.bankapp.repository.UserRepository;
import com.akinovapp.bankapp.utils.ResponseUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TransactionService transactionService;

    private final EmailService emailService;

    //Class constructor
    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService, EmailService emailService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.emailService = emailService;
    }

    //1) Method to create new user
    @Override
   public Response registerUser(UserRequest userRequest){

        /*
        check that user exists
        if not, send a response
        else, save new user and send a response
         */

       //To check if user exists or not
       boolean existByMail = userRepository.existsByEmail(userRequest.getEmail());

       //Check if user is already in the database
       if (existByMail)
           return Response.builder()
                   .responseCode(ResponseUtils.USER_EXISTS_CODE)
                   .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                   .build();

       //If user does not exist, create new user
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.LENGTH_OF_ACCOUNT_NUMBER))
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativeNumber(userRequest.getAlternativeNumber())
                .status(ResponseUtils.ACTIVE)
                .dateOfBirth(userRequest.getDateOfBirth())
                .build();

        //To save new user to repository
        User savedUser = userRepository.save(newUser);

        String accountDetails = savedUser.getFirstName() + " " +
                savedUser.getLastName() + " " + savedUser.getOtherName() + "\n" + "Account Number: " + savedUser.getAccountNumber()
                + "Account Balance: " + savedUser.getAccountBalance();

        // Preparing email content
        EmailDetails emailDetail = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject(ResponseUtils.USER_REGISTERED_SUCCESS)
                .messageBody( "Contragulations! Your account has been successfully created. " +
                        "Kindly find your account details below: \n" +
                                "\n Account Details:\n"+
                        accountDetails)
                .build();

        //Send mail
        emailService.sendSimpleEmail(emailDetail);

        //Response
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS)
                .data(Data.builder()
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }

    //2) Method to list all users
    public List<Response> allUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream().map(
                user -> Response.builder()
                        .responseCode(ResponseUtils.USER_EXISTS_CODE)
                        .responseMessage(ResponseUtils.SUCCES_MESSAGE)
                        .data(Data.builder()
                                        .accountNumber(user.getAccountNumber())
                                                .accountName(user.getFirstName())
                                .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                                .build())
                        .build()).collect(Collectors.toList());
    }

    //3) Method to find user by id
    public Response fetchUser(Long id){

        boolean userExists = userRepository.existsById(id);

        //Check if user exists by id
        if (!userExists)
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();

        User user = userRepository.findById(id).get();

        return Response.builder()
                .responseCode(ResponseUtils.USER_EXISTS_CODE)
                .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                .data(Data.builder()
                        .accountName(user.getLastName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    //4) Method to find account balance
    public Response balanceEnquiry(String accountNumber){
        boolean existByAccountNumber = userRepository.existsByAccountNumber(accountNumber);

        //If user does not exist
        if(!existByAccountNumber)
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();

        //If user exists
        User user = userRepository.findByAccountNumber(accountNumber);


        String accountDetails = user.getFirstName() + " " +
                user.getLastName() + " " + user.getOtherName() + "\n" + "Account Number: " + user.getAccountNumber()
                + "Account Balance: " + user.getAccountBalance();

        // Preparing email content
        EmailDetails emailDetail = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject(ResponseUtils.USER_REGISTERED_SUCCESS)
                .messageBody( "Contragulations! Your account has been successfully created. " +
                        "Kindly find your account details below: \n" +
                                "\n Account Details:\n" +
                        accountDetails)
                .build();

        //Send mail
        emailService.sendSimpleEmail(emailDetail);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCES_MESSAGE)
                .data(Data.builder()
                        .accountName(user.getFirstName() + " "+ user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    //5) Method to find user details...name enquiry (Though...not necessary; similar to method (4)
    public Response nameEnquiry(String accountNumber){
        boolean existByAccountNumber = userRepository.existsByAccountNumber(accountNumber);

        //Check if user exist by account number
        if (!existByAccountNumber)
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();

        //If user exists
        User user = userRepository.findByAccountNumber(accountNumber);

        String accountDetails = user.getFirstName() + " " +
                user.getLastName() + " " + user.getOtherName() + "\n" + "Account Number: " + user.getAccountNumber()
                + "Account Balance: " + user.getAccountBalance();

        // Preparing email content
        EmailDetails emailDetail = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject(ResponseUtils.USER_REGISTERED_SUCCESS)
                .messageBody( "Contragulations! Your account has been successfully created. " +
                        "Kindly find your account details below: \n" +
                                "\n Account Details:\n" +
                        accountDetails)
                .build();

        //Send mail
        emailService.sendSimpleEmail(emailDetail);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCES_MESSAGE)
                .data(Data.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    //6) Method to credit user account balance
    public Response credit(TransactionRequest transactionRequest){
        boolean existByAccountNumber = userRepository.existsByAccountNumber(transactionRequest.getAccountNumber());

        //Check that user exist by Account Number
        if (!existByAccountNumber)
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();

        //If user exist, retrieve the user information
        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());

        //Populate the transactionDto and save in transaction repository
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("CREDIT")
                .amount(user.getAccountBalance())
                .build();
        //Here, transaction gets to database and returned to transaction variable
        transactionService.saveTransaction(transactionDto);

        //Here, user balance changes
        BigDecimal newBalance = user.getAccountBalance().add(transactionRequest.getAmount());
        user.setAccountBalance(newBalance);

        //updated user information gets saved into userRepository/database
        userRepository.save(user);

        String accountDetails = user.getFirstName() + " " +
                user.getLastName() + " " + user.getOtherName() + "\n" + "Account Number: " + user.getAccountNumber()
                + "Account Balance: " + user.getAccountBalance();

        // Preparing email content
        EmailDetails emailDetail = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject(ResponseUtils.USER_REGISTERED_SUCCESS)
                .messageBody( "Contragulations! Your account has been successfully created. " +
                        "Kindly find your account details below: \n" +
                                "\n Account Details:\n" +
                        accountDetails)
                .build();

        //Send mail
        emailService.sendSimpleEmail(emailDetail);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage("BALANCE CREDITED")
                .data(Data.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(newBalance)
                        .build())
                .build();
    }

    //7) Method to debit user account balance
    public Response debit(TransactionRequest transactionRequest){

        boolean existByAccountNumber = userRepository.existsByAccountNumber(transactionRequest.getAccountNumber());

        //Here, user is checked if it exists
        if (!existByAccountNumber)
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();

        //IF the user exists, retrieve the user information
        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());

        //Populate the transactionDto and save in the transaction repository
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("DEBIT")
                .accountNumber(transactionRequest.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        //New balance
        BigDecimal newBalance = user.getAccountBalance().subtract(transactionRequest.getAmount());

        //Update User balance
        user.setAccountBalance(newBalance);
        //Save updated user balance in the user repository
        userRepository.save(user);

        String accountDetails = user.getFirstName() + " " +
                user.getLastName() + " " + user.getOtherName() + "\n" + "Account Number: " + user.getAccountNumber()
                + "Account Balance: " + user.getAccountBalance();

        // Preparing email content
        EmailDetails emailDetail = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject(ResponseUtils.USER_REGISTERED_SUCCESS)
                .messageBody( "Contragulations! Your account has been successfully created. " +
                        "Kindly find your account details below: \n" +
                                "\n Account Details:\n" +
                        accountDetails)
                .build();

        //Send mail
        emailService.sendSimpleEmail(emailDetail);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage("BALANCE DEBITED")
                .data(Data.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();
    }
}
