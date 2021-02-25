package com.rt.qpay99.object;

public class CheckBalanceResponse {

	private boolean CheckBalanceResult;
	private String dBalance;
	private int sResponseID;
	private String sResponseStatus;

	public boolean isCheckBalanceResult() {
		return CheckBalanceResult;
	}

	public void setCheckBalanceResult(boolean checkBalanceResult) {
		CheckBalanceResult = checkBalanceResult;
	}

	public String getdBalance() {
		return dBalance;
	}

	public void setdBalance(String dBalance) {
		this.dBalance = dBalance;
	}

	public int getsResponseID() {
		return sResponseID;
	}

	public void setsResponseID(int sResponseID) {
		this.sResponseID = sResponseID;
	}

	public String getsResponseStatus() {
		return sResponseStatus;
	}

	public void setsResponseStatus(String sResponseStatus) {
		this.sResponseStatus = sResponseStatus;
	}

}
