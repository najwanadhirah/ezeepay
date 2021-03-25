package com.rt.qpay99.object;

public class AgentSalesByMSISDN {

	private String Product;
	private String RetailPrice;
	private String Cost;
	private String ToTalCostValue;
	private String TotalFaceValue;
	private int Count;

	public String getRetailPrice() {
		return RetailPrice;
	}

	public void setRetailPrice(String retailPrice) {
		RetailPrice = retailPrice;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}

	public String getProduct() {
		return Product;
	}

	public void setProduct(String product) {
		Product = product;
	}

	public String getCost() {
		return Cost;
	}

	public void setCost(String cost) {
		Cost = cost;
	}

	public String getToTalCostValue() {
		return ToTalCostValue;
	}

	public void setToTalCostValue(String toTalCostValue) {
		ToTalCostValue = toTalCostValue;
	}

	public String getTotalFaceValue() {
		return TotalFaceValue;
	}

	public void setTotalFaceValue(String totalFaceValue) {
		TotalFaceValue = totalFaceValue;
	}

}
