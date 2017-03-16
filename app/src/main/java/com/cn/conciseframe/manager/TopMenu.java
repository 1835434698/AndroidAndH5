package com.cn.conciseframe.manager;


import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.conciseframe.R;

public class TopMenu implements View.OnClickListener {
	private Activity activity;
	private LinearLayout ll_top;
	
	//左边点击按钮
	private TextView title_back;
//	//右边店家按钮
	private TextView title_right;
	//中间显示标题
	private TextView title_text;
//	
	private static TopMenu topMenu = new TopMenu();
	private TopMenu(){}
	public static TopMenu getInstance(){
		return topMenu;
	}
	
	public void init(Activity activity){
		this.activity = activity;
		ll_top = (LinearLayout) activity.findViewById(R.id.ll_top);
		title_back = (TextView) activity.findViewById(R.id.title_back);
		title_right = (TextView) activity.findViewById(R.id.title_right);
		title_text = (TextView) activity.findViewById(R.id.title_text);
		title_back.setOnClickListener(this);
		title_right.setOnClickListener(this);

	}

	public void setTitle(String title){
		title_text.setText(title);
	}

	public void setTitleLeftView(boolean b){
		if (b){
			title_back.setVisibility(View.VISIBLE);
		}else title_back.setVisibility(View.GONE);
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.title_back:
				if(MiddleView.getInstance().getCurrentView().goBack()){
					MiddleView.getInstance().goBack();
				}
				break;

			case R.id.title_right:
				MiddleView.getInstance().getCurrentView().rightOnClick();
				break;
		}

	}
}
