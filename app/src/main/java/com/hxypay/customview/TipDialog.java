package com.hxypay.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxypay.R;

public class TipDialog extends Dialog implements View.OnClickListener {

    private TextView titleTx,contentTx;
    private Button mBtnCancel;
    private Button mBtnConfirm;
    private OnCustomDialogListener customDialogListener;

    private Context context;
    private String title ,content;
    private String btns[];


    public TipDialog(Context context, String title, String content, String btns[], OnCustomDialogListener customDialogListener) {
        // 设置背景style
        super(context, R.style.custom_dialog);
        this.context = context;
        this.title = title;
        this.content = content;
        this.btns = btns;
        this.customDialogListener = customDialogListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_dialog);
        initView();
    }

    private void initView() {
        titleTx =  (TextView) findViewById(R.id.tx_title);
        titleTx.setText(title);
        contentTx =  (TextView) findViewById(R.id.content_tx);
        if(!TextUtils.isEmpty(content)){
            contentTx.setText(content);
        }

        mBtnCancel = (Button) findViewById(R.id.btn_canceld);
        mBtnConfirm = (Button) findViewById(R.id.btn_comfire);

        mBtnCancel.setText(btns[0]);
        mBtnConfirm.setText(btns[1]);

        mBtnCancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
    }

    public interface OnCustomDialogListener {
         void ok(TipDialog dialog);
         void cancle(TipDialog dialog);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_comfire:
                customDialogListener.ok(this);
                break;
            case R.id.btn_canceld:
                customDialogListener.cancle(this);
                break;
            default:
                break;
        }

    }

}
