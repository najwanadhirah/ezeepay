package com.rt.qpay99.ws.wsHelper;

import java.util.List;

import com.rt.qpay99.SRSApp;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.ws.RTCallWS;

public class ProductInfoHelper {

	public List<ProductInfo> getProductInfo() {
		RTCallWS cs = new RTCallWS();
		cs.GetProductObject(SRSApp.sClientUserName, SRSApp.sClientPassword);
		return null;
	}

}
