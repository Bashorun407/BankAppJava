package com.akinovapp.bankapp.repository;

import com.akinovapp.bankapp.entity.User;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //A list of methods to query the database

    //This checks if the email already exists in the database
    Boolean existsByEmail(String email);

    //This checks if user exists by account number
    Boolean existsByAccountNumber(String accountNumber);

    //This finds user by account number
    User findByAccountNumber(String accountNumber);

}
