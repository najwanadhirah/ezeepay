package com.rt.qpay99.ws;

import java.io.IOException;
import java.util.Collection;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import com.rt.qpay99.Config;
import com.rt.qpay99.util.DLog;

import android.util.Log;

/**
 * Result callback; Check network availability; timeout;
 * 
 * @author victor.kwok
 */
public class WebService {

	private String endPoint;
	private String nameSpace;
	// private boolean debug = WSHelper.debug;
	private String requestDump;
	private String responseDump;
	private int timeout = 0;
	public static boolean showTrace = false;

	// private class MyTimerTask extends TimerTask {
	//
	// private Transport ht;
	// private boolean timeouted = false;
	//
	// public MyTimerTask(Transport ht) {
	// this.ht = ht;
	// }
	//
	// public boolean isTimeouted() {
	// return timeouted;
	// }
	//
	// @Override
	// public void run() {
	// ht.reset();
	// timeouted = true;
	// }
	// }

	public WebService(String endPoint, String nameSpace) {
		this.endPoint = endPoint;
		this.nameSpace = nameSpace;
	}

	public WebService(String endPoint, String nameSpace, int timeout) {
		this.endPoint = endPoint;
		this.nameSpace = nameSpace;
		this.timeout = timeout;
	}

	public RTSoap call(String operation, RTSoap soap)
			throws XmlParserException, IOException, TimeoutException {
		return this.call(null, operation, soap);
	}

	public RTSoap call(String operationNS, String operation, RTSoap soap)
			throws XmlParserException, IOException, TimeoutException {
		RTSoap result = null;
		final CustomAndroidHttpTransport ht = this.timeout <= 0 ? new CustomAndroidHttpTransport(
				this.endPoint) : new CustomAndroidHttpTransport(this.endPoint,
				this.timeout);
		ht.debug = showTrace;
		ht.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");

		SoapObject rpc = new SoapObject(this.nameSpace, operation);
		for (RTSoapProperty element : soap.getProperties()) {
			convert2SoapObject(element, rpc);
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.implicitTypes = true;
		envelope.bodyOut = rpc;

		DLog.e(Config.DEBUG_TAG, "rpc=" + rpc);

		try {

			DLog.e(Config.DEBUG_TAG, "@call:" + operation);
			ht.call(operationNS != null ? operationNS + operation : operation,
					envelope); // invoke method
			DLog.e(Config.DEBUG_TAG, "@done." + operation);

		} catch (XmlPullParserException ex) {
			throw new XmlParserException(ex.getMessage(), ex);
		} finally {
			if (showTrace) {
				this.requestDump = ht.requestDump;
				this.responseDump = ht.responseDump;

				DLog.e(Config.DEBUG_TAG, "request:" + requestDump);
				DLog.e(Config.DEBUG_TAG, "response:" + responseDump);

			}
		}

		Object tmpResponse = envelope.bodyIn;
		if (tmpResponse instanceof SoapObject) {
			SoapObject resultRpc = (SoapObject) tmpResponse;

			// if (resultRpc != null && resultRpc.getPropertyCount() > 0) {
			if (resultRpc != null) {
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2GDSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}
			return result;
		} else if (tmpResponse instanceof SoapFault) {
			SoapFault resultRpc = (SoapFault) tmpResponse;
			System.out.println("Exception=" + resultRpc);
			try {
				throw new RuntimeException(resultRpc);
			} catch (Exception e) {
				return null;
			}
		} else {
			System.out.println("Unknown return object=" + tmpResponse);
			return null;
		}
	}

	public RTSoap call(String operationNS, String operation, RTSoap soap,
			String token) throws XmlParserException, IOException,
			TimeoutException {
		RTSoap result = null;
		final CustomAndroidHttpTransport ht = this.timeout <= 0 ? new CustomAndroidHttpTransport(
				this.endPoint) : new CustomAndroidHttpTransport(this.endPoint,
				this.timeout);
		ht.debug = showTrace;
		ht.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");

		SoapObject rpc = new SoapObject(this.nameSpace, operation);
		for (RTSoapProperty element : soap.getProperties()) {
			convert2SoapObject(element, rpc);
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.implicitTypes = true;
		envelope.bodyOut = rpc;
		// System.out.println("rpc=" + rpc);

		// MyTimerTask task = new MyTimerTask(ht);
		try {
			// new Timer().schedule(task, this.timeout);
			if (showTrace)
				Log.e("Testing", "@call:" + operation);
			// ht.call(operationNS != null ? operationNS + operation :
			// operation,
			// envelope); // invoke method
			ht.call(operationNS != null ? operationNS + operation : operation,
					envelope, token);
			if (showTrace)
				Log.e("Testing", "@done." + operation);
			// task.cancel(); // cancel the timeout
			// if (task.isTimeouted()) {
			// throw new TimeoutException();
			// }
		} catch (XmlPullParserException ex) {
			throw new XmlParserException(ex.getMessage(), ex);
		} finally {
			if (showTrace) {
				this.requestDump = ht.requestDump;
				this.responseDump = ht.responseDump;

				Log.e("WebService", "request:" + requestDump);
				Log.e("WebService", "response:" + responseDump);

			}
		}

		Object tmpResponse = envelope.bodyIn;
		if (tmpResponse instanceof SoapObject) {
			SoapObject resultRpc = (SoapObject) tmpResponse;

			// if (resultRpc != null && resultRpc.getPropertyCount() > 0) {
			if (resultRpc != null) {
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2GDSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}
			return result;
		} else if (tmpResponse instanceof SoapFault) {
			SoapFault resultRpc = (SoapFault) tmpResponse;
			System.out.println("Exception=" + resultRpc);
			throw new RuntimeException(resultRpc);
		} else {
			System.out.println("Unknown return object=" + tmpResponse);
			return null;
		}
	}

	private void convert2GDSoap(Object obj, PropertyInfo pi,
			RTSoapProperty element) {
		if (obj instanceof SoapObject) {
			// System.out.println("SoapObject");
			SoapObject soap = (SoapObject) obj;
			element.setName(pi.getName());

			for (int i = 0; i < soap.getPropertyCount(); i++) {
				PropertyInfo piSub = new PropertyInfo();
				soap.getPropertyInfo(i, piSub);

				RTSoapProperty subElement = new RTSoapProperty();

				this.convert2GDSoap(soap.getProperty(i), piSub, subElement);
				element.addProperty(subElement);
			}
		} else if (obj instanceof SoapPrimitive) {
			// System.out.println("SoapPrimitive");
			SoapPrimitive soap = (SoapPrimitive) obj;
			// System.out.println(pi.getName() + ":" + soap.toString());
			element.setName(pi.getName());
			element.setValue(soap.toString());
			// if (prop.getAttributeCount() > 0) {
			// for (int j = 0; j < prop.getAttributeCount(); j++) {
			// SoapObject so = (SoapObject) prop.getAttribute(j);
			// element.addAttribute(so.getName(),
			// so.getProperties(j).toString());
			// }
			// }
		} else {
			element.setName(pi.getName());
			element.setValue(obj.toString());
		}
	}

	private PropertyInfo createPropertyInfo(RTSoapProperty element) {
		PropertyInfo pi = new PropertyInfo();
		pi.setName(element.getName());
		pi.setNamespace(element.getNameSpace());
		pi.setValue(element.getValue());
		return pi;
	}

	private void convert2SoapObject(RTSoapProperty element, SoapObject rpc) {
		Collection<RTSoapProperty> subElements = element.getProperties();
		if (subElements != null && !subElements.isEmpty()) {
			SoapObject subRpc = new SoapObject(rpc.getNamespace(),
					element.getName());
			for (RTSoapProperty subElement : subElements) {
				this.convert2SoapObject(subElement, subRpc);
			}
			rpc.addProperty(element.getName(), subRpc);
		} else {
			SoapObject result = rpc.addProperty(this
					.createPropertyInfo(element));
			// SoapObject result = rpc.addProperty(element.getName(),
			// element.getValue());
			Collection<RTSoapAttribute> attributes = element.getAttributes();

			if (attributes != null && !attributes.isEmpty()) {
				for (RTSoapAttribute attribute : attributes) {
					AttributeInfo ai = new AttributeInfo();
					ai.setName(attribute.getName());
					ai.setName(attribute.getName());
					ai.setValue(attribute.getValue());
					result.addAttribute(ai);
				}
			}
		}
		System.out.println("rpc=" + rpc);
		DLog.e("TAG", "rpc=" + rpc);

	}

	public String getRequestDump() {
		return requestDump;
	}

	public String getResponseDump() {
		return responseDump;
	}

	/**
	 * Get Timeout in millisecond
	 */
	public long getTimeout() {
		return this.timeout;
	}

	/**
	 * Set Timeout in millisecond
	 * 
	 * @param timeout
	 *            Timeout in millisecond
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
