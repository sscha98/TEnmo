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
    private UserDao userDao;

    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    //post method to post new transfer into database
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    //takes a transfer and principal parameter
    public String createNewTransfer(@RequestBody Transfer transfer, Principal principal) {
        //status message for whether the transfer has been approved or rejected
        String transferStatusApproved = "Transfer Status: Approved!";
        String transferStatusRejected = "Transfer Status: Rejected!";
        //retrieves the account balance based on the logged in user's username
        Double userBalance = userDao.getAccountBalance(userDao.findIdByUsername(principal.getName()));

        //checks to see if the account balance is 0 or negative
        //if the transfer type ==1, then it is a sending of money by the user
        //if the transfer type ==2, then is a request for money by the user
        //rejects transfer if the transfer money exceeds the amount by the sender (send)
        //rejects the transfer if the transfer money exceeds the amount by the receiver (request)
        if (transfer.getTransferAmount() <= 0 || (transfer.getTransferTypeId()==1 && transfer.getTransferAmount() > userBalance )||
                (transfer.getTransferTypeId()==2 && transfer.getTransferAmount() > userDao.getAccountBalance(transfer.getReceiverId()))) {
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

    //get method to retrieve the user's current balance
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public double currentBalance(Principal principal) {
        return userDao.getAccountBalance(userDao.findIdByUsername(principal.getName()));
    }

    //retrieves a list of users from tenmo_user table, excluding the logged in user
    @RequestMapping(path = "/transfers/users",method = RequestMethod.GET)
    public List<String> listUsernameAndId(Principal principal){
        //list to add the users
        List<String> users = new ArrayList<>();
        //retrieves all the users from the tenmo_user table
        List<User> tenmoUsers = userDao.findAll();
        //for each loop to sort through the tenmo users and add to the user list except for the logged in user
        for (User user : tenmoUsers){
            if (!principal.getName().equals(user.getUsername())){
                users.add(user.getUsername() + " : " + user.getId());
            }
        }
        return users;
    }

    //retrieves transfer based on transfer id
    @RequestMapping(path = "/transfers/{id}",method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id, Principal principal){

        //retrieves the transfer based on transfer id as long as the logged in user is either the sender or receiver
        Transfer transfer = transferDao.findByTransferId(id);
        if ((userDao.findIdByUsername(principal.getName())==transfer.getReceiverId()) || (userDao.findIdByUsername(principal.getName())==transfer.getSenderId())){
            return transfer;
        }else {
            return null;
        }
    }

    //retrieves the list of all transfers
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> listTransfers(Principal principal) {
        return transferDao.listByUserId(userDao.findIdByUsername(principal.getName()));
    }

}
