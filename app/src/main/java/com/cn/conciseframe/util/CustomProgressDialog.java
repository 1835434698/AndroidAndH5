package com.cn.conciseframe.util;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.conciseframe.R;

public class CustomProgressDialog extends Dialog {
	private Context context = null;
	private static CustomProgressDialog customProgressDialog = null;
	private static ImageView loadingImageView;
	// 均匀旋转动画
	private static RotateAnimation refreshingAnimation;
	
	public CustomProgressDialog(Context context){
		super(context);
		this.context = context;
		setCancelable(false);
	}
	
	public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	
	public static CustomProgressDialog createDialog(Context context){
		customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.customprogressdialog);
		loadingImageView = (ImageView)customProgressDialog.findViewById(R.id.loadingImageView);
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.progress_rotating);
		// 添加匀速转动动画
		LinearInterpolator lir = new LinearInterpolator();
		refreshingAnimation.setInterpolator(lir);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		
		return customProgressDialog;
	}

	@Override
	public void show() {
		loadingImageView.startAnimation(refreshingAnimation);
		super.show();
	}

	@Override
	public void dismiss() {
		loadingImageView.clearAnimation();
		super.dismiss();
	}

//	public void onWindowFocusChanged(boolean hasFocus){
//
//    	if (customProgressDialog == null){
//    		return;
//    	}
//
//        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
//        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
//        animationDrawable.start();
//    }
 
    /**
     * 
     * [Summary]
     *       setTitile 标题
     * @param strTitle
     * @return
     *
     */
    public CustomProgressDialog setTitile(String strTitle){
    	return customProgressDialog;
    }
    
    /**
     * 
     * [Summary]
     *       setMessage 提示内容
     * @param strMessage
     * @return
     *
     */
    public CustomProgressDialog setMessage(String strMessage){
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
    	
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	
    	return customProgressDialog;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	return false;
    }
    
    @Override
    public void onBackPressed() {
    	
    }
    
}