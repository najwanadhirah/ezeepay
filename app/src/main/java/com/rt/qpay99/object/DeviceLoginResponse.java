package com.rt.qpay99.object;

public class DeviceLoginResponse {

	private boolean DeviceLoginResult;
	private int sClientID;
	private boolean bVerifyTac;
	private String sMasterID;
	private String sMerchantName;

	public String getsMerchantName() {
		return sMerchantName;
	}

	public void setsMerchantName(String sMerchantName) {
		this.sMerchantName = sMerchantName;
	}

	public String getsMasterID() {
		return sMasterID;
	}

	public void setsMasterID(String sMasterID) {
		this.sMasterID = sMasterID;
	}

	public boolean isDeviceLoginResult() {
		return DeviceLoginResult;
	}

	public void setDeviceLoginResult(boolean deviceLoginResult) {
		DeviceLoginResult = deviceLoginResult;
	}

	public int getsClientID() {
		return sClientID;
	}

	public void setsClientID(int sClientID) {
		this.sClientID = sClientID;
	}

	public boolean isbVerifyTac() {
		return bVerifyTac;
	}

	public void setbVerifyTac(boolean bVerifyTac) {
		this.bVerifyTac = bVerifyTac;
	}

}
