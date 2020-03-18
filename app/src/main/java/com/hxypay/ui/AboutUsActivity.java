package com.hxypay.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.response.AboutUsRes;
import com.hxypay.response.PublicRes;
import com.hxypay.tab.OTabActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.tv_versionNumber)
    TextView tv_versionNumber;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        ButterKnife.bind(this);
        initHeadView(this,R.drawable.icon_back_r,"关于我们",0,null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_versionNumber.setText("版本号："+ OTabActivity.getLocalVersionName(this));
        getContet();
    }

    private void getContet() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.ABOUT_US)
                .headers(PublicParam.getParam())
                .params(Params.exitParams())
                .build().execute(new GenericsCallback<AboutUsRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(AboutUsRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (!TextUtils.isEmpty(response.getContent())) {
                        tv_content.setText(response.getContent());
                    }
                    if (!TextUtils.isEmpty(response.getTitle())) {
                        tv_title.setText(response.getTitle());
                    }
                }
            }
        });
    }

}
