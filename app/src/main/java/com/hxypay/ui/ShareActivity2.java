package com.hxypay.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.customview.SharePop;
import com.hxypay.dialogs.TiShiDialog;
import com.hxypay.response.ShareRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.util.ShareUtil;
import com.hxypay.util.SharedPreStorageMgr;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareActivity2 extends BaseActivity {
    private String platform;
    private List<ShareRes.Info2> mData = new ArrayList<ShareRes.Info2>();
    private String shareTitle,shareContent,shareUrl,showBanner;
    private Bitmap logoBitmap;
    private TiShiDialog tiShiDialog;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share2);
        initHeadView(this,0,"推广",0,null);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shareUrl();
    }


    private void initView() {
        logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
    }

    @OnClick(R.id.ll_share_QR)
    public void shareQR(){
        mIntent = new Intent(this,ShareActivity.class);
        startActivity(mIntent);
    }

    //分享素材；
    @OnClick(R.id.ll_share_wx)
    public void shreWX(){
        mIntent = new Intent(ShareActivity2.this,ShareMaterialActivity.class);
        startActivity(mIntent);
    }

    @OnClick(R.id.ll_share_url)
    public void shre_url(){
        share();
    }
    //面对面注册；
    @OnClick(R.id.ll_share_F2F)
    public void share_F2F(){
//        IToast.getIToast().showIToast("暂时未开通");
            phone = SharedPreStorageMgr.getIntance().getStringValue(Constants.ACCOUNT_NAME);
            tiShiDialog = new TiShiDialog(this, "面对面注册", "邀请码：" + phone, "复制号码", "关闭", true);
            tiShiDialog.setClickListener(new TiShiDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    //创建ClipData对象
                    ClipData clipData = ClipData.newPlainText("邀请码：", phone);
                    //添加ClipData对象到剪切板中
                    clipboardManager.setPrimaryClip(clipData);
                    IToast.getIToast().showIToast("复制成功");
                    tiShiDialog.cancel();
                }

                @Override
                public void doCancel() {
                    tiShiDialog.cancel();
                }
            });
        tiShiDialog.show();
    }



    private void share() {
        SharePop sharePop = new SharePop(context, new SharePop.ISharePopListener() {
            @Override
            public void wx(SharePop dialog) {
                dialog.dismiss();
                platform = Wechat.NAME;
                ShareUtil.getInstance().showShare1(context,  platform,Platform.SHARE_WEBPAGE, shareUrl, shareTitle, shareContent, logoBitmap);
                logoBitmap.recycle();
            }

            @Override
            public void wxf(SharePop dialog) {
                dialog.dismiss();
                platform = WechatMoments.NAME;
                ShareUtil.getInstance().showShare1(context, platform,Platform.SHARE_WEBPAGE, shareUrl, shareTitle, shareContent, logoBitmap);;
                logoBitmap.recycle();
            }

            @Override
            public void qq(SharePop dialog) {

            }

            @Override
            public void qqf(SharePop dialog) {

            }
        });
        sharePop.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
    }

    public void shareUrl() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.SHARE_URL)
                .headers(PublicParam.getParam())
                .params(Params.shareUrl())
                .build().execute(new GenericsCallback<ShareRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(ShareRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData() != null && response.getData().size() > 0) {
                            mData.clear();
                            mData = response.getImg();
                            shareTitle = response.getShare().getTitle();
                            shareContent = response.getShare().getContent();
                            shareUrl = response.getShare().getShareUrl();
                        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
