package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;
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

    @Override
    public Transfer addNewTransfer(Transfer transfer) {

        String sql = "INSERT INTO transfer(transfer_id, transfer_type_id, sender_id, receiver_id, transfer_amount) VALUES (Default,?,?,?,?) RETURNING transfer_id;";
//        Integer newId = jdbcTemplate.queryForObject(sql,
//                Integer.class, transfer.getTransferTypeId(), transfer.getSenderName(), transfer.getReceiverName(), transfer.getTransferAmount());
        Integer newId = jdbcTemplate.queryForObject(sql,
                Integer.class, transfer.getTransferTypeId(), transfer.getSenderId(), transfer.getReceiverId(), transfer.getTransferAmount());
        transfer.setId(newId);
        transfers.add(transfer);
        return transfer;
    }

    @Override
    public List<Transfer> listByUserId(int userId) {
        List<Transfer> userTransfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE sender_id = ? OR receiver_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,userId,userId);
        while (results.next()){
            Transfer transfer = mapRowSetToTransfers(results);
            userTransfers.add(transfer);
        }
        return userTransfers;
    }


    private Transfer mapRowSetToTransfers(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setSenderId(results.getInt("sender_id"));
        transfer.setReceiverId(results.getInt("receiver_id"));
//        transfer.setSenderName(results.getString("sender_name"));
//        transfer.setReceiverName(results.getString("receiver_name"));
        transfer.setTransferAmount(results.getDouble("transfer_amount"));
        return transfer;
    }
}
