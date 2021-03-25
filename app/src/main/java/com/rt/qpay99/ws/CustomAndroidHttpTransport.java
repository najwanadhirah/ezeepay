package com.rt.qpay99.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.transport.AndroidServiceConnection;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.Transport;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.rt.qpay99.Config;

public class CustomAndroidHttpTransport extends Transport {
	private String userAgent = "GD kSOAP/2.0";

	/**
	 * Creates instance of HttpTransport with set url
	 * 
	 * @param url
	 *            the destination to POST SOAP data
	 */
	public CustomAndroidHttpTransport(String url) {
		super(url);
	}

	public CustomAndroidHttpTransport(String url, int timeout) {
		super(null, url, timeout);
		this.timeout = timeout;
	}

	/**
	 * set the desired soapAction header field
	 * 
	 * @param soapAction
	 *            the desired soapAction
	 * @param envelope
	 *            the envelope containing the information for the soap call.
	 */
	public void call(String soapAction, SoapEnvelope envelope)
			throws IOException, XmlPullParserException {
		if (soapAction == null)
			soapAction = "\"\"";
		byte[] requestData = createRequestData(envelope);
		requestDump = debug ? new String(requestData, Config.UTF8) : null;
		responseDump = null;
		ServiceConnection connection = getServiceConnection();
		// connection.connect();
		try {
			connection.setRequestProperty("User-Agent", userAgent);
			connection.setRequestProperty("SOAPAction", soapAction);
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestProperty("Connection", "close");
			connection.setRequestProperty("Content-Length", ""
					+ requestData.length);
			connection.setRequestMethod("POST");
			// connection.setRequestProperty("access_token", "tpken");
			OutputStream os = connection.openOutputStream();
			os.write(requestData, 0, requestData.length);
			os.flush();
			os.close();
			requestData = null;

			InputStream is;
			try {
				connection.connect();
				is = connection.openInputStream();
			} catch (IOException e) {
				is = connection.getErrorStream();
				if (is == null) {
					connection.disconnect();
					throw (e);
				}
			}
			if (debug) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buf = new byte[256];
				while (true) {
					int rd = is.read(buf, 0, 256);
					if (rd == -1)
						break;
					bos.write(buf, 0, rd);
				}
				bos.flush();
				buf = bos.toByteArray();
				responseDump = new String(buf, Config.UTF8);
				is.close();
				is = new ByteArrayInputStream(buf);
				if (debug) {
					System.out.println("DBG:request:" + requestDump);
					System.out.println("DBG:response:" + responseDump);
				}
			}
			parseResponse(envelope, is);
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e(this.getClass().getSimpleName(), ex.toString());
		} finally {
			connection.disconnect();
		}
	}

	public void call(String soapAction, SoapEnvelope envelope, String token)
			throws IOException, XmlPullParserException {
		if (soapAction == null)
			soapAction = "\"\"";
		byte[] requestData = createRequestData(envelope);
		requestDump = debug ? new String(requestData, Config.UTF8) : null;
		responseDump = null;

		ServiceConnection connection = getServiceConnection();
		// connection.connect();
		try {

			connection.setRequestProperty("User-Agent", userAgent);
			connection.setRequestProperty("SOAPAction", soapAction);
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestProperty("Connection", "close");
			connection.setRequestProperty("Content-Length", ""
					+ requestData.length);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("access_token", token);
			OutputStream os = connection.openOutputStream();
			os.write(requestData, 0, requestData.length);
			os.flush();
			os.close();
			requestData = null;

			InputStream is;
			try {
				connection.connect();
				is = connection.openInputStream();
			} catch (IOException e) {
				is = connection.getErrorStream();
				if (is == null) {
					connection.disconnect();
					throw (e);
				}
			}
			if (debug) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buf = new byte[256];
				while (true) {
					int rd = is.read(buf, 0, 256);
					if (rd == -1)
						break;
					bos.write(buf, 0, rd);
				}
				bos.flush();
				buf = bos.toByteArray();
				responseDump = new String(buf, Config.UTF8);
				is.close();
				is = new ByteArrayInputStream(buf);
				if (debug) {
					System.out.println("DBG:request:" + requestDump);
					System.out.println("DBG:response:" + responseDump);
				}
			}
			parseResponse(envelope, is);
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e(this.getClass().getSimpleName(), ex.toString());
		} finally {
			connection.disconnect();
		}
	}

	protected ServiceConnection getServiceConnection() throws IOException {
		return new AndroidServiceConnection(url);
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	public List call(String arg0, SoapEnvelope arg1, List arg2)
			throws IOException, XmlPullParserException {
		return null;
	}

	@Override
	public String getHost() {
		return null;
	}

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public int getPort() {
		return 0;
	}
}