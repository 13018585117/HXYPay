package com.hxypay.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.response.RouletteRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText et_naem;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_content)
    EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initHeadView(this,R.drawable.icon_back_r,"意见反馈",0,null);

    }
    @OnClick(R.id.but_submit)
    public void Submit(){
        String name = et_naem.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String content = et_content.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(context, "称呼不能为空", Toast.LENGTH_SHORT).show();
            et_naem.requestFocus();
            return;
        }if (TextUtils.isEmpty(phone)){
            Toast.makeText(context, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            et_phone.requestFocus();
            return;
        }if (TextUtils.isEmpty(content)){
            Toast.makeText(context, "反馈内容不能为空", Toast.LENGTH_SHORT).show();
            et_content.requestFocus();
            return;
        }
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.FEEDBACK)
                .headers(PublicParam.getParam())
                .params(Params.FeedBackParams(name,phone,content))
                .build().execute(new GenericsCallback<RouletteRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(RouletteRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        IToast.getIToast().showIToast(response.getMsg());
                        finish();
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
