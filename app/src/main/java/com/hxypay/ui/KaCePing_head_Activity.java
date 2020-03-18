package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.gyf.immersionbar.ImmersionBar;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.newcaoguo.easyrollingnumber.view.ScrollingDigitalAnimation;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KaCePing_head_Activity extends BaseActivity {

    @BindView(R.id.payBtn)
    Button payBtn;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_kcpJL)
    TextView tv_kcpJL;

    // 显示数字的自定义控件
    @BindView(R.id.tv_count1)
     ScrollingDigitalAnimation number1;
    // 显示数字的自定义控件
    @BindView(R.id.tv_count2)
     ScrollingDigitalAnimation number2;
    // 显示数字的自定义控件
    @BindView(R.id.tv_count3)
     ScrollingDigitalAnimation number3;
    // 显示数字的自定义控件
    @BindView(R.id.tv_count4)
     ScrollingDigitalAnimation number4;
    // 显示数字的自定义控件
    @BindView(R.id.tv_count5)
    ScrollingDigitalAnimation number5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaceping_head);
        ButterKnife.bind(this);

        ImmersionBar.with(this).init();
        /*topbar.mView.setVisibility(View.VISIBLE);
        topbar.setRightTvClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KaCePing_head_Activity.this, KaCePing_JiLu_Activity.class);
                startActivity(intent);
            }
        });*/
        initAnimation();
    }

    private void initAnimation() {
        number1.setNumberString("3");
        number2.setNumberString("5");
        number3.setNumberString("6");
        number4.setNumberString("2");
        number5.setNumberString("9");
        number1.setDuration(100);
        number2.setDuration(100);
        number3.setDuration(100);
        number4.setDuration(100);
        number5.setDuration(100);
    }

    @OnClick(R.id.payBtn)
    public void payBtn(){
        mIntent = new Intent(context,CardTestOCRActivity.class);
        startActivity(mIntent);
    }

    @OnClick(R.id.ll_back)
    public void back(){
        finish();
    }
    @OnClick(R.id.tv_kcpJL)
    public void KCPJL(){
      /*  mIntent = new Intent(KaCePing_head_Activity.this, KaCePing_JiLu_Activity.class);
        startActivity(mIntent);*/
    }
}
