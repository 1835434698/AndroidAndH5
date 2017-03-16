package com.cn.conciseframe.manager;


import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.cn.conciseframe.Constant;
import com.cn.conciseframe.R;
import com.cn.conciseframe.view.LoginAc;


public class MainManager {
	
	private Activity activity;
	private LinearLayout ll_middle;
	
	private static MainManager mainmanager = new MainManager();

	private MainManager() {

	};

	public static MainManager getInstance() {

		return mainmanager;
	}
	
	public void init(Activity activity) {
		this.activity = activity;
		Constant.context = activity;
		
		WindowManager manager = activity.getWindowManager();
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		Constant.widthScreen = outMetrics.widthPixels;
		Constant.heightScreen = outMetrics.heightPixels;

		ll_middle = (LinearLayout) activity.findViewById(R.id.ll_middle);

		TopMenu.getInstance().init(activity);
		BottomMenu.getInstance().init(activity);
		MiddleView.getInstance().setMiddleContainer(ll_middle);
		
		MiddleView.getInstance().startCallbackActivity(LoginAc.class, null);
	}
	

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		MiddleView.getInstance().getCurrentView().onActivityResult(requestCode, resultCode, data);
		
	}
	
	
	public void startActivityForResult(Intent intent, int requestCode) {
		activity.startActivityForResult(intent, requestCode);
	}


}
