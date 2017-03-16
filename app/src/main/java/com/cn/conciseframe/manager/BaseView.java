package com.cn.conciseframe.manager;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cn.conciseframe.Constant;
import com.cn.conciseframe.MainActivity;
import com.cn.conciseframe.MainApplication;
import com.cn.conciseframe.util.ProgressDialog;


public abstract class BaseView {
	protected ViewGroup showview;
	protected Context context;
	protected Bundle bundle;
	public boolean isFirstLayout = true;

	public BaseView(Context context, Bundle bundle) {
		this.context = context;
		this.bundle = bundle;
		init();
		setListener();
	}

	public void onActivityFirstLayout() {
		isFirstLayout = false;
	}
	
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	
	protected abstract void init();

	public void onResume() {
		showview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				showview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				onActivityFirstLayout();
			}
		});
	}
	private EditText edit;
	private InputMethodManager imm;

	public void onPause() {
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(showview.getWindowToken(), 0);

	}
	public void onBundle(){};

	protected abstract void setListener();

	public boolean onDestory() {
		return false;
	}

	public void rightOnClick() {}

	/**
	 * 是否可以返回上一个Page
	 * true可以
	 * false 不可以
	 * @return
     */
	public boolean goBack(){
		return true;
	}

	/**
	 * 开始进度条
	 * @param msg 提示标语
     */
	protected void startProgressDialog(String msg) {
		ProgressDialog.getInstance().startProgressDialog(context, msg);
	}

	/**
	 * 开始进度条
	 */
	protected void startProgressDialog() {
		startProgressDialog("数据提交中...");
	}

	/**
	 * 结束进度条
	 */
	protected void stopProgressDialog() {
		ProgressDialog.getInstance().stopProgressDialog();
	}
	
	public View getView() {
		LayoutParams layoutParams = showview.getLayoutParams();
		if (layoutParams == null) {
			showview.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
		}
		return showview;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

}
