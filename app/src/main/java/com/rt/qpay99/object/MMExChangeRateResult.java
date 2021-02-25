package com.rt.qpay99.object;

import java.sql.Date;

public class MMExChangeRateResult {
	private int mID;
	private String LocalCurrency;
	private String LocalAmount;
	private String ForeignCountry;
	private String ForeignCurrency;
	private String ForeignAmount;
	private String Status;
	private String LastUpdated;
	private Date Created;

	public int getmID() {
		return mID;
	}

	public void setmID(int mID) {
		this.mID = mID;
	}

	public String getLocalCurrency() {
		return LocalCurrency;
	}

	public void setLocalCurrency(String localCurrency) {
		LocalCurrency = localCurrency;
	}

	public String getLocalAmount() {
		return LocalAmount;
	}

	public void setLocalAmount(String localAmount) {
		LocalAmount = localAmount;
	}

	public String getForeignCountry() {
		return ForeignCountry;
	}

	public void setForeignCountry(String foreignCountry) {
		ForeignCountry = foreignCountry;
	}

	public String getForeignCurrency() {
		return ForeignCurrency;
	}

	public void setForeignCurrency(String foreignCurrency) {
		ForeignCurrency = foreignCurrency;
	}

	public String getForeignAmount() {
		return ForeignAmount;
	}

	public void setForeignAmount(String foreignAmount) {
		ForeignAmount = foreignAmount;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getLastUpdated() {
		return LastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		LastUpdated = lastUpdated;
	}

	public Date getCreated() {
		return Created;
	}

	public void setCreated(Date created) {
		Created = created;
	}

}
