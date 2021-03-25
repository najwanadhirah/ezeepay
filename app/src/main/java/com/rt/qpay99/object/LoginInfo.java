package com.rt.qpay99.object;

public class LoginInfo {
	public String sClientUserName;
	public String sClientPassword;
	public String sDeviceID;
	public int sClientID;
	public String bVerifyTac;

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

	public String getsDeviceID() {
		return sDeviceID;
	}

	public void setsDeviceID(String sDeviceID) {
		this.sDeviceID = sDeviceID;
	}

	public int getsClientID() {
		return sClientID;
	}

	public void setsClientID(int sClientID) {
		this.sClientID = sClientID;
	}

	public String getbVerifyTac() {
		return bVerifyTac;
	}

	public void setbVerifyTac(String bVerifyTac) {
		this.bVerifyTac = bVerifyTac;
	}

}
