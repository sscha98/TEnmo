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

        String sql = "INSERT INTO transfer(transfer_id, transfer_type_id, sender_name, receiver_name, transfer_amount, private) VALUES (Default,?,?,?,?,?) RETURNING transfer_id;";
        Integer newId = jdbcTemplate.queryForObject(sql,
                Integer.class, transfer.getTransferTypeId(), transfer.getSenderName(), transfer.getReceiverName(), transfer.getTransferAmount(), transfer.isPrivate());
        transfer.setId(newId);
        transfers.add(transfer);
        return transfer;
    }

    @Override
    public List<Transfer> list() {
        return transfers;
    }


    private Transfer mapRowSetToMessages(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setSenderName(results.getString("sender_name"));
        transfer.setPrivate(results.getBoolean("private"));
        transfer.setReceiverName(results.getString("receiver_name"));
        transfer.setTransferAmount(results.getDouble("transfer_amount"));
        return transfer;
    }
}
