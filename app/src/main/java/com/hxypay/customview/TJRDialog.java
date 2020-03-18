package com.hxypay.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxypay.R;

public class TJRDialog extends Dialog {

    private LinearLayout mCloseLayout;
    private TextView nameText, teleText, qyText, yysText;
    private String name, tele, qy, yys;
    private Context context;

    public TJRDialog(Context context, String name, String tele, String qy, String yys) {
        // 设置背景style
        super(context, R.style.custom_dialog);
        this.context = context;
        this.name = name;
        this.tele = tele;
        this.qy = yys;
        this.yys = yys;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tjr_dialog);
        initView();
    }

    private void initView() {
        mCloseLayout = (LinearLayout) findViewById(R.id.close_layout);
        mCloseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        nameText = (TextView) findViewById(R.id.name_tx);
        teleText = (TextView) findViewById(R.id.tele_tx);
        qyText = (TextView) findViewById(R.id.qy_tx);
        yysText = (TextView) findViewById(R.id.yys_tx);

        nameText.setText(name);
        teleText.setText(tele);
        qyText.setText(qy);
        yysText.setText(yys);
    }

}
