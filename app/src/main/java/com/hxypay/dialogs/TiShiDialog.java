package com.hxypay.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hxypay.R;


public class TiShiDialog extends Dialog {

    private Context context;
    private String message;
    private String title;
    private String confirmButtonText;
    private String cacelButtonText;
    private boolean isShowTitle;

    /* ----------------------------------接口监听---------------------------------------- */

    private ClickListenerInterface clickListenerInterface;
    private TextView tvTitle;

    public interface ClickListenerInterface {
        void doConfirm();

        void doCancel();
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    /* ----------------------------------构造方法：传值------------------------------------- */

    public TiShiDialog(Context context, String title, String message, String confirmButtonText, String cacelButtonText,boolean isShowTitle) {
        super(context, R.style.MyDialog);
        this.isShowTitle = isShowTitle;
        this.context = context;
        this.title = title;
        this.message = message;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
    }


    /* ----------------------------------  onCreate  ------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("InflateParams")
    public void init() {
        setContentView(R.layout.share_dialog);

        tvTitle = (TextView) findViewById(R.id.title);
        TextView tvMessage = (TextView) findViewById(R.id.message);
        TextView tvConfirm = (TextView) findViewById(R.id.yes);
        TextView tvCancel = (TextView) findViewById(R.id.no);

        tvTitle.setText(title);
        tvMessage.setText(message);
        tvConfirm.setText(confirmButtonText);
        tvCancel.setText(cacelButtonText);

        setShowTitle(isShowTitle);
        tvConfirm.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new clickListener());

        setCanceledOnTouchOutside(false);//设置窗口外是否可关闭

        setOnKeyListener(new OnKeyListener() {//设置按back键不关闭
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);//设置窗口位置
//            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口进出动画

            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高
            if (lp != null) {
                lp.width = (int) (d.widthPixels * 0.7); // 设置为屏幕宽度
            }
            dialogWindow.setAttributes(lp);
        }
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yes:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.no:
                    clickListenerInterface.doCancel();
                    break;
            }
        }
    }

    public void setShowTitle(boolean isShow){
        if (tvTitle!=null){
            if (isShow){
                tvTitle.setVisibility(View.VISIBLE);
            }else {
                tvTitle.setVisibility(View.GONE);
            }
        }
    }
}

