package com.hxypay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.response.MyBdRes;

public class MyBDActivity extends BaseActivity {

    private TextView mCbdwText, mBxdText;
    private TextView mBbrText, mBzfwText, mBzjeText, mBxsxText, mKsrqText, mYbztsText, mCxdhText;
    private Button mBtn;

    private MyBdRes.Info1 bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bd);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "我的保单", 0,null);
        bd = (MyBdRes.Info1) getIntent().getSerializableExtra("myBd");
        mCbdwText = (TextView) findViewById(R.id.cbdw_tx);
        mBxdText = (TextView) findViewById(R.id.bxd_tx);

        mBbrText = (TextView) findViewById(R.id.bbr_tx);
        mBzfwText = (TextView) findViewById(R.id.bzfw_tx);
        mBzjeText = (TextView) findViewById(R.id.bzje_tx);
        mBxsxText = (TextView) findViewById(R.id.bxsx_tx);
        mKsrqText = (TextView) findViewById(R.id.ksrq_tx);
        mYbztsText = (TextView) findViewById(R.id.ybzts_tx);
        mCxdhText = (TextView) findViewById(R.id.cxdh_tx);

        mCbdwText.setText(bd.getInsuranName());
        mBxdText.setText(bd.getListNumber());

        mBbrText.setText(bd.getAccountName());
        mBzfwText.setText("被保人名下所有个人账户");
        mBzjeText.setText(bd.getMoney());
        mBxsxText.setText(bd.getExpDate());
        mKsrqText.setText(bd.getEffectTime());
        mYbztsText.setText(bd.getDay() + "天");
        mCxdhText.setText(bd.getListPhone());

        mBtn = (Button) findViewById(R.id.btn_com);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
