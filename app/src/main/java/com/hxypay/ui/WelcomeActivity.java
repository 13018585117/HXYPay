package com.hxypay.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.App;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.response.LoginRes;
import com.hxypay.response.WelRes;
import com.hxypay.tab.OTabActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

/**
 * 欢迎界面
 *
 * @author liujian
 */
public class WelcomeActivity extends BaseActivity {
    private Animation mAnimAlphaIn;
    private RelativeLayout mWellcomLayout;
    private ImageView mWelImg;
    private Handler mHandler = new Handler();
    private String tele, pwd;
   /* private Runnable mActRunnable = new Runnable() {
        @Override
        public void run() {
            tele = mPs.getStringValue(Constants.ACCOUNT_NAME);
            pwd = mPs.getStringValue(Constants.ACCOUNT_PWD);
            if (!TextUtils.isEmpty(tele) && !TextUtils.isEmpty(pwd)) {
                goHomeAct();
            } else {
                goToLoginAct();
            }
        }
    };*/
    private LinearLayout ll_skip;
    private TextView tv_second;
    private MyCountDownTimer myCountDownTimer;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        //无title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        //倒计时
        myCountDownTimer = new MyCountDownTimer(4000, 1000);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        initView();
        initWelBgData();
        initData();
    }


    private void initWelBgData() {
        OkHttpUtils.post()
                .url(Constants.WEL_BG)
                .headers(PublicParam.getParam())
                .params(Params.banner())
                .build().execute(new GenericsCallback<WelRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(WelRes response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        try {
                            Glide.with(WelcomeActivity.this).load(Constants.BASE_URL + response.getData().get(0).getImg()).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    //加载完成后的处理;
                                    mWelImg.setImageDrawable(resource);
                                    tv_second.setText("3s 跳转");
                                    ll_skip.setVisibility(View.VISIBLE);
                                    myCountDownTimer.start();
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        GoLogin.goLogin(response.getCode());
                    }
                }
            }
        });

    }

    private void goHomeAct() {
        login();
    }

    private void goToLoginAct() {
        mIntent = new Intent(context, LoginActivity.class);
        context.startActivity(mIntent);
        finish();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mWellcomLayout = (RelativeLayout) findViewById(R.id.welcome_layout);
        mWelImg = (ImageView) findViewById(R.id.welcome_img);
        ll_skip = findViewById(R.id.ll_skip);
        tv_second = findViewById(R.id.tv_second);
        mAnimAlphaIn = AnimationUtils.loadAnimation(context, R.anim.fade);
        mWellcomLayout.startAnimation(mAnimAlphaIn);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 跳转登录
//        mHandler.postDelayed(mActRunnable, 5000); // n秒之后跳转

       ll_skip.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View v) {
                //跳过
                tele = mPs.getStringValue(Constants.ACCOUNT_NAME);
                pwd = mPs.getStringValue(Constants.ACCOUNT_PWD);
                if (!TextUtils.isEmpty(tele) && !TextUtils.isEmpty(pwd)) {
                    goHomeAct();
                } else {
                    goToLoginAct();
                }
            }
        });
    }


    public void login() {
        OkHttpUtils.post()
                .url(Constants.LOGIN)
                .params(Params.loginParams(tele, pwd, getLocalVersionName(this), "android"))
                .build().execute(new GenericsCallback<LoginRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                goToLoginAct();
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
                        App.getApp().setUserInfo(response);
                        mIntent = new Intent(context, OTabActivity.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        goToLoginAct();
                    }
                } else {
                    goToLoginAct();
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

    @Override
    protected void onStop() {
        if (myCountDownTimer!=null){
            myCountDownTimer.cancel();
        }
        try {
            Glide.with(context).pauseRequests();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }


    /**
     * 倒计时
     */
    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture
         *      表示以「 毫秒 」为单位倒计时的总数
         *      例如 millisInFuture = 1000 表示1秒
         *
         * @param countDownInterval
         *      表示 间隔 多少微秒 调用一次 onTick()
         *      例如: countDownInterval = 1000 ; 表示每 1000 毫秒调用一次 onTick()
         *
         */

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            tv_second.setText("0s 跳过");
            //跳过
            tele = mPs.getStringValue(Constants.ACCOUNT_NAME);
            pwd = mPs.getStringValue(Constants.ACCOUNT_PWD);
            if (!TextUtils.isEmpty(tele) && !TextUtils.isEmpty(pwd)) {
                goHomeAct();
            } else {
                goToLoginAct();
            }
        }
        public void onTick(long millisUntilFinished) {
            tv_second.setText( millisUntilFinished / 1000 + "s 跳过");
        }
    }
}