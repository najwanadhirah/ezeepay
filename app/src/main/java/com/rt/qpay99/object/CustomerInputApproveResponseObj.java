package com.rt.qpay99.object;

public class CustomerInputApproveResponseObj {

	private boolean CustomerInputApproveResult;
	private String sTxID;
	private String sResponseID;
	private String sResponseStatus;

	public boolean isCustomerInputApproveResult() {
		return CustomerInputApproveResult;
	}

	public void setCustomerInputApproveResult(boolean customerInputApproveResult) {
		CustomerInputApproveResult = customerInputApproveResult;
	}

	public String getsTxID() {
		return sTxID;
	}

	public void setsTxID(String sTxID) {
		this.sTxID = sTxID;
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
