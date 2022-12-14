package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transfer {

    private int id;
    private int transferTypeId;
    private String senderName;
    private String receiverName;
    private double transferAmount;

    @JsonProperty("isPrivate")
    private boolean isPrivate = false;

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
    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public double getTransferAmount() {
        return transferAmount;
    }
    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }
    public boolean isPrivate() {
        return isPrivate;
    }
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}