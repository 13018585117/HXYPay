package com.hxypay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.android.swipe.SmartSwipeBack;
import com.gyf.immersionbar.ImmersionBar;
import com.hxypay.customview.CustomAlertDialog;
import com.hxypay.tab.OTabActivity;
import com.hxypay.tab.OTabHost;
import com.hxypay.ui.HomeActivity;
import com.hxypay.util.CashierInputFilter;
import com.hxypay.util.SharedPreStorageMgr;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public CustomAlertDialog mDialog;

    public Context context;

    public Intent mIntent = null;

    private ImageView mCenterImg;
    public TextView mCenterTx,tv_right_img;
    public ImageView mLeftImg, mRightImg;

    public int screenHeight;
    public int screenWidth;

    public String token;

    public String appName;

    public SharedPreStorageMgr mPs;
    private TextView tv_new_information;
    private LinearLayout ll_left_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        appName = getString(R.string.app_name);
        ImmersionBar.with(this).statusBarDarkFont(true,0.2f).init();  //状态栏设置；
        screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
        screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800p）
        mPs = SharedPreStorageMgr.getIntance();
        mDialog = new CustomAlertDialog(this, "加载中...");
//        mDialog.setCancelable(true);
    }

    public void setCenterImgShow(int centerImgId){
        mCenterTx.setVisibility(View.GONE);
        mCenterImg.setVisibility(View.VISIBLE);
        mCenterImg.setBackgroundResource(centerImgId);
    }

    public void initHeadView(Activity act, int leftImgId, String cStr, int rightImgId,String rightText) {
        // head左边设置
        mLeftImg =  act.findViewById(R.id.left_img);
        ll_left_img = act.findViewById(R.id.ll_left_img);
        if (0 == leftImgId) {
            ll_left_img.setVisibility(View.GONE);
        } else {
            ll_left_img.setVisibility(View.VISIBLE);
            mLeftImg.setBackgroundResource(leftImgId);
        }
        mCenterImg = (ImageView) act.findViewById(R.id.center_ig);
        mCenterTx = (TextView) act.findViewById(R.id.center_tx);
        tv_right_img = (TextView) act.findViewById(R.id.tv_right_img);
        tv_new_information = act.findViewById(R.id.tv_new_information);
        if (TextUtils.isEmpty(cStr) || cStr.equals("")) {
            mCenterTx.setVisibility(View.GONE);
        } else {
            mCenterTx.setVisibility(View.VISIBLE);
            mCenterTx.setText(cStr);
        }

        if (TextUtils.isEmpty(rightText)){
            tv_right_img.setVisibility(View.GONE);
        }else{
            tv_right_img.setText(rightText);
            tv_right_img.setVisibility(View.VISIBLE);
        }

        // 设置右边按钮
        mRightImg = (ImageView) act.findViewById(R.id.right_img);
        if (0 == rightImgId) {
            mRightImg.setVisibility(View.GONE);
        }
        else {
            mRightImg.setVisibility(View.VISIBLE);
            mRightImg.setBackgroundResource(rightImgId);
        }

        onLeftListener();
        onRightListener();
        onCenterListener();
    }

    public void keyboardHide(View view) {
        //判断输入法的隐藏状态
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void setMoneyEditText(EditText et){
        InputFilter[] filters={new CashierInputFilter()};
        et.setFilters(filters); //设置金额输入的过滤器，保证只能输入金额类型
    }


    /**
     * 一般为返回
     */
    public void onLeftListener() {
        ll_left_img.setOnClickListener(BaseActivity.this);
    }

    /**
     * 一般home，重写就根据需要传id，和重写事件
     */
    public void onRightListener() {
        mRightImg.setOnClickListener(BaseActivity.this);
    }


    public void onCenterListener() {
        mCenterTx.setOnClickListener(BaseActivity.this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_img:
                finish();
                break;
            case R.id.right_img:
                break;
            case R.id.center_tx:
                break;
            case R.id.tv_right_img:
                break;
            default:
                break;
        }
    }


    /**
     * 加载等待 <一句话功能简述> <功能详细描述>
     *
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    public void showDialog(String msg) {
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

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void show_right(boolean isShow){
        if (tv_new_information!=null){
            if (isShow) {
                tv_new_information.setVisibility(View.VISIBLE);
            }else {
                tv_new_information.setVisibility(View.GONE);
            }
        }
    }
}
