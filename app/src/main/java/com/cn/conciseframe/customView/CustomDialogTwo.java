package com.cn.conciseframe.customView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.conciseframe.R;


/**
 * Created by tangzy on 16/6/26.
 * 拨打电话的提示框
 */
public class CustomDialogTwo extends Dialog implements View.OnClickListener {
    private TextView tv_name;
    private TextView tv_phone_number;
    private TextView tv_cancle;
    private TextView tv_dial_out;

    private String name;
    private String phoneNumber;

    private OKTwoListener listener;
    public CustomDialogTwo(Context context) {
        super(context);
    }

    public CustomDialogTwo(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomDialogTwo(Context context, int theme, String name, String phoneNumber,OKTwoListener listener) {
        super(context, theme);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custoum_dialog_two);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_dial_out = (TextView) findViewById(R.id.tv_dial_out);

        tv_dial_out.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        if (name != null)
            tv_name.setText(name);
        if (phoneNumber != null)
            tv_phone_number.setText(phoneNumber);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_dial_out:
                listener.onEnsure();
                break;
            case R.id.tv_cancle:
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface OKTwoListener {
        void onEnsure();
    }
}
