package com.rt.qpay99.object;

/**
 * Created by User on 1/22/2018.
 */

public class AddressObj {

    private String name;
    private String mobileNumber;
    private String address1;
    private String address2;
    private String passcode;
    private String state;
    private String EmailAddess;


    private int Id;
    public int QPoints;
    public int AgentID;
    private String RefNo;

    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getQPoints() {
        return QPoints;
    }

    public void setQPoints(int QPoints) {
        this.QPoints = QPoints;
    }

    public int getAgentID() {
        return AgentID;
    }

    public void setAgentID(int agentID) {
        AgentID = agentID;
    }



    public String getEmailAddess() {
        return EmailAddess;
    }

    public void setEmailAddess(String emailAddess) {
        EmailAddess = emailAddess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
