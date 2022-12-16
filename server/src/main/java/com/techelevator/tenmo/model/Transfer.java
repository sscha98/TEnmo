package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transfer {
    //Transfer class variables that match the column variables of the sql transfer table
    private int id;
    private int transferTypeId;
    private int senderId;
    private int receiverId;
    private double transferAmount;

    //empty constructor and constructor with transfer variable parameters
    public Transfer() {
    }

    public Transfer(int id, int transferTypeId, int senderId, int receiverId, double transferAmount) {
        this.id = id;
        this.transferTypeId = transferTypeId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.transferAmount = transferAmount;
    }
    //getters and setters for the variables
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getTransferTypeId(){
        return transferTypeId;
    }
    public void setTransferTypeId(int messageTypeId) {
        this.transferTypeId = messageTypeId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public double getTransferAmount() {
        return transferAmount;
    }
    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }



}