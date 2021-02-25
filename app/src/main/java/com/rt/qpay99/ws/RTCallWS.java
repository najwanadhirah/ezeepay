package com.rt.qpay99.ws;

import com.rt.qpay99.Config;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.object.LoginInfo;
import com.rt.qpay99.object.RequestInput;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.net.URLEncoder;

public class RTCallWS {

	private String TAG = "RTCallWS";

	private String WS_NAMESPACE = Config.WS_NAME_SPACE;
	private String WS_URL = Config.WS_URL;
	private String METHOD_GetProductObject = "GetProductObject";
	private String METHOD_RequestInput = "RequestInput";
	private String METHOD_DeviceLogin = "DeviceLogin";
	private String METHOD_CheckBalance = "CheckBalance";
	private String METHOD_CheckCustomerStatus = "CheckCustomerStatus";
	private String METHOD_SubmitBankIn = "SubmitBankIn";

	private String METHOD_SubmitBankInBiCode = "SubmitBankInBiCode";
	private String METHOD_CheckCustomerTxStatus = "CheckCustomerTxStatus";
	private String METHOD_GetAgentProductDiscount = "GetAgentProductDiscount";
	private String METHOD_GetAgentProductRebate = "GetAgentProductRebate";
	private String METHOD_GetAgentSales = "GetAgentSales";
	private String METHOD_GetAgentSalesProfit = "GetAgentSalesProfit";
	private String METHOD_CustomerInput = "CustomerInput";
	private String METHOD_CustomerInputList = "CustomerInputList";
	private String METHOD_CustomerInputApprove = "CustomerInputApprove";
	private String METHOD_RequestPIN = "RequestPIN";
	private String METHOD_GetReloadPIN = "GetReloadPIN";
	private String METHOD_GetReloadPINImmediate = "GetReloadPINImmediate";
	private String METHOD_LogLogin = "LogLogin";
	private String METHOD_ChangePassword = "ChangePassword";
	private String METHOD_UpdatePushNotificationID = "UpdatePushNotificationID";
	private String METHOD_DeviceVerifyTAC = "DeviceVerifyTAC";
	private String METHOD_InsertMobileMoneymessage = "InsertMobileMoneymessage";
	private String METHOD_GetExChangeRateById = "GetExChangeRateById";
	private String METHOD_GetPinTxByDN = "GetPinTxByDN";
	private String METHOD_GetMessageInfo = "GetMessageInfo";
	private String METHOD_RegisterDealer = "RegisterDealer";
	private String METHOD_FBUpdateFBID = "FBUpdateFBID";
	private String METHOD_UpdatePrintCount = "UpdatePrintCount";
	private String METHOD_GetBankInList = "GetBankInList";

	private String METHOD_CheckUserTopupTxByType = "CheckUserTopupTxByType";
	private String METHOD_GetAgentSalesByMSISDN = "GetAgentSalesByMSISDN";

	private String METHOD_ShareCredit = "ShareCredit";

	private String METHOD_QueryAccount = "QueryAccount";
	private String METHOD_RequestBuy = "RequestBuy";


	public RTSoap RequestBuy(RequestInput r) {
		RTSoap result = null;
		String methodName = "RequestBuy";
		SoapObject soapObject = new SoapObject(WS_NAMESPACE, methodName);

		soapObject.addProperty("sClientUserName", r.getsClientUserName());
		soapObject.addProperty("sClientPassword", r.getsClientPassword());

		if (FunctionUtil.isSet(r.getsClientTxID()))
			soapObject.addProperty("sClientTxID", r.getsClientTxID());

		soapObject.addProperty("sProductName", r.getsProductName());

		soapObject.addProperty("dProductPrice", r.getdProductPrice());

		soapObject.addProperty("sTS", r.getsTS());
		soapObject.addProperty("sEncKey", r.getsEnckey());

		if (FunctionUtil.isSet(r.getsCustomerAccountNumber()))
			soapObject.addProperty("sCustomerAccountNumber",
					r.getsCustomerAccountNumber());

		if (FunctionUtil.isSet(r.getsCustomerMobileNumber()))
			soapObject.addProperty("sCustomerMobileNumber",
					r.getsCustomerMobileNumber());

		if (FunctionUtil.isSet(r.getsDealerMobileNumber()))
			soapObject.addProperty("sDealerMobileNumber",
					r.getsDealerMobileNumber());

		if (FunctionUtil.isSet(r.getsRemark()))
			soapObject.addProperty("sRemark", r.getsRemark());

		if (FunctionUtil.isSet(r.getsOtherParameter()))
			soapObject.addProperty("sOtherParameter", r.getsOtherParameter());

		if (FunctionUtil.isSet(r.getsResponseID()))
			soapObject.addProperty("sResponseID", r.getsResponseID());

		if (FunctionUtil.isSet(r.getsResponseStatus()))
			soapObject.addProperty("sResponseStatus", r.getsResponseStatus());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.i(TAG, "bodyIn :" + envelope.bodyIn);
		DLog.i(TAG, "bodyOut :" + envelope.bodyOut);
//		HttpTransportSE httpTranstation = new HttpTransportSE(Config.WS_MAIN_URL);
		CustomAndroidHttpTransport httpTranstation = new CustomAndroidHttpTransport(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_RequestBuy, envelope);
			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);
					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}
			DLog.e(TAG, "true:" + result.toString());
		} catch (Exception e) {
			DLog.e(TAG, "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;
	}

	public RTSoap ShareCredit(String sClientUserName,String sClientPassword, String sCustomerMobile, String sCustomerAmount,String sTS, String sEncKey) {

		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_ShareCredit);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sCustomerMobile", sCustomerMobile);
		soapObject.addProperty("sCustomerAmount", sCustomerAmount);
		soapObject.addProperty("sTS", sTS);
		soapObject.addProperty("sEncKey", sEncKey );


		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation
					.call(WS_NAMESPACE + METHOD_ShareCredit, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);
					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("ShareCredit", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("ShareCredit", "Err :" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap GetAgentSalesByMSISDN(String sClientUserName,String sClientPassword, String sSelectedMSISDN, String sSDate,String sTimeStart, String sTimeEnd) {

		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetAgentSalesByMSISDN);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sSelectedMSISDN", sSelectedMSISDN);
		soapObject.addProperty("sTimeStart", sTimeStart);
		soapObject.addProperty("sTimeEnd", sTimeEnd);
		soapObject.addProperty("sDate", sSDate);


		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation
					.call(WS_NAMESPACE + METHOD_GetAgentSalesByMSISDN, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);
					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("GetAgentSalesByMSISDN", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("GetAgentSalesByMSISDN", "Err :" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}


	public RTSoap QueryAccount(String accountNumber) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_QueryAccount);

		soapObject.addProperty("accountNumber", accountNumber);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(Config.WS_ASTROQUERY_URL);
		try {
			httpTranstation
					.call(WS_NAMESPACE + METHOD_QueryAccount, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);
					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("QueryAccount", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("QueryAccount", "Err :" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}


	public RTSoap CheckUserTopupTxByType(String sClientUserName,
										 String sClientPassword, String sCreditType, String sSDate, String sEDate) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_CheckUserTopupTxByType);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sCreditType", sCreditType);
		soapObject.addProperty("sSDate", sSDate);
		soapObject.addProperty("sEDate", sEDate);


		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation
					.call(WS_NAMESPACE + METHOD_CheckUserTopupTxByType, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);
					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("CheckUserTopupTxByType", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("CheckUserTopupTxByType", "Err :" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}


	public RTSoap GetBankInList(String sClientUserName,
								   String sClientPassword, String sTS, String sEncKey) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetBankInList);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sTS", sTS);
		soapObject.addProperty("sEncKey", sEncKey);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation
					.call(WS_NAMESPACE + METHOD_GetBankInList, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);
					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("GetBankInList", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("GetBankInList", "Err :" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap UpdatePrintCount(String sClientUserName,
	String sLocalMOID, String sTS, String sEncKey) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_UpdatePrintCount);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sLocalMOID", sLocalMOID);
		soapObject.addProperty("sTS", sTS);
		soapObject.addProperty("sEncKey", sEncKey);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation
					.call(WS_NAMESPACE + METHOD_UpdatePrintCount, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);
					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("UpdatePrintCount", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("UpdatePrintCount", "Err :" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}


	public RTSoap FBUpdateFBID(String sFBID,
								 String sAgentID, String sMobileNumber, String sPassword,String sTS,String sEncKey) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_FBUpdateFBID);

		soapObject.addProperty("sFBID", sFBID);
		soapObject.addProperty("sAgentID", sAgentID);
		soapObject.addProperty("sMobileNumber", sMobileNumber);
		soapObject.addProperty("sPassword", sPassword);
		soapObject.addProperty("sTS", sTS);
		soapObject.addProperty("sEncKey", sEncKey);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation
					.call(WS_NAMESPACE + METHOD_FBUpdateFBID, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);
					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("FBUpdateFBID", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("FBUpdateFBID", "false:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}



	public RTSoap RegisterDealer(String sDealerMobile, String sPassword,String sName,
								 String sMobileNumber, String sLocation, String sEmail,String sReferrer,String sTS, String sEncKey) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_RegisterDealer);

		soapObject.addProperty("sDealerMobile", sDealerMobile);
		soapObject.addProperty("sPassword", sPassword);
		soapObject.addProperty("sName", sName);
		soapObject.addProperty("sMobileNumber", sMobileNumber);
		soapObject.addProperty("sLocation", sLocation);
		soapObject.addProperty("sEmail", sEmail);
		soapObject.addProperty("sReferrer", sReferrer);
		soapObject.addProperty("sTS", sTS);
		soapObject.addProperty("sEncKey", sEncKey);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation
					.call(WS_NAMESPACE + METHOD_RegisterDealer, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);
					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("RegisterDealer", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("RegisterDealer", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap GetMessageInfo(String sClientUserName,
			String sClientPassword, String sMessageType) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetMessageInfo);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sMessageType", sMessageType);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation
					.call(WS_NAMESPACE + METHOD_GetMessageInfo, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("GetMessageInfo", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("GetMessageInfo", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap GetPinTxByDN(String sClientUserName, String sClientPassword,
			String sCustomerAccount, String sDNId) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetPinTxByDN);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sCustomerAccount", sCustomerAccount);
		soapObject.addProperty("sDNId", sDNId);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_GetPinTxByDN, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("GetPinTxByDN", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("GetPinTxByDN", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap GetExChangeRateById(String sClientUserName,
			String sClientPassword, int mId) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetExChangeRateById);

		// soapObject.addProperty("sClientUserName", sClientUserName);
		// soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("mId", mId);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_GetExChangeRateById,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("GetExChangeRateById", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("GetExChangeRateById", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap InsertMobileMobilemessage(String sClientUserName,
			String sClientPassword, String MessageStatus, String sMessage,
			String dateTime, String AgentId, String MSISDN) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_InsertMobileMoneymessage);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sMessage", sMessage);
		soapObject.addProperty("MessageStatus", MessageStatus);
		soapObject.addProperty("dateTime", dateTime);
		soapObject.addProperty("AgentId", AgentId);
		soapObject.addProperty("MSISDN", MSISDN);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(
					WS_NAMESPACE + METHOD_InsertMobileMoneymessage, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("InsertMobileMobilemessage", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("InsertMobileMobilemessage", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap UpdatePushNotificationID(String sClientUserName,
			String sClientPassword, String sPNID) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_UpdatePushNotificationID);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sPNID", sPNID);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(
					WS_NAMESPACE + METHOD_UpdatePushNotificationID, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("UpdatePushNotificationID", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("UpdatePushNotificationID", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap ChangePassword(String sClientUserName,
			String sClientPassword, String sNewPassword) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_ChangePassword);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sNewPassword", sNewPassword);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation
					.call(WS_NAMESPACE + METHOD_ChangePassword, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("getLogLogin", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("getLogLogin", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap getLogLogin(String sClientUserName, String sClientPassword,
			String IPAddress, String LoginStatus, String LoginChannel,
			String sGPSLocation) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE, METHOD_LogLogin);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("IPAddress", IPAddress);
		soapObject.addProperty("LoginStatus", LoginStatus);
		soapObject.addProperty("LoginChannel", LoginChannel);
		soapObject.addProperty("sGPSLocation", sGPSLocation);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.e(TAG, "envelope.bodyOut " + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_LogLogin, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("getLogLogin", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("getLogLogin", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap GetReloadPINImmediate(String sClientUserName,
			String sClientPassword, String sLocalMOID) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetReloadPINImmediate);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sLocalMOID", sLocalMOID);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL,40000);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_GetReloadPINImmediate,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("CustomerInputList", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("CustomerInputList", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap getReloadPIN(String sClientUserName, String sClientPassword,
			String sLocalMOID) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetReloadPIN);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sLocalMOID", sLocalMOID);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_GetReloadPIN, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("CustomerInputList", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("CustomerInputList", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap getCustomerInputApprove(String sClientUserName,
			String sClientPassword, String sTxID, String sResponseID) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_CustomerInputApprove);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);

		soapObject.addProperty("sTxID", sTxID);
		soapObject.addProperty("sResponseID", sResponseID);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_CustomerInputApprove,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("CustomerInputList", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("CustomerInputList", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap getCustomerInputList(String sClientUserName,
			String sClientPassword) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_CustomerInputList);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_CustomerInputList,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("CustomerInputList", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("CustomerInputList", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap CustomerInput(RequestInput r) {
		RTSoap result = null;
		String methodName = "CustomerInput";
		SoapObject soapObject = new SoapObject(WS_NAMESPACE, methodName);

		soapObject.addProperty("sClientUserName", r.getsClientUserName());
		soapObject.addProperty("sClientPassword", r.getsClientPassword());

		// if (FunctionUtil.isSet(r.getsClientTxID()))
		// soapObject.addProperty("sClientTxID", r.getsClientTxID());

		soapObject.addProperty("sProductID", r.getsProductID());
		soapObject.addProperty("dProductPrice", r.getdProductPrice());

		if (FunctionUtil.isSet(r.getsCustomerAccountNumber()))
			soapObject.addProperty("sCustomerAccountNumber",
					r.getsCustomerAccountNumber());

		if (FunctionUtil.isSet(r.getsCustomerMobileNumber()))
			soapObject.addProperty("sCustomerMobileNumber",
					r.getsCustomerMobileNumber());

		if (FunctionUtil.isSet(r.getsDealerMobileNumber()))
			soapObject.addProperty("sDealerMobileNumber",
					r.getsDealerMobileNumber());

		if (FunctionUtil.isSet(r.getsRemark()))
			soapObject.addProperty("sRemark", r.getsRemark());

		// if (FunctionUtil.isSet(r.getsOtherParameter()))
		soapObject.addProperty("sOtherParameter", r.getsOtherParameter());
		//
		// if (FunctionUtil.isSet(r.getsResponseID()))
		soapObject.addProperty("sResponseID", r.getsResponseID());
		//
		// if (FunctionUtil.isSet(r.getsResponseStatus()))
		soapObject.addProperty("sResponseStatus", r.getsResponseStatus());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.i(TAG, "bodyIn :" + envelope.bodyIn);
		DLog.i(TAG, "bodyOut :" + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_CustomerInput, envelope);
			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}
			DLog.i(TAG, "true:" + result.toString());
		} catch (Exception e) {
			DLog.i(TAG, "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;
	}

	public RTSoap GetAgentSalesProfit(String sClientUserName,
			String sClientPassword, String sSDate, String sEDate) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetAgentSalesProfit);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sSDate", sSDate);
		soapObject.addProperty("sEDate", sEDate);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_GetAgentSalesProfit,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("GetAgentSalesProfit", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("GetAgentSalesProfit", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap GetAgentSales(String sClientUserName, String sClientPassword,
			String sSDate, String sEDate) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetAgentSales);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sSDate", sSDate);
		soapObject.addProperty("sEDate", sEDate);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.i(TAG, "bodyIn :" + envelope.bodyIn);
		DLog.i(TAG, "bodyOut :" + envelope.bodyOut);
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_GetAgentSales, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("GetAgentSales", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("GetAgentSales", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap GetAgentProductRebate(String sClientUserName,
			String sClientPassword, int sProductID) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetAgentProductRebate);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sProductID", sProductID);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_GetAgentProductRebate,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("GetAgentProductRebate", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("GetAgentProductRebate", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap GetAgentProductDiscount(String sClientUserName,
			String sClientPassword) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_GetAgentProductDiscount);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_GetAgentProductDiscount,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("GetAgentProductDiscount", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("GetAgentProductDiscount", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap CheckCustomerTxStatus(String sClientUserName,
			String sClientPassword, String sCustomerAccount, String sSDate,
			String sEDate) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_CheckCustomerTxStatus);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sCustomerAccount", sCustomerAccount);
		soapObject.addProperty("sSDate", sSDate);
		soapObject.addProperty("sEDate", sEDate);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_CheckCustomerTxStatus,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("CheckCustomerTxStatus", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("CheckCustomerTxStatus", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap SubmitBankIn(String sClientUserName, String sClientPassword,
			String sBank, String sAmount, String sDate, String sTime,
			String sbiCode) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_SubmitBankInBiCode);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sBank", sBank);
		soapObject.addProperty("sAmount", sAmount);
		soapObject.addProperty("sDate", sDate);
		soapObject.addProperty("sTime", sTime);
		soapObject.addProperty("sbiCode", sbiCode);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_SubmitBankInBiCode,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("SubmitBankIn", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("SubmitBankIn", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap CheckCustomerStatus(String sClientUserName,
			String sClientPassword, String sCustomerAccount) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_CheckCustomerStatus);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("sCustomerAccount", sCustomerAccount);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_CheckCustomerStatus,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("CheckCustomerStatus", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("CheckBalance", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public RTSoap CheckBalance(String sClientUserName, String sClientPassword) {
		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_CheckBalance);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL,60000);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_CheckBalance, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("CheckBalance", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("CheckBalance", "fasle:" + e.getMessage());
			e.printStackTrace();

			try {
				DLog.e(TAG, "addLog");
				HttpHandlerHelper sh = new HttpHandlerHelper();
				String errCode = "Err_Code=CheckBalance";
				String Message = "&Message=" + URLEncoder.encode(e.getMessage(), "UTF-8");
				String ServerKey = "&ServerKey=qpay99";
				String MSISDN = "&MSISDN=" + SharedPreferenceUtil.getsClientUserName();
				String Param = "&Param=NO";
				String Remark = "&Remark=NO";
				String url = errCode + ServerKey + MSISDN + Param + Remark + Message;
				DLog.e(TAG, "Response from url: " + Config.ErrorLog_URL + url);
				String jsonStr = sh.makeServiceCall(Config.ErrorLog_URL + url, "POST");
				DLog.e(TAG, "Response from url: " + jsonStr);
			} catch (Exception e2) {
				DLog.e(TAG, "addLog Err " + e2.getMessage());
			}

			return null;
		}
		return result;

	}

	public RTSoap DeviceVerifyTAC(String sClientUserName,
			String sClientPassword, int sClientID, String sDeviceID, String sTAC) {

		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE,
				METHOD_DeviceVerifyTAC);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);

		if (FunctionUtil.isSet(sDeviceID))
			soapObject.addProperty("sDeviceID", sDeviceID);

		soapObject.addProperty("sClientID", sClientID);

		if (FunctionUtil.isSet(sTAC))
			soapObject.addProperty("sTAC", sTAC);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_DeviceVerifyTAC,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}

			DLog.i("DeviceLogin", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("DeviceLogin", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;
	}

	public RTSoap DeviceLogin(LoginInfo login) {

		RTSoap result = null;

		SoapObject soapObject = new SoapObject(WS_NAMESPACE, METHOD_DeviceLogin);

		soapObject.addProperty("sClientUserName", login.getsClientUserName());
		soapObject.addProperty("sClientPassword", login.getsClientPassword());

		if (FunctionUtil.isSet(login.getsDeviceID()))
			soapObject.addProperty("sDeviceID", login.getsDeviceID());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.i(TAG, "bodyIn :" + envelope.bodyIn);
		DLog.i(TAG, "bodyOut :" + envelope.bodyOut);
		CustomAndroidHttpTransport httpTranstation = new CustomAndroidHttpTransport(WS_URL);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_DeviceLogin, envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}

			}

			DLog.i("DeviceLogin", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("DeviceLogin", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;
	}

	public RTSoap RequestInput(RequestInput r) {
		RTSoap result = null;
		String methodName = "RequestInput";
		SoapObject soapObject = new SoapObject(WS_NAMESPACE, methodName);

		soapObject.addProperty("sClientUserName", r.getsClientUserName());
		soapObject.addProperty("sClientPassword", r.getsClientPassword());

		if (FunctionUtil.isSet(r.getsClientTxID()))
			soapObject.addProperty("sClientTxID", r.getsClientTxID());

		soapObject.addProperty("sProductID", r.getsProductID());
		soapObject.addProperty("dProductPrice", r.getdProductPrice());

		if (FunctionUtil.isSet(r.getsCustomerAccountNumber()))
			soapObject.addProperty("sCustomerAccountNumber",
					r.getsCustomerAccountNumber());

		if (FunctionUtil.isSet(r.getsCustomerMobileNumber()))
			soapObject.addProperty("sCustomerMobileNumber",
					r.getsCustomerMobileNumber());

		if (FunctionUtil.isSet(r.getsDealerMobileNumber()))
			soapObject.addProperty("sDealerMobileNumber",
					r.getsDealerMobileNumber());

		if (FunctionUtil.isSet(r.getsRemark()))
			soapObject.addProperty("sRemark", r.getsRemark());

		if (FunctionUtil.isSet(r.getsOtherParameter()))
			soapObject.addProperty("sOtherParameter", r.getsOtherParameter());

		if (FunctionUtil.isSet(r.getsResponseID()))
			soapObject.addProperty("sResponseID", r.getsResponseID());

		if (FunctionUtil.isSet(r.getsResponseStatus()))
			soapObject.addProperty("sResponseStatus", r.getsResponseStatus());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		DLog.i(TAG, "bodyIn :" + envelope.bodyIn);
		DLog.i(TAG, "bodyOut :" + envelope.bodyOut);

		try {

			DLog.e(TAG, "addLog");
			HttpHandlerHelper sh = new HttpHandlerHelper();
			String errCode = "Err_Code=RequestInput";
			String Message = "&Message=" + URLEncoder.encode("TESTING", "UTF-8");
			String ServerKey = "&ServerKey=qpay99";
			String MSISDN = "&MSISDN=" + SharedPreferenceUtil.getsClientUserName();
			String Param = "&Param=NO";
			String Remark = "&Remark=NO";
			String url = errCode + ServerKey + MSISDN + Param + Remark + Message;
			DLog.e(TAG, "Response from url: " + Config.ErrorLog_URL + url);
//			String jsonStr = sh.makeServiceCall(Config.ErrorLog_URL + url, "POST");
//			DLog.e(TAG, "Response from url: " + jsonStr);
		} catch (Exception e2) {
			DLog.e(TAG, "addLog Err " + e2.getMessage());
		}



		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL,40000);
		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_RequestInput, envelope);
			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);
					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}
			}
			DLog.e(TAG, "true:" + result.toString());
		} catch (ConnectTimeoutException ex) {
			DLog.e(TAG, "ConnectTimeoutException:" + ex.getMessage());
			try {
				DLog.e(TAG, "addLog1 SocketTimeoutException ");
				HttpHandlerHelper sh = new HttpHandlerHelper();
				String errCode = "Err_Code=RequestInput";
				String Message = "&Message=ConnectTimeoutException";
				String ServerKey = "&ServerKey=qpay99";
				DLog.e(TAG, "addLog2");
				String MSISDN = "&MSISDN=" + SharedPreferenceUtil.getsClientUserName();
//				String Param = "&Param=NO" + URLEncoder.encode(envelope.bodyIn.toString(), "UTF-8");
				String Param = "&Param=NO";
				String Remark = "&Remark=NO";
				DLog.e(TAG, "addLog3");
				String url = errCode + ServerKey + MSISDN + Param + Remark + Message;
				DLog.e(TAG, "Response from url: " + Config.ErrorLog_URL + url);
				String jsonStr = sh.makeServiceCall(Config.ErrorLog_URL + url, "POST");
				DLog.e(TAG, "Response from url: " + jsonStr);
			} catch (Exception e2) {
				DLog.e(TAG, "addLog Err " + e2.getMessage());
			}

		}catch (SocketTimeoutException ex){
			DLog.e(TAG, "SocketTimeoutException:" + ex.getMessage());
			try {
				DLog.e(TAG, "addLog1 SocketTimeoutException ");
				HttpHandlerHelper sh = new HttpHandlerHelper();
				String errCode = "Err_Code=RequestInput";
				String Message = "&Message=SocketTimeoutException";
				String ServerKey = "&ServerKey=qpay99";
				DLog.e(TAG, "addLog2");
				String MSISDN = "&MSISDN=" + SharedPreferenceUtil.getsClientUserName();
//				String Param = "&Param=NO" + URLEncoder.encode(envelope.bodyIn.toString(), "UTF-8");
				String Param = "&Param=NO";
				String Remark = "&Remark=NO";
				DLog.e(TAG, "addLog3");
				String url = errCode + ServerKey + MSISDN + Param + Remark + Message;
				DLog.e(TAG, "Response from url: " + Config.ErrorLog_URL + url);
				String jsonStr = sh.makeServiceCall(Config.ErrorLog_URL + url, "POST");
				DLog.e(TAG, "Response from url: " + jsonStr);
			} catch (Exception e2) {
				DLog.e(TAG, "addLog Err " + e2.getMessage());
			}

		} catch (Exception e) {
			DLog.e(TAG, "fasle:" + e.getMessage());
			e.printStackTrace();

			try {
				DLog.e(TAG, "addLog1");
				HttpHandlerHelper sh = new HttpHandlerHelper();
				String errCode = "Err_Code=RequestInput";
				String Message = "&Message=Exception";
				String ServerKey = "&ServerKey=qpay99";
				DLog.e(TAG, "addLog2");
				String MSISDN = "&MSISDN=" + SharedPreferenceUtil.getsClientUserName();
//				String Param = "&Param=NO" + URLEncoder.encode(envelope.bodyIn.toString(), "UTF-8");
				String Param = "&Param=NO";
				String Remark = "&Remark=NO";
				DLog.e(TAG, "addLog3");
				String url = errCode + ServerKey + MSISDN + Param + Remark + Message;
				DLog.e(TAG, "Response from url: " + Config.ErrorLog_URL + url);
				String jsonStr = sh.makeServiceCall(Config.ErrorLog_URL + url, "POST");
				DLog.e(TAG, "Response from url: " + jsonStr);
			} catch (Exception e2) {
				DLog.e(TAG, "addLog Err " + e2.getMessage());
			}
			return null;
		}

		return result;
	}

	public RTSoap GetProductObject(String sClientUserName,
			String sClientPassword) {
		RTSoap result = null;

		String methodName = "GetProductObject";
		SoapObject soapObject = new SoapObject(WS_NAMESPACE, methodName);

		soapObject.addProperty("sClientUserName", sClientUserName);
		soapObject.addProperty("sClientPassword", sClientPassword);
		soapObject.addProperty("lProductInfo", "");
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		HttpTransportSE httpTranstation = new HttpTransportSE(WS_URL);

		try {
			httpTranstation.call(WS_NAMESPACE + METHOD_GetProductObject,
					envelope);

			Object tmpResponse = envelope.bodyIn;
			if (tmpResponse instanceof SoapObject) {
				SoapObject resultRpc = (SoapObject) tmpResponse;
				result = new RTSoap();
				for (int i = 0; i < resultRpc.getPropertyCount(); i++) {
					PropertyInfo pi = new PropertyInfo();
					resultRpc.getPropertyInfo(i, pi);

					RTSoapProperty subElement = new RTSoapProperty();
					convert2RTSoap(resultRpc.getProperty(i), pi, subElement);
					result.addProperty(subElement);
				}

			}

			DLog.i("test", "true:" + result.toString());
		} catch (Exception e) {
			DLog.i("test", "fasle:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;
	}

	private void convert2RTSoap(Object obj, PropertyInfo pi,
			RTSoapProperty element) {
		if (obj instanceof SoapObject) {
			// System.out.println("SoapObject");
			SoapObject soap = (SoapObject) obj;
			element.setName(pi.getName());

			for (int i = 0; i < soap.getPropertyCount(); i++) {
				PropertyInfo piSub = new PropertyInfo();
				soap.getPropertyInfo(i, piSub);
				RTSoapProperty subElement = new RTSoapProperty();
				this.convert2RTSoap(soap.getProperty(i), piSub, subElement);
				element.addProperty(subElement);
			}
		} else if (obj instanceof SoapPrimitive) {
			// System.out.println("SoapPrimitive");
			SoapPrimitive soap = (SoapPrimitive) obj;
			System.out.println(pi.getName() + ":" + soap.toString());
			element.setName(pi.getName());
			element.setValue(soap.toString());

		} else {
			element.setName(pi.getName());
			element.setValue(obj.toString());
		}
	}
}
