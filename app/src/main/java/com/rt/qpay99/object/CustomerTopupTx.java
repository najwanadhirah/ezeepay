package com.rt.qpay99.object;

/**
 * Created by User on 1/17/2017.
 */

public class CustomerTopupTx {
    private String Amount;
    private String NewBalance;
    private String Type;
    private String CreatedTS;
    private String Remarks;

    public String getNewBalance() {
        return NewBalance;
    }

    public void setNewBalance(String newBalance) {
        NewBalance = newBalance;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCreatedTS() {
        return CreatedTS;
    }

    public void setCreatedTS(String createdTS) {
        CreatedTS = createdTS;
    }


}
