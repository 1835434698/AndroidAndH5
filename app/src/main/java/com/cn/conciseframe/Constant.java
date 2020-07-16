package com.cn.conciseframe;

import android.content.Context;
import android.os.Environment;

public class Constant {
	
	public static Context context;
	public static final String url = "http://10.0.4.4:8080/MySpringWeb/mvc/";
//	public static final String url = "http://192.168.191.1:8080/MySpringWeb/mvc/";
//	public static final String url = "http://192.168.191.1:8081/MyPHPWeb/mobile/";
	public static int widthScreen;
	public static int heightScreen;
	
	public final static int ERROR_CODE = 9999;
//
	public static String MESSSAGE = "网络错误";
//	public static String path = Environment.getExternalStorageDirectory().toString()+"/ConciseFrame";
	public static String path;
//	public static final String savePath = path + "/saveImages/";
//	public static final String upLoadPath =  path+ "/upLoadImages/";
//	public static final String tempPath = path + "/tempImages/";
	public static final String logPath = path + "/log/";
	public static final String logException = "logException.txt";




}
