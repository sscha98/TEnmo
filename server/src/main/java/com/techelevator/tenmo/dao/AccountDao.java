package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.security.Principal;

public interface AccountDao {

    Account findAccountByUsername(UserDao userDao);

    double getAccountBalance(int userId);

    int findAccountIdByUsername(String username);



}
