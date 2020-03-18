package com.hxypay.customview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hxypay.R;

public class KeyPop1 extends PopupWindow {
    private Button btn_one;
    private Button btn_two;
    private Button btn_three;
    private Button btn_four;
    private Button btn_five;
    private Button btn_six;
    private Button btn_seven;
    private Button btn_eight;
    private Button btn_nine;
    private Button btn_zero;
    private ImageButton btn_delete;

    private EditText mPwd1Text, mPwd2Text, mPwd3Text, mPwd4Text, mPwd5Text, mPwd6Text;

    public KeyPop1(Context context, final KeyPopListener keyPopListener) {
        final View view = LayoutInflater.from(context).inflate(R.layout.key_pop1,
                null);
        this.keyPopListener = keyPopListener;
        btn_one = (Button) view.findViewById(R.id.btn_one);
        btn_two = (Button) view.findViewById(R.id.btn_two);
        btn_three = (Button) view.findViewById(R.id.btn_three);
        btn_four = (Button) view.findViewById(R.id.btn_four);
        btn_five = (Button) view.findViewById(R.id.btn_five);
        btn_six = (Button) view.findViewById(R.id.btn_six);
        btn_seven = (Button) view.findViewById(R.id.btn_seven);
        btn_eight = (Button) view.findViewById(R.id.btn_eight);
        btn_nine = (Button) view.findViewById(R.id.btn_nine);
        btn_zero = (Button) view.findViewById(R.id.btn_zero);
        btn_delete = (ImageButton) view.findViewById(R.id.btn_delete);

        mPwd1Text = (EditText) view.findViewById(R.id.pwd1_tx);
        mPwd2Text = (EditText) view.findViewById(R.id.pwd2_tx);
        mPwd3Text = (EditText) view.findViewById(R.id.pwd3_tx);
        mPwd4Text = (EditText) view.findViewById(R.id.pwd4_tx);
        mPwd5Text = (EditText) view.findViewById(R.id.pwd5_tx);
        mPwd6Text = (EditText) view.findViewById(R.id.pwd6_tx);

        mPwd1Text.setEnabled(false);
        mPwd1Text.setFocusable(false);
        mPwd1Text.setKeyListener(null);//重点

        mPwd2Text.setEnabled(false);
        mPwd2Text.setFocusable(false);
        mPwd2Text.setKeyListener(null);//重点

        mPwd3Text.setEnabled(false);
        mPwd3Text.setFocusable(false);
        mPwd3Text.setKeyListener(null);//重点

        mPwd4Text.setEnabled(false);
        mPwd4Text.setFocusable(false);
        mPwd4Text.setKeyListener(null);//重点

        mPwd5Text.setEnabled(false);
        mPwd5Text.setFocusable(false);
        mPwd5Text.setKeyListener(null);//重点

        mPwd6Text.setEnabled(false);
        mPwd6Text.setFocusable(false);
        mPwd6Text.setKeyListener(null);//重点

        btn_one.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("1");
            }
        });
        btn_two.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("2");
            }
        });
        btn_three.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("3");
            }
        });
        btn_four.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("4");
            }
        });
        btn_five.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("5");
            }
        });
        btn_six.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("6");
            }
        });
        btn_seven.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("7");
            }
        });
        btn_eight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("8");
            }
        });
        btn_nine.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("9");
            }
        });
        btn_zero.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("0");
            }
        });
        btn_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                pwdInput("-1");
            }
        });

        this.setContentView(view);
        this.setOutsideTouchable(true);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.operation_anim_style);
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // this.setBackgroundDrawable(dw);

        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.popLayout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                        keyPopListener.diss();
                    }
                }
                return true;
            }
        });
    }

    public interface KeyPopListener {
        void onClick(String pwd);

        void diss();
    }

    private KeyPopListener keyPopListener;

    public void setKeyPopListener(KeyPopListener keyPopListener) {
        this.keyPopListener = keyPopListener;
    }

    public KeyPopListener getKeyPopListener() {
        return keyPopListener;
    }


    public void pwdInput(String num) {
        if (num.equals("-1")) {
            if (!TextUtils.isEmpty(mPwd6Text.getText().toString())) {
                mPwd6Text.setText("");
                return;
            }
            if (!TextUtils.isEmpty(mPwd5Text.getText().toString())) {
                mPwd5Text.setText("");
                return;
            }
            if (!TextUtils.isEmpty(mPwd4Text.getText().toString())) {
                mPwd4Text.setText("");
                return;
            }
            if (!TextUtils.isEmpty(mPwd3Text.getText().toString())) {
                mPwd3Text.setText("");
                return;
            }
            if (!TextUtils.isEmpty(mPwd2Text.getText().toString())) {
                mPwd2Text.setText("");
                return;
            }
            if (!TextUtils.isEmpty(mPwd1Text.getText().toString())) {
                mPwd1Text.setText("");
                return;
            }
        } else {
            if (TextUtils.isEmpty(mPwd1Text.getText().toString())) {
                mPwd1Text.setText(num);
                return;
            }
            if (TextUtils.isEmpty(mPwd2Text.getText().toString())) {
                mPwd2Text.setText(num);
                return;
            }
            if (TextUtils.isEmpty(mPwd3Text.getText().toString())) {
                mPwd3Text.setText(num);
                return;
            }
            if (TextUtils.isEmpty(mPwd4Text.getText().toString())) {
                mPwd4Text.setText(num);
                return;
            }
            if (TextUtils.isEmpty(mPwd5Text.getText().toString())) {
                mPwd5Text.setText(num);
                return;
            }
            if (TextUtils.isEmpty(mPwd6Text.getText().toString())) {
                mPwd6Text.setText(num);
                String pwd = mPwd1Text.getText().toString() + mPwd2Text.getText().toString() + mPwd3Text.getText().toString()
                        + mPwd4Text.getText().toString() + mPwd5Text.getText().toString() + mPwd6Text.getText().toString();
                keyPopListener.onClick(pwd);
                return;
            }
        }
    }
}
