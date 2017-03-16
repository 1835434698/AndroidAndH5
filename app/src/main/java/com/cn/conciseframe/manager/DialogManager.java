package com.cn.conciseframe.manager;



import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.cn.conciseframe.R;


public class DialogManager {
	
	/**
	 * 退出
	 * 
	 * @param context
	 */
	public static void showExitSystem(Context context) {
		Builder builder = new Builder(context);
		builder.setIcon(R.drawable.ic_launcher)
				//
				.setTitle(R.string.app_name)
				//
				.setMessage("是否退出应用？")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}
				}).setNegativeButton("取消", null).show();

	}
	
	/**
	 * 显示错误提示框
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showErrorDialog(Context context, String msg) {
		new Builder(context)//
		.setIcon(R.drawable.ic_launcher)//
		.setTitle(R.string.app_name)//
		.setMessage(msg)//
		.setNegativeButton("确定", null)//
		.show();
	}
	
	public static void showErrorDialog(Context context, String title, String msg) {
		new Builder(context)//
		.setIcon(R.drawable.ic_launcher)//
		.setTitle(title)//
		.setMessage(msg)//
		.setNegativeButton("确定", null)//
		.show();
	}
	
//	/**
//	 *
//	 * @param context
//	 * @param msg
//	 */
//	public static void showSelectDialog(Context context, String title, String msg) {
//		new Builder(context)//
//		.setIcon(R.drawable.ic_launcher)//
//		.setTitle(title)//
//		.setMessage(msg)//
//		.setPositiveButton("确定", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				MiddleView.getInstance().getCurrentView().dialogSelectOK();
//			}
//		}).setNegativeButton("取消", null)
//		.show();
//	}
//	/**
//	 *
//	 * @param context
//	 * @param msg
//	 */
//	public static void showSelectDialog(Context context, String msg) {
//		new Builder(context)//
//		.setIcon(R.drawable.ic_launcher)//
//		.setTitle(R.string.app_name)//
//		.setMessage(msg)//
//		.setPositiveButton("确定", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				MiddleView.getInstance().getCurrentView().dialogSelectOK();
//			}
//		}).setNegativeButton("取消", null)
//		.show();
//	}
	

}
