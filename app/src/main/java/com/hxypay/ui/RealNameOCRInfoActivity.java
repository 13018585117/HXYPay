package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.customview.KeyPop1;
import com.hxypay.response.PublicRes;
import com.hxypay.tab.OTabActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

public class RealNameOCRInfoActivity extends BaseActivity {

    private EditText mIdNameEt, mIdNoEt, mBankNoEt;
    private EditText mTeleEt, mAuthEt;
    private TextView mPayPwdText;//支付密码
    private Button mSmsCodeBtn, mComBtn;

    private String idName, idNo, idFontUrl, idBackUrl, cardNo, bankUrl, tele, auth, payPwd;

    private KeyPop1 keyPop1;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_ocr_info);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "实名认证", 0,null);
        mIdNameEt = (EditText) findViewById(R.id.id_name_et);
        mIdNoEt = (EditText) findViewById(R.id.id_no_et);
        mBankNoEt = (EditText) findViewById(R.id.bank_no_et);
        mTeleEt = (EditText) findViewById(R.id.tele_et);
        mAuthEt = (EditText) findViewById(R.id.auth_et);
        mPayPwdText = (TextView) findViewById(R.id.pay_pwd_text);


        idName = getIntent().getExtras().getString("idName");
        idNo = getIntent().getExtras().getString("idNo");
        idFontUrl = getIntent().getExtras().getString("idFontUrl");
        idBackUrl = getIntent().getExtras().getString("idBackUrl");
        cardNo = getIntent().getExtras().getString("cardNo");
        bankUrl = getIntent().getExtras().getString("bankUrl");

        mIdNameEt.setText(idName);
        mIdNoEt.setText(idNo);

        mBankNoEt.setText(cardNo);



        mSmsCodeBtn = (Button) findViewById(R.id.btn_sms_code);
        mComBtn = (Button) findViewById(R.id.btn_com);

        mPayPwdText.setOnClickListener(this);
        mSmsCodeBtn.setOnClickListener(this);
        mComBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pay_pwd_text:
                keyPop1 = new KeyPop1(context, new KeyPop1.KeyPopListener() {
                    @Override
                    public void onClick(String pwd) {
                        keyPop1.dismiss();
                        payPwd = pwd;
                        mPayPwdText.setText("******");
                    }

                    @Override
                    public void diss() {
                        keyPop1.dismiss();
                    }
                });
                keyPop1.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_sms_code:
                tele = mTeleEt.getText().toString();
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号");
                    return;
                }
                getSmsCode();
                break;
            case R.id.btn_com:
                idName = mIdNameEt.getText().toString();
                idNo = mIdNoEt.getText().toString();
                cardNo = mBankNoEt.getText().toString();
                tele = mTeleEt.getText().toString();
                auth = mAuthEt.getText().toString();
                if (TextUtils.isEmpty(idName)) {
                    IToast.getIToast().showIToast("请输入持卡人身份证姓名");
                    return;
                }
                if (TextUtils.isEmpty(idNo)) {
                    IToast.getIToast().showIToast("请输入持卡人身份证号码");
                    return;
                }
                if (TextUtils.isEmpty(cardNo)) {
                    IToast.getIToast().showIToast("请输入储蓄卡号");
                    return;
                }
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号码");
                    return;
                }
                if (TextUtils.isEmpty(auth)) {
                    IToast.getIToast().showIToast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(payPwd)) {
                    IToast.getIToast().showIToast("请设置支付密码");
                    return;
                }
                realName();
                break;
        }
    }

    private void realName() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.REAL_NAME_AUTH)
                .headers(PublicParam.getParam())
                .params(Params.merRealNameAuthParams(tele, auth, idName, idNo, cardNo, bankUrl, idFontUrl, idBackUrl, payPwd, "1"))
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
                        IToast.getIToast().showIToast("认证成功");
                        mIntent = new Intent(context,OTabActivity.class);
                        startActivity(mIntent);
                        OTabActivity.mainAct.mTabHost.setCurrentTab(4);
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
