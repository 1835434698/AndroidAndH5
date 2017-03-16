package com.cn.conciseframe.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断网络类型
 * 
 * @author tang
 * 
 */
public class NetUtil {

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetType(Context context) {
		// 判断手机的链接渠道
		// WLAN（wi-fi）
		boolean isWIFI = isWIFIConnectivity(context);
		// 手机APN接入点
		boolean isMobile = isMobileConnectivity(context);
		// 当前无可利用的通信渠道
		if (!isWIFI && !isMobile) {
			return false;
		}

		return true;
	}

	/**
	 * 手机APN接入点
	 * 
	 * @param context
	 * @return
	 */

	private static boolean isMobileConnectivity(Context context) {
		// ConnectivityManager---systemService---Context
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}

	/**
	 * 判断手机的链接渠道 WLAN（wi-fi）
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isWIFIConnectivity(Context context) {
		// ConnectivityManager---systemService---Context
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}

}