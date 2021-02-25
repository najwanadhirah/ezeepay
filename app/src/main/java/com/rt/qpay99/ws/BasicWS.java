package com.rt.qpay99.ws;

import java.io.IOException;

import android.util.Log;

import com.rt.qpay99.Config;

public class BasicWS {

	private String endPoint;
	private String nameSpace;
	protected String nameSpace2;

	public BasicWS(String endPoint, String nameSpace) {
		this.endPoint = endPoint;
		this.nameSpace = nameSpace;
	}

	public BasicWS(String endPoint, String nameSpace, String nameSpace2) {
		this.endPoint = endPoint;
		this.nameSpace = nameSpace;
		this.nameSpace2 = nameSpace2;
	}

	protected RTSoap callWS(String operation, RTSoap soap)
			throws XmlParserException, IOException, TimeoutException {
		return this.callWS(null, operation, soap);
	}

	protected RTSoap callWS(String operation, RTSoap soap, int timeout)
			throws XmlParserException, IOException, TimeoutException {
		return timeout > 0 ? this.callWS(null, operation, soap, timeout) : this
				.callWS(null, operation, soap);
	}

	protected RTSoap callWS(String operationNS, String operation, RTSoap soap,
			int timeout) throws XmlParserException, IOException,
			TimeoutException {
		WebService ws = new WebService(endPoint, nameSpace, timeout);
		WebService.showTrace = Config.isDebug;
		if (Config.isDebug) {
			Log.d(this.getClass().getSimpleName(), "GDSoap" + soap);
			Log.d(this.getClass().getSimpleName(), "Calling " + this.endPoint
					+ " operation: " + operation);
		}
		try {
			RTSoap response = ws.call(operationNS, operation, soap);
			return response;
		} finally {
			if (Config.isDebug) {
				Log.d(this.getClass().getSimpleName(),
						ws.getRequestDump() != null ? ws.getRequestDump()
								: "NULL");
				Log.d(this.getClass().getSimpleName(),
						ws.getResponseDump() != null ? ws.getResponseDump()
								: "NULL");
			}
		}
	}

	protected RTSoap callWS(String operationNS, String operation, RTSoap soap)
			throws XmlParserException, IOException, TimeoutException {
		WebService ws = new WebService(endPoint, nameSpace);
		WebService.showTrace = Config.isDebug;
		if (Config.isDebug) {
			Log.d(this.getClass().getSimpleName(), "GDSoap" + soap);
			Log.d(this.getClass().getSimpleName(), "Calling " + this.endPoint
					+ " operation: " + operation);
		}
		try {
			RTSoap response = ws.call(operationNS, operation, soap);
			return response;
		} finally {
			if (Config.isDebug) {
				Log.d(this.getClass().getSimpleName(),
						ws.getRequestDump() != null ? ws.getRequestDump()
								: "NULL");
				Log.d(this.getClass().getSimpleName(),
						ws.getResponseDump() != null ? ws.getResponseDump()
								: "NULL");
			}
		}
	}

	protected RTSoap callWSTK(String operation, RTSoap soap, String token)
			throws XmlParserException, IOException, TimeoutException {
		return this.callWSTK(null, operation, soap, token);
	}

	protected RTSoap callWSTK(String operation, RTSoap soap, int timeout,
			String token) throws XmlParserException, IOException,
			TimeoutException {
		return timeout > 0 ? this.callWSTK(null, operation, soap, timeout,
				token) : this.callWSTK(null, operation, soap, token);
	}

	protected RTSoap callWSTK(String operationNS, String operation,
			RTSoap soap, int timeout, String token) throws XmlParserException,
			IOException, TimeoutException {
		WebService ws = new WebService(endPoint, nameSpace, timeout);
		WebService.showTrace = Config.isDebug;
		if (Config.isDebug) {
			Log.d(this.getClass().getSimpleName(), "GDSoap" + soap);
			Log.d(this.getClass().getSimpleName(), "Calling " + this.endPoint
					+ " operation: " + operation);
		}
		try {
			RTSoap response = ws.call(operationNS, operation, soap, token);
			return response;
		} finally {
			if (Config.isDebug) {
				Log.d(this.getClass().getSimpleName(),
						ws.getRequestDump() != null ? ws.getRequestDump()
								: "NULL");
				Log.d(this.getClass().getSimpleName(),
						ws.getResponseDump() != null ? ws.getResponseDump()
								: "NULL");
			}
		}
	}

	protected RTSoap callWSTK(String operationNS, String operation,
			RTSoap soap, String token) throws XmlParserException, IOException,
			TimeoutException {
		WebService ws = new WebService(endPoint, nameSpace);
		WebService.showTrace = Config.isDebug;
		if (Config.isDebug) {
			Log.d(this.getClass().getSimpleName(), "GDSoap" + soap);
			Log.d(this.getClass().getSimpleName(), "Calling " + this.endPoint
					+ " operation: " + operation);
		}
		try {
			RTSoap response = ws.call(operationNS, operation, soap, token);
			return response;
		} finally {
			if (Config.isDebug) {
				Log.d(this.getClass().getSimpleName(),
						ws.getRequestDump() != null ? ws.getRequestDump()
								: "NULL");
				Log.d(this.getClass().getSimpleName(),
						ws.getResponseDump() != null ? ws.getResponseDump()
								: "NULL");
			}
		}
	}

}