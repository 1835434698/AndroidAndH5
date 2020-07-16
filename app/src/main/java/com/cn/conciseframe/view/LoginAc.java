package com.cn.conciseframe.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.conciseframe.Constant;
import com.cn.conciseframe.MyThreadPoolExecutor;
import com.cn.conciseframe.R;
import com.cn.conciseframe.bean.Light;
import com.cn.conciseframe.bean.State;
import com.cn.conciseframe.customView.CustomDialog;
import com.cn.conciseframe.manager.DialogManager;
import com.cn.conciseframe.net.NetUtil;
import com.cn.conciseframe.net.OkHttpManager;
import com.cn.conciseframe.manager.BaseView;
import com.cn.conciseframe.manager.BottomMenu;
import com.cn.conciseframe.manager.MiddleView;
import com.cn.conciseframe.manager.TopMenu;
import com.cn.conciseframe.util.Logger;
import com.cn.conciseframe.util.Toasts;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by tangzy on 16/6/5.
 */
public class LoginAc extends BaseView implements View.OnClickListener {
    private static final String TAG = "LoginAc";
    private EditText et_login_id;
    private EditText et_login_password;
    private TextView tv_forget_password;
    private TextView tv_login_register;
    private TextView tv_login;
    private String userName;
    private String userPassword;

    public LoginAc(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    protected void init() {
        showview = (ViewGroup) View.inflate(context, R.layout.ac_login,null);
        et_login_id = (EditText) showview.findViewById(R.id.et_login_id);
        et_login_password = (EditText) showview.findViewById(R.id.et_login_password);
        tv_login = (TextView) showview.findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
        CustomDialog ustomDialog =  new CustomDialog(context);
        Class<? extends CustomDialog> aClass = ustomDialog.getClass();
        Field[] fields = ustomDialog.getClass().getFields();
        Field[] declaredFields = aClass.getDeclaredFields();
        tv_forget_password = (TextView) showview.findViewById(R.id.tv_forget_password);
        tv_forget_password.setOnClickListener(this);
        tv_login_register = (TextView) showview.findViewById(R.id.tv_login_register);
        tv_login_register.setOnClickListener(this);

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        TopMenu.getInstance().setTitleLeftView(false);
        TopMenu.getInstance().setTitle(context.getResources().getString(R.string.login));
        BottomMenu.getInstance().setViewVisibility(false);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onDestory() {
        return super.onDestory();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_login:

                if (netCheck()&&textCheck()){
                    startProgressDialog();
                    final Map<String, String> httpParams =  new HashMap<>();
                    httpParams.put("userName",userName);
                    httpParams.put("userPassword",userPassword);
//                    httpParams.put("devicecode", "343257687798790");
//                    final String url = Constant.url+"login";
//                    final String url = "http://www.baidu.com";

                    OkHttpManager.asyncRequest("login", httpParams,listener,false);
//                    OkHttpManager.asyncRequest("mobileLogin.php", httpParams,listener,false);

//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            AsyncOkHttpManager.asyncHttpRequest(url ,httpParams, false);
//                        }
//                    }).start();

//
//                    ResponseResult responseResult =null;
//                    try {
//                         responseResult = AsyncOkHttpManager.asyncRequest(url ,httpParams, false);
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
////
//                    String status = responseResult.getStatus();
//                    Response response = responseResult.getResponse();
//
//
//
//                    stopProgressDialog();
//                    MiddleView.getInstance().startCleanActivity(FirstPageAc.class, null);

                }


            break;
            case R.id.tv_forget_password:
                MiddleView.getInstance().startCallbackActivity(ForgetAc.class, null);

                break;
            case R.id.tv_login_register:
                MiddleView.getInstance().startCallbackActivity(RegisterAc.class, null);

            default:
                break;
        }
    }

    private OkHttpManager.ResponseListener listener = new OkHttpManager.ResponseListener() {
        @Override
        public void onResp(String respons, String uri) {
            Logger.d(TAG, "onResp = "+respons);
            stopProgressDialog();
            JSONObject json = null;
            try {
                json = new JSONObject(respons);
                Toasts.showToast(json.optString("retMessage"), Toast.LENGTH_SHORT);
                if (json.optString("retCode").equals("000000")){
                    MiddleView.getInstance().startCleanActivity(FirstPageAc.class, null);

                }
            } catch (JSONException e) {
                Toasts.showToast("返回数据格式不正确", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }
        }

        @Override
        public void onErr(String respons, String uri) {
            stopProgressDialog();
            Logger.d(TAG, "onErr = "+respons);
            Toasts.showToast("网络地址错误", Toast.LENGTH_SHORT);
//            MiddleView.getInstance().startCleanActivity(FirstPageAc.class, null);

        }
    };


    private boolean textCheck(){
        userName = et_login_id.getText().toString().trim();
        userPassword = et_login_password.getText().toString().trim();
        if (userName.equals("")) {
            DialogManager.showErrorDialog(context, "用户名不能为空");
            return false;
        }
        if (userPassword.equals("")) {
            DialogManager.showErrorDialog(context, "密码不能为空");
            return false;
        }
        return true;
    }


    private boolean netCheck() {
        if (!NetUtil.checkNetType(context)) {
            DialogManager.showErrorDialog(context, "请打开手机网络连接");
            return false;
        }
        return true;
    }


}
