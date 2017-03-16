package com.cn.conciseframe;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.cn.conciseframe.manager.DialogManager;
import com.cn.conciseframe.manager.MainManager;
import com.cn.conciseframe.manager.MiddleView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);

        MainManager.getInstance().init(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MiddleView.getInstance().getCurrentView().onResume();
    }

    @Override
    public void onBackPressed() {
        if(!MiddleView.getInstance().goBack())
            DialogManager.showExitSystem(this);
    }
}
