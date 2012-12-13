package com.example.lib;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class HttpPack {
	public static boolean hasConnected(Activity t){
		final ConnectivityManager connMgr = (ConnectivityManager) t
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable()) {
			return true;
		} else if (mobile.isAvailable()) {
			return true;
		}
		return false;

    }
}
