package com.rt.qpay99.object;


/**
 * Created by kimwei on 7/26/2016.
 */
public class BankIn {
    private String Bank;
    private String Amount;
    private String DateBI;
    private String Time;
    private String Status;
    private String RecipientReference;
    private String Created;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public String getDateBI() {
        return DateBI;
    }

    public void setDateBI(String dateBI) {
        DateBI = dateBI;
    }

    public String getRecipientReference() {
        return RecipientReference;
    }

    public void setRecipientReference(String recipientReference) {
        RecipientReference = recipientReference;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
