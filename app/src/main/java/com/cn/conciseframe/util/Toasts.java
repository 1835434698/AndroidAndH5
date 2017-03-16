package com.cn.conciseframe.util;

import android.widget.Toast;

import com.cn.conciseframe.Constant;

/**
 * Created by tangzy on 2016/8/10.
 */
public class Toasts {
    public static void showToast(String msg, int time){
        Toast.makeText(Constant.context, msg, time).show();
    }
}
