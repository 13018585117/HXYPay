package com.hxypay.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.bean.PayResult;
import com.hxypay.customview.IToast;
import com.hxypay.response.SJ2Res;
import com.hxypay.response.SJ3Res;
import com.hxypay.response.SJ3Res1;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.wxchat.WXPay;

import java.util.Map;

public class MemBerSJActivity extends BaseActivity {

    private static final int MY_PERMISSION_READ_PHONE = 0;
    private static final int SDK_PAY_FLAG = 1;

    private TextView mMoneyText;
    private LinearLayout mWechatLayout, mAlipayLayout;
    private ImageView mWechatCheckImg, mAlipayCheckImg;

    private TextView mDescText1, mDescText2;

    private Button mBtn;

    private int payType = 0;

    private String userLevelId, money, level;

    private String orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_sj);
        initView();
        sj2();
    }

    private void initView() {
        userLevelId = getIntent().getExtras().getString("userLevelId");
        money = getIntent().getExtras().getString("money");
        level = getIntent().getExtras().getString("level");

        initHeadView(this, R.drawable.icon_back_r, "开通" + level, 0,null);

        mMoneyText = (TextView) findViewById(R.id.money_tx);
        mMoneyText.setText(money);
        mWechatLayout = (LinearLayout) findViewById(R.id.wechat_layout);
        mAlipayLayout = (LinearLayout) findViewById(R.id.alipay_layout);

        mWechatCheckImg = (ImageView) findViewById(R.id.wechat_check_img);
        mAlipayCheckImg = (ImageView) findViewById(R.id.alipay_check_img);

        mDescText1 = (TextView) findViewById(R.id.desc1_tx);
        mDescText2 = (TextView) findViewById(R.id.desc2_tx);
        mDescText1.setText(".开通" + level + "后即可永久享受" + level + "权益");
        mDescText2.setText("." + level + "一经开通不支持退款");

        mBtn = (Button) findViewById(R.id.btn_com);

        mWechatLayout.setOnClickListener(this);
        mAlipayLayout.setOnClickListener(this);
        mBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.wechat_layout:
                payType = 1;
                setPayType(mWechatCheckImg);
                break;
            case R.id.alipay_layout:
                payType = 2;
                setPayType(mAlipayCheckImg);
                break;
            case R.id.btn_com:
                if (payType == 0) {
                    IToast.getIToast().showIToast("请选择支付方式");
                    return;
                }
                if (payType == 1) {

//                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSION_READ_PHONE);
//                    } else {
                        sj4();
//                    }

                }
                if (payType == 2) {
                    sj3();
                }
                break;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == MY_PERMISSION_READ_PHONE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                sj4();
//            } else {
//                //Permission Denied
//                IToast.getIToast().showIToast("请手动打开拨打电话权限");
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    private void setPayType(ImageView img) {
        mWechatCheckImg.setBackgroundResource(R.drawable.check_n);
        mAlipayCheckImg.setBackgroundResource(R.drawable.check_n);

        img.setBackgroundResource(R.drawable.check_y);
    }

    private void sj3() {
        showDialog("加载中...");
        OkHttpUtils.get()
                .url(Constants.ALIPAY_PAY)
                .headers(PublicParam.getParam())
                .params(Params.sj3(orderInfo))
                .build().execute(new GenericsCallback<SJ3Res>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
                finish();
            }

            @Override
            public void onResponse(SJ3Res response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        alipay(response.getDizhi());
                    } else {
                        if (response.getCode().equals("0006")) {
                            GoLogin.goLogin(response.getCode());
                        } else {
                            IToast.getIToast().showIToast(response.getMsg());
                            finish();
                        }
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                    finish();
                }
            }
        });
    }

    private void sj4() {
        showDialog("加载中...");
        OkHttpUtils.get()
                .url(Constants.WECHAT_PAY)
                .headers(PublicParam.getParam())
                .params(Params.sj3(orderInfo))
                .build().execute(new GenericsCallback<SJ3Res1>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
                finish();
            }

            @Override
            public void onResponse(SJ3Res1 response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        IWXAPI api = WXAPIFactory.createWXAPI(MemBerSJActivity.this,response.getOrder.getAppid());
                        api.registerApp(response.getOrder.getAppid());
                        if (!api.isWXAppInstalled()) {
                            Toast.makeText(MemBerSJActivity.this, "请先安装微信客户端", Toast.LENGTH_SHORT).show();
                        }else {
                            PayReq request = new PayReq();
                            request.appId = response.getOrder.getAppid();
                            request.partnerId = response.getOrder.getPartnerid();
                            request.prepayId = response.getOrder.getPrepayid();
                            request.packageValue = "Sign=WXPay";
                            request.nonceStr = response.getOrder.getNoncestr();
                            request.timeStamp = response.getOrder.getTimestamp();
                            request.sign = response.getOrder.getSign();
                            request.extData = "sj";
                            WXPay.pay(context, request);
                            finish();
                        }
                    } else {
                        if (response.getCode().equals("0006")) {
                            GoLogin.goLogin(response.getCode());
                        } else {
                            IToast.getIToast().showIToast(response.getMsg());
                            finish();
                        }
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                    finish();
                }
            }
        });
    }


    private void sj2() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.SJ2)
                .headers(PublicParam.getParam())
                .params(Params.sj2(money, userLevelId))
                .build().execute(new GenericsCallback<SJ2Res>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
                finish();
            }

            @Override
            public void onResponse(SJ2Res response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        orderInfo = response.getMsg();
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                        finish();
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                    finish();
                }
            }
        });
    }


    private void alipay(final String orderStr) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(MemBerSJActivity.this);
                Map<String, String> result = alipay.payV2(orderStr, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        IToast.getIToast().showIToast("支付成功");
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        finish();
                    }
                    break;
                }
            }
        }

        ;
    };

}




