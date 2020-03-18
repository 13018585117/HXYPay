package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.customview.KeyPop1;
import com.hxypay.response.DepositRes;
import com.hxypay.response.PublicRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.util.VerifyRuler;

import java.math.BigDecimal;

public class TXActivity extends BaseActivity {


    private EditText mMoneyEt;
    private TextView mKtxMoney, mAllTxTx;//可提现金额，全部提现
    private ImageView mBankLogoImg;
    private TextView mBankNameTx, mCardNoTx;

    private Button mComBtn;

    private String money, payPwd;
    private KeyPop1 keyPop1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx);

        initView();
        depositCardData();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "提现", 0,"提现记录");
        money = getIntent().getExtras().getString("ktxMoney");
        mMoneyEt = (EditText) findViewById(R.id.money_et);
        mKtxMoney = (TextView) findViewById(R.id.ktx_money_tx);
        mAllTxTx = (TextView) findViewById(R.id.all_tx_tx);
        mBankLogoImg = (ImageView) findViewById(R.id.bank_logo_img);
        mBankNameTx = (TextView) findViewById(R.id.bank_name_tx);
        mCardNoTx = (TextView) findViewById(R.id.card_no_tx);
        mKtxMoney.setText("可提现收益 " + money);
        mComBtn = (Button) findViewById(R.id.btn_com);
        setMoneyEditText(mMoneyEt);

        mAllTxTx.setOnClickListener(this);
        mComBtn.setOnClickListener(this);
    }

    @Override
    public void onRightListener() {
        super.onRightListener();
        tv_right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(TXActivity.this,BillActivity.class);
                mIntent.putExtra("currentItem",1);
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.all_tx_tx:
                mMoneyEt.setText(money);
                break;
            case R.id.btn_com:
                money = mMoneyEt.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    IToast.getIToast().showIToast("请输入需要提现的金额");
                    return;
                }
                if (new BigDecimal(money).compareTo(new BigDecimal(100)) == -1) {
                    IToast.getIToast().showIToast("可提现金额需超过100元");
                    return;
                }
                try {
                    if (!(Long.parseLong(money) % 100 == 0)) {
                        IToast.getIToast().showIToast("提现金额为100的倍数");
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                keyboardHide(findViewById(R.id.layout));
                keyPop1 = new KeyPop1(context, new KeyPop1.KeyPopListener() {
                    @Override
                    public void onClick(String pwd) {
                        keyPop1.dismiss();
                        payPwd = pwd;
                        txMoney();
                    }

                    @Override
                    public void diss() {
                        keyPop1.dismiss();
                    }
                });
                keyPop1.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);

                break;
        }
    }

    //获取储蓄卡
    private void depositCardData() {
        OkHttpUtils.post()
                .url(Constants.DEPOSIT_CARD_LIST)
                .headers(PublicParam.getParam())
                .params(Params.myMerchantParams())
                .build().execute(new GenericsCallback<DepositRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(DepositRes response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData() != null && response.getData().size() > 0) {
                            setDepositCardView(response.getData().get(0));
                        }
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

    private void setDepositCardView(DepositRes.Info1 dd) {
        Glide.with(context).load(dd.getBankLogo()).into(mBankLogoImg);
        mBankNameTx.setText(dd.getBankName());
        mCardNoTx.setText(VerifyRuler.cardShow(dd.getAccount()));
    }

    private void txMoney() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.WITHDRAW)
                .headers(PublicParam.getParam())
                .params(Params.withdraw(money, payPwd))
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
                        IToast.getIToast().showIToast("提现成功");
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
