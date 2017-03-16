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
public class RegisterAc extends BaseView implements View.OnClickListener {

    private EditText et_register_id;
    private EditText et_register_code;
    private TextView tv_send_code;
    private EditText et_register_password;
    private EditText et_register_con_ps;
    private TextView tv_register;

    public RegisterAc(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    protected void init() {
        showview = (ViewGroup) View.inflate(context, R.layout.ac_register,null);
        et_register_id = (EditText) showview.findViewById(R.id.et_register_id);
        et_register_code = (EditText) showview.findViewById(R.id.et_register_code);
        tv_send_code = (TextView) showview.findViewById(R.id.tv_send_code);
        tv_send_code.setOnClickListener(this);
        tv_register = (TextView) showview.findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        et_register_password = (EditText) showview.findViewById(R.id.et_register_password);
        et_register_con_ps = (EditText) showview.findViewById(R.id.et_register_con_ps);


    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onResume() {
        super.onResume();

        TopMenu.getInstance().setTitleLeftView(true);
        TopMenu.getInstance().setTitle(context.getResources().getString(R.string.register));
        BottomMenu.getInstance().setViewVisibility(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_send_code:
                break;
            case R.id.tv_register:
                default:
                break;
        }

    }
}
