package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.response.PublicRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

public class UpdatePhoneActivity extends BaseActivity {
    private TextView mTeleText;
    private EditText mAuthEt;
    private Button mSmsCodeBtn, mComBtn;

    private String tele, auth;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.AUTH_CODE_S:
                    mSmsCodeBtn.setEnabled(false);
                    break;
                case 0:
                    mSmsCodeBtn.setText("获取验证码");
                    mSmsCodeBtn.setEnabled(true);
                    break;
                default:
                    mSmsCodeBtn.setText(msg.what + "s重新获取");
                    mSmsCodeBtn.setEnabled(false);
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_phone);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "更换手机号", 0,null);
        mTeleText = (TextView) findViewById(R.id.tele_tx);
        mAuthEt = (EditText) findViewById(R.id.auth_et);

        tele = mPs.getStringValue(Constants.ACCOUNT_NAME);
        mTeleText.setText(tele);

        mSmsCodeBtn = (Button) findViewById(R.id.btn_sms_code);
        mComBtn = (Button) findViewById(R.id.btn_com);

        mSmsCodeBtn.setOnClickListener(this);
        mComBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_sms_code:
                tele = mTeleText.getText().toString();
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号");
                    return;
                }
                getSmsCode();
                break;
            case R.id.btn_com:
                tele = mTeleText.getText().toString();
                auth = mAuthEt.getText().toString();
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(auth)) {
                    IToast.getIToast().showIToast("请输入验证码");
                    return;
                }
                updatePhone();
                break;
            default:
                break;
        }
    }

    public void getSmsCode() {
        showDialog("加载中...");
        count();
        OkHttpUtils.post()
                .url(Constants.SMS_CODE)
                .headers(PublicParam.getParam())
                .params(Params.smsCodeParams(tele))
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
                        IToast.getIToast().showIToast("短信验证码已发送至：" + tele);
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

    public void updatePhone() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.UPDATE_PHONE)
                .headers(PublicParam.getParam())
                .params(Params.updatePhoneParams("1", auth, tele))
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
                        mIntent = new Intent(context, UpdatePhoneOKActivity.class);
                        startActivity(mIntent);
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

    private void count() {
        mSmsCodeBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = Constants.AUTH_CODE_S; i >= 0; i--) {
                    handler.sendMessage(handler.obtainMessage(i));
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}