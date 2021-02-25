package com.rt.qpay99.object;

public class CustomerTxStatusInfo {

	private String Product;
	private String Amount;
	private String Status;
	private String DN;
	private String Code;
	private String DateTime;
	private String LocalMOID;
	private String sReloadMSISDN;
	private String RetailPrice;
	private int Retry;

	public int getRetry() {
		return Retry;
	}

	public void setRetry(int retry) {
		Retry = retry;
	}

	public String getRetailPrice() {
		return RetailPrice;
	}

	public void setRetailPrice(String retailPrice) {
		RetailPrice = retailPrice;
	}

	public String getsReloadMSISDN() {
		return sReloadMSISDN;
	}

	public void setsReloadMSISDN(String sReloadMSISDN) {
		this.sReloadMSISDN = sReloadMSISDN;
	}

	public String getLocalMOID() {
		return LocalMOID;
	}

	public void setLocalMOID(String localMOID) {
		LocalMOID = localMOID;
	}

	public String getProduct() {
		return Product;
	}

	public void setProduct(String product) {
		Product = product;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getDN() {
		return DN;
	}

	public void setDN(String dN) {
		DN = dN;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}

}
