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
    public String createNewTransfer(@RequestBody Transfer transfer, Principal principal) {
        String transferStatusApproved = "Transfer Status: Approved!";
        String transferStatusRejected = "Transfer Status: Rejected!";
        Double userBalance = userDao.getAccountBalance(principal.getName());
        if (transfer.getTransferAmount() <= 0 || transfer.getTransferAmount() > userBalance) {
            return transferStatusRejected;
        }
        else if ((transfer.getTransferTypeId() == 1) && !(principal.getName().equals(transfer.getReceiverName()))){
            transferDao.addNewTransfer(transfer);
            userDao.sendMoney(transfer.getTransferAmount(), principal.getName());
            userDao.receiveMoney(transfer.getTransferAmount(), transfer.getReceiverName());
            return transferStatusApproved;
        }
        else if ((transfer.getTransferTypeId() == 2) && !(principal.getName().equals(transfer.getReceiverName()))){
            transferDao.addNewTransfer(transfer);
            userDao.sendMoney(transfer.getTransferAmount(), transfer.getReceiverName());
            userDao.receiveMoney(transfer.getTransferAmount(), principal.getName());
            return transferStatusApproved;
        }
        return transferStatusRejected;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public double currentBalance(Principal principal) {
        return userDao.getAccountBalance(principal.getName());
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
