package com.cn.conciseframe.customView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cn.conciseframe.R;

/**
 * Created by tangzy on 16/6/26.
 */
public class CustomDialog extends Dialog{
    private OKListener listener;
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomDialog(Context context, int theme, OKListener listener) {
        super(context, theme);
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custoum_dialog);
        TextView bt_finish = (TextView) findViewById(R.id.bt_finish);
        bt_finish.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
                listener.onSuccess();
            }
        });

    }

    public interface OKListener {
        void onSuccess();

    }
}
