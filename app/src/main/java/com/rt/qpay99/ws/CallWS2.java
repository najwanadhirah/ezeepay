package com.rt.qpay99.ws;

import com.rt.qpay99.Config;
import com.rt.qpay99.util.DLog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CallWS2 {

	public boolean connectWebService(String productModel, String editionNum) {
		String namespace = Config.WS_NAME_SPACE;
		String url = Config.WS_URL; //   Ӧ  url

		String methodName = "GetProductObject";
		SoapObject soapObject = new SoapObject(namespace, methodName);

		soapObject.addProperty("sClientUserName", "60129336318"); //
		soapObject.addProperty("sClientPassword", "696088888"); //
		soapObject.addProperty("lProductInfo", "");

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(url);
		try {
			httpTranstation.call(namespace + "GetProductObject", envelope);

			Object result = envelope.getResponse();
			DLog.i("test", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("test", "fasle:" + e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
