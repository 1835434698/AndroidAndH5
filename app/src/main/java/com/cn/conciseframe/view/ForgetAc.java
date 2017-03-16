package com.cn.conciseframe.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.conciseframe.R;
import com.cn.conciseframe.manager.BaseView;
import com.cn.conciseframe.manager.BottomMenu;
import com.cn.conciseframe.manager.TopMenu;


/**
 * Created by tangzy on 16/6/5.
 */
public class ForgetAc extends BaseView implements View.OnClickListener {

    private EditText et_forget_id;
    private EditText et_forget_code;
    private EditText et_forget_password;
    private EditText et_forget_con_ps;
    private TextView tv_send_code;
    private TextView tv_forget;

    public ForgetAc(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    protected void init() {
        showview = (ViewGroup) View.inflate(context, R.layout.ac_forget,null);
        et_forget_id = (EditText) showview.findViewById(R.id.et_forget_id);
        et_forget_code = (EditText) showview.findViewById(R.id.et_forget_code);
        et_forget_password = (EditText) showview.findViewById(R.id.et_forget_password);
        et_forget_con_ps = (EditText) showview.findViewById(R.id.et_forget_con_ps);
        tv_send_code = (TextView) showview.findViewById(R.id.tv_send_code);
        tv_forget = (TextView) showview.findViewById(R.id.tv_forget);


    }

    @Override
    protected void setListener() {
        tv_send_code.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        TopMenu.getInstance().setTitleLeftView(true);
        TopMenu.getInstance().setTitle(context.getResources().getString(R.string.forget));
        BottomMenu.getInstance().setViewVisibility(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_send_code:
                break;
            case R.id.tv_forget:

            default:
                break;
        }

    }
}
