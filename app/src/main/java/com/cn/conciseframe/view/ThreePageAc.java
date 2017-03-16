package com.cn.conciseframe.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cn.conciseframe.Constant;
import com.cn.conciseframe.ListView.PullToRefreshLayout;
import com.cn.conciseframe.R;
import com.cn.conciseframe.adapter.CommonAdapter;
import com.cn.conciseframe.adapter.ViewHolder;
import com.cn.conciseframe.bean.NotificationBean;
import com.cn.conciseframe.customView.CustomDialog;
import com.cn.conciseframe.manager.BaseView;
import com.cn.conciseframe.manager.BottomMenu;
import com.cn.conciseframe.manager.MiddleView;
import com.cn.conciseframe.manager.TopMenu;
import com.cn.conciseframe.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangzy on 16/6/26.
 */
public class ThreePageAc extends BaseView implements PullToRefreshLayout.OnRefreshListener{

    private static final String TAG = "ThreePageAc";
    private ListView listView;
    private PullToRefreshLayout layout;
    private CommonAdapter<NotificationBean> adapter;
    private List<NotificationBean> listShow ;

    public ThreePageAc(Context context, Bundle bundle) {
        super(context, bundle);
    }


    @Override
    protected void init() {
        showview = (ViewGroup) View.inflate(context, R.layout.test_listview, null);
        listShow = new ArrayList<NotificationBean>();
        layout = (PullToRefreshLayout) showview.findViewById(R.id.refresh_view);
        layout.setOnRefreshListener(this);
        listView = (ListView) showview.findViewById(R.id.content_view);
        initListView();
        layout.setLoadNoMore(true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initListView() {
        adapter = new CommonAdapter<NotificationBean>(context, listShow, R.layout.item_notification) {
            @Override
            public void convert(ViewHolder helper, NotificationBean item) {
                helper.setText(R.id.tv_endLocation, item.getEndLocation());


                helper.setOnClick(R.id.tv_sure, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CustomDialog cd = new CustomDialog(Constant.context, R.style.driver_customDialog, new CustomDialog.OKListener() {
                            @Override
                            public void onSuccess() {

                            }
                        });

                        cd.show();


                    }
                });
            }

        };
        listView.setAdapter(adapter);
        NotificationBean bean = new NotificationBean();
        for (int i = 0; i < 20; i++) {
            bean = new NotificationBean();
            bean.setID("A20160316091"+i);
            bean.setEndLocation("逸林希尔顿酒店");
            try {
                listShow.add(bean);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void setListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        TopMenu.getInstance().setTitleLeftView(false);
        TopMenu.getInstance().setTitle(context.getResources().getString(R.string.threePage));
        BottomMenu.getInstance().setViewVisibility(true);
        BottomMenu.getInstance().setBottomView(BottomMenu.THREEPAGE);

    }



    @Override
    public void onRefresh() {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                layout.onRefreshFinish();
            }
        }.sendEmptyMessageDelayed(0, 2 * 1000);
    }

    @Override
    public void onMore() {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                layout.onLoadmoreFinish();
            }
        }.sendEmptyMessageDelayed(0, 2 * 1000);

    }

}
