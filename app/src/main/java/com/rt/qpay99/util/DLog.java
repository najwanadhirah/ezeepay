package com.rt.qpay99.util;

import android.util.Log;

import com.rt.qpay99.Config;

public class DLog {

	public static void d(String tag, String msg) {
		if (Config.showLog)
			Log.d(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (Config.showLog)
			Log.w(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (Config.showLog)
			Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (Config.showLog)
			Log.e(tag, msg);
	}

}
