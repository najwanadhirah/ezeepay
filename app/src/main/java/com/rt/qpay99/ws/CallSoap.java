package com.rt.qpay99.ws;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.rt.qpay99.Config;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;

public class CallSoap {
	public final String SOAP_ACTION = "http://tempuri.org/GetProduct";

	public final String OPERATION_NAME = "GetProduct";

	public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org";

	public final String SOAP_ADDRESS = "http://onlinereload.dyndns.org/SRSClientAPI/Connect.asmx";

	private String TAG = this.getClass().getName();

	public CallSoap() {
	}

	public String Call(String sClientUserName, String sClientPassword) {
		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,
				OPERATION_NAME);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("sClientUserName");
		pi.setValue(sClientUserName);
		pi.setType(String.class);
		pi.setNamespace(Config.WS_NAME_SPACE);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName("sClientPassword");
		pi.setValue(sClientPassword);
		pi.setType(String.class);
		pi.setNamespace(Config.WS_NAME_SPACE);
		request.addProperty(pi);

		// pi = new PropertyInfo();
		// pi.setName("lProductInfo");
		// pi.setValue("");
		// pi.setType(String.class);
		// pi.setNamespace(Config.WS_NAME_SPACE);
		// request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);

		HttpTransportSE ht = new HttpTransportSE(SOAP_ADDRESS);
		Object response = null;
		try {
			ht.call(SOAP_ACTION, envelope);

			DLog.e(TAG, "ht.requestDump " + ht.requestDump);
			DLog.e(TAG, "ht.responseDump " + ht.responseDump);
			response = envelope.getResponse();

		} catch (Exception exception) {
			response = exception.toString();
			DLog.e(TAG, exception.toString());

		}
		DLog.e(TAG, response.toString());
		return response.toString();
	}
}
