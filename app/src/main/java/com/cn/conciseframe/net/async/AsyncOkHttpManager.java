package com.cn.conciseframe.net.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.cn.conciseframe.net.async.bean.ResponseResult;

import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by tangzy on 2016/7/6.
 * this AsyncOkHttpManager 工具已经做了异步返回，可以直接在调用的地方等待返回不用启用handles
 */
public class AsyncOkHttpManager {
    private static final String TAG = "AsyncOkHttpManager";

    private static final int MSG_EVENT_GET = 0x2;

    private static Map<String, Handler> handleMap = new HashMap<String, Handler>();

    public static ResponseResult asyncRequest(final String url, final Map<String, String> httpParams, final boolean isPost){
        final ResponseResult result = new ResponseResult();

        if (handleMap.containsKey(url)) {
            result.setStatus(ResponseResult.STATUS_REFUSE);
            result.setErrorMsg(ResponseResult.MSG_REFUSE);
            return result;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Handler responseHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Map<String, Handler> map = handleMap;
                        switch (msg.what){
                            case MSG_EVENT_GET:  // 处理更新事件
                                Log.d(TAG, " MSG_EVENT_GET: { // 处理更新事件");
                                if (msg.obj != null){
                                    result.setStatus(ResponseResult.STATUS_OK);
                                    result.setResponse((Response) msg.obj);
                                }else {
                                    result.setStatus(ResponseResult.STATUS_NET_ERROR);
                                    result.setErrorMsg(ResponseResult.MSG_NET_ERROR);
                                }
                                getLooper().quit();
                                result.setOperationed(true);
                                synchronized (map) {
                                    map.remove(url);
                                }

                            break;
                            default:
                                Log.d(TAG, " default:");
                                break;
                        }
                    }
                };
                synchronized (handleMap){
                    handleMap.put(url,responseHandler);
                }
                handleCpsEvent(asyncHttpRequest(url, httpParams, isPost));
                Looper.loop();
            }
        }).start();
        while (!result.isOperationed){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        return result;
    }

    private static void handleCpsEvent(Response response) {
        Map<String, Handler> obj = handleMap;
        synchronized (obj) {
            int size = obj.size();
            Log.d(TAG, "size = "+size);
            if (size == 0) {
                Log.d(TAG, "send event no handler to handle event!!");
                return;
            }
            Iterator<Handler> it = obj.values().iterator();
            while (it.hasNext()) {
                Log.d(TAG, "-------while-------");
                it.next().obtainMessage(MSG_EVENT_GET, response).sendToTarget();
            }
        }
    }

    public static Response asyncHttpRequest(String url, Map<String, String> httpParams, final boolean isPost){
       try {
           OkHttpClient okHttpClient = new OkHttpClient();
           //处理证书，暂时忽略证书验证
//           SSLContext sc = SSLContext.getInstance("TLS");
//           sc.init(null, new TrustManager[]{tm}, new SecureRandom());
//           okHttpClient.setSslSocketFactory(sc.getSocketFactory());
//           okHttpClient.setHostnameVerifier(hostName);

           Request request = null;
           if (isPost){
               FormBody.Builder builder = new  FormBody.Builder();
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
           }else {
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
           }
           return okHttpClient.newCall(request).execute();
       }catch (Exception e){
           Log.e(TAG,e+"");
       }
        return null;
    }


    private static TrustManager tm = new X509TrustManager() {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                       String authType) throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                       String authType) throws java.security.cert.CertificateException {
        }
    };

    private static HostnameVerifier hostName = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }
    };


}
