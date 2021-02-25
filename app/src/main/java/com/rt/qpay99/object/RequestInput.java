package com.rt.qpay99.object;

public class RequestInput {

	private String sClientUserName;
	private String sClientPassword;
	private String sClientTxID;
	private String sProductID;
	private String sProductName;
	private String dProductPrice;
	private String sCustomerAccountNumber;
	private String sCustomerMobileNumber;
	private String sDealerMobileNumber;
	private String sRemark;
	private String sOtherParameter;
	private String sResponseID;
	private String sResponseStatus;

	private String sTS;
	private String sEnckey;

	public String getsProductName() {
		return sProductName;
	}

	public void setsProductName(String sProductName) {
		this.sProductName = sProductName;
	}

	public String getsTS() {
		return sTS;
	}

	public void setsTS(String sTS) {
		this.sTS = sTS;
	}

	public String getsEnckey() {
		return sEnckey;
	}

	public void setsEnckey(String sEnckey) {
		this.sEnckey = sEnckey;
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

	public String getsClientTxID() {
		return sClientTxID;
	}

	public void setsClientTxID(String sClientTxID) {
		this.sClientTxID = sClientTxID;
	}

	public String getsProductID() {
		return sProductID;
	}

	public void setsProductID(String sProductID) {
		this.sProductID = sProductID;
	}

	public String getdProductPrice() {
		return dProductPrice;
	}

	public void setdProductPrice(String dProductPrice) {
		this.dProductPrice = dProductPrice;
	}

	public String getsCustomerAccountNumber() {
		return sCustomerAccountNumber;
	}

	public void setsCustomerAccountNumber(String sCustomerAccountNumber) {
		this.sCustomerAccountNumber = sCustomerAccountNumber;
	}

	public String getsCustomerMobileNumber() {
		return sCustomerMobileNumber;
	}

	public void setsCustomerMobileNumber(String sCustomerMobileNumber) {
		this.sCustomerMobileNumber = sCustomerMobileNumber;
	}

	public String getsDealerMobileNumber() {
		return sDealerMobileNumber;
	}

	public void setsDealerMobileNumber(String sDealerMobileNumber) {
		this.sDealerMobileNumber = sDealerMobileNumber;
	}

	public String getsRemark() {
		return sRemark;
	}

	public void setsRemark(String sRemark) {
		this.sRemark = sRemark;
	}

	public String getsOtherParameter() {
		return sOtherParameter;
	}

	public void setsOtherParameter(String sOtherParameter) {
		this.sOtherParameter = sOtherParameter;
	}

	public String getsResponseID() {
		return sResponseID;
	}

	public void setsResponseID(String sResponseID) {
		this.sResponseID = sResponseID;
	}

	public String getsResponseStatus() {
		return sResponseStatus;
	}

	public void setsResponseStatus(String sResponseStatus) {
		this.sResponseStatus = sResponseStatus;
	}

}
