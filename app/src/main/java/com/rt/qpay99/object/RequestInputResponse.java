package com.rt.qpay99.object;

public class RequestInputResponse {

	private boolean RequestInputResult;
	private String sResponseID;
	private String sResponseStatus;

	public boolean isRequestInputResult() {
		return RequestInputResult;
	}

	public void setRequestInputResult(boolean requestInputResult) {
		RequestInputResult = requestInputResult;
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
