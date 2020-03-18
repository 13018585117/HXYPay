package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.response.CreditRes;
import com.hxypay.response.PublicRes;
import com.hxypay.tab.OTabActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.util.VerifyRuler;

import java.util.ArrayList;
import java.util.List;

public class CardUpdateActivity extends BaseActivity {


    private TextView mCardNoText, mBankNameText;
    private TextView mZdrText, mHkrText;

    private String zdr, hkr;
    private int rqIndex;
    private OptionsPickerView pvOptions;

    private CreditRes.Info1 card;

    private Button mUpdateCardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_update);

        initView();
    }

    private void initView() {
        card = (CreditRes.Info1) getIntent().getSerializableExtra("card");
        initHeadView(this, R.drawable.icon_back_r, "修改卡片资料", 0,null);

        mCardNoText = (TextView) findViewById(R.id.card_no_tx);
        mBankNameText = (TextView) findViewById(R.id.bank_name_tx);
        mZdrText = (TextView) findViewById(R.id.zdr_tx);
        mHkrText = (TextView) findViewById(R.id.hkr_tx);

        mCardNoText.setText(VerifyRuler.cardShow(card.getAccount()));
        mBankNameText.setText(card.getBankName());
        mZdrText.setText(card.getBillDay());
        mHkrText.setText(card.getRepaymentDay());

        mUpdateCardBtn = (Button) findViewById(R.id.btn_update);

        mZdrText.setOnClickListener(this);
        mHkrText.setOnClickListener(this);
        mUpdateCardBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.zdr_tx:
                rqIndex = 1;
                dateSelect();
                break;
            case R.id.hkr_tx:
                rqIndex = 2;
                dateSelect();
                break;
            case R.id.btn_update:
                zdr = mZdrText.getText().toString();
                hkr = mHkrText.getText().toString();
                if (TextUtils.isEmpty(zdr)) {
                    IToast.getIToast().showIToast("请选择账单日");
                    return;
                }
                if (TextUtils.isEmpty(hkr)) {
                    IToast.getIToast().showIToast("请选择还款日");
                    return;
                }
                updateCard();
                break;
        }
    }

    private void updateCard() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.UPDATE_CARD)
                .headers(PublicParam.getParam())
                .params(Params.updateCardParams(card.getId(), zdr, hkr))
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
                        mIntent = new Intent(context, OTabActivity.class);
                        startActivity(mIntent);
                        OTabActivity.mainAct.mTabHost.setCurrentTab(1);
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

}
