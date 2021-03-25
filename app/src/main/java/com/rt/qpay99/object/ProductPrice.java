package com.rt.qpay99.object;

public class ProductPrice {

	private String priceTag;
	private String priceValue;
	private boolean allowDisplay = true;

	public ProductPrice(String priceTag, String priceValue) {
		this.priceTag = priceTag;
		this.priceValue = priceValue;

	}

	public String getPriceTag() {
		return priceTag;
	}

	public void setPriceTag(String priceTag) {
		this.priceTag = priceTag;
	}

	public String getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(String priceValue) {
		this.priceValue = priceValue;
	}

	public boolean isAllowDisplay() {
		return allowDisplay;
	}

	public void setAllowDisplay(boolean allowDisplay) {
		this.allowDisplay = allowDisplay;
	}

}
