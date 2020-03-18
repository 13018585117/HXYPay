package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.BuildConfig;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.response.PublicRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

public class SettingActivity extends BaseActivity {

    private LinearLayout mUpdatePayPwdLayout,mUpdateLoginPwdLayout,update_about_we_layout,update_version_layout,exit_we_layout,feedback_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        initHeadView(this,R.drawable.icon_back_r,"系统设置",0,null);
        mUpdatePayPwdLayout = (LinearLayout)findViewById(R.id.update_pay_pwd_layout);
        mUpdateLoginPwdLayout = (LinearLayout)findViewById(R.id.update_login_pwd_layout);
        update_version_layout = (LinearLayout)findViewById(R.id.update_version_layout);
        update_about_we_layout = (LinearLayout)findViewById(R.id.update_about_we_layout);
        feedback_layout = (LinearLayout)findViewById(R.id.feedback_layout);
        exit_we_layout = (LinearLayout)findViewById(R.id.exit_we_layout);
        TextView tv_version = findViewById(R.id.tv_version);
        tv_version.setText("V "+BuildConfig.VERSION_NAME);
        mUpdatePayPwdLayout.setOnClickListener(this);
        mUpdateLoginPwdLayout.setOnClickListener(this);
        update_version_layout.setOnClickListener(this);
        update_about_we_layout.setOnClickListener(this);
        exit_we_layout.setOnClickListener(this);
        feedback_layout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.update_pay_pwd_layout:
                mIntent = new Intent(context,UpdatePayPwdActivity.class);
                startActivity(mIntent);
                break;
            case R.id.update_login_pwd_layout:
                mIntent = new Intent(context,UpdatePwdActivity.class);
                startActivity(mIntent);
                break;
            case R.id.update_version_layout:
                IToast.getIToast().showIToast("当前版本为V "+BuildConfig.VERSION_NAME);
                break;

            case R.id.update_about_we_layout:
                mIntent = new Intent(context,AboutUsActivity.class);
                startActivity(mIntent);
                break;

            case R.id.feedback_layout:
                mIntent = new Intent(context,FeedBackActivity.class);
                startActivity(mIntent);
                break;

            case R.id.exit_we_layout:
                exit();
                break;
        }
    }

    private void exit() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.EXIT)
                .headers(PublicParam.getParam())
                .params(Params.exitParams())
                .build().execute(new GenericsCallback<PublicRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(PublicRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mPs.remove(Constants.ACCOUNT_PWD);
                        mIntent = new Intent(context, LoginActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(mIntent);
                        finish();
                        System.exit(0);
                    } else {
                        IToast.getIToast().showIToast("请重新退出");
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("请重新退出");
                }
            }
        });
    }
}
