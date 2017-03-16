package com.cn.conciseframe;

import android.app.Application;


/**
 * Created by tangzy on 16/6/4.
 */
public class MainApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Constant.context = this;
        Constant.path = this.getFilesDir().getAbsolutePath();

        MyCrashHandler handler = MyCrashHandler.getMyCrashHandler();
        handler.init(getApplicationContext());
        Thread.currentThread().setUncaughtExceptionHandler(handler);
    }
    //    AutoLayoutConif
}
