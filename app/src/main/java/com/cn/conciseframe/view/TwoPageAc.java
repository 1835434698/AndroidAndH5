package com.cn.conciseframe.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.cn.conciseframe.Constant;
import com.cn.conciseframe.R;
import com.cn.conciseframe.WebActivity;
import com.cn.conciseframe.customView.CustomDialogTwo;
import com.cn.conciseframe.manager.BaseView;
import com.cn.conciseframe.manager.BottomMenu;
import com.cn.conciseframe.manager.TopMenu;
import com.cn.conciseframe.net.FileOkHttp;
import com.cn.conciseframe.net.OkHttpManager;
import com.cn.conciseframe.util.FileUtils;
import com.cn.conciseframe.util.Logger;
import com.cn.conciseframe.util.Toasts;
import com.cn.conciseframe.util.UtilZip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.com.okhttp.filelibrary.listener.impl.UIProgressListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by tangzy on 16/7/3.
 */
public class TwoPageAc extends BaseView implements View.OnClickListener{


    private static final String TAG = "TwoPageAc";
    private CustomDialogTwo cd;
    private Button button;
    private GridView gridView;
    private BaseAdapter adapter;
    private static int length = 0;
    String path = Constant.path+"/";

    static int[] gvImageId = {R.drawable.iv_griview_item, R.drawable.iv_griview_item, R.drawable.iv_griview_item,
            R.drawable.iv_griview_item, R.drawable.iv_griview_item, R.drawable.iv_griview_item};
    static JSONArray gvNames = new JSONArray();

//            {"第一", "第二","第三", "第四","第五", "第六"};

    Handler handler = new Handler();

    public TwoPageAc(Context context, Bundle bundle) {
        super(context, bundle);
    }

    @Override
    protected void init() {
        showview = (ViewGroup) View.inflate(context, R.layout.ac_two_page, null);
        button = (Button) showview.findViewById(R.id.button);
        gridView = (GridView) showview.findViewById(R.id.gridView);
        adapter = new GridViewAdapter();
        gridView.setAdapter(adapter);



        cd = new CustomDialogTwo(context, R.style.driver_customDialog_two, "张先生", "18501942558", new CustomDialogTwo.OKTwoListener() {
            @Override
            public void onEnsure() {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }else {
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:18501942558")));
                    cd.dismiss();
                }
            }
        });
        Window window = cd.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = 0;
        params.y = -150;
        window.setAttributes(params);

    }

    @Override
    protected void setListener() {
        button.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        TopMenu.getInstance().setTitleLeftView(false);
        TopMenu.getInstance().setTitle(context.getResources().getString(R.string.twoPage));
        BottomMenu.getInstance().setViewVisibility(true);
        BottomMenu.getInstance().setBottomView(BottomMenu.TWOPAGE);

        getGridView();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                cd.show();
                break;
        }
    }

    public void getGridView() {

        Logger.d(TAG, "getGridView ");
        final Map<String, String> httpParams =  new HashMap<>();
        httpParams.put("userName","123456");
//        final String url = Constant.url+"gridView";
        OkHttpManager.asyncRequest("gridView", httpParams,listener, false);
//        OkHttpManager.asyncRequest("gridView.php", httpParams,listener, false);
    }


    private OkHttpManager.ResponseListener listener = new OkHttpManager.ResponseListener() {
        @Override
        public void onResp(String respons, String uri) {
            Logger.d(TAG, "onResp = "+respons);
            if (uri.equals("gridView")){
//            if (uri.equals("gridView.php")){
                JSONObject json = null;
                try {
                    json = new JSONObject(respons);
                    if (json.optString("retCode").equals("000000")){
    //                    gvNames = json.optJSONObject("gridview");
                        gvNames = json.optJSONArray("gridView");
                        length = gvNames.length();
                        adapter.notifyDataSetChanged();
                    }else
                        Toasts.showToast(json.optString("retMessage"), Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    Toasts.showToast("返回数据格式不正确", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }else if (uri.equals("getFile")){
                stopProgressDialog();

            }

        }

        @Override
        public void onErr(String respons, String uri) {
            stopProgressDialog();
            Logger.d(TAG, "onErr = " + respons);
        }
    };

    private class GridViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(context, R.layout.gridview_item, null);
            RelativeLayout gridView_item = (RelativeLayout) view.findViewById(R.id.gridView_item);
            ImageView iv_itemimage = (ImageView) view.findViewById(R.id.iv_userfragment_itemimage);
            TextView tv_itemname = (TextView) view.findViewById(R.id.tv_userfragment_itemname);
//            iv_itemimage.setImageResource(gvImageId[position]);
//            tv_itemname.setText(gvNames[position]);
            final JSONObject json = gvNames.optJSONObject(position);

            tv_itemname.setText(json.optString("text"));
            gridView_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(path+json.optString("name")+".zip");
                    if (!file.exists())
                        downLoad(json);
                    else {
                        File file1 = new File(path+json.optString("name")+"/index.html");
                        if (!file1.exists()){
                            try {
                                UtilZip.UnZipFolder(path+json.optString("name")+".zip", path);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Intent intent = new Intent(context, WebActivity.class);
                            intent.putExtra("path",json.optString("name"));
                            context.startActivity(intent);
                        }
                    }
                }
            });
            return view;
        }
    }

    private void downLoad(final JSONObject json) {

        Logger.d(TAG, "getFile ");
        startProgressDialog("下载中...");

        Map<String, String> httpParams =  new HashMap<>();
        httpParams.put("userName","123456");
        httpParams.put("filename",json.optString("name"));

        UIProgressListener uiProgressListener = new UIProgressListener() {
            @Override
            public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                Logger.d(TAG, "dome = "+done);

            }

            @Override
            public void onUIStart(long currentBytes, long contentLength, boolean done) {
                super.onUIStart(currentBytes, contentLength, done);
            }

            @Override
            public void onUIFinish(long currentBytes, long contentLength, boolean done) {
                super.onUIFinish(currentBytes, contentLength, done);
            }
        };

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.d(TAG, "onErr ");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressDialog();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.d(TAG, "onResp");
                try {
                    FileUtils.writeToFile(response.body().bytes(),Constant.path,json.optString("name")+".zip");
                    UtilZip.UnZipFolder(path+json.optString("name")+".zip", path);
                }catch (Exception e){
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressDialog();
                    }
                });
            }
        };

        FileOkHttp.fileDown("getFile",httpParams,uiProgressListener,callback);


    }

}

