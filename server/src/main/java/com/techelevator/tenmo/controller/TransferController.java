package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.RegisterUserDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.ClientInfoStatus;
import java.util.List;

//@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {

    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao,AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public void createNewTransfer(@RequestBody Transfer transfer) {
        transferDao.addNewTransfer(transfer);
    }


    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public void currentBalance(@RequestBody Principal principal) {
        accountDao.getAccountBalance(accountDao.findAccountIdByUsername(principal.getName()));

    }
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> listTransfers(@RequestParam(defaultValue = "") String username) {
        return transferDao.list();

    }
    //@RequestMapping(value = "/account/{id}/balance", method = RequestMethod.GET)
    //public void balance(@PathVariable int id) {
    //    System.out.println(accountDao.getAccountBalance(id));
    //}

   // @RequestMapping(path = "/users", method = RequestMethod.GET)
    //public List<User> listUsers() {
    //    return userDao.findAll();
    //}



}
