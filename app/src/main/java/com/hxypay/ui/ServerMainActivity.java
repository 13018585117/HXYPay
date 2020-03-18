package com.hxypay.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.ServerListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.response.ServerRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;

public class ServerMainActivity extends BaseActivity {

    private List<ServerRes.Info> mData = new ArrayList<ServerRes.Info>();
    private ListView mListView;
    private ServerListAdapter mAdapter;
    private static final int MY_PERMISSION_REQUEST_CALL_PHONE = 0;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_main);

        initView();
        initData();
    }

    private void initView() {
        initHeadView(this,R.drawable.icon_back_r,"客服服务",0,null);
        mListView = (ListView)findViewById(R.id.list_data);
        mAdapter = new ServerListAdapter(context,mData);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mData.get(position).getType().equals("1")){
                    if (checkApkExist(context, "com.tencent.mobileqq")){
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+mData.get(position).getInformation()+"&version=1")));
                    }else{
                        IToast.getIToast().showIToast("手机未安装QQ应用");
                    }
                }
                if (mData.get(position).getType().equals("2")){
                    mIntent = new Intent(context,ServerActivity.class);
                    mIntent.putExtra("wxNo",mData.get(position).getInformation());
                    startActivity(mIntent);
                }
                if (mData.get(position).getType().equals("3")){
                    phone = mData.get(position).getInformation();
                    if (ContextCompat.checkSelfPermission(ServerMainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ServerMainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSION_REQUEST_CALL_PHONE);
                    } else {
                        callPhone();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                //Permission Denied
                IToast.getIToast().showIToast("请手动打开拨打电话权限");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callPhone(){
        mIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(mIntent);
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void initData(){
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.SERVER)
                .headers(PublicParam.getParam())
                .params(Params.server())
                .build().execute(new GenericsCallback<ServerRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(ServerRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mData.clear();
                        mData = response.getData();
                        mAdapter.setData(mData);
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }



}
