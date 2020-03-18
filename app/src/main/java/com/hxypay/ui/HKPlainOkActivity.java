package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.BaseActivity;
import com.hxypay.response.CreditRes;
import com.hxypay.tab.OTabActivity;


public class HKPlainOkActivity extends BaseActivity {

    private ImageView mStateImg;
    private TextView mStateText, mCardNameText, mCardNoText, mAmoutText;

    private String resCode, resMessage, amout;
    private CreditRes.Info1 card;

    private Button mOkBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hk_plain_ok);
        initView();
    }

    public void initView() {
        initHeadView(this, 0, "智能还款", 0,null);
        resCode = getIntent().getExtras().getString("resCode");
        resMessage = getIntent().getExtras().getString("resMessage");
        amout = getIntent().getExtras().getString("money");
        card = (CreditRes.Info1) getIntent().getSerializableExtra("card");

        mStateImg = (ImageView) findViewById(R.id.hk_state_img);
        mStateText = (TextView) findViewById(R.id.hk_state_text);
        mCardNameText = (TextView) findViewById(R.id.card_name_tx);
        mCardNoText = (TextView) findViewById(R.id.card_no_tx);
        mAmoutText = (TextView) findViewById(R.id.hk_amout_text);
        mOkBt = (Button) findViewById(R.id.btn_ok);

        resultShow();

        mOkBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCardList();
            }
        });
    }

    private void goToCardList() {
        mIntent = new Intent(context, OTabActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mIntent);
    }

    private void resultShow() {
        if (resCode.equals("0000")) {
            mStateImg.setBackgroundResource(R.drawable.success);
            mStateText.setText("计划已生成");
        } else {
            mStateImg.setBackgroundResource(R.drawable.fail);
            mStateText.setText("计划失敗" + (TextUtils.isEmpty(resMessage) ? "" : resMessage));
        }
        mCardNameText.setText(card.getBankName());
        mCardNoText.setText(card.getAccount());
        mAmoutText.setText("￥" + amout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //退出
            mIntent = new Intent(context, OTabActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(mIntent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
