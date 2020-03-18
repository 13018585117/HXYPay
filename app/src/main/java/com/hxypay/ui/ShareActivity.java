package com.hxypay.ui;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.CustomRoundAngleImageView;
import com.hxypay.customview.IToast;
import com.hxypay.customview.SharePop;
import com.hxypay.dialogs.TiShiDialog;
import com.hxypay.response.ShareRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.MediaScanner;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.util.ShareUtil;
import com.hxypay.util.Utils;
import com.hxypay.util.VerifyRuler;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import pub.devrel.easypermissions.EasyPermissions;

public class ShareActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private MZBannerView mMZBanner;
    private List<ShareRes.Info2> mData = new ArrayList<ShareRes.Info2>();

    private Button mSaveBtn, mShareBtn;
    private String shareTitle, shareContent, shareUrl;
    private String platform;
    private Bitmap logoBitmap;

    private int index;

    private ArrayList<View> views = new ArrayList<View>();
    private ArrayList<String> bitmapUrl = new ArrayList<String>();
    private Bitmap bitmap = null;
    private String merState = "0";
    private String userLevelId = "2";
    private TiShiDialog shareDialog;
    private TiShiDialog shimingDialog;

    private String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private MediaScanner mediaScanner;
    private Bitmap shareBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mediaScanner = new MediaScanner(this);
        initView();
        shareUrl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVip();
    }

    private void isVip() {
        mSaveBtn.setVisibility(View.VISIBLE);
        mShareBtn.setVisibility(View.VISIBLE);
//        merchantInfo();
    }
  /*  private void merchantInfo() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.MERCHANT_INFO)
                .headers(PublicParam.getParam())
                .params(Params.merchantInfoParams())
                .build().execute(new GenericsCallback<MerchantInfoRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                merState = "2";
                dismissDialog();
            }

            @Override
            public void onResponse(final MerchantInfoRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        merState = response.getData().getIsReal();
                        userLevelId = response.getData().getType_id();
                        if (userLevelId.equals("2")){
                            mMZBanner.setVisibility(View.INVISIBLE);
                            mSaveBtn.setVisibility(View.GONE);
                            mShareBtn.setVisibility(View.GONE);
                            if (shareDialog==null) {
                                shareDialog = new TiShiDialog(ShareActivity.this, "温馨提示", "免费用户无法进行分享\n请先进行升级", "前往升级", "取消",false);
                            }
                            shareDialog.setClickListener(new TiShiDialog.ClickListenerInterface() {
                                @Override
                                public void doConfirm() {
//                                    setMerchanInfoView(response.getData());
//                                    sj1();
                                    shareDialog.cancel();
                                }

                                @Override
                                public void doCancel() {
                                    OTabHost.mTabHost.setCurrentTab(0);
                                    shareDialog.cancel();
                                }
                            });
                            shareDialog.show();
                        }else {
                            mMZBanner.setVisibility(View.VISIBLE);
                            mSaveBtn.setVisibility(View.VISIBLE);
                            mShareBtn.setVisibility(View.VISIBLE);
                        }
                    } else {
                        merState = "2";
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    merState = "2";
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }*/
   /* private void sj1() {
        OkHttpUtils.post()
                .url(Constants.SJ1)
                .headers(PublicParam.getParam())
                .params(Params.sj1(userLevelId))
                .build().execute(new GenericsCallback<SJ1Res>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(SJ1Res response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        String sjLevel = response.getData().getLevel();
                        String sjMoney = response.getData().getPay();

                        if (TextUtils.isEmpty(sjLevel) || TextUtils.isEmpty(sjMoney) || TextUtils.isEmpty(userLevelId)) {
                            IToast.getIToast().showIToast("请登录重试！");
                            return;
                        }
                        if (merState.equals("0")) {
                            IToast.getIToast().showIToast("商户未认证，请实名认证");
                            //跳实名；
                            isShiMing();

                        } else if (merState.equals("1")) {

                            if (Integer.parseInt(userLevelId) >= 4) {
                                IToast.getIToast().showIToast("您已为当前等级");
                                return;
                            }

                            Intent mIntent = new Intent(context, MemBerSJActivity.class);
                            mIntent.putExtra("userLevelId", userLevelId);
                            mIntent.putExtra("money", sjMoney);
                            mIntent.putExtra("level", sjLevel);
                            context.startActivity(mIntent);
                        } else if (merState.equals("2")) {
                            IToast.getIToast().showIToast("商户未知状态，请重试");
                        }
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }

    private void isShiMing() {
        if (shimingDialog == null) {
            shimingDialog = new TiShiDialog(ShareActivity.this, "温馨提示", "商户未认证，请实名认证\n请先进行实名", "前往实名", "取消",false);
        }
        shimingDialog.setClickListener(new TiShiDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                shimingDialog.cancel();
                mIntent = new Intent(context, RealNameOCRActivity.class);
                startActivity(mIntent);
            }

            @Override
            public void doCancel() {
                shimingDialog.cancel();
            }
        });
        shimingDialog.show();
    }

    private void setMerchanInfoView(MerchantInfoRes.Info info) {
        if (info != null) {
            String headPic = info.getHeadPic();


            String state = info.getIsReal();
        }
    }*/

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "分享", 0,null);
        logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);

        mSaveBtn = (Button) findViewById(R.id.save_btn);
        mShareBtn = (Button) findViewById(R.id.share_btn);
        mSaveBtn.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);

        mMZBanner = (MZBannerView) findViewById(R.id.banner);
        mMZBanner.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                index = i;
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.save_btn:
                saveImg();
                break;
            case R.id.share_btn:
                SharePop sharePop = new SharePop(context, new SharePop.ISharePopListener() {
                    @Override
                    public void wx(SharePop dialog) {
                        dialog.dismiss();
                        platform = Wechat.NAME;
                        share(2);
                    }

                    @Override
                    public void wxf(SharePop dialog) {
                        dialog.dismiss();
                        platform = WechatMoments.NAME;
                        share(2);
                    }

                    @Override
                    public void qq(SharePop dialog) {

                    }

                    @Override
                    public void qqf(SharePop dialog) {

                    }
                });
                sharePop.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    private void saveImg(){
        getPermission();
//        Log.e("保存图片路径",bitmapUrl.get(index));
        try {
            showDialog("加载中");
            Bitmap bitmapByView = Utils.getBitmapByView(views.get(index));
            returnBitMap(bitmapByView,index);
        } catch (Exception e) {
            e.printStackTrace();
            dismissDialog();
        }
    }

    /**
     * @param type 1为分享URL，2为图片；
     *
     */
    private void share(int type) {
        shareBitmap = Utils.getBitmapByView(views.get(index));
        if (type == 1) {
            ShareUtil.getInstance().showShare1(context, platform, Platform.SHARE_WEBPAGE, shareUrl, shareTitle, shareContent, shareBitmap);
        }else if (type == 2){
            ShareUtil.getInstance().showShare1(context, platform, Platform.SHARE_IMAGE, shareUrl, shareTitle, shareContent, shareBitmap);
        }
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
                            showBanner(mData,response);
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


    private void showBanner(List<ShareRes.Info2> mData, final ShareRes shareRes) {
        getBitmapUrl(mData);
        mMZBanner.setIndicatorVisible(false);
        mMZBanner.setCanLoop(false);
        // 设置数据
        mMZBanner.setPages(mData, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder(shareRes);
            }
        });
//        mMZBanner.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    public class BannerViewHolder implements MZViewHolder<ShareRes.Info2> {
        CustomRoundAngleImageView img;
        ShareRes shareRes;
        private ImageView iv_ewm;
        private TextView tv_name;
        private Bitmap bitmap;
        private Bitmap bitmap_qrCode;
        private RelativeLayout rl_layout;

        public BannerViewHolder(ShareRes shareRes){
            this.shareRes = shareRes;
        }
        @Override
        public View createView(Context context) {
            // 返回页面布局
            View convertView = LayoutInflater.from(context).inflate(R.layout.activity_share_item, null);
            img = (CustomRoundAngleImageView) convertView.findViewById(R.id.img);
            rl_layout =  convertView.findViewById(R.id.rl_layout);
            iv_ewm = (ImageView) convertView.findViewById(R.id.iv_ewm);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            return convertView;
        }

        @Override
        public void onBind(Context context, int position, ShareRes.Info2 datas) {
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
            }
            if (bitmap_qrCode==null) {
                bitmap_qrCode = CodeUtils.createImage(shareRes.getShare().getShareUrl(), 140, 140, bitmap);
            }
            Glide.with(context).load(datas.getSrc()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    //加载完成后的处理
                    img.setImageDrawable(resource);
                    iv_ewm.setImageBitmap(bitmap_qrCode);
                    if(TextUtils.isEmpty(shareRes.getName())){
                        tv_name.setVisibility(View.GONE);
                    }else {
                        tv_name.setText(VerifyRuler.nameShow(shareRes.getName()));
                        tv_name.setVisibility(View.VISIBLE);
                    }
                }
            });
            bitmap.recycle();
            views.add(position,rl_layout);
        }

    }


    private void getBitmapUrl(List<ShareRes.Info2> mData) {
        bitmapUrl.clear();
        for (int i = 0; i < mData.size(); i++) {
            bitmapUrl.add(mData.get(i).getSrc());
        }
    }

    public Bitmap returnBitMap(final Bitmap bitmap , final int index) {
        try {
            ShareUtil.getInstance().saveImageToGallery(context, bitmap, "share"+index+".jpg");
            dismissDialog();
            String[] filePaths = new String[]{Environment.getExternalStorageDirectory().getPath()+ File.separator +"HXYPay"};
            String[] mimeTypes = new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg")};
            mediaScanner.scanFiles(filePaths,mimeTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
//            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册使用权限", 1, permissions);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (logoBitmap!=null){
            logoBitmap.recycle();
            logoBitmap = null;
        }
        if (shareBitmap!=null){
            shareBitmap.recycle();
            shareBitmap =null;
        }
    }
}
