package com.rt.qpay99.object;

public class BasicSales {

	private String sClientUserName;
	private String sClientPassword;
	private String sSDate;
	private String sEDate;
	private String sTimeStart;
	private String sSelectedMSISDN;
	private String sTimeEnd;

	public String getsSelectedMSISDN() {
		return sSelectedMSISDN;
	}

	public void setsSelectedMSISDN(String sSelectedMSISDN) {
		this.sSelectedMSISDN = sSelectedMSISDN;
	}

	public String getsTimeStart() {
		return sTimeStart;
	}

	public void setsTimeStart(String sTimeStart) {
		this.sTimeStart = sTimeStart;
	}

	public String getsTimeEnd() {
		return sTimeEnd;
	}

	public void setsTimeEnd(String sTimeEnd) {
		this.sTimeEnd = sTimeEnd;
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

	public String getsSDate() {
		return sSDate;
	}

	public void setsSDate(String sSDate) {
		this.sSDate = sSDate;
	}

	public String getsEDate() {
		return sEDate;
	}

	public void setsEDate(String sEDate) {
		this.sEDate = sEDate;
	}

}
