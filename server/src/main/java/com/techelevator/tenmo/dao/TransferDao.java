package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    boolean addNewTransfer(Transfer transfer);
    List<Transfer> listByUserId(int userId);
    Transfer findByTransferId(int transferId);
}
