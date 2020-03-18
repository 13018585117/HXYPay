package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.bean.PayResult;
import com.hxypay.customview.IToast;
import com.hxypay.response.CT1Res;
import com.hxypay.response.CT2Res;
import com.hxypay.response.CT3Res;
import com.hxypay.response.PublicRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.Map;

public class CardTestOCRInfoActivity extends BaseActivity {

    private EditText mIdNameEt, mIdNoEt, mBankNoEt;
    private EditText mTeleEt, mAuthEt;
    private TextView mMoneyText;
    private Button mSmsCodeBtn, mComBtn;

    private static final int SDK_PAY_FLAG = 2;

    private String idName, idNo, cardNo, money, tele, auth, orderId, alipayInfo;

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
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_card_test_ocr_info);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "卡测评", 0,null);
        mIdNameEt = (EditText) findViewById(R.id.id_name_et);
        mIdNoEt = (EditText) findViewById(R.id.id_no_et);
        mBankNoEt = (EditText) findViewById(R.id.bank_no_et);
        mTeleEt = (EditText) findViewById(R.id.tele_et);
        mAuthEt = (EditText) findViewById(R.id.auth_et);
        mMoneyText = (TextView) findViewById(R.id.money_tx);

        idName = getIntent().getExtras().getString("idName");
        idNo = getIntent().getExtras().getString("idNo");
        cardNo = getIntent().getExtras().getString("cardNo");
        money = getIntent().getExtras().getString("money");

        mIdNameEt.setText(idName);
        mIdNoEt.setText(idNo);
        mBankNoEt.setText(cardNo);
        mMoneyText.setText("￥ " + money);

        mSmsCodeBtn = (Button) findViewById(R.id.btn_sms_code);
        mComBtn = (Button) findViewById(R.id.btn_com);

        mSmsCodeBtn.setOnClickListener(this);
        mComBtn.setOnClickListener(this);

//        timer.schedule(task, 0, 1 * 1000);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_sms_code:
                tele = mTeleEt.getText().toString();
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号");
                    return;
                }
                getSmsCode();
                mAuthEt.requestFocus();
                break;
            case R.id.btn_com:
                idName = mIdNameEt.getText().toString();
                idNo = mIdNoEt.getText().toString();
                cardNo = mBankNoEt.getText().toString();
                tele = mTeleEt.getText().toString();
                auth = mAuthEt.getText().toString();
                if (TextUtils.isEmpty(idName)) {
                    IToast.getIToast().showIToast("请输入持卡人身份证姓名");
                    mIdNameEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(idNo)) {
                    IToast.getIToast().showIToast("请输入持卡人身份证号码");
                    mIdNoEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(cardNo)) {
                    IToast.getIToast().showIToast("请输入银行卡号");
                    mBankNoEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号码");
                    mTeleEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(auth)) {
                    IToast.getIToast().showIToast("请输入验证码");
                    mAuthEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(money)) {
                    IToast.getIToast().showIToast("支付金额为空");
                    return;
                }
                cardTestGetOrderId();
                break;
        }
    }


    private void cardTestGetOrderId() {
        OkHttpUtils.post()
                .url(Constants.CARD_TEST_MONEY_AND_ORDERID)
                .headers(PublicParam.getParam())
                .params(Params.cardTestMoneyAndOrderId("2"))
                .build().execute(new GenericsCallback<CT1Res>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(CT1Res response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        orderId = response.getData().getOrder_id();
                        alipayInfo = response.getData().getAlipay();

                        alipay(alipayInfo);

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

    private void alipay(final String orderStr) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(CardTestOCRInfoActivity.this);
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
                        IToast.getIToast().showIToast("成功支付");
                        cardTestResultQuery();
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        finish();
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


    private void cardTest() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.CARD_START_TEST)
                .headers(PublicParam.getParam())
                .params(Params.cardTest(idName, tele, idNo, cardNo, auth, orderId))
                .build().execute(new GenericsCallback<CT3Res>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(CT3Res response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (TextUtils.isEmpty(response.getUrl())) {
                            IToast.getIToast().showIToast("测评失败，请联系客服！");
                            return;
                        }
                        mIntent = new Intent(context, WBViewActivity.class);
                        mIntent.putExtra("title", "卡测评结果");
                        mIntent.putExtra("url", response.getUrl());
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


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//        if (task != null) {
//            task.cancel();
//            task = null;
//        }
//    }

//    Timer timer = new Timer();
//    Task task = new Task();
//
//    public class Task extends TimerTask {
//        @Override
//        public void run() {
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    cardTestResultQuery();
//                }
//            });
//        }
//    }


    private void cardTestResultQuery() {
        if (TextUtils.isEmpty(orderId)) {
            return;
        }
        OkHttpUtils.post()
                .url(Constants.CARD_TEST_RESULT_QUERY)
                .headers(PublicParam.getParam())
                .params(Params.cardTestResultQuery(orderId))
                .build().execute(new GenericsCallback<CT2Res>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(CT2Res response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        cardTest();
                    }else{
                        GoLogin.goLogin(response.getCode());
                    }
                }
            }
        });
    }


}
