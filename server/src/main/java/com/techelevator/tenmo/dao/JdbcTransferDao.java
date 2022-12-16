package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    private List<Transfer> transfers = new ArrayList<>();


    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //runs a post request to add a new transfer to the transfer table
    @Override
    public boolean addNewTransfer(Transfer transfer) {
        //sql query script to insert into transfer table
        String sql = "INSERT INTO transfer(transfer_id, transfer_type_id, sender_id, receiver_id, transfer_amount) VALUES (Default,?,?,?,?) RETURNING transfer_id;";
        Integer newId;
        //try: runs the sql query to add to transfer table with column data from transfer getter methods
        //catch: returns false when exception is caught
        try{
            newId = jdbcTemplate.queryForObject(sql,
                Integer.class, transfer.getTransferTypeId(), transfer.getSenderId(), transfer.getReceiverId(), transfer.getTransferAmount());
            transfer.setId(newId);
            transfers.add(transfer);
        }catch (DataAccessException e){
            return false;
        }
        return true;
    }

    // retrieves a list of transfers based on userId
    @Override
    public List<Transfer> listByUserId(int userId) {
        List<Transfer> userTransfers = new ArrayList<>();
        //sql query script to return rows from transfer table where the sender_id or receiver_id matches userId
        String sql = "SELECT * FROM transfer WHERE sender_id = ? OR receiver_id = ?";
        // returns all the rows from sql scipt
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,userId,userId);
        //declares new Transfers from the rows returned by the sql statement and adds to the list of transfers
        while (results.next()){
            Transfer transfer = mapRowSetToTransfers(results);
            userTransfers.add(transfer);
        }
        return userTransfers;
    }

    //returns transfer based on transferId
    //sql query selects from the transfer table where the transfer_id matches transferId
    @Override
    public Transfer findByTransferId(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,transferId);
        if (rowSet.next()){
            return mapRowSetToTransfers(rowSet);
        }else {
            return null;
        }
    }

    //declares a new Transfer from the rows returned by an SQL query and uses the column data to set the transfer variables
    private Transfer mapRowSetToTransfers(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setSenderId(results.getInt("sender_id"));
        transfer.setReceiverId(results.getInt("receiver_id"));
        transfer.setTransferAmount(results.getDouble("transfer_amount"));
        return transfer;
    }
}
