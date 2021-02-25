package com.rt.qpay99.object;

public class RequestReloadPinObject {
	private String sClientUserName;
	private String sClientPassword;
	private String sLocalMOID;
	private String sSerialNumber;
	private String sReloadPin;
	private String sExpiryDate;
	private String sReloadTelco;
	private String sAmount;
	private String sInstruction;
	private String sDescription;
	private String sDNReceivedID;
	private boolean getReloadPINResult;
	private String sPurchaseTS;
	private String sRetailPrice;
	private String sBatchID;

	public String getsBatchID() {
		return sBatchID;
	}

	public void setsBatchID(String sBatchID) {
		this.sBatchID = sBatchID;
	}

	public String getsRetailPrice() {
		return sRetailPrice;
	}

	public void setsRetailPrice(String sRetailPrice) {
		this.sRetailPrice = sRetailPrice;
	}
	public String getsPurchaseTS() {
		return sPurchaseTS;
	}

	public void setsPurchaseTS(String sPurchaseTS) {
		this.sPurchaseTS = sPurchaseTS;
	}

	public String getsDNReceivedID() {
		return sDNReceivedID;
	}

	public void setsDNReceivedID(String sDNReceivedID) {
		this.sDNReceivedID = sDNReceivedID;
	}

	public String getsClientUserName() {
		return sClientUserName;
	}

	public void setsClientUserName(String sClientUserName) {
		this.sClientUserName = sClientUserName;
	}

	public String getsClientPassword() {
		return sClientPassword;
	}

	public void setsClientPassword(String sClientPassword) {
		this.sClientPassword = sClientPassword;
	}

	public String getsLocalMOID() {
		return sLocalMOID;
	}

	public void setsLocalMOID(String sLocalMOID) {
		this.sLocalMOID = sLocalMOID;
	}

	public String getsSerialNumber() {
		return sSerialNumber;
	}

	public void setsSerialNumber(String sSerialNumber) {
		this.sSerialNumber = sSerialNumber;
	}

	public String getsReloadPin() {
		return sReloadPin;
	}

	public void setsReloadPin(String sReloadPin) {
		this.sReloadPin = sReloadPin;
	}

	public String getsExpiryDate() {
		return sExpiryDate;
	}

	public void setsExpiryDate(String sExpiryDate) {
		this.sExpiryDate = sExpiryDate;
	}

	public String getsReloadTelco() {
		return sReloadTelco;
	}

	public void setsReloadTelco(String sReloadTelco) {
		this.sReloadTelco = sReloadTelco;
	}

	public String getsAmount() {
		return sAmount;
	}

	public void setsAmount(String sAmount) {
		this.sAmount = sAmount;
	}

	public String getsInstruction() {
		return sInstruction;
	}

	public void setsInstruction(String sInstruction) {
		this.sInstruction = sInstruction;
	}

	public String getsDescription() {
		return sDescription;
	}

	public void setsDescription(String sDescription) {
		this.sDescription = sDescription;
	}

	public boolean isGetReloadPINResult() {
		return getReloadPINResult;
	}

	public void setGetReloadPINResult(boolean getReloadPINResult) {
		this.getReloadPINResult = getReloadPINResult;
	}

}
