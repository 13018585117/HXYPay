package com.hxypay.ui;

import android.os.Bundle;

import com.hxypay.BaseActivity;
import com.hxypay.R;

public class RegiestProActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_regiest_pro);

        initHeadView(this, R.drawable.recharge, "用户协议", 0,null);
    }


}