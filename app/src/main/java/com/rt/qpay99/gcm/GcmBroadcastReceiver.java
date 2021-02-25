/**
 *
 */
package com.rt.qpay99.gcm;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

public class GcmBroadcastReceiver extends BroadcastReceiver {
	static final String TAG = "GcmBroadcastReceiver";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	int numMessages = 0;
	NotificationCompat.Builder builder;
	Context ctx;
	String mMessage;
	private static String UPDATE_PRODUCT = "UPDATE_PRODUCT";
	private static String UPDATE_MESSAGE = "UPDATE_MESSAGE";

	@Override
	public void onReceive(Context context, Intent intent) {
		DLog.e(TAG, "onReceive GCM");
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		ctx = context;
		String messageType = gcm.getMessageType(intent);
		Bundle extras = intent.getExtras();
		// extras.getString("message");
		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			sendNotification("Send error: " + intent.getExtras().toString(),
					null);
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
				.equals(messageType)) {
			sendNotification("Deleted messages on server: "
					+ intent.getExtras().toString(), null);
		} else {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				mMessage = extras.getString("message");
				if(mMessage!=null)
					if (mMessage.equalsIgnoreCase(UPDATE_PRODUCT)) {
						new retrieveProductInfoAsync().execute();

					} else if (mMessage.equalsIgnoreCase(UPDATE_MESSAGE)) {
						DLog.e(TAG, "Get MESSAGE_TAB");
						getMessageInfo("MESSAGE_TAB");
					} else {
						sendNotification(mMessage, extras);
					}

			}
		}
		setResultCode(Activity.RESULT_OK);
	}

	RTWS rtWS = new RTWS();
	private void getMessageInfo(final String sMessageType) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

			}

			@Override
			protected String doInBackground(Void... arg0) {
				// TODO Auto-generated method stub

				return rtWS
						.GetMessageInfo(
								SharedPreferenceUtil.getsClientUserName(),
								SharedPreferenceUtil.getsClientPassword(),
								sMessageType);

			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (FunctionUtil.isSet(result)) {
					Config.MOVING_TEXT = result;
					DLog.e(TAG, "Config.MOVING_TEXT " + Config.MOVING_TEXT);
				}

			}

		}.execute();
	}

	// Put the GCM message into a notification and post it.
	private void sendNotification(String msg, Bundle bundle) {
		boolean showPushNotification = true;
		if (showPushNotification) {
			mNotificationManager = (NotificationManager) ctx
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Context context = ctx.getApplicationContext();
			Intent intent = new Intent(context, GCMNotificationActivity.class);
			intent.putExtras(bundle);

			PendingIntent contentIntent = PendingIntent.getActivity(context,
					(int) System.currentTimeMillis(), intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context)
					.setLargeIcon(
							BitmapFactory.decodeResource(
									context.getResources(),
									R.drawable.ic_launcher))
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(ctx.getString(R.string.app_name))
					.setTicker(msg)
					.setStyle(
							new NotificationCompat.BigTextStyle().bigText(msg))
					.setAutoCancel(false)
					.setContentText(msg)
					.setDefaults(
							Notification.DEFAULT_SOUND
									| Notification.DEFAULT_VIBRATE);

			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify((int) System.currentTimeMillis(),
					mBuilder.build());
		}
	}

	private List<ProductInfo> productInfos;
	private List<ProductInfo> topUps = new ArrayList<ProductInfo>();
	private List<ProductInfo> topUps_os = new ArrayList<ProductInfo>();
	private List<ProductInfo> payBills = new ArrayList<ProductInfo>();
	private List<ProductInfo> pins = new ArrayList<ProductInfo>();
	RTWS cs = new RTWS();

	public class retrieveProductInfoAsync extends
			AsyncTask<String, Void, List<ProductInfo>> {

		protected List<ProductInfo> doInBackground(String... arg0) {
			return cs.getProductInfo();
		}

		protected void onPostExecute(List<ProductInfo> result) {

			for (ProductInfo product : result) {
				if (product.getName().toLowerCase().indexOf("pin") > 0) {
					DLog.e("PIN", product.getName());
					if ("ACTIVE".equalsIgnoreCase(product.getStatus()))
						pins.add(product);
				} else if (product.getName().toLowerCase().indexOf("bill") > 0) {
					if ("ACTIVE".equalsIgnoreCase(product.getStatus()))
						payBills.add(product);
					DLog.e("BILL", product.getName());
				} else if (product.getName().toLowerCase().indexOf("flexi") > 0) {
					if ("ACTIVE".equalsIgnoreCase(product.getStatus()))
						topUps_os.add(product);
					DLog.e("FLEXI", product.getName());
				} else {
					if ("ACTIVE".equalsIgnoreCase(product.getStatus()))
						topUps.add(product);
					DLog.e("ORTHES", product.getName());
				}
				SharedPreferenceUtil.editPRODUCT_PAYBILL_PREFERENCE(payBills);
				SharedPreferenceUtil.editPRODUCT_PIN_PREFERENCE(pins);
				SharedPreferenceUtil
						.editPRODUCT_TOPUP_OVERSEA_PREFERENCE(topUps_os);
				SharedPreferenceUtil.editPRODUCT_TOPUP_PREFERENCE(topUps);
				SharedPreferenceUtil.editGetProductInfo(false);
			}
		}
	}
}
