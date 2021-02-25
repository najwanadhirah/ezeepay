/**
 *
 */
package com.rt.qpay99.gcm;

import android.os.Bundle;

public class GCMUtil {

	private static Bundle gcmBunlde;

	/**
	 * @return the gcmBunlde
	 */
	public static final Bundle getGcmBunlde() {
		return gcmBunlde;
	}

	/**
	 * @param gcmBunlde
	 *            the gcmBunlde to set
	 */
	public static final void setGcmBunlde(Bundle gcmBunlde) {
		GCMUtil.gcmBunlde = gcmBunlde;
	}

}
