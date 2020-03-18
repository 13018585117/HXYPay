package com.hxypay.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.gyf.immersionbar.ImmersionBar;
import com.hxypay.App;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.customview.PhotoPop;
import com.hxypay.customview.TJRDialog;
import com.hxypay.dialogs.TiShiDialog;
import com.hxypay.response.HeadPicRes;
import com.hxypay.response.MerchantInfoRes;
import com.hxypay.response.MyBdRes;
import com.hxypay.response.MyHYJYRes;
import com.hxypay.response.MyTdRes;
import com.hxypay.response.ProfitRes;
import com.hxypay.response.PublicRes;
import com.hxypay.util.Base64BitmapUtil;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.NumAnim;
import com.hxypay.util.Params;
import com.hxypay.util.PictureUtil;
import com.hxypay.util.PublicParam;
import com.hxypay.util.VerifyRuler;
import com.hxypay.view.ObservableScrollView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.hxypay.ui.UpdateCardOCRActivity.getBitmapFormUri;


public class MineActivity2 extends BaseActivity implements ObservableScrollView.ScrollViewListener {
    @BindView(R.id.name_first_img)
    CircleImageView mHeadImg;
    @BindView(R.id.name_tx)
    TextView mNameTx;
    @BindView(R.id.level_img)
    ImageView mLevelImg;
    @BindView(R.id.tele_tx)
    TextView mTeleTx;
    @BindView(R.id.layout_setting)
    LinearLayout layout_setting;
    @BindView(R.id.tv_doMoney)
    TextView mKtxText;
    @BindView(R.id.tx_bt)
    Button tx_bt;
    @BindView(R.id.zsy_tx)
    TextView mYtxText;
    @BindView(R.id.ytx_tx)
    TextView mZsyText;
    @BindView(R.id.tv_tgsy_money)
    TextView tv_tgsy_money;
    @BindView(R.id.tv_frsy_money)
    TextView tv_frsy_money;
    @BindView(R.id.tv_sksy_money)
    TextView tv_sksy_money;
    @BindView(R.id.tv_dksy_money)
    TextView tv_dksy_money;
    @BindView(R.id.tv_cpsy_money)
    TextView tv_cpsy_money;
    @BindView(R.id.tv_tdsy_money)
    TextView tv_tdsy_money;

    @BindView(R.id.tv_zcr)
    TextView tv_zcr;
    @BindView(R.id.tv_sivr)
    TextView tv_sivr;
    @BindView(R.id.tv_hhr)
    TextView tv_hhr;
    @BindView(R.id.level_tx)
    TextView level_tx;
    @BindView(R.id.exit_we_layout)
    LinearLayout exit_we_layout;

    @BindView(R.id.tv_fellow_dayMoney)
    TextView tv_fellow_dayMoney;
    @BindView(R.id.tv_fellow_monthMoney)
    TextView tv_fellow_monthMoney;

    @BindView(R.id.oslv_home)
    ObservableScrollView oslv_home;

    @BindView(R.id.ll_devolutionShow)
    LinearLayout ll_devolutionShow;

    @BindView(R.id.name_first_img2)
    CircleImageView mHeadImg2;
    @BindView(R.id.name_tx2)
    TextView mNameTx2;
    @BindView(R.id.level_img2)
    ImageView mLevelImg2;
    @BindView(R.id.tele_tx2)
    TextView mTeleTx2;
    @BindView(R.id.layout_setting2)
    LinearLayout layout_setting2;
    @BindView(R.id.level_tx2)
    TextView level_tx2;
    @BindView(R.id.tv_doMoney2)
    TextView mKtxText2;
    @BindView(R.id.tx_bt2)
    Button tx_bt2;

    private String merState = "0";
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String IMAGE_FILE_NAME = "head.jpg";
    private Bitmap mHeadBitmap;
    private String base64FontStr = "data:image/jpg;base64,";
    private String headImgBase64;
    private TiShiDialog shimingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine2);
        ButterKnife.bind(this);
        oslv_home.setOnScrollViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String tele = mPs.getStringValue(Constants.ACCOUNT_NAME);
        mTeleTx.setText(VerifyRuler.teleShow(tele));
        mTeleTx2.setText(VerifyRuler.teleShow(tele));
        merchantInfo();
        getProfit("0");
        du();
        transactionData();
    }

    //设置；
    @OnClick(R.id.layout_setting)
    public void settingBut(){
        mIntent = new Intent(context, SettingActivity.class);
        startActivity(mIntent);
    }
    @OnClick(R.id.layout_setting2)
    public void settingBut2(){
        mIntent = new Intent(context, SettingActivity.class);
        startActivity(mIntent);
    }
    //提现；
    @OnClick(R.id.tx_bt)
    public void tx_bt(){
       tx();
    }
    @OnClick(R.id.tx_bt2)
    public void tx_bt2(){
       tx();
    }

    private void tx() {
        if (merState.equals("0")) {
            isShiMing();
            return;
        }
        String ktxMoney = mKtxText.getText().toString();
        if (TextUtils.isEmpty(ktxMoney)) {
            IToast.getIToast().showIToast("暂无可提现金额");
            return;
        }
        mIntent = new Intent(context, TXActivity.class);
        mIntent.putExtra("ktxMoney", mKtxText.getText().toString());
        startActivity(mIntent);
    }

    //认证中心；
    @OnClick(R.id.layout_authentication)
    public void layout_authentication(){
        if (merState.equals("0")) {
            mIntent = new Intent(context, RealNameOCRActivity.class);
            startActivity(mIntent);
        } else if (merState.equals("1")) {
            IToast.getIToast().showIToast("已认证");
        } else if (merState.equals("2")) {
            IToast.getIToast().showIToast("商户未知状态，请重试");
        }
    }

    //我的收益；
    @OnClick(R.id.ll_me_sy)
    public void meSY(){
        mIntent = new Intent(context, ProfitActivity.class);
        startActivity(mIntent);
    }
    //收益
    @OnClick(R.id.ll_shouyi)
    public void shouyi(){
        mIntent = new Intent(context, ProfitActivity.class);
        startActivity(mIntent);
    }

    //我的团队；
    @OnClick(R.id.ll_meTeam)
    public void meTeam(){
        mIntent = new Intent(context, FriendsActivity.class);
        startActivity(mIntent);
    }

    //好友列表；
    @OnClick(R.id.layout_friends)
    public void layout_friends(){
        mIntent = new Intent(context, FriendsActivity.class);
        startActivity(mIntent);
    }
    //客户服务；
    @OnClick(R.id.layout_service)
    public void layout_service(){
        mIntent = new Intent(context, ServerMainActivity.class);
        startActivity(mIntent);
    }
    //我的保单；
    @OnClick(R.id.layout_insurance)
    public void layout_insurance(){
        bd();
    }
    //我的上级；
    @OnClick(R.id.layout_myreferrer)
    public void layout_myreferrer(){
        if(!TextUtils.isEmpty(App.getApp().getUserInfo().getAgentName())) {
            TJRDialog tipDialog = new TJRDialog(context, App.getApp().getUserInfo().getAgentName(),
                    App.getApp().getUserInfo().getAgentPhone(), App.getApp().getUserInfo().getAgentAddress(),
                    App.getApp().getUserInfo().getAgentOperate());
            tipDialog.show();
        }
    }

    //卡包；
    @OnClick(R.id.layout_my_kabao)
    public void kabao(){
        mIntent = new Intent(MineActivity2.this, CardBagActivity.class);
        startActivity(mIntent);
    };

    @OnClick(R.id.layout_my_jiaoyi)
    public void jiaoyi(){
        mIntent = new Intent(MineActivity2.this,BillActivity.class);
        startActivity(mIntent);
    }

    //更换头像；
    @OnClick(R.id.name_first_img)
    public void name_FirstBut(){
        headPortrait();
    }
    @OnClick(R.id.name_first_img2)
    public void name_FirstBut2(){
        headPortrait();
    }

    private void headPortrait() {
        PhotoPop photoPop = new PhotoPop(context, new PhotoPop.IPhotoListener() {
            @Override
            public void updateCard(PhotoPop dialog) {
                dialog.dismiss();
                takePhoto();
            }

            @Override
            public void deleteCard(PhotoPop dialog) {
                dialog.dismiss();
                picGallery();
            }
        });
        photoPop.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
    }

    //好友交易数据；
    @OnClick(R.id.ll_fellow)
    public void fellow() {
//        Toast.makeText(context, "暂未开放！", Toast.LENGTH_SHORT).show();
        mIntent = new Intent(this, FellowBusinessActivity.class);
        startActivity(mIntent);
    }

    //更换手机号
    @OnClick(R.id.layout_updatePhone)
    public void updatePhone(){
        mIntent = new Intent(this,UpdatePhoneActivity.class);
        startActivity(mIntent);
    }

    //意见反馈；
    @OnClick(R.id.layout_my_Feedback)
    public void feedBack(){
        mIntent = new Intent(this,FeedBackActivity.class);
        startActivity(mIntent);
    }

    //退出
    @OnClick(R.id.exit_we_layout)
    public void exit_we_layout(){
        exit();
    }
    private void exit() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.EXIT)
                .headers(PublicParam.getParam())
                .params(Params.exitParams())
                .build().execute(new GenericsCallback<PublicRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(PublicRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mPs.remove(Constants.ACCOUNT_PWD);
                        mIntent = new Intent(context, LoginActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(mIntent);
                        finish();
                        System.exit(0);
                    } else {
                        IToast.getIToast().showIToast("请重新退出");
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("请重新退出");
                }
            }
        });
    }

    private void merchantInfo() {
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
            public void onResponse(MerchantInfoRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        merState = response.getData().getIsReal();
                        String userLevelId = response.getData().getType_id();
                        setMerchanInfoView(response.getData());
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
    }

    private void setMerchanInfoView(MerchantInfoRes.Info info) {
        if (info != null) {
            String headPic = info.getHeadPic();
            if (!TextUtils.isEmpty(headPic)) {
                Glide.with(context).load(headPic).into(mHeadImg);
                Glide.with(context).load(headPic).into(mHeadImg2);
            }
            String name = info.getName();
            if (TextUtils.isEmpty(name)) {
                mNameTx.setText("未实名");
                mNameTx2.setText("未实名");
            } else {
                mNameTx.setText(VerifyRuler.nameShow(name));
                mNameTx2.setText(VerifyRuler.nameShow(name));
            }
            //设置头部
            String level = info.getLevel();//会员等级显示
            if (!TextUtils.isEmpty(level)) {
                level_tx.setText(level);
                level_tx2.setText(level);
            }
            String typeId = info.getType_id();//会员图标显示
            if (!TextUtils.isEmpty(typeId)) {
                if (typeId.endsWith("1")) {
                    mLevelImg.setBackgroundResource(R.drawable.vip_no);
                    mLevelImg2.setBackgroundResource(R.drawable.vip_no);
                }else if(typeId.endsWith("2")){
                    mLevelImg.setBackgroundResource(R.drawable.vip);
                    mLevelImg2.setBackgroundResource(R.drawable.vip);
                }
                /*else if (typeId.endsWith("3")) {
                    mLevelImg.setBackgroundResource(R.drawable.vip2);
                } else if (typeId.endsWith("4")) {
                    mLevelImg.setBackgroundResource(R.drawable.vip3);
                } */else {
                    mLevelImg.setBackgroundResource(R.drawable.vip);
                    mLevelImg2.setBackgroundResource(R.drawable.vip);
                }
            }
            String state = info.getIsReal();
            if (!TextUtils.isEmpty(state)) {
             /*   if (state.endsWith("1")) {
                    mStateText.setText("已认证");
                } else if (state.endsWith("2")) {
                    mStateText.setText("未认证");
                }*/
            }
        }
    }

    //我的团队人数；
    private void du(){
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.MY_TD)
                .headers(PublicParam.getParam())
                .build().execute(new GenericsCallback<MyTdRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(MyTdRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData() != null) {

                            /*tv_tgsy_money.setText(response.getData().getUpgradeMoney().equals("0")?"0.00":response.getData().getUpgradeMoney());
                            tv_frsy_money.setText(response.getData().getProfitMoney().equals("0")?"0.00":response.getData().getProfitMoney());
                            tv_sksy_money.setText(response.getData().getCardMoney().equals("0")?"0.00":response.getData().getCardMoney());
                            tv_dksy_money.setText(response.getData().getLoanMoney().equals("0")?"0.00":response.getData().getLoanMoney());
                            tv_cpsy_money.setText(response.getData().getTestCardMoney().equals("0")?"0.00":response.getData().getTestCardMoney());*/
                            try {
                                NumAnim.startAnim(tv_tgsy_money,Float.parseFloat(response.getData().getUpgradeMoney()) );
                                NumAnim.startAnim(tv_frsy_money,Float.parseFloat(response.getData().getProfitMoney()));
                                NumAnim.startAnim(tv_sksy_money,Float.parseFloat(response.getData().getCardMoney()));
                                NumAnim.startAnim(tv_dksy_money,Float.parseFloat(response.getData().getLoanMoney()));
                                NumAnim.startAnim(tv_cpsy_money,Float.parseFloat(response.getData().getTestCardMoney()));
                                NumAnim.startAnim(tv_tdsy_money,Float.parseFloat(response.getData().getTeamIncome()));
                                tv_zcr.setText(response.getData().getTeamNumber()+"");
                                tv_sivr.setText(response.getData().getTeamVIPNumber()+"");
                                tv_hhr.setText(response.getData().getTeamSVIPNumber()+"");
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
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

    //好友交易数据；
    private void transactionData(){
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.TRANSACTIONDATA)
                .headers(PublicParam.getParam())
                .build().execute(new GenericsCallback<MyHYJYRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(MyHYJYRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData() != null) {
                            try {
                                NumAnim.startAnim(tv_fellow_dayMoney,Float.parseFloat(response.getData().getDailyTrading()) );
                                NumAnim.startAnim(tv_fellow_monthMoney,Float.parseFloat(response.getData().getMonthlyTransaction()));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
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


    private void picGallery() {
        // 文件权限申请
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 权限还没有授予，进行申请
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200); // 申请的 requestCode 为 200
        } else {
            // 如果权限已经申请过，直接进行图片选择
//            mPhotoPop.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            // 判断系统中是否有处理该 Intent 的 Activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_GET);
            } else {
                Toast.makeText(context, "未找到图片查看器", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void takePhoto() {
        PictureUtil.mkdirOSCRootDirectory();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 权限还没有授予，进行申请
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 300); // 申请的 requestCode 为 300
        } else {
            imageCapture();
        }
    }

    private void imageCapture() {
        Intent intent;
        Uri pictureUri;
        //getMyPetRootDirectory()得到的是Environment.getExternalStorageDirectory() + File.separator+"MyPet"
        //也就是我之前创建的存放的文件夹
        File pictureFile = new File(PictureUtil.getJHRootDirectory(), IMAGE_FILE_NAME);
        // 判断当前系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //这一句非常重要
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //""中的内容是随意的，但最好用package名.provider名的形式，清晰明了
            pictureUri = FileProvider.getUriForFile(this,
                    "com.hxypay.fileprovider", pictureFile);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureUri = Uri.fromFile(pictureFile);
        }
        // 去拍照,拍照的结果存到oictureUri对应的路径中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        Log.e("take photo", "before take photo" + pictureUri.toString());
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private void bd() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.MY_BD)
                .headers(PublicParam.getParam())
                .params(Params.myBDParams())
                .build().execute(new GenericsCallback<MyBdRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(MyBdRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData() != null) {
                            if (!TextUtils.isEmpty(response.getData().getInsuranName())
                                    && !TextUtils.isEmpty(response.getData().getMoney())) {
                                mIntent = new Intent(context, MyBDActivity.class);
                                mIntent.putExtra("myBd", response.getData());
                                startActivity(mIntent);
                            } else {
                                IToast.getIToast().showIToast("暂无保单");
                            }
                        } else {
                            IToast.getIToast().showIToast("暂无保单");
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


    /*获取的收益金额*/
    public void getProfit(String pageNum) {
        OkHttpUtils.post()
                .url(Constants.MERCHANT_PROFIT)
                .headers(PublicParam.getParam())
                .params(Params.profitParams(pageNum))
                .build().execute(new GenericsCallback<ProfitRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(ProfitRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
//                        Toast.makeText(context, "进来了"+response.getCode(), Toast.LENGTH_SHORT).show();
                        try {
                            NumAnim.startAnim(mKtxText,Float.parseFloat(response.getNotCash()) );
                            NumAnim.startAnim(mKtxText2,Float.parseFloat(response.getNotCash()) );
                            NumAnim.startAnim(mZsyText,Float.parseFloat(response.getDeposit()));
                            NumAnim.startAnim(mYtxText,Float.parseFloat(response.getCash()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            mKtxText.setText(response.getNotCash());
                            mKtxText2.setText(response.getNotCash());
                            mZsyText.setText(response.getDeposit());
                            mYtxText.setText(response.getCash());
                        }
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相册选取
                case REQUEST_IMAGE_GET:
                    Uri uri = PictureUtil.getImageUri(this, data);
                    showUploadImage(uri);
                    break;
                // 拍照
                case REQUEST_IMAGE_CAPTURE:
                    File pictureFile = new File(PictureUtil.getJHRootDirectory(), IMAGE_FILE_NAME);
                    Uri pictureUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pictureUri = FileProvider.getUriForFile(this,
                                "com.hxypay.fileprovider", pictureFile);
                    } else {
                        pictureUri = Uri.fromFile(pictureFile);
                    }
                    showUploadImage(pictureUri);
                    break;
                default:
                    break;
            }
        }
    }
    private void showUploadImage(Uri imageUri) {
        mHeadBitmap = getBitmapFormUri(this, imageUri);
        String path = PictureUtil.getAbsoluteImagePath(MineActivity2.this, imageUri);
        int jd = PictureUtil.readPictureDegree(path);
        mHeadBitmap = PictureUtil.rotaingImageView(jd, mHeadBitmap);

        headImgBase64 = base64FontStr + Base64BitmapUtil.bitmapToBase64(mHeadBitmap);
        mHeadBitmap.recycle();
        sendHeadImg();
    }
    private void sendHeadImg() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.SEND_HEAD_PIC)
                .headers(PublicParam.getParam())
                .params(Params.uploadImg(headImgBase64))
                .build().execute(new GenericsCallback<HeadPicRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(HeadPicRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        IToast.getIToast().showIToast("上传成功");
                        Glide.with(context).load(response.getImg()).into(mHeadImg);
                        Glide.with(context).load(response.getImg()).into(mHeadImg2);
//                                mHeadImg.setImageBitmap(mHeadBitmap);
                    } else {
                        IToast.getIToast().showIToast("上传失败");
                        GoLogin.goLogin(response.getCode());
                    }
                }
            }
        });
    }

    /*监听滚动*/
    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy) {
//       Log.e("滚动","y="+y+"    oldy="+oldx);//325
        if (y<325){
            ll_devolutionShow.setVisibility(View.GONE);
            ImmersionBar.with(this).statusBarColor(R.color.transparent).init();
        }else {
            ll_devolutionShow.setVisibility(View.VISIBLE);
            ImmersionBar.with(this).statusBarColor(R.color.app_theme_color).init();
        }
    }

    private void isShiMing() {
        if (shimingDialog == null) {
            shimingDialog = new TiShiDialog(MineActivity2.this, "温馨提示", "商户未认证，请实名认证\n请先进行实名", "前往实名", "取消",false);
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
}
