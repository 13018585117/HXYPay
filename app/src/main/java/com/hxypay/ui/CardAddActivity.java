package com.hxypay.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.response.MerchantInfoRes;
import com.hxypay.response.PublicRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.util.VerifyRuler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CardAddActivity extends BaseActivity {

    private TextView mNameText, mIdText;
    private EditText mCardNoEt, mCvnEt;
    private EditText mTeleEt, mAuthEt;
    private TextView mYxqText, mZdrText, mHkrText;
    private Button mSmsCodeBtn, mComBtn;

    private String name, tele, cardNo, cvn, yxq, zdr, hkr, auth;

    private int rqIndex;

    private List<String> monthList = new ArrayList<String>();
    private List<String> yearList = new ArrayList<String>();
    private String month, year;

    private OptionsPickerView pvOptions;

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
        setContentView(R.layout.activity_card_add);
        initView();
        Toast.makeText(context, "onResume", Toast.LENGTH_SHORT).show();
        merchantInfo();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "添加卡", 0,null);
        mNameText = (TextView) findViewById(R.id.name_tx);
        mIdText = (TextView) findViewById(R.id.id_tx);
        mCardNoEt = (EditText) findViewById(R.id.card_no_et);
        mCvnEt = (EditText) findViewById(R.id.cvn_et);
        mTeleEt = (EditText) findViewById(R.id.tele_et);
        mAuthEt = (EditText) findViewById(R.id.auth_et);
        mYxqText = (TextView) findViewById(R.id.yxq_tx);
        mZdrText = (TextView) findViewById(R.id.zdr_tx);
        mHkrText = (TextView) findViewById(R.id.hkr_tx);
        mSmsCodeBtn = (Button) findViewById(R.id.btn_sms_code);
        mComBtn = (Button) findViewById(R.id.btn_com);

        mYxqText.setOnClickListener(this);
        mZdrText.setOnClickListener(this);
        mHkrText.setOnClickListener(this);
        mSmsCodeBtn.setOnClickListener(this);
        mComBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.yxq_tx:
                initMonthAndYear();

                break;
            case R.id.zdr_tx:
                rqIndex = 1;
                dateSelect();
                break;
            case R.id.hkr_tx:
                rqIndex = 2;
                dateSelect();
                break;
            case R.id.btn_sms_code:
                tele = mTeleEt.getText().toString();
                if (TextUtils.isEmpty(tele)) {
                    IToast.getIToast().showIToast("请输入手机号");
                    return;
                }
                mAuthEt.requestFocus();
                getSmsCode();
                break;
            case R.id.btn_com:
                cardNo = mCardNoEt.getText().toString();
                yxq = mYxqText.getText().toString();
                cvn = mCvnEt.getText().toString();
                zdr = mZdrText.getText().toString();
                hkr= mHkrText.getText().toString();
                tele = mTeleEt.getText().toString();
                auth = mAuthEt.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    IToast.getIToast().showIToast("您还未实名认证！");
                    return;
                }
                if (TextUtils.isEmpty(cardNo)) {
                    IToast.getIToast().showIToast("请输入卡号");
                    return;
                }
                if (TextUtils.isEmpty(yxq)) {
                    IToast.getIToast().showIToast("请选择有效期");
                    return;
                }
                if (TextUtils.isEmpty(cvn)) {
                    IToast.getIToast().showIToast("请输入信用卡卡背后末三位");
                    return;
                }
                if (TextUtils.isEmpty(zdr)) {
                    IToast.getIToast().showIToast("请选择账单日");
                    return;
                }
                if (TextUtils.isEmpty(hkr)) {
                    IToast.getIToast().showIToast("请选择还款日");
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
                addCard();
                break;
        }
    }

    private void addCard(){
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.ADD_CARD)
                .headers(PublicParam.getParam())
                .params(Params.addCardParams(cardNo,name,cvn,yxq,tele,zdr,hkr,auth))
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
                        IToast.getIToast().showIToast("添加成功");
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

    private List<String> rqList = new ArrayList<String>();

    private void dateSelect() {
        keyboardHide(findViewById(R.id.layout));
        rqList.clear();
        for (int i = 1; i <= 31; i++) {
            rqList.add(i < 10 ? ("0" + i) : i + "");
        }
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String str = rqList.get(options1);
                if (rqIndex == 1) {
                    mZdrText.setText(str);
                }
                if (rqIndex == 2) {
                    mHkrText.setText(str);
                }
            }
        }).setTitleText((rqIndex == 1) ? "账单日" : "还款日").setCancelText("取消").setSubmitText("完成").build();
        pvOptions.setPicker(rqList);
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.show();
    }


    private void initMonthAndYear() {
        keyboardHide(findViewById(R.id.layout));
        monthList.clear();
        for (int i = 1; i <= 12; i++) {
            monthList.add(i < 10 ? ("0" + i + "月") : (i + "月"));
        }
        yearList.clear();
        Calendar c = Calendar.getInstance();//
        int currYear = c.get(Calendar.YEAR); // 获取当前年份
        int currYear30 = currYear + 50;//50年后
        for (int i = currYear; i <= currYear30; i++) {
            yearList.add(i + "年");
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String tempMonth = monthList.get(options1);
                month = tempMonth.substring(0, 2);
                String tempYear = yearList.get(options2);
                year = tempYear.substring(2, 4);
                mYxqText.setText(month + year);
            }
        }).setTitleText("有效期").setCancelText("取消").setSubmitText("完成").build();
        pvOptions.setNPicker(monthList, yearList, null);//二级选择器*/
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.show();
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


    private void merchantInfo() {
        OkHttpUtils.post()
                .url(Constants.MERCHANT_INFO)
                .headers(PublicParam.getParam())
                .params(Params.merchantInfoParams())
                .build().execute(new GenericsCallback<MerchantInfoRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(MerchantInfoRes response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        name = response.getData().getName();
                        mNameText.setText(VerifyRuler.nameShow(name));
                        mIdText.setText(VerifyRuler.idCardShow(response.getData().getIdCard()));
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
