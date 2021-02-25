package com.rt.qpay99;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Base64;

import androidx.multidex.MultiDex;

import com.android.vending.billing.IInAppBillingService;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.crashlytics.android.Crashlytics;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.object.ProductPrice;
import com.rt.qpay99.service.GCMService;
import com.rt.qpay99.service.GPSTrackerService;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class SRSApp extends Application {

	private static Context mContext;

	public static String sClientUserName;
	public static String sClientPassword;
	public static BluetoothAdapter mBluetoothAdapter;
	public static BroadcastReceiver receiver;
	private ArrayList<BluetoothDevice> bondDevices = null;
	private String TAG = "SRSApp";
	public static boolean isPrinterConnected;
	public static GPSTrackerService gps;
	public static String printerMacAdd = "";
	public static TelephonyManager telephonyManager;
	public static Resources res;
	public static PackageInfo mPackageInfo;
	public static String NOT_REQUIRED_PRINTER = "notRequiredPrinter";
	public static String versionName = "";
	public static boolean isNetworkConnected = false;
	public static boolean noConnectivity;
	public static boolean SIM_ABSENT = true;
    public static String appName= "";
	public static List<ProductPrice> CELCOMMAGICPriceList;
	public static LocationManager lm;
	public static List<ProductPrice> CELCOMADDONPriceList;

	public static List<AgentProductDiscount> DiscountRates;
	public static HashMap<String, AgentProductDiscount> hashmapDiscountRate = new HashMap<>();


	public static IInAppBillingService mService;

	ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name,
									   IBinder service) {
			mService = IInAppBillingService.Stub.asInterface(service);
		}
	};


	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Intent serviceIntent =
				new Intent("com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


		Fabric.with(this, new Crashlytics());
		//FacebookSdk.sdkInitialize(getApplicationContext());
		new SharedPreferenceUtil(getApplicationContext());
		mContext = this;
		isPrinterConnected = false;

		TypefaceProvider.registerDefaultIconSets();

		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		gps = new GPSTrackerService(mContext);
		res = getResources();
//		if(!Config.isDebug){
//			startService(new Intent(this, GCMService.class));
//		}
		startService(new Intent(this, GCMService.class));
//		FirebaseApp.initializeApp(mContext);
		try {
			mPackageInfo = mContext.getPackageManager().getPackageInfo(
					getPackageName(), 0);
			versionName = mPackageInfo.versionName;

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			versionName = "";
			e.printStackTrace();
		}
		DLog.e(TAG, "versionName " + versionName);

		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_receipt_qpay);


        appName = getString(R.string.app_name);
        appName = getString(R.string.app_name);




		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		DLog.e(TAG, "extStorageDirectory =======================>"
				+ extStorageDirectory);

		File file = new File(extStorageDirectory, "ic_receipt_icon.PNG");
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			try {
				outStream.flush();
				outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

			DLog.e(TAG,"getSimOperatorName ------------------ " + telMgr.getSimOperatorName());

			int simState = telMgr.getSimState();
			switch (simState) {
			case TelephonyManager.SIM_STATE_ABSENT:
				SIM_ABSENT = true;
				DLog.e(TAG, "SIM_ABSENT " + SIM_ABSENT);
				// do something
				break;
			case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
				// do something
				break;
			case TelephonyManager.SIM_STATE_PIN_REQUIRED:
				// do something
				break;
			case TelephonyManager.SIM_STATE_PUK_REQUIRED:
				// do something
				break;
			case TelephonyManager.SIM_STATE_READY:
				SIM_ABSENT = false;
				DLog.e(TAG, "SIM_ABSENT " + SIM_ABSENT);
				// do something
				break;
			case TelephonyManager.SIM_STATE_UNKNOWN:
				// do something
				break;
			}
		} catch (Exception ex) {

		}


        PackageInfo info;
        try {

            info = getPackageManager().getPackageInfo("com.rt.qpay99",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                // String something = new
                // String(Base64.encodeBytes(md.digest()));
                DLog.e("Hash key===================>", something);
				DLog.d("Hash key2:====================", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (NameNotFoundException e1) {
            DLog.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            DLog.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            DLog.e("exception", e.toString());
        }


		addProductPrice();


		try {
			PackageInfo info2 = getPackageManager().getPackageInfo(
					"com.rt.qpay99",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info2.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				DLog.d("KeyHash:====================", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

		FunctionUtil.getPINFormat("1234567890123456");
	}


	void addProductPrice() {
		CELCOMMAGICPriceList= new ArrayList<ProductPrice>();
		CELCOMMAGICPriceList.add(new ProductPrice("8", "8"));
		CELCOMMAGICPriceList.add(new ProductPrice("18", "18"));
		CELCOMMAGICPriceList.add(new ProductPrice("38", "38"));
		CELCOMMAGICPriceList.add(new ProductPrice("48", "48"));
		CELCOMMAGICPriceList.add(new ProductPrice("68", "68"));


		CELCOMADDONPriceList= new ArrayList<ProductPrice>();
		CELCOMADDONPriceList.add(new ProductPrice("1", "1"));
		CELCOMADDONPriceList.add(new ProductPrice("2", "2"));
		CELCOMADDONPriceList.add(new ProductPrice("3", "3"));
		CELCOMADDONPriceList.add(new ProductPrice("5", "5"));
		CELCOMADDONPriceList.add(new ProductPrice("6", "6"));
		CELCOMADDONPriceList.add(new ProductPrice("20", "20"));
	}

	void detectLocation() {

	}

	public static boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}
	
//	public static boolean hasActiveInternetConnection(Context context) {
//    if (isNetworkAvailable(context)) {
//        try {
//            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
//            urlc.setRequestProperty("User-Agent", "Test");
//            urlc.setRequestProperty("Connection", "close");
//            urlc.setConnectTimeout(1500); 
//            urlc.connect();
//            return (urlc.getResponseCode() == 200);
//        } catch (IOException e) {
//        Log.e(LOG_TAG, "Error checking internet connection", e);
//        }
//    } else {
//    Log.d(LOG_TAG, "No network available!");
//    }
//    return false;
//}

}
