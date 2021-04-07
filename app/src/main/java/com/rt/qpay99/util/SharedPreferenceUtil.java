package com.rt.qpay99.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rt.qpay99.object.AddressObj;
import com.rt.qpay99.object.CustomerTxStatusInfo;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.object.RequestReloadPinObject;

import java.util.List;

public class SharedPreferenceUtil {

	private static SharedPreferences setting;
	private static SharedPreferences.Editor editor;
	public static final String PREFS_NAME = "QPay99PrefsFile";

	private static final String CLIENT_ID_PREFERENCE = "sClientID";
	private static final String CLIENT_USERNAME_PREFERENCE = "sClientUserName";
	private static final String CLIENT_PASSWORD_PREFERENCE = "sClientPassword";
	private static final String CLIENT_DEVICE_ID_PREFERENCE = "sDeviceID";


	private static final String PRODUCT_DATA_PREFERENCE = "ProductDATA";
	private static final String PRODUCT_TOPUP_PREFERENCE = "ProductTopUp";
	private static final String PRODUCT_TOPUP_OVERSEA_PREFERENCE = "ProductTopUp_os";
	private static final String PRODUCT_PAYBILL_PREFERENCE = "ProductPayBill";
	private static final String PRODUCT_PIN_PREFERENCE = "ProductPin";
    private static final String PRODUCT_SIM_PREFERENCE = "ProductSim";
	private static final String PRODUCT_SIMPACK_PREFERENCE = "Simpack";

	private static final String PRODUCT_QPROMO_PREFERENCE = "QPromo";

	private static final String GET_PRODUCTINFO = "getProductInfo";
	private static final String PRINT_TOPUP = "printTopup";
	private static final String REQUIRED_PRINTER = "requiredPrinter";
	private static final String REQUIRED_VERIFY = "requiredVerify";
	private static final String GCM_REG_ID = "gcmRegisterId";

	private static final String LAST_PIN_No = "lastPinNo";
	private static final String CLIENT_MASTERID = "clientMasterId";
    private static final String MOVING_TEXT = "movingText";
	private static final String SESSION_EXPIRED = "sessionExpired";

	private static final String MERCHANTNAME = "merchantName";

	private static final String SAVE_LOGIN = "saveLogin";

	private static final String PRODUCT_SHARE_VIA = "productShareVia";
	private static final String PRINTER_MAC_ADDRESS = "printerMacAdd";

	private static final String PRINTER_NAME = "printerName";

	private static final String SHAKE_WIN_TIME = "ShakeNwinTime";
	private static final String DAILY_PROMO = "dailyPromo";


	private static final String PRODUCT_SHARE_VIA_RELOADPIN = "productShareViaReloadPin";

	private static final String ACCOUNT_STATUS = "accountStatus";
	private static final String SERVER_KEY = "serverKey";

	private  static final String SAVE_ID_TOUCH_NAME = "IdTouchName";
	private static final String SAVE_ID_TOUCH_PASSWORD = "IdTouchPassword";

	private static final String SERVER_NAME = "serverName";

	private static final String PROFILE_PIC_LOC = "profilePic";
	private static final String CUST_ADDRESS_LIST_PREFERENCE = "CustAddressList";
	private static final String LOGIN_STATUS = "LoginStatus";

	private static final String LOCATION_COORDINATE = "LocationCoordinate";


	public static String getLocationCoordinate() {
		return setting != null ? setting.getString(LOCATION_COORDINATE, "") : "";
	}

	public static void editLocationCoordinate(String LocationCoordinate) {
		editor.putString(LOCATION_COORDINATE, LocationCoordinate);
		editor.commit();
	}



	public static String getLoginStatus() {
		return setting != null ? setting.getString(LOGIN_STATUS, "") : "";
	}

	public static void editLoginStatus(String LoginStatus) {
		editor.putString(LOGIN_STATUS, LoginStatus);
		editor.commit();
	}


	public static String getDailyPromo() {
		return setting != null ? setting.getString(DAILY_PROMO, "") : "";
	}

	public static void editDailyPromo(String dailyPromo) {
		editor.putString(DAILY_PROMO, dailyPromo);
		editor.commit();
	}

	public static String getShakeNwinTime() {
		return setting != null ? setting.getString(SHAKE_WIN_TIME, "") : "";
	}

	public static void editShakeNwinTime(String ShakeNwinTime) {
		editor.putString(SHAKE_WIN_TIME, ShakeNwinTime);
		editor.commit();
	}


	public static List<AddressObj> getCUST_ADDRESS_LIST_PREFERENCE() {
		Gson gson = new Gson();
		String json = setting != null ? setting.getString(
				CUST_ADDRESS_LIST_PREFERENCE, "") : "0";
		return gson.fromJson(json, new TypeToken<List<AddressObj>>() {
		}.getType());

	}

	public static void editCUST_ADDRESS_LIST_PREFERENCE(List<AddressObj> CustAddressList) {
		Gson gson = new Gson();
		String json = gson.toJson(CustAddressList);
		editor.putString(CUST_ADDRESS_LIST_PREFERENCE, json.toString());
		editor.commit();
	}


	public static void editSharedPreferencedvalue(String key , String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public static String getSharedPreferencedValue(String key) {
		return setting != null ? setting.getString(key, "") : "";
	}


	public SharedPreferenceUtil(final Context context) {
		SharedPreferenceUtil.init(context);
	}

	private static void init(final Context context) {
		if (context != null) {
			setting = context.getSharedPreferences(PREFS_NAME,
					Context.MODE_PRIVATE);
			editor = setting.edit();
		}
	}

	public static String getPrinterName() {
		return setting != null ? setting.getString(PRINTER_NAME, "") : "";
	}

	public static void editPrinterName(String printerName) {
		editor.putString(PRINTER_NAME, printerName);
		editor.commit();
	}

	public static String getPrinterMacAdd() {
		return setting != null ? setting.getString(PRINTER_MAC_ADDRESS, "") : "";
	}

	public static void editprinterMacAdd(String printerMacAdd) {
		editor.putString(PRINTER_MAC_ADDRESS, printerMacAdd);
		editor.commit();
	}

	public static Boolean getSaveLogin() {
		return setting != null ? setting.getBoolean(SAVE_LOGIN, false) : false;
	}

	public static void editSaveLogin(boolean saveLogin) {
		editor.putBoolean(SAVE_LOGIN, saveLogin);
		editor.commit();
	}

	public static String getMerchantName() {
		return setting != null ? setting.getString(MERCHANTNAME, "") : "";
	}

	public static void editMerchantName(String merchantName) {
		editor.putString(MERCHANTNAME, merchantName);
		editor.commit();
	}

    public static String getMovingText() {
        return setting != null ? setting.getString(MOVING_TEXT, "") : "";
    }

	public static Long getSessionExpired() {
		return setting != null ? setting.getLong(SESSION_EXPIRED, -1) : -1;
	}

	public static void editgetSessionExpired(Long sessionExpired) {
		editor.putLong(SESSION_EXPIRED, sessionExpired);
		editor.commit();
	}

    public static void editMovingText(String movingText) {
        editor.putString(MOVING_TEXT, movingText);
        editor.commit();
    }

	public static String getClientMasterId() {
		return setting != null ? setting.getString(CLIENT_MASTERID, "") : "";
	}

	public static void editClientMasterId(String clientMasterId) {
		editor.putString(CLIENT_MASTERID, clientMasterId);
		editor.commit();
	}

	public static String getsClientUserName() {
		return setting != null ? setting.getString(CLIENT_USERNAME_PREFERENCE,
				null) : null;
	}

	public static String getLastPinNo() {
		return setting != null ? setting.getString(LAST_PIN_No, null) : null;
	}

	public static void editLastPinNo(String lastPinNo) {
		editor.putString(LAST_PIN_No, lastPinNo);
		editor.commit();
	}

	public static String getGcmRegisterId() {
		return setting != null ? setting.getString(GCM_REG_ID, null) : null;
	}

	public static void editGcmRegisterId(String gcmRegisterId) {
		editor.putString(GCM_REG_ID, gcmRegisterId);
		editor.commit();
	}

	public static String getsClientPassword() {
		return setting != null ? setting.getString(CLIENT_PASSWORD_PREFERENCE,
				null) : null;
	}

	public static String getsDeviceID() {
		return setting != null ? setting.getString(CLIENT_DEVICE_ID_PREFERENCE,
				null) : null;
	}

	public static void editsClientUserName(String sClientUserName) {
		editor.putString(CLIENT_USERNAME_PREFERENCE, sClientUserName);
		editor.commit();
	}

	public static void editsClientPassword(String sClientPassword) {
		editor.putString(CLIENT_PASSWORD_PREFERENCE, sClientPassword);
		editor.commit();
	}

	public static void editsDeviceID(String sDeviceID) {
		editor.putString(CLIENT_DEVICE_ID_PREFERENCE, sDeviceID);
		editor.commit();
	}

	public static void editsClientID(int sClientID) {
		editor.putInt(CLIENT_ID_PREFERENCE, sClientID);
		editor.commit();
	}

	public static void editGetProductInfo(Boolean getProductInfo) {
		editor.putBoolean(GET_PRODUCTINFO, getProductInfo);
		editor.commit();
	}

	public static void editIsRequiredPrinter(Boolean requiredPrinter) {
		editor.putBoolean(REQUIRED_PRINTER, requiredPrinter);
		editor.commit();
	}

	public static void editIsRequiredVerify(Boolean requiredVerify) {
		editor.putBoolean(REQUIRED_VERIFY, requiredVerify);
		editor.commit();
	}

	public static int getsClientID() {
		return setting != null ? setting.getInt(CLIENT_ID_PREFERENCE, 1) : 1;
	}

	public static boolean getProductInfo() {
		return setting != null ? setting.getBoolean(GET_PRODUCTINFO, true)
				: false;
	}

	public static boolean isRequiredPrinter() {
		return setting != null ? setting.getBoolean(REQUIRED_PRINTER, false)
				: false;
	}

	public static boolean isRequiredCheckPassword() {
		return setting != null ? setting.getBoolean(REQUIRED_VERIFY, false)
				: false;
	}

	public static void editPRODUCT_TOPUP_OVERSEA_PREFERENCE(
			List<ProductInfo> product) {
		Gson gson = new Gson();
		String json = gson.toJson(product);
		editor.putString(PRODUCT_TOPUP_OVERSEA_PREFERENCE, json.toString());
		editor.commit();
	}

	public static List<ProductInfo> getPRODUCT_TOPUP_OVERSEA_PREFERENCE() {
		List<ProductInfo> p;
		Gson gson = new Gson();
		String json = setting != null ? setting.getString(
				PRODUCT_TOPUP_OVERSEA_PREFERENCE, "") : "0";
		return gson.fromJson(json, new TypeToken<List<ProductInfo>>() {
		}.getType());

	}

    public static List<ProductInfo> getPRODUCT_SIM_PREFERENCE() {
        List<ProductInfo> p;
        Gson gson = new Gson();
        String json = setting != null ? setting.getString(
                PRODUCT_SIM_PREFERENCE, "") : "0";
        return gson.fromJson(json, new TypeToken<List<ProductInfo>>() {
        }.getType());

    }

    public static void editPRODUCT_SIM_PREFERENCE(List<ProductInfo> product) {
        Gson gson = new Gson();
        String json = gson.toJson(product);
        editor.putString(PRODUCT_SIM_PREFERENCE, json.toString());
        editor.commit();
    }

	public static void editPRODUCT_PAYBILL_PREFERENCE(List<ProductInfo> product) {
		Gson gson = new Gson();
		String json = gson.toJson(product);
		editor.putString(PRODUCT_PAYBILL_PREFERENCE, json.toString());
		editor.commit();
	}

	public static List<ProductInfo> getPRODUCT_PAYBILL_PREFERENCE() {
		List<ProductInfo> p;
		Gson gson = new Gson();
		String json = setting != null ? setting.getString(
				PRODUCT_PAYBILL_PREFERENCE, "") : "0";
		return gson.fromJson(json, new TypeToken<List<ProductInfo>>() {
		}.getType());

	}

	public static void editPRODUCT_PIN_PREFERENCE(List<ProductInfo> product) {
		Gson gson = new Gson();
		String json = gson.toJson(product);
		editor.putString(PRODUCT_PIN_PREFERENCE, json.toString());
		editor.commit();
	}

	public static List<ProductInfo> getPRODUCT_PIN_PREFERENCE() {
		List<ProductInfo> p;
		Gson gson = new Gson();
		String json = setting != null ? setting.getString(
				PRODUCT_PIN_PREFERENCE, "") : "0";
		return gson.fromJson(json, new TypeToken<List<ProductInfo>>() {
		}.getType());

	}

	public static void editPRODUCT_QPROMO_PREFERENCE(List<ProductInfo> product) {
		Gson gson = new Gson();
		String json = gson.toJson(product);
		editor.putString(PRODUCT_QPROMO_PREFERENCE, json.toString());
		editor.commit();
	}

	public static List<ProductInfo> getPRODUCT_QPROMO_PREFERENCE() {
		List<ProductInfo> p;
		Gson gson = new Gson();
		String json = setting != null ? setting.getString(
				PRODUCT_QPROMO_PREFERENCE, "") : "0";
		return gson.fromJson(json, new TypeToken<List<ProductInfo>>() {
		}.getType());

	}

	public static void editPRODUCT_DATA_PREFERENCE(List<ProductInfo> product) {
		Gson gson = new Gson();
		String json = gson.toJson(product);
		editor.putString(PRODUCT_DATA_PREFERENCE, json.toString());
		editor.commit();
	}

	public static List<ProductInfo> getPRODUCT_DATA_PREFERENCE() {
		List<ProductInfo> p;
		Gson gson = new Gson();
		String json = setting != null ? setting.getString(
				PRODUCT_DATA_PREFERENCE, "") : "0";
		return gson.fromJson(json, new TypeToken<List<ProductInfo>>() {
		}.getType());

	}


	public static void editPRODUCT_TOPUP_PREFERENCE(List<ProductInfo> product) {
		Gson gson = new Gson();
		String json = gson.toJson(product);
		editor.putString(PRODUCT_TOPUP_PREFERENCE, json.toString());
		editor.commit();
	}

	public static List<ProductInfo> getPRODUCT_TOPUP_PREFERENCE() {
		List<ProductInfo> p;
		Gson gson = new Gson();
		String json = setting != null ? setting.getString(
				PRODUCT_TOPUP_PREFERENCE, "") : "0";
		return gson.fromJson(json, new TypeToken<List<ProductInfo>>() {
		}.getType());

	}

	public static boolean isPrintTopup() {
		return setting != null ? setting.getBoolean(PRINT_TOPUP, false)
				: false;
	}

	public static void editIsPrintTopup(Boolean printTopup) {
		editor.putBoolean(PRINT_TOPUP, printTopup);
		editor.commit();
	}

	public static void editPRODUCT_SIMPACK_PREFERENCE(List<ProductInfo> product) {
		Gson gson = new Gson();
		String json = gson.toJson(product);
		editor.putString(PRODUCT_SIMPACK_PREFERENCE, json.toString());
		editor.commit();
	}

	public static List<ProductInfo> getPRODUCT_SIMPACK_PREFERENCE() {
		List<ProductInfo> p;
		Gson gson = new Gson();
		String json = setting != null ? setting.getString(
				PRODUCT_SIMPACK_PREFERENCE, "") : "0";
		return gson.fromJson(json, new TypeToken<List<ProductInfo>>() {
		}.getType());

	}

	public static CustomerTxStatusInfo getShareVia() {
		List<ProductInfo> p;
		Gson gson = new Gson();
		String json = setting != null ? setting.getString(
				PRODUCT_SHARE_VIA, null) : null;
		return gson.fromJson(json, new TypeToken<CustomerTxStatusInfo>() {
		}.getType());
	}

	public static void editShareVia(CustomerTxStatusInfo c){
		Gson gson = new Gson();
		String json = gson.toJson(c);
		editor.putString(PRODUCT_SHARE_VIA, json.toString());
		editor.commit();
	}

	public static RequestReloadPinObject getShareViaRelaodPin() {
		List<ProductInfo> p;
		Gson gson = new Gson();
		String json = setting != null ? setting.getString(
				PRODUCT_SHARE_VIA_RELOADPIN, null) : null;
		return gson.fromJson(json, new TypeToken<RequestReloadPinObject>() {
		}.getType());
	}

	public static void editShareViaRelaodPin(RequestReloadPinObject c){
		Gson gson = new Gson();
		String json = gson.toJson(c);
		editor.putString(PRODUCT_SHARE_VIA_RELOADPIN, json.toString());
		editor.commit();
	}

	public static String getAccountStatus() {
		return setting != null ? setting.getString(ACCOUNT_STATUS, "NEW") : "NEW";
	}

	public static void editAccountStatus(String accountStatus) {
		editor.putString(ACCOUNT_STATUS, accountStatus);
		editor.commit();
	}

	public static String getServerKey() {
		return setting != null ? setting.getString(SERVER_KEY, "Server") : "Server";
	}

	public static void editServerKey(String serverName) {
		editor.putString(SERVER_KEY, serverName);
		editor.commit();
	}

	public static String getBioName() {
		//return setting != null ? setting.getBoolean(SAVE_FINGERPRINT, true) : false;
		return setting != null ? setting.getString(SAVE_ID_TOUCH_NAME, "") : "";
	}

	public static void editBioName(String IdTouchName) {
		editor.putString(SAVE_ID_TOUCH_NAME, IdTouchName);
		editor.commit();
	}

	public static String getBioPassword() {
		//return setting != null ? setting.getBoolean(SAVE_FINGERPRINT, true) : false;
		return setting != null ? setting.getString(SAVE_ID_TOUCH_PASSWORD, "") : "";
	}

	public static void editBioPassword(String IdTouchPassword) {
		editor.putString(SAVE_ID_TOUCH_PASSWORD, IdTouchPassword);
		editor.commit();
	}

	public static String getServerName() {
		return setting != null ? setting.getString(SERVER_NAME, "SERVER") : "SERVER";
	}

	public static void editServerName(String serverName) {
		editor.putString(SERVER_NAME, serverName);
		editor.commit();
	}


}
