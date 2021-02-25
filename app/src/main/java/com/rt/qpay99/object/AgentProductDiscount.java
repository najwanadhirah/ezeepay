package com.rt.qpay99.object;

public class AgentProductDiscount {

	private int productId;
	private String ProductName;
	private String DiscountType;
	private String DiscountRate;
	private String LastUpdated;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getDiscountType() {
		return DiscountType;
	}

	public void setDiscountType(String discountType) {
		DiscountType = discountType;
	}

	public String getDiscountRate() {
		return DiscountRate;
	}

	public void setDiscountRate(String discountRate) {
		DiscountRate = discountRate;
	}

	public String getLastUpdated() {
		return LastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		LastUpdated = lastUpdated;
	}

}
