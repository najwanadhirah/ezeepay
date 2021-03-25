/**
 *
 */
package com.rt.qpay99.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.IBinder;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.rt.qpay99.Config;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

public class GCMService extends Service {
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";
	static final String TAG = "GCMService";
	/**
	 * Default lifespan (7 days) of a reservation until it is considered
	 * expired.
	 */
	public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;
	Context context;
	String regid;
	// SharedPreferences prefs;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		regid = getRegistrationId(context);
		Config.GCM_RegId = regid;
		if (regid.length() == 0) {
			registerBackground();
		} else {
			new submitRegisterationIdAsync().execute();

		}
		gcm = GoogleCloudMessaging.getInstance(this);
	}

	/**
	 * Gets the current registration id for application on GCM service.
	 * <p>
	 * If result is empty, the registration has failed.
	 * 
	 * @return registration id, or empty string if the registration is not
	 *         complete.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.length() == 0) {
			DLog.e(TAG, "Registration not found.");
			return "";
		}
		DLog.e(TAG, "Registration Id:" + registrationId);
		// check if app was updated; if so, it must clear registration id to
		// avoid a race condition if GCM sends a message
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion || isRegistrationExpired()) {
			DLog.e(TAG, "App version changed or registration expired.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(SharedPreferenceUtil.PREFS_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @return Application's version name from the {@code PackageManager}.
	 */
	private static String getAppVersionName(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Checks if the registration has expired.
	 * <p>
	 * To avoid the scenario where the device sends the registration to the
	 * server but the server loses it, the app developer may choose to
	 * re-register after REGISTRATION_EXPIRY_TIME_MS.
	 * 
	 * @return true if the registration has expired.
	 */
	private boolean isRegistrationExpired() {
		final SharedPreferences prefs = getGCMPreferences(context);
		// checks if the information is not stale
		long expirationTime = prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME,
				-1);
		return System.currentTimeMillis() > expirationTime;
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration id, app versionCode, and expiration time in the
	 * application's shared preferences.
	 */
	private void registerBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					DLog.e(TAG, "registerBackground, Registration Id:" + regid);
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(Config.GCM_SENDER_ID);
					msg = "Device registered, registration id=" + regid;

					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.

					// For this demo: we don't need to send it because the
					// device
					// will send upstream messages to a server that echo back
					// the message
					// using the 'from' address in the message.

					// Save the regid - no need to register again.
					if (!FunctionUtil.isSet(SharedPreferenceUtil
							.getGcmRegisterId())) {
						setRegistrationId(context, regid);

						new submitRegisterationIdAsync().execute();
					}

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
			}
		}.execute(null, null, null);
	}

	/**
	 * Stores the registration id, app versionCode, and expiration time in the
	 * application's {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration id
	 */
	private void setRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		DLog.e(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		long expirationTime = System.currentTimeMillis()
				+ REGISTRATION_EXPIRY_TIME_MS;

		DLog.e(TAG, "Setting registration expiry time to "
				+ new Timestamp(expirationTime));
		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
		editor.commit();
	}

	public class submitRegisterationIdAsync extends
			AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... regId) {
			DLog.e(TAG, "submitRegisterationIdAsync ");
			//Switch to FCM
			//SharedPreferenceUtil.editGcmRegisterId(regid);
			//Config.GCM_RegId = regid;

			return false;
		}
	}

	// The is for development purposed. The registration ID will submit through
	// API
	private void submitRegisterationIdAsync2(final String regId) {

		try {

			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpPost httpPost = new HttpPost(
					"http://42.152.224.48:8020/SIMPackStore/GCMClientIDReceiver.aspx?gcm="
							+ regid);
			DLog.e(TAG, "httpPost " + httpPost);
			// Building post parameters
			// key and value pair
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
			nameValuePair.add(new BasicNameValuePair("gcm", regId));

			// Url Encoding the POST parameters
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			} catch (UnsupportedEncodingException e) {
				// writing error to Log
				e.printStackTrace();
			}

			// Making HTTP Request
			try {
				HttpResponse response = httpClient.execute(httpPost);

				// writing response to log
				DLog.d("Http Response:", response.toString());
			} catch (ClientProtocolException e) {
				// writing exception to log
				e.printStackTrace();
			} catch (IOException e) {
				// writing exception to log
				e.printStackTrace();

			}
		} catch (Exception ex) {
			DLog.e(TAG, "EX " + ex.getMessage());
		}
	}
}
