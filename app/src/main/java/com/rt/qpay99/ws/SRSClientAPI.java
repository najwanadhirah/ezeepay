package com.rt.qpay99.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rt.qpay99.Config;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;

public class SRSClientAPI extends BasicWS {
	public SRSClientAPI(String connectString, String nameSpace) {
		super(connectString, nameSpace);
	}

	public List<ProductInfo> GetProductObject() {

		ArrayList<ProductInfo> result = null;

		RTSoap soap = new RTSoap();
		soap.addProperty("sClientUserName", "60129336318");
		soap.addProperty("sClientPassword", "696088888");
		soap.addProperty("lProductInfo", "");
		try {
			RTSoap response = this.callWS("GetProductObject", soap);
			DLog.e(Config.DEBUG_TAG, "" + response.toString());
			// result = convert2ArtistInfoList(response);
		} catch (XmlParserException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TimeoutException e1) {
			e1.printStackTrace();
		}

		return result;
	}

}
