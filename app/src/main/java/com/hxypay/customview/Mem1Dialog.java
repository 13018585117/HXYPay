package com.hxypay.customview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.R;
import com.hxypay.response.SJ1Res;
import com.hxypay.response.SJ_ImgRes;
import com.hxypay.ui.MemBerSJActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;


public class Mem1Dialog extends Dialog {

    private Context context;
    private String message;
    private String isRealName;
    private String userLevelId;
    private String confirmButtonText;
    private String cacelButtonText;
    private CustomAlertDialog mDialog;

    /* ----------------------------------构造方法：传值------------------------------------- */

    public Mem1Dialog(Context context, String message, String isRealName, String userLevelId, String confirmButtonText, String cacelButtonText) {
        super(context, R.style.MyDialog);

        this.context = context;
        this.message = message;
        this.isRealName = isRealName;
        this.userLevelId = userLevelId;
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
        setContentView(R.layout.mem_dialog);

        TextView tvMessage = (TextView) findViewById(R.id.content_tx);
        Button tvConfirm = (Button) findViewById(R.id.sj);
        Button tvCancel = (Button) findViewById(R.id.back);
        LinearLayout close_layout = findViewById(R.id.close_layout);
        mDialog = new CustomAlertDialog(context, "加载中...");
        tvMessage.setText(message);
        tvConfirm.setText(confirmButtonText);
        tvCancel.setText(cacelButtonText);

        tvConfirm.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new clickListener());
        close_layout.setOnClickListener(new clickListener());
        setCanceledOnTouchOutside(false);//设置窗口外是否可关闭

       /* setOnKeyListener(new OnKeyListener() {//设置按back键不关闭
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });*/

        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);//设置窗口位置
//            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口进出动画

            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高
            if (lp != null) {
                lp.width = (int) (d.widthPixels * 0.8); // 设置为屏幕宽度
            }
            dialogWindow.setAttributes(lp);
        }
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sj:
                    if (isRealName.equals("0")) {
                        IToast.getIToast().showIToast("商户未认证，请实名认证");
                        return;
                    } else if (isRealName.equals("1")) {
                        sj1();
                        dismiss();
                    } else if (isRealName.equals("2")) {
                        IToast.getIToast().showIToast("商户未知状态，请重试");
                        return;
                    }
                    break;
                case R.id.back:
                    dismiss();
                    break;
                    case R.id.close_layout:
                        dismiss();
                        break;
            }
        }
    }
    private void sj1() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.SJ1)
                .headers(PublicParam.getParam())
                .params(Params.sj1(userLevelId))
                .build().execute(new GenericsCallback<SJ_ImgRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(SJ_ImgRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {

                        Intent mIntent = new Intent(context, MemBerSJActivity.class);
                        mIntent.putExtra("userLevelId", response.getData().get(0).getType());
                        mIntent.putExtra("money", response.getData().get(0).getPay());
                        mIntent.putExtra("level", response.getData().get(0).getName());
                        context.startActivity(mIntent);

                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }


    /**
     * 加载等待 <一句话功能简述> <功能详细描述>
     *
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    private void showDialog(String msg) {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.setDialogShowMessage(msg);
            mDialog.setCancelable(true);
            mDialog.show();
        }
    }

    private void setMessage(String msg) {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.setDialogShowMessage(msg);
        }
    }

    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
        }
    }
}
