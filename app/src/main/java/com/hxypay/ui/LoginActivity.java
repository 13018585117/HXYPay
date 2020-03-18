package com.hxypay.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.App;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.dialogs.TiShiDialog;
import com.hxypay.response.LoginRes;
import com.hxypay.tab.OTabActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private EditText mTeleEt, mPwdEt;
    private TextView mForgetPwdTx;
    private Button mLoginBtn, mRegBtn;

    private String tele, pwd;
    private boolean isShowpw = false;

    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,};
    private ImageView iv_showPassword;
    private LinearLayout ll_kfPhone;
    private TextView tv_phoneNumber;
    private TiShiDialog tiShiDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);

        initView();

        if(isPermissions()){

        }else{
            EasyPermissions.requestPermissions(LoginActivity.this, "申请权限", 0, perms);
        }
    }

    private void initView() {
        mTeleEt = (EditText) findViewById(R.id.tele_et);
        mPwdEt = (EditText) findViewById(R.id.pwd_et);
        mForgetPwdTx = (TextView) findViewById(R.id.forget_pwd_tx);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mRegBtn = (Button) findViewById(R.id.btn_reg);
        iv_showPassword = findViewById(R.id.iv_showPassword);
        ll_kfPhone = findViewById(R.id.ll_kfPhone);
        tv_phoneNumber = findViewById(R.id.tv_phoneNumber);

        mForgetPwdTx.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mRegBtn.setOnClickListener(this);
        iv_showPassword.setOnClickListener(this);
        ll_kfPhone.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String accName = mPs.getStringValue(Constants.ACCOUNT_NAME);
        if (!TextUtils.isEmpty(accName)) {
            mTeleEt.setText(accName);
            mTeleEt.setSelection(accName.length());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.forget_pwd_tx:
                mIntent = new Intent(context, ForgetPwdActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_login://milo7816
//                tele = "13620208428";
//                pwd = "222222";
//                tele = "13816700917";
//                pwd = "123456";
//                tele = "13726217504";
//                pwd = "112233";
//                tele ="13710414597";
//                pwd = "111222";
                boolean isWritePermission = PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", getPackageName());
                boolean isReadPermission = PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission("android.permission.READ_EXTERNAL_STORAGE", getPackageName());
                if (!isWritePermission || !isReadPermission){
                    setStoragePms();
                    return;
                }
                if(isPermissions()){

                }else{
                    EasyPermissions.requestPermissions(LoginActivity.this, "申请权限", 0, perms);
                    return;
                }
                boolean notificationEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();
                if (!notificationEnabled){
                    if (tiShiDialog==null){
                        tiShiDialog = new TiShiDialog(LoginActivity.this,"应用通知权限申请", "在设置-应用-好信用管家-应用通知权限，开启好信用管家应用通知权限才能时时刻刻收取我们消息！", "去设置", "取消",true);
                    }
                    tiShiDialog.setClickListener(new TiShiDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            mIntent = new Intent();
                            mIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
                            mIntent.setData(uri);
                            startActivity(mIntent);
                            tiShiDialog.cancel();
                        }

                        @Override
                        public void doCancel() {
                            tiShiDialog.cancel();
                        }
                    });
                    tiShiDialog.show();
                    return;
                }
                tele = mTeleEt.getText().toString().trim();
                pwd = mPwdEt.getText().toString().trim();
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号");
                    mTeleEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    IToast.getIToast().showIToast("请输入密码");
                    mPwdEt.requestFocus();
                    return;
                }
                login();
                break;
            case R.id.btn_reg:
                mIntent = new Intent(context, RegiestActivity.class);
                startActivity(mIntent);
                break;

            case R.id.iv_showPassword: //显示密码；
                isShowpw = !isShowpw;
                if (isShowpw){
                    iv_showPassword.setImageResource(R.drawable.show_password);
                    mPwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                }else {
                    iv_showPassword.setImageResource(R.drawable.no_password);
                    mPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                mPwdEt.setSelection(mPwdEt.getText().length());
                break;

            case R.id.ll_kfPhone:  //拨打客服电话；
                boolean isPhonePermission = PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission("android.permission.CALL_PHONE", getPackageName());
                if (isPhonePermission) {
                    String phone = tv_phoneNumber.getText().toString();
//
                    callPhone(phone);
                }else {
                    if(isPermissions()){

                    }else{
                        EasyPermissions.requestPermissions(LoginActivity.this, "申请权限", 0, perms);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void callPhone(String phone){
        mIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(mIntent);
    }


    public void login() {
        OkHttpUtils.post()
                .url(Constants.LOGIN)
                .params(Params.loginParams(tele, pwd, getLocalVersionName(this), "android"))
                .build().execute(new GenericsCallback<LoginRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(LoginRes response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mPs.saveStringValue(Constants.ACCOUNT_NAME, tele);
                        mPs.saveStringValue(Constants.ACCOUNT_PWD, pwd);
                        mPs.saveStringValue(Constants.UID, response.getUid());
                        mPs.saveStringValue(Constants.TOKEN, response.getToken());
                        mPs.saveStringValue(Constants.XYK_URL, response.getUrl());
                        mPs.saveStringValue(Constants.DK_URL,response.getLoans());
                        App.getApp().setUserInfo(response);
                        mIntent = new Intent(context, OTabActivity.class);
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

    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //退出
            android.os.Process.killProcess(android.os.Process.myPid());
            finish();
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    private boolean isPermissions() {
        return EasyPermissions.hasPermissions(this, perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if(requestCode == 0){
//            IToast.getIToast().showIToast("0000");
        }
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
//       IToast.getIToast().showIToast("请手动开启存储权限");
    }


    private void setStoragePms(){
        if (tiShiDialog==null) {
            tiShiDialog = new TiShiDialog(this, "权限申请", "在设置-应用-好信用管家-权限中开启读写存储设备权限，允许好信用管家使用存储权限来保存用户数据", "去设置", "取消",true);
        }
        tiShiDialog.setClickListener(new TiShiDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
              // 跳转到应用设置界面
                mIntent = new Intent();
                mIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                mIntent.setData(uri);
                startActivityForResult(mIntent, 123);
                tiShiDialog.cancel();
            }

            @Override
            public void doCancel() {
                tiShiDialog.cancel();
            }
        });
        tiShiDialog.show();
    }

}