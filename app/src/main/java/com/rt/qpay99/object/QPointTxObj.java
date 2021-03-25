package com.rt.qpay99.object;

import java.util.Date;
import java.util.UUID;


public class QPointTxObj {
    private UUID ID;
    private int AgentID;
    private int QPoint;
    private String MSISDN;
    private String Descr;
    private String Status;
    private Date  CreatedTS;
    private Date  ExpiredTS;

    public QPointTxObj(int AgentID, int QPoint, String MSISDN, String Descr, String Status) {
        this.ID = UUID.randomUUID();
        this.AgentID = AgentID;
        this.QPoint = QPoint;
        this.MSISDN = MSISDN;
        this.Descr = Descr;
        this.Status = Status;
        this.CreatedTS =  new Date();
        this.ExpiredTS = new Date();
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public int getAgentID() {
        return AgentID;
    }

    public void setAgentID(int agentID) {
        AgentID = agentID;
    }

    public int getQPoint() {
        return QPoint;
    }

    public void setQPoint(int QPoint) {
        this.QPoint = QPoint;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public String getDescr() {
        return Descr;
    }

    public void setDescr(String descr) {
        Descr = descr;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Date getCreatedTS() {
        return CreatedTS;
    }

    public void setCreatedTS(Date createdTS) {
        CreatedTS = createdTS;
    }

    public Date getExpiredTS() {
        return ExpiredTS;
    }

    public void setExpiredTS(Date expiredTS) {
        ExpiredTS = expiredTS;
    }
}
