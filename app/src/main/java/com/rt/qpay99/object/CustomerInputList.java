package com.rt.qpay99.object;

public class CustomerInputList {
	private String TxID;
	private String ProductID;
	private String ProductName;
	private String ProductPrice;
	private String CustomerAccountNumber;
	private String CustomerMobileNumber;
	private String Status;

	public String getTxID() {
		return TxID;
	}

	public void setTxID(String txID) {
		TxID = txID;
	}

	public String getProductID() {
		return ProductID;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getProductPrice() {
		return ProductPrice;
	}

	public void setProductPrice(String productPrice) {
		ProductPrice = productPrice;
	}

	public String getCustomerAccountNumber() {
		return CustomerAccountNumber;
	}

	public void setCustomerAccountNumber(String customerAccountNumber) {
		CustomerAccountNumber = customerAccountNumber;
	}

	public String getCustomerMobileNumber() {
		return CustomerMobileNumber;
	}

	public void setCustomerMobileNumber(String customerMobileNumber) {
		CustomerMobileNumber = customerMobileNumber;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

}
