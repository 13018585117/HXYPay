package com.hxypay.customview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxypay.R;

public class KeyPop extends PopupWindow {
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

    private TextView mMoneyText;
    private Button mBtn;
    private KeyPopListener keyPopListener;

    private String money;

    public KeyPop(Context context, String money, final KeyPopListener keyPopListener) {
        final View view = LayoutInflater.from(context).inflate(R.layout.key_pop,
                null);
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
        mMoneyText = (TextView) view.findViewById(R.id.money_tx);
        mMoneyText.setText(money);
        mBtn = (Button) view.findViewById(R.id.btn_com);
        mBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = mMoneyText.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    IToast.getIToast().showIToast("请输入金额");
                    return;
                }
                if (Integer.parseInt(money) < 10000 || Integer.parseInt(money) > 200000) {
                    IToast.getIToast().showIToast("请输入1w-20w金额");
                    return;
                }
                keyPopListener.onFinish(money);
            }
        });


        btn_one.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("1");
            }
        });
        btn_two.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("2");
            }
        });
        btn_three.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("3");
            }
        });
        btn_four.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("4");
            }
        });
        btn_five.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("5");
            }
        });
        btn_six.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("6");
            }
        });
        btn_seven.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("7");
            }
        });
        btn_eight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("8");
            }
        });
        btn_nine.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("9");
            }
        });
        btn_zero.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("0");
            }
        });
        btn_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setmMoneyText("-1");
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
                    }
                }
                return true;
            }
        });
    }

    private void setmMoneyText(String money) {
        if (Integer.parseInt(money) < 0) {
            //删除
            String temp = mMoneyText.getText().toString();
            if (temp.length() == 0) {
                mMoneyText.setText("");
            } else {
                mMoneyText.setText(temp.substring(0, temp.length() - 1));
            }
        } else {
            String temp = mMoneyText.getText().toString();
            mMoneyText.setText(temp + money);
        }
    }

    public interface KeyPopListener {
        void onFinish(String num);
    }

    public void setKeyPopListener(KeyPopListener keyPopListener) {
        this.keyPopListener = keyPopListener;
    }

    public KeyPopListener getKeyPopListener() {
        return keyPopListener;
    }
}
