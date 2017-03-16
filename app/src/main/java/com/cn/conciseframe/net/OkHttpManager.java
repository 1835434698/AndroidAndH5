package com.cn.conciseframe.net;

import android.os.Handler;
import android.util.Log;

import com.cn.conciseframe.Constant;
import com.cn.conciseframe.util.Logger;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tangzy on 2016/6/28.
 * this AsyncOkHttpManager 需要使用handles
 */
public class OkHttpManager {

    private static final String TAG = "OkHttpManager";
    private static String url;
    private static OkHttpClient  okHttpClient = new OkHttpClient();
    private static final Handler handler = new Handler();
    private static String strResult;

    private static PersistentCookieStore cookieStore;
    private static CookieJarImpl cookieJarImpl;
    public static void asyncRequest(final String uri, final Map<String, String> httpParams, final ResponseListener listener, final boolean isPost) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    url = Constant.url+uri;
                    if (cookieStore == null){
                        cookieStore = new PersistentCookieStore(Constant.context);

                    }
                    //创建一个RequestBody(参数1：数据类型 参数2传递的json串)

                    Request request = null;
                    if (isPost){
                        request = httpPost(httpParams, url);
                    }else {
                        request = httpGet(request, url);
                    }
                    cookieJarImpl = new CookieJarImpl(cookieStore);
                    okHttpClient = okHttpClient.newBuilder().cookieJar(cookieJarImpl).build();

                    //发送请求获取响应
                    final Response response = okHttpClient.newCall(request).execute();

                    if(response.isSuccessful()){
                        strResult=response.body().string();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onResp(strResult,uri);
                            }
                        });
                    }else{
                        strResult=response.body().string();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onErr(strResult,uri);
                            }
                        });
                    }
                } catch (Exception e){
                    Log.i(TAG, "error = " + e);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onErr("网络错误003",uri);
                        }
                    });
                }
            }

            private Request httpGet(Request request, String url) throws UnsupportedEncodingException {
                StringBuilder sb = new StringBuilder();
                if (httpParams != null && !httpParams.isEmpty()) {
                    Set<String> strings = httpParams.keySet();
                    for (String key : strings) {
                        sb.append(key);
                        if (httpParams.get(key) != null){
                            sb.append("=");
                            sb.append(URLEncoder.encode(httpParams.get(key), "UTF-8"));
                        }
                        sb.append("&");
                    }
                    if(url.indexOf("?") >= 0){
                        if(sb.length() > 0){
                            url = url + "&" + sb.substring(0, sb.length() - 1);
                        }
                    } else if(sb.length() > 0){
                        url = url + "?" + sb.substring(0, sb.length() - 1);
                    }
                    request = new Request.Builder().url(url).build();
                }
                return request;
            }
        };
        thread.start();
    }

    private static Request httpPost(Map<String, String> httpParams, String url) {
        Request request;
        FormBody.Builder  builder = new FormBody.Builder();
        if (httpParams != null && !httpParams.isEmpty()) {
            Set<String> strings = httpParams.keySet();
            for (String key : strings) {
                String value = httpParams.get(key);
                builder.add(key,value);
            }
        }
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
//               RequestBody requestBody = builder.build();
        request =  new Request.Builder().url(url).post(builder.build()).build();
        return request;
    }


    public static interface ResponseListener {
        public void onResp(String respons, String uri);

        public void onErr(String respons, String uri);
    }


}


