package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    Transfer addNewTransfer(Transfer transfer);
    List<Transfer> list();
}
