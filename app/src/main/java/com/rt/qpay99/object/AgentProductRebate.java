package com.rt.qpay99.object;

public class AgentProductRebate {
	private String ProductID;
	private String ProductName;
	private String Denomination;
	private String RebateType;
	private String RebateRate;
	private String LastUpdated;

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

	public String getDenomination() {
		return Denomination;
	}

	public void setDenomination(String denomination) {
		Denomination = denomination;
	}

	public String getRebateType() {
		return RebateType;
	}

	public void setRebateType(String rebateType) {
		RebateType = rebateType;
	}

	public String getRebateRate() {
		return RebateRate;
	}

	public void setRebateRate(String rebateRate) {
		RebateRate = rebateRate;
	}

	public String getLastUpdated() {
		return LastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		LastUpdated = lastUpdated;
	}

}
