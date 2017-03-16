package com.cn.conciseframe.manager;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cn.conciseframe.R;
import com.cn.conciseframe.view.FirstPageAc;
import com.cn.conciseframe.view.ThreePageAc;
import com.cn.conciseframe.view.TwoPageAc;

public class BottomMenu implements View.OnClickListener {
	private Activity activity;

	private LinearLayout ll_bottom;
	private TextView bottom_notice;
	private TextView bottom_notice_un;
	private TextView bottom_trip;
	private TextView bottom_trip_un;
	private TextView bottom_center;
	private TextView bottom_center_un;

	public static int FIRSTPAGE = 0;
	public static int TWOPAGE = 1;
	public static int THREEPAGE = 2;

	private static BottomMenu bottomMenu = new BottomMenu();
	private BottomMenu(){}
	public static BottomMenu getInstance(){
		return bottomMenu;
	}

	public void init(Activity activity){
		this.activity = activity;
		ll_bottom = (LinearLayout) activity.findViewById(R.id.ll_bottom);
		bottom_notice = (TextView) activity.findViewById(R.id.bottom_notice);
		bottom_notice_un = (TextView) activity.findViewById(R.id.bottom_notice_un);
		bottom_trip = (TextView) activity.findViewById(R.id.bottom_trip);
		bottom_trip_un = (TextView) activity.findViewById(R.id.bottom_trip_un);
		bottom_center = (TextView) activity.findViewById(R.id.bottom_center);
		bottom_center_un = (TextView) activity.findViewById(R.id.bottom_center_un);
		bottom_notice.setOnClickListener(this);
		bottom_notice_un.setOnClickListener(this);
		bottom_trip.setOnClickListener(this);
		bottom_trip_un.setOnClickListener(this);
		bottom_center.setOnClickListener(this);
		bottom_center_un.setOnClickListener(this);
	}

	public void setViewVisibility(boolean b) {
		if (b) {
			ll_bottom.setVisibility(View.VISIBLE);
		}else {
			ll_bottom.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置底部显示状态
	 * @param bottomView 0 firstPage 1 TwoPage 2 ThreePage
     */
	public void setBottomView(int bottomView) {
		switch (bottomView){
			case 0:
				bottom_notice.setVisibility(View.VISIBLE);
				bottom_notice_un.setVisibility(View.GONE);

				bottom_trip_un.setVisibility(View.VISIBLE);
				bottom_trip.setVisibility(View.GONE);

				bottom_center_un.setVisibility(View.VISIBLE);
				bottom_center.setVisibility(View.GONE);
				break;
			case 1:
				bottom_notice.setVisibility(View.GONE);
				bottom_notice_un.setVisibility(View.VISIBLE);

				bottom_trip_un.setVisibility(View.GONE);
				bottom_trip.setVisibility(View.VISIBLE);

				bottom_center_un.setVisibility(View.VISIBLE);
				bottom_center.setVisibility(View.GONE);
				break;
			case 2:
				bottom_notice.setVisibility(View.GONE);
				bottom_notice_un.setVisibility(View.VISIBLE);

				bottom_trip_un.setVisibility(View.VISIBLE);
				bottom_trip.setVisibility(View.GONE);

				bottom_center_un.setVisibility(View.GONE);
				bottom_center.setVisibility(View.VISIBLE);
				break;
		}
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.bottom_notice:
			case R.id.bottom_notice_un:
				MiddleView.getInstance().startFromAssignedActivity(FirstPageAc.class,null,FirstPageAc.class);
				break;

			case R.id.bottom_trip:
			case R.id.bottom_trip_un:
				MiddleView.getInstance().startFromAssignedActivity(TwoPageAc.class,null,FirstPageAc.class);


				break;
			case R.id.bottom_center:
			case R.id.bottom_center_un:
				MiddleView.getInstance().startFromAssignedActivity(ThreePageAc.class,null,FirstPageAc.class);

				break;
		}

	}
}
