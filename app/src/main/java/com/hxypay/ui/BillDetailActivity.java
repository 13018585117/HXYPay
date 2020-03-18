package com.hxypay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.response.ChargeTxBillRes;
import com.hxypay.response.HKBillRes;

public class BillDetailActivity extends BaseActivity {

    private TextView mTimeText, mTypeText, mMoneyText;
    private TextView mFeeText, mDescText, mCardText, mStateText;
    private ImageView mStateImg;

    private Button mBtn;

    private String billType;//1 还款  2 充值提现
    private HKBillRes.Info1 hkBills;
    private ChargeTxBillRes.Info1 chargeTxBills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "详情", 0,null);


        mTimeText = (TextView) findViewById(R.id.time_tx);
        mTypeText = (TextView) findViewById(R.id.type_tx);
        mMoneyText = (TextView) findViewById(R.id.money_tx);
        mFeeText = (TextView) findViewById(R.id.fee_tx);
        mDescText = (TextView) findViewById(R.id.desc_tx);
        mCardText = (TextView) findViewById(R.id.card_tx);
        mStateText = (TextView) findViewById(R.id.state_tx);
        mStateImg = (ImageView) findViewById(R.id.state_img);
        mBtn = (Button) findViewById(R.id.btn_com);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        billType = getIntent().getExtras().getString("type");
        if (billType.equals("1")) {
            hkBills = (HKBillRes.Info1) getIntent().getSerializableExtra("bills");
            setHKbills(hkBills);
        }
        if (billType.equals("2")) {
            chargeTxBills = (ChargeTxBillRes.Info1) getIntent().getSerializableExtra("bills");
            setChargeTxBills(chargeTxBills);
        }

    }

    private void setHKbills(HKBillRes.Info1 datas) {
        mDescText.setText("信用卡：");
        mTimeText.setText(datas.getPayTime());
        mTypeText.setText(datas.getType());
        mMoneyText.setText("￥" + datas.getAmount());
        mFeeText.setText("￥" + datas.getFeeRate());
        mCardText.setText(datas.getBankName() + " 尾号(" + datas.getAccount() + ")");
        String state = datas.getState();
        switch (Integer.parseInt(state)) {
            case 0:
                mStateImg.setBackgroundResource(R.drawable.clz);
                mStateText.setText("未还款");
                break;
            case 1:
                mStateImg.setBackgroundResource(R.drawable.success);
                mStateText.setText("已还款");
                break;
            case 2:
                mStateImg.setBackgroundResource(R.drawable.fail);
                mStateText.setText("还款失败");
                break;
            case 3:
                mStateImg.setBackgroundResource(R.drawable.clz);
                mStateText.setText("处理中");
                break;
            case 4:
                mStateImg.setBackgroundResource(R.drawable.success);
                mStateText.setText("已退款");
                break;
            default:
                break;
        }
    }

    private void setChargeTxBills(ChargeTxBillRes.Info1 datas) {
        mDescText.setText("提现到：");
        mTimeText.setText(datas.getCreated());
        mTypeText.setText(datas.getType());
        mMoneyText.setText("￥" + datas.getMoney());
        mFeeText.setText("￥" + datas.getFee());
        mCardText.setText(datas.getBankName() + " 尾号(" + datas.getAccount() + ")");
        String state = datas.getState();
        switch (Integer.parseInt(state)) {
            case 0:
                mStateImg.setBackgroundResource(R.drawable.clz);
                mStateText.setText("处理中");
                break;
            case 1:
                mStateImg.setBackgroundResource(R.drawable.success);
                mStateText.setText("成功");
                break;
            case 2:
                mStateImg.setBackgroundResource(R.drawable.fail);
                mStateText.setText("失败");
                break;
        }
    }

}
