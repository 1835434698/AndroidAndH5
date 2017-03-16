package com.cn.conciseframe.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.telephony.TelephonyManager;

import com.cn.conciseframe.Constant;


public class Utils {
	
	private static TelephonyManager tm;
	private static PackageManager manager;
	private static PackageInfo info;

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion() {
		try {
			String version = getPgInfo().versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取IMEI
	 * @return
	 */
	public static String getIMEI() {
		
		return getTM().getDeviceId();
	}
	
	
	private static PackageManager getPM(){
		if (manager == null) {
			manager = Constant.context.getPackageManager();
		}
		return manager;
	}
	private static PackageInfo getPgInfo() throws Exception{
		if (info == null) {
			info = getPM().getPackageInfo(Constant.context.getPackageName(), 0);
		}
		return info;
	}
	public static TelephonyManager getTM(){
		if (tm == null) {
			tm = (TelephonyManager) Constant.context.getSystemService(Activity.TELEPHONY_SERVICE);
		}
		return tm;
	}
	private static PackageInfo packageInfosing;
	public static String getSingInfo() {
		try {
			if (packageInfosing == null) {
				
				packageInfosing = Constant.context.getPackageManager().getPackageInfo(Constant.context.getPackageName(), PackageManager.GET_SIGNATURES);
			}
			Signature[] signs = packageInfosing.signatures;
			
			StringBuilder builder = new StringBuilder();
			for (Signature signature : signs) {
                builder .append(signature.toCharsString());
            }
            /************** 得到应用签名 **************/
//			Logg.t("得到应用签名 = "+builder.toString());
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 获得可用的内存
    public static long getmem_UNUSED() {
        long MEM_UNUSED;
	// 得到ActivityManager
        ActivityManager am = (ActivityManager) Constant.context.getSystemService(Context.ACTIVITY_SERVICE);
	// 创建ActivityManager.MemoryInfo对象  

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

	// 取得剩余的内存空间 

        MEM_UNUSED = mi.availMem / 1024;
        return MEM_UNUSED;
    }

	private static String htmlPath;
	public static String getHtmlPath(){
		if (htmlPath == null){
			htmlPath = Constant.context.getFilesDir().toString()+"/";
		}
		return htmlPath;
	}


}
