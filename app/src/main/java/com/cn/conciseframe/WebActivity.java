package com.cn.conciseframe;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cn.conciseframe.util.Logger;
import com.cn.conciseframe.util.Utils;

import java.io.File;

/**
 * Created by tangzy on 2016/8/3.
 */
public class WebActivity extends Activity{

    private static final String TAG = "WebActivity";
    private WebView mWebView;
    private WebSettings mWebSettings;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileName = getIntent().getStringExtra("path");

        mWebView = new WebView(this);
        setContentView(mWebView);

        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setSupportZoom(false);
        // 设置支持JavaScript等
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());



//      String strPath = "file:///mnt"+Constant.path+"/"+fileName+"/index.html";

//        String strPath = "file:///mnt/sdcard/"+fileName+"/index.html";

//      String strPath = "file://"+"/mnt/sdcard/ConciseFrame/"+fileName+"/index.html";

      String strPath = "file:///"+this.getFilesDir().getAbsolutePath()+ "/"+fileName+"/index.html";
         Logger.d(TAG, "f.exists() = "+new File(strPath).exists());
        Logger.d(TAG, "html = "+strPath);

        mWebView.loadUrl(strPath);
//        mWebView.loadUrl("file:///android_asset/new_file.html");


//        mWebView.loadUrl("file:///android_asset/charge_count.html");
//        mWebView.loadUrl("file:///android_asset/index.html");

        mWebView.addJavascriptInterface(new MyContact(), "ui");




    }
    private final class MyContact {
        @JavascriptInterface
        public void goback(String index)
        {
            Log.d("11111111",index);
        }

        @JavascriptInterface
        public void goback()
        {
//            goback();
            finish();
        }

        @JavascriptInterface
        public void getPictuer(){
//            dsdsfa
        }


    }

    class MyWebViewClient extends WebViewClient {
        public MyWebViewClient() {
            super();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
        }
    }

}
