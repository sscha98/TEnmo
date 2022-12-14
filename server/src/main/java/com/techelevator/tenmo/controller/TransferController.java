package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccount;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.RegisterUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {


    private AccountDao accountDao;
    private UserDao userDao;

    public TransferController(UserDao userDao) {
        this.userDao = userDao;
        this.accountDao = new JdbcAccount(userDao);
    }
    @RequestMapping(value = "/account/balance", method = RequestMethod.GET)
    public void balance(@Valid @RequestBody Principal principal) {
            //accountDao.getAccountBalance();
    }


}
