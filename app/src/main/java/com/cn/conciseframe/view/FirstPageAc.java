package com.cn.conciseframe.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cn.conciseframe.Constant;
import com.cn.conciseframe.R;
import com.cn.conciseframe.adapter.CommonAdapter;
import com.cn.conciseframe.adapter.ViewHolder;
import com.cn.conciseframe.bean.NotificationBean;
import com.cn.conciseframe.customView.CustomDialog;
import com.cn.conciseframe.manager.BaseView;
import com.cn.conciseframe.manager.BottomMenu;
import com.cn.conciseframe.manager.MiddleView;
import com.cn.conciseframe.manager.TopMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangzy on 16/6/26.
 */
public class FirstPageAc extends BaseView{

    private static final String TAG = "FirstPageAc";

    public FirstPageAc(Context context, Bundle bundle) {
        super(context, bundle);
    }


    @Override
    protected void init() {
        showview = (ViewGroup) View.inflate(context, R.layout.ac_first_page, null);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        TopMenu.getInstance().setTitleLeftView(false);
        TopMenu.getInstance().setTitle(context.getResources().getString(R.string.firstPage));
        BottomMenu.getInstance().setViewVisibility(true);
        BottomMenu.getInstance().setBottomView(BottomMenu.FIRSTPAGE);




    }
}
