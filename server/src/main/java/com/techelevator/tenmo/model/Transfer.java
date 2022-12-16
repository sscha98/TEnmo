package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transfer {

    private int id;
    private int transferTypeId;
//    private String senderName;
//    private String receiverName;
    private int senderId;
    private int receiverId;
    private double transferAmount;



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


//    public String getSenderName() {
//        return senderName;
//    }
//    public void setSenderName(String senderName) {
//        this.senderName = senderName;
//    }
//    public String getReceiverName() {
//        return receiverName;
//    }
//    public void setReceiverName(String receiverName) {
//        this.receiverName = receiverName;
//    }


}