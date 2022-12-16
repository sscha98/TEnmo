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
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
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
        Double userBalance = userDao.getAccountBalance(userDao.findIdByUsername(principal.getName()));
        if (transfer.getTransferAmount() <= 0 || transfer.getTransferAmount() > userBalance) {
            return transferStatusRejected;
        }
        else if ((transfer.getTransferTypeId() == 1) && !(principal.getName().equals(transfer.getReceiverId()))){
            transferDao.addNewTransfer(transfer);
            userDao.sendMoney(transfer.getTransferAmount(), userDao.findIdByUsername(principal.getName()));
            userDao.receiveMoney(transfer.getTransferAmount(), transfer.getReceiverId());
            return transferStatusApproved;
        }
        else if ((transfer.getTransferTypeId() == 2) && !(principal.getName().equals(transfer.getReceiverId()))){
            transferDao.addNewTransfer(transfer);
            userDao.sendMoney(transfer.getTransferAmount(), transfer.getReceiverId());
            userDao.receiveMoney(transfer.getTransferAmount(), userDao.findIdByUsername(principal.getName()));
            return transferStatusApproved;
        }
        return transferStatusRejected;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public double currentBalance(Principal principal) {
        return userDao.getAccountBalance(userDao.findIdByUsername(principal.getName()));
    }

    @RequestMapping(path = "/transfers/users",method = RequestMethod.GET)
    public List<String> listUsernameAndId(Principal principal){
        List<String> users = new ArrayList<>();
        List<User> tenmoUsers = userDao.findAll();
        for (User user : tenmoUsers){
            if (!principal.getName().equals(user.getUsername())){
                users.add(user.getUsername() + " : " + user.getId());
            }
        }
        return users;
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> listTransfers(Principal principal) {
//        List<Transfer> transfers = transferDao.list();
//        List<Transfer> userTransfers = new ArrayList<>();
//        for (Transfer transfer : transfers){
//            if (transfer.getSenderName().equals(principal.getName())|| transfer.getReceiverName().equals(principal.getName())){
//                userTransfers.add(transfer);
//            }
//        }
        return transferDao.listByUserId(userDao.findIdByUsername(principal.getName()));
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
