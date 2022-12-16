package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.security.Principal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);
    void sendMoney(double transferAmount,int senderId);
    void receiveMoney(double transferAmount, int receiverId);
    boolean create(String username, String password);
    double getAccountBalance(int userId);
}
