package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.CardPop;
import com.hxypay.customview.IToast;
import com.hxypay.customview.KeyPop1;
import com.hxypay.response.CreditRes;
import com.hxypay.response.DepositRes;
import com.hxypay.response.FastPayRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 多余的；
 */
public class FastPayAllCardActivity extends BaseActivity {
    private RelativeLayout mCardLayout1;
    private RelativeLayout mCardLayout;
    private List<CreditRes.Info1> mCreditData = new ArrayList<CreditRes.Info1>();
    private DepositRes.Info1 depositCard;
    private EditText mAmoutEt;
    private Button mComBtn;

    private String amout, cardId, payPwd;
    private KeyPop1 keyPop1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_fastpay);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        creditCardData();
    }

    private void initView() {
        depositCard = (DepositRes.Info1) getIntent().getSerializableExtra("depositCard");
        initHeadView(this, R.drawable.icon_back_r, "充值/提现", 0,null);

        mCardLayout1 = (RelativeLayout) findViewById(R.id.card_layout_1);
        mCardLayout = (RelativeLayout) findViewById(R.id.card_layout);

        mAmoutEt = (EditText) findViewById(R.id.amout_et);
        setMoneyEditText(mAmoutEt);

        mComBtn = (Button) findViewById(R.id.btn_com);
        mComBtn.setOnClickListener(this);

        mCardLayout1.setOnClickListener(this);

        setDepositCardView(depositCard);
    }

    @Override
    public void onRightListener() {
        super.onRightListener();
        mRightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(context, CardAddOcrActivity.class);
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.card_layout_1:
                if (mCreditData != null && mCreditData.size() > 0) {
                    CardPop cardPop = new CardPop(context, screenHeight, mCreditData, new CardPop.ICardListener() {
                        @Override
                        public void card(CardPop dialog, CreditRes.Info1 card) {
                            dialog.dismiss();
                            setCreditCardView(card);
                        }
                    });
                    cardPop.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                } else {
                    IToast.getIToast().showIToast("请添加信用卡");
                }
                break;
            case R.id.btn_com:
                amout = mAmoutEt.getText().toString();
                if (TextUtils.isEmpty(cardId)) {
                    IToast.getIToast().showIToast("请先添加信用卡片");
                    return;
                }
                if (TextUtils.isEmpty(amout)) {
                    IToast.getIToast().showIToast("请输入转账金额");
                    return;
                }
                keyboardHide(findViewById(R.id.layout));
                keyPop1 = new KeyPop1(context, new KeyPop1.KeyPopListener() {
                    @Override
                    public void onClick(String pwd) {
                        keyPop1.dismiss();
                        payPwd = pwd;
                        fastPay();
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


    private void fastPay() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.FASTPAY)
                .headers(PublicParam.getParam())
                .params(Params.fastPayParams(cardId, amout, payPwd,"10","广东省 广州市",Constants.UID))
                .build().execute(new GenericsCallback<FastPayRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(FastPayRes response, int id) {
                dismissDialog();
                if (response != null) {
//                    Toast.makeText(context, "牛牛B"+response.getCode(), Toast.LENGTH_SHORT).show();
                    if (response.getCode().equals("0000")) {
//                        pay_info 支付地址
//                        pay_code 支付参数
//                        注：支付方式是 如果支付参数不为空的话把支付参数拼接到支付链接地址后面进行支付
//                        如果支付参数为空  直接打开支付链接进行支付
//                        1. 处理方式：前端将pay_code里面参数进行URLDecoder解码，拼接成form表单,发送POST 到pay_info支付地址
//                        2. pay_ code 里面的参数要循环处理,参数会随着通道变化
//                        3. pay_info支付地址不是固定的, 会随着通道变化
                        String payUrl = response.getData().getPay_info();
                        String payParms = response.getData().getPay_code();
                        Gson gson = new Gson();
                        Map<String, String> map = gson.fromJson(payParms, new TypeToken<Map<String, String>>() {
                        }.getType());
                        if (map != null && map.size() > 0) {//如果支付参数为空  直接打开支付链接进行支付
                            List<String> k = new ArrayList<String>();
                            k.clear();
                            Set<String> keys = map.keySet();// 得到全部的key
                            Iterator<String> iter = keys.iterator();
                            while (iter.hasNext()) {
                                String str = iter.next();
                                k.add(str);
                            }
                            for (int i = 0; i < k.size(); i++) {
                                if (i == 0) {
                                    payUrl += "?" + k.get(i) + "=" + toURLEncoded(map.get(k.get(i)));
                                } else {
                                    payUrl += "&" + k.get(i) + "=" + toURLEncoded(map.get(k.get(i)));
                                }
                            }
                        }
                        mIntent = new Intent(context, FastPayWBViewActivity.class);
                        mIntent.putExtra("title", "个人收款");
                        mIntent.putExtra("url", payUrl);
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

    public String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {

        }
        return "";
    }

    private void creditCardData() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.CREDIT_CARD_LIST)
                .headers(PublicParam.getParam())
                .params(Params.cardMgrParams())
                .build().execute(new GenericsCallback<CreditRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(CreditRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mCreditData.clear();
                        mCreditData = response.getData();
                        if (mCreditData.size() > 0) {
                            initHeadView(FastPayAllCardActivity.this, R.drawable.icon_back_r, "充值/提现", 0,null);
                            setCreditCardView(mCreditData.get(0));
                        } else {
                            initHeadView(FastPayAllCardActivity.this, R.drawable.icon_back_r, "充值/提现", R.drawable.add,null);
                        }
                    } else {
                        if (response.getCode().equals("0004")) {
                            initHeadView(FastPayAllCardActivity.this, R.drawable.icon_back_r, "充值/提现", R.drawable.add,null);
                        }
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }


    private void setCreditCardView(CreditRes.Info1 dd) {
        cardId = dd.getId();
        mCardLayout1.setVisibility(View.VISIBLE);
        ImageView img1 = (ImageView) findViewById(R.id.img_1);
        ImageView itemBankLogo1 = (ImageView) findViewById(R.id.bank_logo_img_1);
        ImageView itemBankLogo1_1 = (ImageView) findViewById(R.id.bank_logo_img1_1);
        TextView itemUpdate1 = (TextView) findViewById(R.id.update_tx_1);
        TextView itemBankName1 = (TextView) findViewById(R.id.bank_name_tx_1);
        TextView itemBankType1 = (TextView) findViewById(R.id.bank_type_tx_1);
        TextView getItemBankNo1 = (TextView) findViewById(R.id.bank_card_tx_1);

        Glide.with(context).load(dd.getBankBack()).into(img1);
        Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo1);
        Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo1_1);
        itemBankName1.setText(dd.getBankName());
        itemBankType1.setText("信用卡");
        String cardNo = dd.getAccount();
        getItemBankNo1.setText(" ****    ****    ****    " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
        itemUpdate1.setVisibility(View.GONE);
    }


    private void setDepositCardView(DepositRes.Info1 dd) {
        mCardLayout.setVisibility(View.VISIBLE);
        ImageView itemBankLogo = (ImageView) findViewById(R.id.bank_logo_img);
        ImageView itemBankLogo1 = (ImageView) findViewById(R.id.bank_logo_img1);
        TextView itemUpdate = (TextView) findViewById(R.id.update_tx);
        TextView itemBankName = (TextView) findViewById(R.id.bank_name_tx);
        TextView itemBankType = (TextView) findViewById(R.id.bank_type_tx);
        TextView getItemBankNo = (TextView) findViewById(R.id.bank_card_tx);

        Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo);
        Glide.with(context).load(dd.getBankLogo()).into(itemBankLogo1);
        itemBankName.setText(dd.getBankName());
        itemBankType.setText("储蓄卡");
        String cardNo = dd.getAccount();
        getItemBankNo.setText(" ****    ****    ****    " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
        itemUpdate.setVisibility(View.GONE);
    }

}
