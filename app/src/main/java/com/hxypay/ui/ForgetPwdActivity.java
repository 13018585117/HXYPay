package com.hxypay.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.hxypay.util.VerifyRuler;

public class ForgetPwdActivity extends BaseActivity {

    private EditText mTeleEt, mAuthEt, mPwdEt ,mAgainPwdEt;
    private Button mSmsCodeBtn, mComBtn;

    private String  tele, auth, pwd ,againPwd;

    private ImageView mBackImg;
    private boolean isShowpw = false;

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
    private ImageView iv_showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_forget_pwd);

        initView();
    }

    private void initView() {
        mTeleEt = (EditText) findViewById(R.id.tele_et);
        mAuthEt = (EditText) findViewById(R.id.auth_et);
        mPwdEt = (EditText) findViewById(R.id.pwd_et);
        mAgainPwdEt = (EditText) findViewById(R.id.pwd_again_et);
        mBackImg = (ImageView) findViewById(R.id.left_img);
        iv_showPassword = (ImageView) findViewById(R.id.iv_showPassword);

        mSmsCodeBtn = (Button) findViewById(R.id.btn_sms_code);
        mComBtn = (Button) findViewById(R.id.btn_com);

        mBackImg.setOnClickListener(this);
        mSmsCodeBtn.setOnClickListener(this);
        mComBtn.setOnClickListener(this);
        iv_showPassword.setOnClickListener(this);
        mPwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isOk = VerifyRuler.pwdAstrict(s.toString());
                String s1 = mPwdEt.getText().toString();
                if (!isOk){
                    if (s1.length()>=1) {
                        Toast.makeText(context, "密码只能为数字或字母", Toast.LENGTH_SHORT).show();
                        mPwdEt.setText(s1.substring(0, s1.length() - 1));
                        mPwdEt.setSelection(mPwdEt.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mAgainPwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isOk = VerifyRuler.pwdAstrict(s.toString());
                String s1 = mAgainPwdEt.getText().toString();
                if (!isOk){
                    if (s1.length()>=1) {
                        Toast.makeText(context, "密码只能为数字或字母", Toast.LENGTH_SHORT).show();
                        mAgainPwdEt.setText(s1.substring(0, s1.length() - 1));
                        mAgainPwdEt.setSelection(mAgainPwdEt.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_img:
                finish();
                break;
            case R.id.btn_sms_code:
                tele = mTeleEt.getText().toString();
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号");
                    mTeleEt.requestFocus();
                    return;
                }
                getSmsCode();
                break;
            case R.id.btn_com:
                tele = mTeleEt.getText().toString();
                auth = mAuthEt.getText().toString();
                pwd = mPwdEt.getText().toString();
                againPwd = mAgainPwdEt.getText().toString();
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号");
                    mTeleEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(auth)) {
                    IToast.getIToast().showIToast("请输入验证码");
                    mAuthEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    IToast.getIToast().showIToast("请输入密码");
                    mPwdEt.requestFocus();
                    return;
                }else if(pwd.length()<3) {
                    IToast.getIToast().showIToast("密码最少位3位");
                    mPwdEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(againPwd)) {
                    IToast.getIToast().showIToast("请确认密码");
                    mAgainPwdEt.requestFocus();
                    return;
                }else if(againPwd.length()<3) {
                    IToast.getIToast().showIToast("密码最少位3位");
                    mAgainPwdEt.requestFocus();
                    return;
                }
                if(!againPwd.endsWith(pwd)){
                    IToast.getIToast().showIToast("输入密码不一致");
                    mAgainPwdEt.requestFocus();
                    return;
                }
                forgetPwd();
                break;
            case R.id.iv_showPassword: //显示密码；
                isShowpw = !isShowpw;
                if (isShowpw){
                    iv_showPassword.setImageResource(R.drawable.show_password);
                    mPwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mAgainPwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                }else {
                    iv_showPassword.setImageResource(R.drawable.no_password);
                    mPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mAgainPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                mPwdEt.setSelection(mPwdEt.getText().length());
                mAgainPwdEt.setSelection(mAgainPwdEt.getText().length());
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

    public void forgetPwd() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.FIND_PWD)
                .params(Params.forgetPwdParams(tele, pwd, againPwd,auth))
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
                        IToast.getIToast().showIToast("修改成功");
                        mIntent = new Intent(context, LoginActivity.class);
                        startActivity(mIntent);
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

    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static String getUniqueID(Context context) {
        return "10000000000";
    }
}